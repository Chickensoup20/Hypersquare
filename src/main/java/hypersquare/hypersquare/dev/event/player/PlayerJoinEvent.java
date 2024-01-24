package hypersquare.hypersquare.dev.event.player;

import hypersquare.hypersquare.item.Event;
import hypersquare.hypersquare.item.EventItem;
import hypersquare.hypersquare.item.PlayerEventItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinEvent implements Event {

    public ItemStack item() {
        return new EventItem()
                .setMaterial(Material.POTATO)
                .setName(Component.text("Player Join Game Event").color(NamedTextColor.GREEN))
                .setDescription(Component.text("Executes code when a player"),
                                Component.text("joins the plot."))
                .addAdditionalInfo(Component.text("This event runs even"),
                                   Component.text("when a player rejoins the"),
                                   Component.text("plot. (i.e. /play)"))
                .build();
    }

    public String getId() {
        return "player_event_join";
    }

    public String getSignName() {
        return "Join";
    }

    public String getName() {
        return "Player Join Game Event";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.PLOT_AND_SERVER_EVENTS_CATEGORY;
    }
}
