package hypersquare.hypersquare.dev.value.impl;

import com.google.gson.JsonObject;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.value.CodeValue;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.util.component.BasicComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.List;

public class StringValue implements CodeValue<String, String> {
    @Override
    public Component getName() {
        return Component.text("String").color(DisplayValue.STRING.color);
    }

    @Override
    public Material getMaterial() {
        return Material.STRING;
    }

    @Override
    public String getTypeId() {
        return "string";
    }

    @Override
    public String defaultValue() {
        return "";
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
            BasicComponent.gray("A series of characters which"),
            BasicComponent.gray("is highly manipulatable."),
            BasicComponent.gray("Recommended for variable"),
            BasicComponent.gray("operations")
        );
    }

    public List<Component> getHowToSet() {
        return List.of(
            BasicComponent.gray("Type in chat while holding"),
            BasicComponent.gray("this item."),
            Hypersquare.cleanMM.deserialize("<!italic><gray>E.g. '<white>Sample Text</white>'")
        );
    }

    @Override
    public JsonObject getVarItemData(String value) {
        JsonObject varData = new JsonObject();
        varData.addProperty("value", value);
        return varData;
    }


    @Override
    public String fromJson(JsonObject obj) {
        return obj.get("value").getAsString();
    }

    @Override
    public String fromString(String data, String previous) {
        return data;
    }

    @Override
    public Component getValueName(String value) {
        return Component.text(value).color(NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public String realValue(String value) {
        return value;
    }

    @Override
    public JsonObject serialize(Object obj) {
        if (obj instanceof String str) {
            return getVarItemData(str);
        }
        return null;
    }
}
