package hypersquare.hypersquare.dev.action;

import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.CodeStacktrace;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import org.jetbrains.annotations.NotNull;

public interface RepeatAction extends Action, CallbackAfterAction {

    boolean check(ExecutionContext ctx, boolean initial);

    @Override
    default void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        if (check(ctx, true)) {
            ctx.trace().pushFrame(new CodeStacktrace.Frame(ctx.data().actions, targetSel));
        }
    }

    @Override
    default void after(ExecutionContext ctx, CodeSelection targetSel) {
        if (check(ctx, false)) {
            ctx.trace().pushFrame(new CodeStacktrace.Frame(ctx.data().actions, targetSel));
        }
    }
}
