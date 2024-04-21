package hypersquare.hypersquare.util.string;

import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ClickTexts {
    public static final Component LEFT_CLICK = createClickComponent(Colors.LIME, Colors.GRAY_DARK, Colors.GRAY);
    public static Component RIGHT_CLICK = createClickComponent(Colors.GRAY, Colors.GRAY_DARK, Colors.LIME);
    public static Component MIDDLE_CLICK = createClickComponent(Colors.GRAY, Colors.LIME, Colors.GRAY);

    private static Component createClickComponent(TextColor leftColor, TextColor middleColor, TextColor rightColor) {
        return Component.empty().decoration(TextDecoration.ITALIC, false)
            .append(Component.text("⏴").color(leftColor))
            .append(Component.text("|").color(middleColor))
            .append(Component.text("⏵").color(rightColor));
    }
}
