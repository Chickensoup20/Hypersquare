package hypersquare.hypersquare.menu.system;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MenuItem {
    public final ItemStack item;
    private @Nullable Runnable clickHandler;

    public MenuItem(Material mat) {
        item = new ItemStack(mat);

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(""));
        meta.addItemFlags(
            ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS,
            ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS,
            ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM,
            ItemFlag.HIDE_PLACED_ON
        );

        item.setItemMeta(meta);
    }

    public MenuItem(ItemStack item) {
        this.item = item;
    }

    public MenuItem name(Component name) {
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);
        return this;
    }

    public MenuItem lore(List<Component> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.lore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public MenuItem material(Material mat) {
        item.setType(mat);
        return this;
    }

    public MenuItem onClick(Runnable onClick) {
        clickHandler = onClick;
        return this;
    }

    public void performClick() {
        if (clickHandler != null) clickHandler.run();
    }

    public void amount(int amount) {
        item.setAmount(amount);
    }

    public void flags(ItemFlag... flags) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flags);
        item.setItemMeta(meta);
    }

    public void hideAllFlags() {
        flags(ItemFlag.values());
    }
}
