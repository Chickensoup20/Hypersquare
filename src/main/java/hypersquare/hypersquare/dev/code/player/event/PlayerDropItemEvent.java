package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.action.CancellableEvent;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.EventItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerDropItemEvent implements CancellableEvent {
    @Override
    public String getId() {
        return "drop_item";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "DropItem";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.ITEM_EVENTS_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.SUGAR)
            .setName(Component.text("Player Drop Item Event").color(NamedTextColor.YELLOW))
            .setDescription(
                Component.text("Executes code when a player"),
                Component.text("drops an item."))
            .setCancellable(true)
            .build()
            ;
    }

    @Override
    public Target[] compatibleTargets() {
        return new Target[]{Target.DEFAULT_PLAYER, Target.DEFAULT_ENTITY};
    }
}
