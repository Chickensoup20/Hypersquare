package hypersquare.hypersquare.item.action.game;

import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static hypersquare.hypersquare.Hypersquare.cleanMM;

public enum GameActionItems implements ActionMenuItem {
    BLOCK_MANAGEMENT_CATEGORY(Material.BEDROCK, "<green>Block Manipulation", "<gray>Setting, Copying and modifying blocks", 13);

    public final Material material;
    public final String name;
    public final String lore;
    public final int slot;

    GameActionItems(Material material, String name, String lore, int slot) {
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
