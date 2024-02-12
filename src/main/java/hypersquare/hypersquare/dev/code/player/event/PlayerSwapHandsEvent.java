package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerSwapHandsEvent implements Event {
    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.SHIELD)
            .setName(Component.text("Player Swap Hands Event").color(Colors.SKY))
            .setDescription(
                Component.text("Executes code when a player"),
                Component.text("swaps items in their offhand and hand."))
            .setCancellable(true)
            .build()
            ;
    }

    @Override
    public String getId() {
        return "swap_hands";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "SwapHands";
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
