package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.action.CancellableEvent;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerMoveEvent implements CancellableEvent {
    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.GRASS_BLOCK)
            .setName(Component.text("Player Move Event").color(Colors.PURPLE))
            .setDescription(
                Component.text("Executes code when a player"),
                Component.text("is moving."))
            .setCancellable(true)
            .build()
            ;
    }

    @Override
    public String getId() {
        return "move";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "Move";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.MOVEMENT_EVENTS_CATEGORY;
    }

    @Override
    public Target[] compatibleTargets() {
        return new Target[]{Target.DEFAULT_PLAYER};
    }
}
