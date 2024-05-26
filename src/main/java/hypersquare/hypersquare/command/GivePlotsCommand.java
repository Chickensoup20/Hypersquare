package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.plot.PlayerDatabase;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import org.bukkit.entity.Player;

import java.util.List;

public class GivePlotsCommand implements HyperCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        for (String plotSize : List.of("basic", "large")) {
            cd.register(literal("giveplots").then(argument("player", EntityArgument.player())
                .then(literal(plotSize).then(argument("amount", IntegerArgumentType.integer())
                    .executes(ctx -> {
                        int amount = ctx.getArgument("amount", Integer.class);
                        Player target;
                        try {
                            target = EntityArgument.getPlayer(ctx, "player").getBukkitEntity();
                        } catch (Exception e) {
                            HSException.sendError(ctx.getSource().getBukkitSender(), "That player is not online.");
                            return DONE;
                        }

                        if (!ctx.getSource().getBukkitSender().hasPermission("hypersquare.giveplots")) {
                            HSException.sendError(ctx.getSource().getBukkitSender(), "");
                            return DONE;
                        }

                        PlayerDatabase.increaseMaxPlots(target.getUniqueId(), plotSize, amount);
                        String plotsmessage = "plot";
                        if (amount != 1) plotsmessage = "plots";
                        Utilities.sendInfo(ctx.getSource().getBukkitSender(), Component.text("Gave " + amount + " " + plotSize + " " + plotsmessage + " to " + target.getName()));
                        Utilities.sendInfo(target, Component.text("Recieved " + amount + " " + plotSize + " " + plotsmessage + " from " + ctx.getSource().getBukkitSender().getName()));

                        return DONE;
                    })
                ))
            ).executes(ctx -> {
                Utilities.sendUsageError(ctx.getSource().getBukkitSender(), "Usage: /giveplot <player> <type> <amount>");
                return DONE;
            }));
        }
    }
}
