package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.action.CancellableEvent;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EntityDamagePlayerEvent implements CancellableEvent {
    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.WOODEN_SWORD)
            .setName(Component.text("Entity Damage Player Event").color(Colors.RED_LIGHT))
            .setDescription(
                Component.text("Executes code when an"),
                Component.text("entity damages a player."))
            .setCancellable(true)
            .build()
            ;
    }

    @Override
    public String getId() {
        return "entity_damage_player";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "EntityDmgPlayer";
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

