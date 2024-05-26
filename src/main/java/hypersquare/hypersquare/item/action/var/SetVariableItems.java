package hypersquare.hypersquare.item.action.var;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum SetVariableItems implements ActionMenuItem {

    VARIABLE_SETTING(Material.IRON_BLOCK, DisplayValue.VARIABLE.color, "Variable Setting", "<gray>Various variable%n<gray>setting actions", 1),
    NUMERICAL_ACTIONS(Material.SLIME_BALL, DisplayValue.NUMBER.color, "Numerical Actions", "<gray>Basic and advanced%n<gray>number actions", 3),
    STRING_MANIPULATION(Material.STRING, DisplayValue.STRING.color, "String Manipulation", "<gray>Actions for the%n<gray>manipulation of%n<gray>strings", 5),
    TEXT_MANIPULATION(Material.BOOK, DisplayValue.TEXT.color, "Text Manipulation", "<gray>Actions for the%n<gray>manipulation of%n<gray>texts", 7),

    LOCATION_MANIPULATION(Material.PAPER, DisplayValue.LOCATION.color, "Location Manipulation", "<gray>Actions for the%n<gray>manipulation of%n<gray>locations", 19),
    ITEM_MANIPULATION(Material.ITEM_FRAME, DisplayValue.ITEM.color, "Item Manipulation", "<gray>Actions for the%n<gray>manipulation of%n<gray>items", 21),
    LIST_MANIPULATION(Material.ENDER_CHEST, DisplayValue.LIST.color, "List Manipulation", "<gray>Actions for the%n<gray>manipulation of%n<gray>lists", 23),
    DICTIONARY_MANIPULATION(Material.CHEST_MINECART, DisplayValue.DICTIONARY.color, "Dictionary Manipulation", "<gray>Actions for the%n<gray>manipulation of%n<gray>dictionaries", 25),

    WORLD_ACTIONS(Material.SPECTRAL_ARROW, DisplayValue.NUMBER.color, "<red>World Actions", "<gray>Actions that get%n<gray>info from the%n<gray>world", 37),
    PARTICLE_MANIPULATION(Material.WHITE_DYE, DisplayValue.PARTICLE.color, "Particle Manipulation", "<gray>Actions for the%n<gray>manipulation of%n<gray>particles", 39),
    VECTOR_MANIPULATION(Material.PRISMARINE_SHARD, DisplayValue.VECTOR.color, "Vector Manipulation", "<gray>Actions for the%n<gray>manipulation of%n<gray>vectors", 41),
    MISCELLANEOUS_ACTIONS(Material.BEDROCK, DisplayValue.POTION.color, "Miscellaneous Actions", "<gray>Actions that do not%n<gray>belong is other%n<gray>categories", 43),
    ;

    public final Material material;
    public final TextColor nameColor;
    public final String name;
    public final String lore;
    public final int slot;

    SetVariableItems(Material material, TextColor nameColor, String name, String lore, int slot) {
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
