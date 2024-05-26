package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.plot.ChangeGameMode;
import hypersquare.hypersquare.util.PlotUtilities;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayCommand implements HyperCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        cd.register(literal("play").executes(ctx -> {
            CommandSender sender = ctx.getSource().getBukkitSender();
            if (sender instanceof Player player) {
                if (!Hypersquare.mode.get(player).equals("at spawn")) {
                    int plotID = PlotUtilities.getPlotId(player.getWorld());
                    ChangeGameMode.playMode(player, plotID);
                }
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return DONE;
        }));
    }
}
