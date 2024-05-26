package hypersquare.hypersquare.util.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Class to create Components that come un-italicized out of the box
 */
public class BasicComponent {
    /**
     * Creates a plain text component in white.
     *
     * @param text Content
     * @return Component
     */
    public static Component create(String text) {
        return Component.text(text).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE);
    }

    /**
     * Creates an empty plain text component in white.
     *
     * @return Component
     */
    public static Component create() {
        return create("");
    }

    /**
     * Creates a plain text component in gray.
     *
     * @param text Content
     * @return Component
     */
    public static Component gray(String text) {
        return Component.text(text).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY);
    }

    public static ComponentLike create(Object val) {
        return create(String.valueOf(val));
    }
}
