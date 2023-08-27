package hypersquare.hypersquare.Commands;

import hypersquare.hypersquare.ChangeGameMode;
import hypersquare.hypersquare.Plot;
import hypersquare.hypersquare.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Dev implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int plotID = Utilities.getPlotID(player.getWorld());
            ChangeGameMode.devMode(player,plotID);
            Plot.loadRules(plotID,"dev");
        } else {
            sender.sendMessage("This command can only be used by players.");
        }
        return true;
    }
}
