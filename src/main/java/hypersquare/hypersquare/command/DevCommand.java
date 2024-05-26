package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.plot.ChangeGameMode;
import hypersquare.hypersquare.plot.PlotDatabase;
import hypersquare.hypersquare.util.PlotUtilities;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DevCommand implements HyperCommand {


    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        cd.register(literal("dev").executes(this::run));
        cd.register(literal("code").executes(this::run));
    }

    private int run(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getBukkitSender();
        if (sender instanceof Player player) {
            int plotID = PlotUtilities.getPlotId(player.getWorld());
            if (plotID != 0) {
                if (PlotDatabase.getRawDevs(plotID).contains(player.getUniqueId().toString())) {
                    ChangeGameMode.devMode(player, plotID);
                } else {
                    HSException.sendError(player, "You do not have dev permissions for this plot!");
                }
            } else {
                HSException.sendError(player, "You must be on a plot!");
            }
        } else {
            sender.sendMessage("This command can only be used by players.");
        }
        return DONE;
    }


}
