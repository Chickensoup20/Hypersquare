package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.item.event.EventItem;
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

    @Override
    public String getId() {
        return "join";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "Join";
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
