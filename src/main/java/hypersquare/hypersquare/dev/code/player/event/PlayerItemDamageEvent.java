package hypersquare.hypersquare.dev.code.player.event;

import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.item.event.EventItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerItemDamageEvent implements Event {
    @Override
    public String getId() {
        return "damage_item";
    }

    @Override
    public String getCodeblockId() {
        return "player_event";
    }

    @Override
    public String getSignName() {
        return "DamageItem";
    }

    @Override
    public PlayerEventItems getCategory() {
        return PlayerEventItems.ITEM_EVENTS_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new EventItem()
            .setMaterial(Material.SHELTER_POTTERY_SHERD)
            .setName(Component.text("Player Damage Item Event").color(Colors.RED_LIGHT))
            .setDescription(
                Component.text("Executes code when a player"),
                Component.text("damages an item."))
            .setCancellable(false)
            .build()
            ;
    }

    @Override
    public Target[] compatibleTargets() {
        return new Target[]{Target.DEFAULT_PLAYER, Target.DEFAULT_ENTITY};
    }
}
