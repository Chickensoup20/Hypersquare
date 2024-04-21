package hypersquare.hypersquare.dev.action;

import hypersquare.hypersquare.item.event.Event;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public interface CancellableEvent extends Event {
    default void cancelEvent(@NotNull Cancellable bukkitEvent) {
        bukkitEvent.setCancelled(true);
    }

    default void uncancelEvent(@NotNull Cancellable bukkitEvent) {
        bukkitEvent.setCancelled(false);
    }
}
