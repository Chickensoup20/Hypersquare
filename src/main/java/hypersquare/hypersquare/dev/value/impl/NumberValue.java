package hypersquare.hypersquare.dev.value.impl;

import com.google.gson.JsonObject;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.value.CodeValue;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.util.component.BasicComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.List;

public class NumberValue implements CodeValue<Long, DecimalNumber> {

    @Override
    public Component getName() {
        return Component.text("Number").color(DisplayValue.NUMBER.color);
    }

    @Override
    public Material getMaterial() {
        return Material.SLIME_BALL;
    }

    @Override
    public String getTypeId() {
        return "number";
    }

    @Override
    public Long defaultValue() {
        return 0L;
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
            BasicComponent.gray("Represents a number of an"),
            BasicComponent.gray("integer or a decimal. It can"),
            BasicComponent.gray("have up to 6 decimal places.")
        );
    }

    @Override
    public List<Component> getHowToSet() {
        return List.of(
            BasicComponent.gray("Type in chat while holding"),
            BasicComponent.gray("this item."),
            Hypersquare.cleanMM.deserialize("<!italic><gray>E.g. <white>4</white> or <white>1.25</white>")
        );
    }

    @Override
    public JsonObject getVarItemData(Long value) {
        JsonObject object = new JsonObject();
        object.addProperty("value", value);
        return object;
    }

    @Override
    public Long fromJson(JsonObject obj) {
        return obj.get("value").getAsLong();
    }

    @Override
    public Long fromString(String data, Long previous) {
        String whole = data;
        String decimal = "";
        if (data.contains(".")) {
            whole = data.substring(0, data.indexOf("."));
            decimal = data.substring(data.indexOf(".") + 1);
        }

        if (decimal.length() > 6) decimal = decimal.substring(0, 6);
        decimal = decimal + "0".repeat(6 - decimal.length());

        return Long.parseLong(whole + decimal);
    }

    @Override
    public Component getValueName(Long value) {
        return Component.text(new DecimalNumber(0, value).toString()).color(NamedTextColor.RED)
            .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public DecimalNumber realValue(Long value) {
        return new DecimalNumber(0, value);
    }

    @Override
    public JsonObject serialize(Object obj) {
        if (obj instanceof Double d) return getVarItemData(new DecimalNumber(d).rawData());
        if (obj instanceof Float f) return getVarItemData(new DecimalNumber(f).rawData());
        if (obj instanceof Long l) return getVarItemData(new DecimalNumber(l).rawData());
        if (obj instanceof Integer i) return getVarItemData(new DecimalNumber(i).rawData());
        if (obj instanceof Short s) return getVarItemData(new DecimalNumber(s).rawData());
        if (obj instanceof Byte b) return getVarItemData(new DecimalNumber(b).rawData());
        if (obj instanceof DecimalNumber d) return getVarItemData(d.rawData());
        return null;
    }
}
