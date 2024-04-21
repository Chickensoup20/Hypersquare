package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.action.CancellableEvent;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.EventItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerLeftClickEvent implements CancellableEvent {
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.IRON_PICKAXE)
            .setName(Component.text("Player Left Click Event").color(NamedTextColor.AQUA))
            .setDescription(
                Component.text("Executes code when a player"),
                Component.text("left clicks."))
            .addAdditionalInfo(Component.text("Does not execute if the"),
                Component.text("player dealt damage."))
            .setCancellable(true)
            .build();
    }

    @Override
    public String getId() {
        return "left_click";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "LeftClick";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.CLICK_EVENTS_CATEGORY;
    }

    @Override
    public Target[] compatibleTargets() {
        return new Target[]{Target.DEFAULT_PLAYER};
    }
}
