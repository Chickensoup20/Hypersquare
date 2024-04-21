package hypersquare.hypersquare.dev.value;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.util.Utilities;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> HS Class type
 * @param <S> Java Class type
 */
public interface CodeValue<T, S> {
    Component getName();

    Material getMaterial();

    String getTypeId();

    List<Component> getDescription();

    List<Component> getHowToSet();

    JsonObject getVarItemData(T type);

    default boolean isUnsetable() {
        return false;
    }

    default Component getValueName(T value) { // Defaults to the CodeValue's name
        return getName().decoration(TextDecoration.ITALIC, false);
    }

    default void onRightClick(Player player, T value) {
    }

    default void onLeftClick(Player player, T value) {
    }

    default void onShiftRightClick(Player player, T value) {
    }

    default void onShiftLeftClick(Player player, T value) {
    }

    T fromJson(JsonObject obj);

    T defaultValue();

    T fromString(String data, T previous);

    S realValue(T value);

    JsonObject serialize(Object obj);

    default boolean isType(JsonObject d) {
        try {
            return d.get("type").getAsString().equals(getTypeId());
        } catch (Exception err) {
            return false;
        }
    }

    default T coerce(JsonObject obj) {
        CodeValues hsVal = CodeValues.getType(obj);
        if (hsVal == null) return null;
        try {
            Object val = hsVal.realValue(hsVal.fromJson(obj));
            if (val instanceof ItemStack item) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) val = meta.displayName();
                if (val == null) {
                    val = Utilities.capitalizeAll(item.getType().name().toLowerCase().replace("_", " "));
                }
            }
            if (val instanceof Component c) {
                val = PlainTextComponentSerializer.plainText().serialize(c);
            }
            return fromString(String.valueOf(val), null);
        } catch (Exception ignored) {
            return null;
        }
    }

    default ItemStack getItem(T value) {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(getValueName(value).decoration(TextDecoration.ITALIC, false));

        JsonObject obj = getVarItemData(value);
        obj.addProperty("type", getTypeId());
        meta.getPersistentDataContainer().set(
            new NamespacedKey(Hypersquare.pluginName, "varitem"),
            PersistentDataType.STRING,
            obj.toString()
        );

        item.setItemMeta(meta);
        return item;
    }

    default T fromItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        String varItemData = meta.getPersistentDataContainer().get(
            new NamespacedKey(Hypersquare.pluginName, "varitem"),
            PersistentDataType.STRING
        );
        if (varItemData == null) return null;

        try {
            JsonObject value = JsonParser.parseString(varItemData).getAsJsonObject();
            if (!value.get("type").getAsString().equals(getTypeId()))
                throw new IllegalArgumentException("Invalid fromItem call.");
            return fromJson(value);
        } catch (Exception err) {
            return null;
        }
    }

    default ItemStack emptyValue() {
        ItemStack item = getItem(defaultValue());
        ItemMeta meta = item.getItemMeta();

        meta.displayName(getName().decoration(TextDecoration.ITALIC, false));

        List<Component> lore = new ArrayList<>(List.of());
        lore.addAll(getDescription());

        if (!getHowToSet().isEmpty()) {
            lore.add(Component.empty());
            lore.add(Component.text("How to set:").color(Colors.PINK_LIGHT).decoration(TextDecoration.ITALIC, false));

            lore.addAll(getHowToSet());
        }

        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }
}
