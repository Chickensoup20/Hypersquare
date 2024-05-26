package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerUnsneakEvent implements Event {
    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.CHAINMAIL_LEGGINGS)
            .setName(Component.text("Player Unsneak Event").color(Colors.BLUE))
            .setDescription(
                Component.text("Executes code when a player"),
                Component.text("stops sneaking."))
            .build()
            ;
    }

    @Override
    public String getId() {
        return "unsneak";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "Unsneak";
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
