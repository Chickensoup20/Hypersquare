package hypersquare.hypersquare.play.execution;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.value.CodeValues;
import hypersquare.hypersquare.dev.value.impl.VariableValue;
import hypersquare.hypersquare.play.ActionArguments;
import hypersquare.hypersquare.play.CodeVariable;
import hypersquare.hypersquare.play.CodeVariableScope;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.function.Function;

public record ExecutionContext(
    CodeExecutor executor,
    ActionArguments args,
    CodeStacktrace trace,
    Action action, CodeActionData data
) {

    @Contract(pure = true)
    public CodeVariableScope getScope(VariableValue.@NotNull Scope scope) {
        return switch (scope) {
            case LINE -> trace.lineScope;
            case GLOBAL -> executor.globalScope;
            default -> null; // TODO: implement other scopes
        };
    }

    @SuppressWarnings("unchecked")
    public <T> T getTag(String id, Function<String, T> o) {
        BarrelTag tag = action.getTag(id);
        if (tag == null) throw new NullPointerException("Unknown tag! " + id);
        Pair<String, VariableValue.HSVar> tagValue = data.getTags().getOrDefault(id, new Pair<>(tag.defaultOption().name(), null));
        if (tagValue.getB() != null) {
            CodeVariable var = (CodeVariable) CodeValues.VARIABLE.realValue(tagValue.getB());
            var.bind(this);
            String query = var.get(CodeValues.STRING);
            for (BarrelTag.Option opt : tag.options()) {
                if (opt.text().equals(query)) return (T) opt.id();
            }
        }
        return o.apply(tagValue.getA());
    }

    public void sleep(int dur) {
        trace.halt = true;
        executor.trigger(trace, CodeExecutor.EventRunnable::run);
    }
}
