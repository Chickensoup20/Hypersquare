package hypersquare.hypersquare.menu.system;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class Menu {

    public static final HashMap<Player, Menu> openMenus = new HashMap<>();
    public final HashMap<Integer, MenuItem> items = new HashMap<>();
    private final Inventory inventory;

    /**
     * Creates a new menu
     *
     * @param title Inventory title
     * @param rows  Amount of rows (1-6)
     */
    public Menu(Component title, int rows) {
        inventory = Bukkit.createInventory(null, rows * 9, title);
    }

    /**
     * Sets an item in the menu
     *
     * @param slot Slot to set item in
     * @param item Item to set
     * @return {@link Menu}
     */
    public Menu slot(int slot, @NotNull MenuItem item) {
        inventory.setItem(slot, item.item);
        items.put(slot, item);
        return this;
    }

    /**
     * Sets all the items in a menu
     *
     * @param items Items to set
     * @return {@link Menu}
     * @implNote Menu items don't clear before this
     */
    public Menu items(@NotNull List<MenuItem> items) {
        for (int i = 0; i < items.size(); i++) {
            slot(i, items.get(i));
        }
        return this;
    }

    /**
     * Gets the inventory size of the menu
     *
     * @return Inventory size
     */
    public int getSize() {
        return inventory.getSize();
    }

    /**
     * Inserts an item to the menu, replacing the first empty slot,
     * or does nothing if no empty slot is found.
     *
     * @param item Item to insert
     */
    public void addItem(MenuItem item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack current = inventory.getItem(i);
            if (current == null || current.getType() == Material.AIR) {
                slot(i, item);
                return;
            }
        }
    }

    /**
     * Runs the click event for the item in the specified slot
     *
     * @param event The event that caused this.
     */
    public void performClick(@NotNull InventoryClickEvent event) {
        if (!items.containsKey(event.getSlot())) return;
        items.get(event.getSlot()).performClick();
    }

    /**
     * Opens the menu for the player
     */
    public void open(@NotNull Player player) {
        player.openInventory(inventory);
        openMenus.put(player, this);
    }

    public void shiftClick(InventoryClickEvent event) {
    }

    public void middleClick(InventoryClickEvent event) {
    }
}
