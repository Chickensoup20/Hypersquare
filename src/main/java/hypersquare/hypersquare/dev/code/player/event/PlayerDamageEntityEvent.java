package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.action.CancellableEvent;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerDamageEntityEvent implements CancellableEvent {
    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.STONE_SWORD)
            .setName(Component.text("Player Damage Entity Event").color(Colors.RED_LIGHT))
            .setDescription(
                Component.text("Executes code when a"),
                Component.text("player damages an entity."))
            .setCancellable(true)
            .build()
            ;
    }

    @Override
    public String getId() {
        return "player_damage_entity";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "DamageEntity";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.DAMAGE_EVENTS_CATEGORY;
    }

    @Override
    public Target[] compatibleTargets() {
        return new Target[]{Target.DEFAULT_PLAYER, Target.VICTIM};
    }
}

