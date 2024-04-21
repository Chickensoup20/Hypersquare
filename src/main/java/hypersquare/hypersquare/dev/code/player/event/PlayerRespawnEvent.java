package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerRespawnEvent implements Event {
    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.OAK_SAPLING)
            .setName(Component.text("Player Respawn Event").color(Colors.AQUA_DARK))
            .setDescription(
                Component.text("Executes code when"),
                Component.text("a player respawns."))
            .build()
            ;
    }

    @Override
    public String getId() {
        return "respawn";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "Respawn";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.DEATH_EVENTS_CATEGORY;
    }

    @Override
    public Target[] compatibleTargets() {
        return new Target[]{Target.DEFAULT_PLAYER};
    }
}

