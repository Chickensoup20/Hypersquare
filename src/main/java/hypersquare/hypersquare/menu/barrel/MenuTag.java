package hypersquare.hypersquare.menu.barrel;

import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.TagOptionsData;
import hypersquare.hypersquare.dev.value.impl.VariableValue;
import hypersquare.hypersquare.menu.system.MenuItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class MenuTag extends MenuItem {

    public final BarrelTag tag;

    public MenuTag(BarrelTag tag, TagOptionsData data) {
        super(currentOption(tag, data).icon());
        this.tag = tag;

        name(Component.text("Tag: ")
            .decoration(TextDecoration.ITALIC, false)
            .color(Colors.MUSTARD)
            .append(Component.text(tag.name()))
        );

        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());

        VariableValue.HSVar var = data.getTags().getOrDefault(tag.id(), new Pair<>(null, null)).getB();
        if (var != null) {
            lore.add(Component.text("[").decoration(TextDecoration.ITALIC, false)
                .append(Component.text(var.scope().letter))
                .append(Component.text("]"))
                .color(var.scope().color)
                .append(Component.text(" " + var.name())
                    .color(NamedTextColor.WHITE))
            );
            lore.add(Component.text("» ").decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GREEN)
                .decoration(TextDecoration.BOLD, false)
                .append(Component.text(" Shift click to remove.")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.BOLD, false))
            );
            lore.add(Component.empty());
        }

        for (BarrelTag.Option option : tag.options()) {
            if (option == currentOption(tag, data)) {
                lore.add(Component.text("» ")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(Colors.SKY_DARK)
                    .append(Component.text(option.text())
                        .color(Colors.SKY))
                );
            } else {
                lore.add(Component.text("» ")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(Colors.GRAY_DARK)
                    .append(Component.text(option.text())
                        .color(Colors.GRAY))
                );
            }
        }

        lore(lore);
        hideAllFlags();
    }

    public static BarrelTag.Option currentOption(@NotNull BarrelTag tag, @NotNull TagOptionsData data) {
        return tag.getOption(data.getTags().getOrDefault(tag.id(), new Pair<>(tag.defaultOption().name(), null)).getA());
    }
}
