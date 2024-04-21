package hypersquare.hypersquare.util;

import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * NOTICE: This utility was developer as part of AeolusLib. While you can use it for your own projects, You are NOT allowed to delete or move this header comment.
 * Utility:
 * Chainable {@link ItemStack}s
 *
 * @author MCAeolus, modified by RedVortex_
 * @version 1.1
 */

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta itemM;

    /**
     * Init item chainable via given Material parameter.
     *
     * @param itemType the {@link Material} to initiate the instance with.
     * @since 1.0
     */
    public ItemBuilder(final Material itemType) {
        item = new ItemStack(itemType);
        itemM = item.getItemMeta();
    }

    /**
     * Init item chainable via given ItemStack parameter.
     *
     * @param itemStack the {@link ItemStack} to initialize the instance with.
     * @since 1.0
     */
    public ItemBuilder(final ItemStack itemStack) {
        item = itemStack;
        itemM = item.getItemMeta();
    }

    /**
     * Init the item chainable with no defined Material/ItemStack
     *
     * @since 1.0
     */
    public ItemBuilder() {
        item = new ItemStack(Material.AIR);
        itemM = item.getItemMeta();
    }

    /**
     * Changes the Material type of the {@link ItemStack}
     *
     * @param material the new {@link Material} to set for the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder type(final Material material) {
        build().setType(material);
        return this;
    }

    /**
     * Changes the {@link ItemStack}s size.
     *
     * @param itemAmt the new Integer count of the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder amount(final Integer itemAmt) {
        build().setAmount(itemAmt);
        return this;
    }

    /**
     * Changes the {@link ItemStack}s display name.
     *
     * @param name the new Component for the ItemStack's display name to be set to.
     * @return the current instance for chainable application.
     * @since 1.1
     */
    public ItemBuilder name(final Component name) {
        meta().displayName(name);
        build().setItemMeta(meta());
        return this;
    }

    /**
     * Adds a line of lore to the {@link ItemStack}
     *
     * @param lore String you want to add to the ItemStack's lore.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    @Deprecated
    public ItemBuilder lore(final String lore) {
        List<String> lores = meta().getLore();
        if (lores == null) {
            lores = new ArrayList<>();
        }
        lores.add(lore);
        meta().setLore(lores);
        build().setItemMeta(meta());
        return this;
    }

    /**
     * Adds a line of lore to the {@link ItemStack}
     *
     * @param lore Component you want to add to the ItemStack's lore.
     * @return the current instance for chainable application.
     * @since 1.1
     */
    public ItemBuilder lore(final Component lore) {
        List<Component> lores = meta().lore();
        if (lores == null) {
            lores = new ArrayList<>();
        }
        lores.add(MiniMessage.miniMessage().deserialize("<!italic>").append(lore));
        meta().lore(lores);
        build().setItemMeta(meta());
        return this;
    }

    /**
     * Adds a line of lore to the {@link ItemStack}
     *
     * @param lore Components you want to set to the ItemStack's lore.
     * @return the current instance for chainable application.
     * @since 1.1
     */
    public ItemBuilder lore(final List<Component> lore) {
        for (Component line : lore) {
            lore(line);
        }
        return this;
    }

    /**
     * Clears the {@link ItemStack}s lore and replaces it with the defined Component list.
     *
     * @param lores Component list to set the ItemStack's lore to.
     * @return the current instance for chainable application.
     * @since 1.1
     */
    public ItemBuilder lores(final List<Component> lores) {
        meta().lore(lores);
        return this;
    }

    /**
     * Sets a custom String tag to the {@link ItemStack}
     *
     * @param key   The namespaced key to set the value to
     * @param value The value to set the key to
     */
    public ItemBuilder setCustomTag(NamespacedKey key, String value) {
        meta().getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        return this;
    }

    public ItemBuilder setCustomIntTag(NamespacedKey key, int value) {
        meta().getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        return this;
    }

    /**
     * Changes the durability of the current {@link ItemStack}
     *
     * @param durability the new int amount to set the ItemStack's durability to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder durability(final int durability) {
        ((Damageable) build()).setDamage((short) durability);
        return this;
    }

    /**
     * Changes the data value of the {@link ItemStack}
     *
     * @param data the new int data value (parsed as byte) to set the ItemStack's durability to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    @SuppressWarnings("deprecation")
    public ItemBuilder data(final int data) {
        build().setData(new MaterialData(build().getType(), (byte) data));
        return this;
    }

    /**
     * Adds an UnsafeEnchantment to the {@link ItemStack} with a defined level int value.
     *
     * @param enchantment the {@link Enchantment} to add to the ItemStack.
     * @param level       the int amount that the Enchantment's level will be set to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        build().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Adds an UnsafeEnchantment to the {@link ItemStack} with a level int value of 1.
     *
     * @param enchantment the {@link Enchantment} to add to the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment) {
        build().addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder hideFlags() {
        meta().addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta().addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta().addItemFlags(ItemFlag.HIDE_DYE);
        meta().addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta().addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        meta().addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta().addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta().setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder damage(int damage) {
        if (build() instanceof Damageable) {
            ((Damageable) build()).setDamage(damage);
        }
        return this;
    }

    public ItemBuilder setPotionColor(Color color) {
        if (meta() instanceof PotionMeta)
            ((PotionMeta) meta()).setColor(color);
        return this;
    }


    /**
     * Clears all {@link Enchantment}s from the current {@link ItemStack} then adds the defined array of Enchantments to the ItemStack.
     *
     * @param enchantments the Enchantment array to replace any current enchantments applied on the ItemStack.
     * @param level        the int level value for all Enchantments to be set to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantments(final Enchantment[] enchantments, final int level) {
        build().getEnchantments().clear();
        for (Enchantment enchantment : enchantments) {
            build().addUnsafeEnchantment(enchantment, level);
        }
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the current {@link ItemStack} then adds the defined array of Enchantments to the ItemStack with a level int value of 1.
     *
     * @param enchantments the Enchantment array to replace any current enchantments applied on the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantments(final Enchantment[] enchantments) {
        build().getEnchantments().clear();
        for (Enchantment enchantment : enchantments) {
            build().addUnsafeEnchantment(enchantment, 1);
        }
        return this;
    }

    /**
     * Clears the defined {@link Enchantment} from the {@link ItemStack}
     *
     * @param enchantment the Enchantment to remove from the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearEnchantment(final Enchantment enchantment) {
        Map<Enchantment, Integer> itemEnchantments = build().getEnchantments();
        for (Enchantment enchantmentC : itemEnchantments.keySet()) {
            if (enchantment == enchantmentC) {
                itemEnchantments.remove(enchantmentC);
            }
        }
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearEnchantments() {
        build().getEnchantments().clear();
        return this;
    }

    /**
     * Clears the defined {@link Component} of lore from the {@link ItemStack}
     *
     * @param lore the String to be removed from the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.1
     */
    public ItemBuilder clearLore(final Component lore) {
        if (!meta().lore().contains(lore)) return this;
        meta().lore().remove(lore);
        build().setItemMeta(meta());
        return this;
    }

    /**
     * Clears all lore {@link Component}s from the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.1
     */
    public ItemBuilder clearLores() {
        meta().lore().clear();
        build().setItemMeta(meta());
        return this;
    }

    /**
     * Sets the {@link Color} of any LEATHER_ARMOR {@link Material} types of the {@link ItemStack}
     *
     * @param color the Color to set the LEATHER_ARMOR ItemStack to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder color(final Color color) {
        if (build().getType() == Material.LEATHER_HELMET
            || build().getType() == Material.LEATHER_CHESTPLATE
            || build().getType() == Material.LEATHER_LEGGINGS
            || build().getType() == Material.LEATHER_BOOTS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) meta();
            meta.setColor(color);
            build().setItemMeta(meta);
        }
        return this;
    }

    /**
     * Clears the {@link Color} of any LEATHER_ARMOR {@link Material} types of the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearColor() {
        if (build().getType() == Material.LEATHER_HELMET
            || build().getType() == Material.LEATHER_CHESTPLATE
            || build().getType() == Material.LEATHER_LEGGINGS
            || build().getType() == Material.LEATHER_BOOTS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) meta();
            meta.setColor(null);
            build().setItemMeta(meta);
        }
        return this;
    }

    /**
     * Sets the skullOwner {@link SkullMeta} of the current SKULL_ITEM {@link Material} type {@link ItemStack}
     *
     * @param name the {@link String} value to set the SkullOwner meta to for the SKULL_ITEM Material type ItemStack.
     * @return the current instance for chainable application
     * @since 1.0
     */
    public ItemBuilder skullOwner(final String name) {
        if (build().getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) meta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
            build().setItemMeta(meta());
        }
        return this;
    }

    /**
     * Returns the {@link ItemMeta} of the {@link ItemStack}
     *
     * @return the ItemMeta of the ItemStack.
     */
    public ItemMeta meta() {
        return itemM;
    }

    /**
     * Returns the {@link ItemStack} of the {@link ItemBuilder} instance.
     *
     * @return the ItemStack of the ItemBuilder instance.
     */
    public ItemStack build() {
        meta().displayName(Component.empty().color(Colors.WHITE).decoration(TextDecoration.ITALIC, false).append(meta().displayName()));
        item.setItemMeta(meta());
        return item;
    }

}
