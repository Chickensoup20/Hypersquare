package hypersquare.hypersquare.dev.value;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.value.impl.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public enum CodeValues implements CodeValue {
    NULL(new NullValue()),
    NUMBER(new NumberValue()),
    STRING(new StringValue()),
    TEXT(new TextValue()),
    ITEM(new ItemValue()),
    VARIABLE(new VariableValue()),
    LOCATION(new LocationValue());

    private final CodeValue v;

    CodeValues(CodeValue v) {
        this.v = v;
    }

    public static JsonObject toJson(Object obj) {
        for (CodeValues v : CodeValues.values()) {
            JsonObject result = v.serialize(obj);
            if (result != null) {
                result.addProperty("type", v.getTypeId());
                return result;
            }
        }
        return null;
    }

    public static CodeValues getType(JsonObject obj) {
        for (CodeValues v : CodeValues.values()) {
            if (v.isType(obj)) return v;
        }
        return null;
    }

    public static JsonObject getVarItemData(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        String data = meta.getPersistentDataContainer().get(new NamespacedKey(Hypersquare.pluginName, "varitem"), PersistentDataType.STRING);
        if (data == null) return null;
        try {
            return JsonParser.parseString(data).getAsJsonObject();
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public Component getName() {
        return v.getName();
    }

    @Override
    public Material getMaterial() {
        return v.getMaterial();
    }

    @Override
    public String getTypeId() {
        return v.getTypeId();
    }

    @Override
    public List<Component> getDescription() {
        return v.getDescription();
    }

    @Override
    public List<Component> getHowToSet() {
        return null;
    }

    @Override
    public JsonObject getVarItemData(Object type) {
        return v.getVarItemData(type);
    }

    public boolean isUnsetable() {
        return v.isUnsetable();
    }

    @Override
    public Component getValueName(Object value) {
        return v.getValueName(value);
    }

    @Override
    public void onRightClick(Player player, Object value) {
        CodeValue.super.onRightClick(player, value);
        v.onRightClick(player, value);
    }

    @Override
    public void onShiftRightClick(Player player, Object value) {
        CodeValue.super.onShiftRightClick(player, value);
        v.onShiftRightClick(player, value);
    }

    @Override
    public void onLeftClick(Player player, Object value) {
        CodeValue.super.onLeftClick(player, value);
        v.onLeftClick(player, value);
    }

    @Override
    public void onShiftLeftClick(Player player, Object value) {
        CodeValue.super.onShiftLeftClick(player, value);
        v.onShiftLeftClick(player, value);
    }

    @Override
    public Object fromJson(JsonObject obj) {
        return v.fromJson(obj);
    }

    @Override
    public Object defaultValue() {
        return v.defaultValue();
    }

    @Override
    public Object fromString(String data, Object previous) {
        return v.fromString(data, previous);
    }

    @Override
    public Object realValue(Object value) {
        return v.realValue(value);
    }

    @Override
    public ItemStack getItem(Object value) {
        return v.getItem(value);
    }

    @Override
    public Object fromItem(ItemStack item) {
        return v.fromItem(item);
    }

    @Override
    public JsonObject serialize(Object obj) {
        return v.serialize(obj);
    }
}
