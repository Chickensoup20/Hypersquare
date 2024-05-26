package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.commands.CommandSourceStack;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ColorPaletteCommand implements HyperCommand {
    final String square = "█";

    @NotNull
    private static List<String> getColorGroups(Field[] fields) {
        List<String> colorGroups = new ArrayList<>();
        for (Field field : fields) {
            if (field.getType().equals(TextColor.class)) {
                String colorName = field.getName();
                String colorGroup = colorName.split("_")[0];
                if (!colorGroups.contains(colorGroup)) {
                    if (colorGroup.equals("WHITE") || colorGroup.equals("BLACK")) continue;
                    colorGroups.add(colorGroup);
                }
            }
        }
        return colorGroups;
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        for (String alias : List.of("colorpalette", "palette", "colorpal"))
            cd.register(literal(alias)
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getBukkitSender();
                    if (sender instanceof Player player) {
                        Component finalMessage = Component.empty();

                        finalMessage = finalMessage.append(
                            Component.text("◀ ")
                                .color(TextColor.color(Colors.SKY))
                                .append(Component.text(" ".repeat(8))
                                    .color(Colors.SKY_DARK)
                                    .decoration(TextDecoration.STRIKETHROUGH, true)
                                )
                                .append(Component.text(" COLOR PALETTE ")
                                    .color(Colors.SKY_LIGHT))
                                .append(Component.text(" ".repeat(8))
                                    .color(Colors.SKY_DARK)
                                    .decoration(TextDecoration.STRIKETHROUGH, true)
                                )
                                .append(Component.text(" ▶"))
                        );

                        Field[] fields = Colors.class.getFields();
                        List<String> colorGroups = getColorGroups(fields);

                        for (String colorGroup : colorGroups) {
                            finalMessage = finalMessage.appendNewline();
                            finalMessage = finalMessage.append(Component.text("  ").append(generateColorStrip(colorGroup, fields)));
                        }

                        finalMessage = finalMessage.appendNewline();
                        finalMessage = finalMessage.append(
                            Component.text("◀ ")
                                .color(TextColor.color(Colors.SKY))
                                .append(Component.text(" ".repeat(37))
                                    .color(Colors.SKY_DARK)
                                    .decoration(TextDecoration.STRIKETHROUGH, true)
                                )
                                .append(Component.text(" ▶"))
                        );

                        player.sendMessage(finalMessage);
                    } else {
                        sender.sendMessage("This command can only be used by players.");
                    }
                    return DONE;
                })
            );
    }

    @NotNull
    private Component generateColorStrip(String colorGroup, Field[] fields) {
        Component message = Component.empty();
        for (Field field : fields) {
            if (field.getType().equals(TextColor.class)) {
                String colorName = field.getName();
                if (colorGroup.equals("GRAY") && (colorName.equals("WHITE") || colorName.equals("BLACK"))) {
                    message = message.append(generateColorSquare(field));
                }
                if (colorName.startsWith(colorGroup)) {
                    message = message.append(generateColorSquare(field));
                }
            }
        }
        return message;
    }

    private Component generateColorSquare(Field field) {
        try {
            TextColor color = (TextColor) field.get(null);
            String colorTag = "<" + color.asHexString() + ">";
            return Component.text(this.square)
                .color(color)
                .hoverEvent(
                    Component.text(WordUtils.capitalize(field.getName().replace('_', ' ').toLowerCase()))
                        .color(TextColor.color(color))
                        .append(Component.text(" - ")
                            .color(Colors.GRAY))
                        .append(Component.text(color.asHexString()))
                        .appendNewline()
                        .append(Component.text("Click to copy")
                            .color(Colors.GRAY_DARK))
                        .appendNewline()
                        .append(Component.text("Shift-click to insert")
                            .color(Colors.GRAY_DARK))
                        .decoration(TextDecoration.ITALIC, false)
                )
                .clickEvent(ClickEvent.copyToClipboard(colorTag))
                .insertion(colorTag);
        } catch (IllegalAccessException ignored) {
            return Component.empty();
        }
    }
}
