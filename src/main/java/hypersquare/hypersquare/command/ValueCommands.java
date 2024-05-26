package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.value.CodeValues;
import hypersquare.hypersquare.dev.value.impl.LocationValue;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.play.error.HSException;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ValueCommands implements HyperCommand {

    private final static RequiredArgumentBuilder<CommandSourceStack, String> valueArg = RequiredArgumentBuilder.argument("value", StringArgumentType.greedyString());

    private static String getValArg(CommandContext<?> ctx) {
        return StringArgumentType.getString(ctx, "value");
    }

    private static int exec(@NotNull CommandContext<CommandSourceStack> ctx, Consumer<Player> x) {
        if (ctx.getSource().getBukkitSender() instanceof Player player) {
            if (!Hypersquare.mode.get(player).equals("coding")) return DONE;
            x.accept(player);
        }
        return DONE;
    }

    private static int execValArg(@NotNull CommandContext<CommandSourceStack> ctx, BiConsumer<Player, String> x) {
        return exec(ctx, p -> {
            String v = getValArg(ctx);
            try {
                x.accept(p, v);
            } catch (Exception ignored) {
                HSException.sendError(p, "Invalid input: '" + v + "'");
            }
        });
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        register(cd, false, CodeValues.NULL, "null", "nul");
        register(cd, true, CodeValues.STRING, "string", "str");
        register(cd, true, CodeValues.TEXT, "text", "txt");
        register(cd, true, CodeValues.NUMBER, "number", "num");
        register(cd, true, CodeValues.VARIABLE, "variable", "var");
        registerLocation(cd, "location", "loc");
    }

    private void register(CommandDispatcher<CommandSourceStack> cd, boolean useValArg, CodeValues value, String @NotNull ... cmds) {
        for (String alias : cmds) {
            var literal = literal(alias);
            if (useValArg) literal = literal.then(valueArg.executes(ctx -> execValArg(ctx, (p, v) -> {
                ItemStack item;
                try {
                    item = value.getItem(value.fromString(v, null));
                } catch (Exception ignored) {
                    HSException.sendError(p, "Invalid input: '" + v + "'");
                    return;
                }
                p.getInventory().addItem(item);
            })));
            else
                literal = literal.executes(ctx -> exec(ctx, p -> p.getInventory().addItem(value.getItem(value.defaultValue()))));
            cd.register(literal);
        }
    }

    private void registerLocation(CommandDispatcher<CommandSourceStack> cd, String @NotNull ... cmds) {
        for (String alias : cmds) {
            cd.register(literal(alias)
                .then(literal("get")
                    .then(valueArg
                        .executes(ctx -> execValArg(ctx, (p, v) -> {
                            ItemStack item = CodeValues.LOCATION.getItem(CodeValues.LOCATION.fromString(v, null));
                            p.getInventory().addItem(item);
                        }))
                    )
                )
                .then(literal("set")
                    .then(literal("x")
                        .then(valueArg
                            .executes(ctx -> execValArg(ctx, (p, v) -> {
                                LocationValue.HSLocation location = (LocationValue.HSLocation) CodeValues.LOCATION.fromItem(p.getInventory().getItemInMainHand());
                                ItemStack item = CodeValues.LOCATION.getItem(LocationValue.HSAxis.X.set(location, new DecimalNumber(Double.parseDouble(v))));
                                p.getInventory().setItemInMainHand(item);
                            }))
                        )
                    )
                    .then(literal("y")
                        .then(valueArg
                            .executes(ctx -> execValArg(ctx, (p, v) -> {
                                LocationValue.HSLocation location = (LocationValue.HSLocation) CodeValues.LOCATION.fromItem(p.getInventory().getItemInMainHand());
                                ItemStack item = CodeValues.LOCATION.getItem(LocationValue.HSAxis.Y.set(location, new DecimalNumber(Double.parseDouble(v))));
                                p.getInventory().setItemInMainHand(item);
                            }))
                        )
                    )
                    .then(literal("z")
                        .then(valueArg
                            .executes(ctx -> execValArg(ctx, (p, v) -> {
                                LocationValue.HSLocation location = (LocationValue.HSLocation) CodeValues.LOCATION.fromItem(p.getInventory().getItemInMainHand());
                                ItemStack item = CodeValues.LOCATION.getItem(LocationValue.HSAxis.Z.set(location, new DecimalNumber(Double.parseDouble(v))));
                                p.getInventory().setItemInMainHand(item);
                            }))
                        )
                    )
                    .then(literal("pitch")
                        .then(valueArg
                            .executes(ctx -> execValArg(ctx, (p, v) -> {
                                LocationValue.HSLocation location = (LocationValue.HSLocation) CodeValues.LOCATION.fromItem(p.getInventory().getItemInMainHand());
                                ItemStack item = CodeValues.LOCATION.getItem(LocationValue.HSAxis.PITCH.set(location, new DecimalNumber(Double.parseDouble(v))));
                                p.getInventory().setItemInMainHand(item);
                            }))
                        )
                    )
                    .then(literal("yaw")
                        .then(valueArg
                            .executes(ctx -> execValArg(ctx, (p, v) -> {
                                LocationValue.HSLocation location = (LocationValue.HSLocation) CodeValues.LOCATION.fromItem(p.getInventory().getItemInMainHand());
                                ItemStack item = CodeValues.LOCATION.getItem(LocationValue.HSAxis.YAW.set(location, new DecimalNumber(Double.parseDouble(v))));
                                p.getInventory().setItemInMainHand(item);
                            }))
                        )
                    )
                )
            );
        }
    }
}
