package hypersquare.hypersquare.item.event;

import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import org.bukkit.inventory.ItemStack;

public interface Event {
    ItemStack item();

    String getId();

    String getCodeblockId();

    String getSignName();

    PlayerEventItems getCategory();

    /**
     * The targets that this event is compatible with
     *
     * @return The compatible targets
     * @implNote The SELECTED {@link Target} is always prepended to the list
     */
    Target[] compatibleTargets();
}
