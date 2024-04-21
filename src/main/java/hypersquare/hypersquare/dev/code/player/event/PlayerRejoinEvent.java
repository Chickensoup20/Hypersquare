package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.item.event.EventItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerRejoinEvent implements Event {

    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.BAKED_POTATO)
            .setName(Component.text("Player Rejoin Game Event").color(NamedTextColor.YELLOW))
            .setDescription(Component.text("Executes code when a player"),
                Component.text("rejoins the plot using /play"))
            .addAdditionalInfo(Component.text("This event is triggered"),
                Component.text("before the join event, and"),
                Component.text("after the leave event."))
            .build();
    }

    @Override
    public String getId() {
        return "rejoin";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "Rejoin";
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
