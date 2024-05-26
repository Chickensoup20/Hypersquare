package hypersquare.hypersquare.item.action.repeat;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum RepeatItems implements ActionMenuItem {

    NUMERIC(Material.SLIME_BALL, NamedTextColor.RED, "Number based.", "<gray>Repeats that are%n<gray>related to numbers.", 0);

    public final Material material;
    public final TextColor nameColor;
    public final String name;
    public final String lore;
    public final int slot;

    RepeatItems(Material material, TextColor nameColor, String name, String lore, int slot) {
        this.material = material;
        this.nameColor = nameColor;
        this.name = name;
        this.lore = lore;
        this.slot = slot;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    public ItemStack build() {
        return Utilities.formatItem(lore, material, "<" + nameColor.asHexString() + ">" + name);
    }

    @Override
    public @NotNull Component getName() {
        return Hypersquare.cleanMM.deserialize(name);
    }
}
