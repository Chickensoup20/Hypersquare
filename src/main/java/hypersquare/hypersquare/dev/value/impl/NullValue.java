package hypersquare.hypersquare.dev.value.impl;

import com.google.gson.JsonObject;
import hypersquare.hypersquare.dev.value.CodeValue;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.util.component.BasicComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.List;

public class NullValue implements CodeValue<String, String> {
    @Override
    public Component getName() {
        return Component.text("Null").color(DisplayValue.NULL.color)
            .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public Material getMaterial() {
        return Material.STRUCTURE_VOID;
    }

    @Override
    public String getTypeId() {
        return "null";
    }

    @Override
    public String defaultValue() {
        return null;
    }

    @Override
    public boolean isUnsetable() {
        return true;
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
            BasicComponent.gray("Represents a null value."),
            BasicComponent.gray("Typically used as a"),
            BasicComponent.gray("non-existent value."),
            BasicComponent.gray("Returned by failed variable"),
            BasicComponent.gray("actions.")
        );
    }

    public List<Component> getHowToSet() {
        return List.of();
    }

    @Override
    public JsonObject getVarItemData(String value) {
        return new JsonObject();
    }

    @Override
    public String fromJson(JsonObject obj) {
        return "null";
    }

    @Override
    public String fromString(String data, String previous) {
        return null;
    }

    @Override
    public String realValue(String value) {
        return "null";
    }

    @Override
    public JsonObject serialize(Object obj) {
        return null;
    }
}
