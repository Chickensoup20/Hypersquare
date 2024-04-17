package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.plot.PlotManager;
import hypersquare.hypersquare.util.PlotUtilities;
import hypersquare.hypersquare.util.Utilities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocateCommand implements HyperCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        cd.register(literal("locate").then(argument("player", EntityArgument.player()).executes(ctx -> {
            locatePlayer(ctx, EntityArgument.getPlayer(ctx, "player").getBukkitEntity());
            return DONE;
        })).executes(ctx -> {
            if (ctx.getSource().getBukkitSender() instanceof Player player) {
                locatePlayer(ctx, player);
            }
            return DONE;
        }));
        cd.register(literal("find").then(argument("player", EntityArgument.player()).executes(ctx -> {
            locatePlayer(ctx, EntityArgument.getPlayer(ctx, "player").getBukkitEntity());
            return DONE;
        })).executes(ctx -> {
            if (ctx.getSource().getBukkitSender() instanceof Player player) {
                locatePlayer(ctx, player);
            }
            return DONE;
        }));
    }

    private void locatePlayer(CommandContext<CommandSourceStack> ctx, Player target) {
        CommandSender self = ctx.getSource().getBukkitSender();
        if (target == null)
        {
            HSException.sendError(self,"This player is not online.");
            return;
        }
        String color = "<#AAD4FF>";
        String color2 = "<#AAAAFF>";
        String targetName = target.getName() + " is";
        if (target == self) targetName = "You are";
        String mode = Hypersquare.mode.get(target);
        if (mode.equals("at spawn") || mode.equals("editing spawn")) {
            List<String> messages = new ArrayList<>();
            messages.add(color + targetName + " currently <white>" + mode + color + " on:");
            messages.add(color2 + "→ " + color + "Server: <white>Node 1");
            Utilities.sendMultiMiniMessage(self, messages);
        } else {
            int plotID = PlotUtilities.getPlotId(target.getWorld());
            String cmd = "<click:run_command:/join " + plotID + "><hover:show_text:'<color:#AAD4AA>Click to join'>";
            List<String> messages = new ArrayList<>();
            String plotName = PlotManager.getPlotName(plotID);
            messages.add(color + targetName + cmd + " currently <white>" + mode + color + " on:");
            messages.add("");
            messages.add(color2 + cmd + "→ " + "<white>" + plotName + " <dark_gray>[" + color + plotID + "<dark_gray>]");
            messages.add(color2 + cmd + "→ " + color + "Owner: <white>" + Bukkit.getOfflinePlayer(UUID.fromString(PlotManager.getPlotOwner(plotID))).getName());
            messages.add(color2 + cmd + "→ " + color + "Server: <white>Node " + PlotManager.getPlotNode(plotID));
            Utilities.sendMultiMiniMessage(self, messages, true);
        }
    }
}
