package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.plot.ChangeGameMode;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditSpawn implements HyperCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        cd.register(literal("editspawn").executes(ctx -> {
            CommandSender sender = ctx.getSource().getBukkitSender();
            if (sender instanceof Player player) {
                if (player.hasPermission("hypersquare.editspawn")) {
                    ChangeGameMode.editSpawn(player);
                } else {
                    HSException.sendError(player, "You do not have permission to execute this command.");
                }
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return DONE;
        }));
    }
}
