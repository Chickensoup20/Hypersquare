package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.item.event.EventItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerLeaveEvent implements Event {

    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.POISONOUS_POTATO)
            .setName(Component.text("Player Leave Game Event").color(NamedTextColor.RED))
            .setDescription(Component.text("Executes code when a player"),
                Component.text("leaves the plot."))
            .build();
    }

    @Override
    public String getId() {
        return "leave";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "Leave";
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
