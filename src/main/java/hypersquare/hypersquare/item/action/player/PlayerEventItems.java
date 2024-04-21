package hypersquare.hypersquare.item.action.player;

import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static hypersquare.hypersquare.Hypersquare.cleanMM;

public enum PlayerEventItems implements ActionMenuItem {
    PLOT_AND_SERVER_EVENTS_CATEGORY(Material.COMMAND_BLOCK, "<gold>Plot and Server Events", "<gray>Joining and leaving plots,%n<gray>plot commands", 10),
    CLICK_EVENTS_CATEGORY(Material.DIAMOND_PICKAXE, "<blue>Click Events", "<gray>Right and left clicking,%n<gray>breaking blocks", 13),
    MOVEMENT_EVENTS_CATEGORY(Material.LEATHER_BOOTS, "<dark_aqua>Movement Events", "<gray>Jumping, walking,%n<gray>and flying", 16),
    ITEM_EVENTS_CATEGORY(Material.ITEM_FRAME, "<yellow>Item Events", "<gray>Dropping, picking up%n<gray>and eating items", 28),
    DAMAGE_EVENTS_CATEGORY(Material.IRON_SWORD, "<red>Damage Events", "<gray>Getting damaged,%n<gray>shooting a bow", 31),
    DEATH_EVENTS_CATEGORY(Material.SKELETON_SKULL, "<dark_red>Death Events", "<gray>Dying, and respawning", 34);

    public final Material material;
    public final String name;
    public final String lore;
    public final int slot;

    PlayerEventItems(Material material, String name, String lore, int slot) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.slot = slot;
    }

    public ItemStack build() {
        return Utilities.formatItem(lore, material, name);
    }

    public @NotNull Component getName() {
        return cleanMM.deserialize(name);
    }

    public @NotNull Component getLore() {
        return cleanMM.deserialize(lore.replace("%n", "\n"));
    }

    @Override
    public int getSlot() {
        return slot;
    }
}
