package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.value.CodeValues;
import hypersquare.hypersquare.util.Utilities;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ValueCommands implements HyperCommand {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        register(cd, CodeValues.STRING, "string", "str");
        register(cd, CodeValues.TEXT, "text", "txt");
        register(cd, CodeValues.NUMBER, "number", "num");
    }

    private void register(CommandDispatcher<CommandSourceStack> cd, CodeValues value, String... cmds) {
        for (String alias : cmds) {
            cd.register(literal(alias)
                .then(argument("value", StringArgumentType.greedyString())
                    .executes(ctx -> {
                        if (ctx.getSource().getBukkitSender() instanceof Player player) {
                            if (!Hypersquare.mode.get(player).equals("coding")) return DONE;
                            String v = StringArgumentType.getString(ctx, "value");
                            try {
                                @SuppressWarnings("unchecked")
                                ItemStack item = value.getItem(value.fromString(v, null));
                                player.getInventory().addItem(item);
                            } catch (Exception ignored) {
                                Utilities.sendError(player, "Invalid input: '" + v + "'");
                            }
                        }
                        return DONE;
                    })
                )
            );
        }
    }
}
