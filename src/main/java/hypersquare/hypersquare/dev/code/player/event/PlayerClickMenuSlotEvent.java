package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.action.CancellableEvent;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerClickMenuSlotEvent implements CancellableEvent {
    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.CHEST)
            .setName(Component.text("Player Click Menu Event").color(Colors.SKY))
            .setDescription(
                Component.text("Executes code when a player"),
                Component.text("clicks a slot inside an open inventory."))
            .setCancellable(true)
            .build()
            ;
    }

    @Override
    public String getId() {
        return "click_menu_slot";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "ClickMenu";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.ITEM_EVENTS_CATEGORY;
    }

    @Override
    public Target[] compatibleTargets() {
        return new Target[]{Target.DEFAULT_PLAYER};
    }
}
