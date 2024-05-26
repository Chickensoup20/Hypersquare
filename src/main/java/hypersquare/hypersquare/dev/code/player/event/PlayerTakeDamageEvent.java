package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.action.CancellableEvent;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerTakeDamageEvent implements CancellableEvent {
    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.DEAD_BUSH)
            .setName(Component.text("Player Take Damage Event").color(Colors.RED_LIGHT))
            .setDescription(
                Component.text("Executes code when a"),
                Component.text("player takes damage."))
            .setCancellable(true)
            .build()
            ;
    }

    @Override
    public String getId() {
        return "take_damage";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "TakeDamage";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.DAMAGE_EVENTS_CATEGORY;
    }

    @Override
    public Target[] compatibleTargets() {
        return new Target[]{Target.DEFAULT_PLAYER};
    }
}

