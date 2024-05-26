package hypersquare.hypersquare.item.action.player;

import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static hypersquare.hypersquare.Hypersquare.cleanMM;

public enum PlayerActionItems implements ActionMenuItem {
    ITEM_MANAGEMENT_CATEGORY(Material.CHEST, "<gold>Item Management", "<gray>Giving, removing, setting%n<gray>and saving items", 1),
    PLAYER_ACTION_COMMUNICATION(Material.JUNGLE_SIGN, "<red>Communication", "<gray>Displaying text and%n<gray>playing sounds", 3),
    INVENTORY_MENUS_CATEGORY(Material.ITEM_FRAME, "<yellow>Inventory Menus", "<gray>Display and modification%n<gray>of item menus", 5),
    SCOREBOARD_MANIPULATION_CATEGORY(Material.PAINTING, "<blue>Scoreboard Manipulation", "<gray>Scoreboard manipulation,%n<gray>addition and removal", 7),
    STATISTICS_CATEGORY(Material.APPLE, "<green>Statistics", "<gray>Modification of player%n<gray>statistics such as health,%n<gray>hunger and XP", 19),
    SETTINGS_CATEGORY(Material.ANVIL, "<light_purple>Settings", "<gray>Changing player abilities,%n<gray>permissions and other%n<gray>settings", 21),
    MOVEMENT_CATEGORY(Material.LEATHER_BOOTS, "<dark_aqua>Movement", "<gray>Launching, teleportation, and%n<gray>other means of moving players", 23),
    WORLD_CATEGORY(Material.SPECTRAL_ARROW, "<red>World", "<gray>Targeted world actions%n<gray>and actions that change%n<gray>the world's appearance", 25),
    VISUAL_EFFECTS_CATEGORY(Material.WHITE_DYE, "<#aa55ff>Visual Effects", "<gray>Displaying short visual%n<gray>effects and particles%n<gray>to the target", 38),
    APPEARANCE_CATEGORY(Material.PLAYER_HEAD, "<yellow>Appearance", "<gray>Actions that affect the%n<gray>target's appearance, such%n<gray>as disguises", 40),
    MISCELLANEOUS_CATEGORY(Material.BEDROCK, "<dark_purple>Miscellaneous", "<gray>Actions that do not belong%n<gray>in other categories", 42);

    public final Material material;
    public final String name;
    public final String lore;
    public final int slot;

    PlayerActionItems(Material material, String name, String lore, int slot) {
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
