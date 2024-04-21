package hypersquare.hypersquare.item.action.player;

import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static hypersquare.hypersquare.Hypersquare.cleanMM;

public enum IfPlayerItems implements ActionMenuItem {
    TOGGLEABLE_CONDITIONS_CATEGORY(Material.REDSTONE, "<aqua>Toggleable Conditions", "<gray>If sneaking, is swimming,%n<gray>is sprinting, etc.", 10),
    LOCATIONAL_CONDITIONS_CATEGORY(Material.PAPER, "<green>Locational Conditions", "<gray>Is near, is standing%n<gray>on, is looking at, etc.", 12),
    ITEM_CONDITIONS_CATEGORY(Material.ITEM_FRAME, "<gold>Item Conditions", "<gray>Has item, is holding,%n<gray>is wearing, etc.", 14),
    MISCELLANEOUS_CONDITIONS_CATEGORY(Material.BEDROCK, "<dark_purple>Miscellaneous Conditions", "<gray>Name equals, is riding,%n<gray>has effect, etc.", 16);

    public final Material material;
    public final String name;
    public final String lore;
    public final int slot;

    IfPlayerItems(Material material, String name, String lore, int slot) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.slot = slot;
    }

    public ItemStack build() {
        return Utilities.formatItem(lore, material, name);
    }

    @Override
    public int getSlot() {
        return slot;
    }

    public @NotNull Component getName() {
        return cleanMM.deserialize(name);
    }

    public @NotNull Component getLore() {
        return cleanMM.deserialize(lore.replace("%n", "\n"));
    }
}
