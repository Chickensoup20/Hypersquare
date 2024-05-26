package hypersquare.hypersquare.dev.action;

import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.CodeStacktrace;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface IfAction extends Action {

    boolean check(Entity entity, ExecutionContext ctx);

    @Override
    default void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        List<Entity> filtered = new ArrayList<>();
        for (Entity e : targetSel.all()) {
            if (check(e, ctx)) filtered.add(e);
        }
        if (!filtered.isEmpty()) {
            ctx.trace().pushFrame(new CodeStacktrace.Frame(ctx.data().actions, new CodeSelection(filtered)));
        }
    }
}
