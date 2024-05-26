package hypersquare.hypersquare.dev;

import hypersquare.hypersquare.item.value.DisplayValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record BarrelParameter(DisplayValue type, boolean plural, boolean optional, Component description, String id) {
    public BarrelParameter(DisplayValue type, Component description, String id) {
        this(type, false, false, description, id);
    }

    public BarrelParameter(DisplayValue type, boolean plural, boolean optional, Component description, List<Component> notes, String id) {
        this(type, plural, optional, addNotes(description, notes), id);
    }

    private static Component addNotes(Component description, @NotNull List<Component> notes) {
        if (notes.isEmpty()) return description;
        for (Component note : notes) {
            description = description.appendNewline() // \n<blue>⏵ <gray>[TEXT]
                .append(Component.text("⏵").color(NamedTextColor.BLUE).append(Component.text(" ").color(NamedTextColor.GRAY).append(note)));
        }
        return description;
    }
}

