package hypersquare.hypersquare.play.execution;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.gson.JsonObject;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.Actions;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.action.CallbackAfterAction;
import hypersquare.hypersquare.dev.codefile.CodeFile;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.codefile.data.CodeData;
import hypersquare.hypersquare.dev.codefile.data.CodeLineData;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.dev.target.TargetPriority;
import hypersquare.hypersquare.dev.target.TargetSet;
import hypersquare.hypersquare.dev.target.TargetType;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.play.ActionArguments;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.CodeVariableScope;
import hypersquare.hypersquare.play.error.CodeErrorType;
import hypersquare.hypersquare.play.error.HSException;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CodeExecutor {
    public final static int RUNNING_LIMIT = 100;

    public final int plotId;
    public final SetMultimap<Event, EventRunnable> running = MultimapBuilder.hashKeys().hashSetValues().build();
    public final CodeVariableScope globalScope = new CodeVariableScope();

    public CodeExecutor(int plotId) {
        this.plotId = plotId;
    }

    public void halt() {
        for (var r : running.values());
        running.clear();
    }

    // TODO: note for functions & processes:
    // there should be an internal event that is used to trigger function/process calls
    // so that the stacktrace is renewed and the line var scope works
    // calling a call function action would save the current stacktrace, trigger a function call event
    // and after the function ends, it'll resume the previous stacktrace

    public void trigger(Event event, org.bukkit.event.Event bukkitEvent, CodeSelection selection) {
        if (running.size() >= RUNNING_LIMIT) throw new HSException(CodeErrorType.RUN_LIMIT, null);
        if (event == null) throw new HSException(CodeErrorType.INVALID_EVENT, null);
        CodeData data = new CodeFile(plotId).getCodeData();
        for (CodeLineData line : data.codelines) {
            if (!line.event.equals(event.getId()) || !line.type.equals(event.getCodeblockId())) continue;
            // run every line that has the same event
            CodeStacktrace trace = new CodeStacktrace(event, bukkitEvent, new CodeStacktrace.Frame(line.actions, selection));
            trigger(trace, r -> r.run());
        }
    }

    public void trigger(@NotNull CodeStacktrace trace, @NotNull Consumer<EventRunnable> runTask) {
        EventRunnable r = new EventRunnable(trace);
        runTask.accept(r);
        running.put(trace.event, r);
    }

    public void continueEval(@NotNull CodeStacktrace trace) {
        while (true) {
            // TODO: Check world tick time
            // (urgent)

            // NOTE: Temporary server TPS check removed as it uses spigot timings which is marked for removal
            // (we are using spark in prod server)
                /*
                if (Bukkit.getServer().getAverageTickTime() <= 17) {
                    CodeError.sendError(plotId, CodeErrorType.LOW_MSPT);
                    cancelEval = true;
                    break;
                }
                */
            if (trace.isDone()) break;
            CodeStacktrace.Frame frame = trace.next();
            if (frame == null) break;
            CodeActionData data = frame.next();

            if (data == null) {
                trace.popFrame();
                if (trace.isDone()) break;
                frame = trace.next();
                data = frame.current();

                Actions action = Actions.getAction(data.action, data.codeblock);
                if (action != null && action.a instanceof CallbackAfterAction cb) {
                    execute(cb::after, action, trace, frame, data);
                }
            } else {
                Action action = Actions.getAction(data.action, data.codeblock);
                if (action == null) {
                    throw new HSException(CodeErrorType.INVALID_ACT, new NullPointerException("CodeActionData is invalid?"));
                }
                execute(action::execute, action, trace, frame, data);
            }
            if (trace.halt) break;
        }
    }

    private void execute(RunFunction run, Action action, CodeStacktrace trace, CodeStacktrace.Frame frame, CodeActionData data) {
        ExecutionContext ctx;
        try {
            ctx = getCtx(action, trace, data);
        } catch (Exception e) {
            throw new HSException(CodeErrorType.FAILED_CONTEXT, e);
        }
        CodeSelection targetSel;
        try {
            targetSel = getTargetSel(data.target, action, trace, frame.selection);
        } catch (Exception e) {
            throw new HSException(CodeErrorType.FAILED_TARGET, e);
        }
        try {
            run.invoke(ctx, targetSel);
        } catch (Exception e) {
            throw new HSException(CodeErrorType.INTERNAL_ERROR, e);
        }
    }

    private CodeSelection getTargetSel(String target, Action action, CodeStacktrace trace, CodeSelection selection) {
        CodeSelection targetSel = null;
        if (target == null) {
            String codeblock = action.getCodeblockId();
            TargetType type = TargetType.ofCodeblock(codeblock);
            if (type == null) return selection; // codeblock can't be targeted, preserve original selection
            TargetSet set = TargetPriority.ofType(type);
            for (Target t : set.targets()) {
                try {
                    targetSel = t.get(trace.bukkitEvent, selection);
                } catch (Exception ignored) {
                }
            }
            if (targetSel == null)
                throw new NullPointerException("Couldn't find any target prioritization for " + codeblock);
        } else targetSel = Objects.requireNonNull(Target.getTarget(target)).get(trace.bukkitEvent, selection);
        return targetSel;
    }

    private @NotNull ExecutionContext getCtx(@NotNull Action action, CodeStacktrace trace, CodeActionData data) {
        HashMap<String, List<JsonObject>> arguments = new HashMap<>();
        for (hypersquare.hypersquare.dev.BarrelParameter param : action.parameters()) {
            List<JsonObject> args = data.getArguments().getOrDefault(param.id(), List.of());
            if (!param.type().isIndicator && args.isEmpty() && !param.optional()) throw new HSException(CodeErrorType.MISSING_PARAM, null);
            arguments.put(param.id(), args);
        }
        ActionArguments args = new ActionArguments(arguments);
        ExecutionContext ctx = new ExecutionContext(this, args, trace, action, data);
        args.bind(ctx);
        return ctx;
    }

    public class EventRunnable {
    private interface RunFunction {
        void invoke(ExecutionContext ctx, CodeSelection targetSel);
    }

    public class EventRunnable extends BukkitRunnable {
        private final CodeStacktrace trace;

        EventRunnable(CodeStacktrace trace) {
            this.trace = trace;
        }

        @Override
        public void run() {
            trace.halt = false;
            try {
                continueEval(trace);
            } catch (Exception e) {
                new HSException(plotId, CodeErrorType.RUNTIME_ERROR, e).sendMessage();
            }
            running.remove(trace.event, this);
        }
    }
}
}

