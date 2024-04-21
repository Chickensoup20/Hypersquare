package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import hypersquare.hypersquare.util.Utilities;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlightSpeedCommand implements HyperCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        cd.register(literal("fs").executes(ctx -> {
                Utilities.sendUsageError(ctx.getSource().getBukkitSender(), "/fs <percentage>");
                return DONE;
            }).then(
                argument("percentage", IntegerArgumentType.integer(0, 1000))
                    .executes(ctx -> {
                        CommandSender sender = ctx.getSource().getBukkitSender();
                        if (sender instanceof Player player) {
                            player.setFlySpeed((float) ctx.getArgument("percentage", Integer.class) / 1000);
                        } else {
                            sender.sendMessage("This command can only be used by players.");
                        }
                        return DONE;
                    })
            )
        );
    }
}
