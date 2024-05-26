package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.action.CancellableEvent;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerChatEvent implements CancellableEvent {
    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.BOOK)
            .setName(Component.text("Player Chat Event").color(Colors.YELLOW))
            .setDescription(Component.text("Executes code when a player"),
                Component.text("sends a message in chat."))
            .setCancellable(true)
            .build();
    }

    @Override
    public String getId() {
        return "chat";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "Chat";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.PLOT_AND_SERVER_EVENTS_CATEGORY;
    }

    @Override
    public Target[] compatibleTargets() {
        return new Target[]{Target.DEFAULT_PLAYER};
    }
}
