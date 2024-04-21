package hypersquare.hypersquare.item.action;

import hypersquare.hypersquare.util.color.Colors;
import hypersquare.hypersquare.util.component.BasicComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public enum SpecialActionType {
    ADVANCED(Colors.ORANGE_LIGHT, "Advanced"),
    DEV(Colors.ROSE, "Dev");

    final TextColor color;
    final String name;

    SpecialActionType(TextColor color, String name) {
        this.color = color;
        this.name = name;
    }

    public Component getComp() {
        return BasicComponent.create(name).color(color);
    }
}
