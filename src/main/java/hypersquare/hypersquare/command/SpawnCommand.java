package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import hypersquare.hypersquare.plot.ChangeGameMode;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements HyperCommand {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        cd.register(literal("spawn")
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getBukkitSender();
                if (sender instanceof Player player) {
                    ChangeGameMode.spawn(player);
                } else {
                    sender.sendMessage("This command can only be used by players.");
                }
                return DONE;
            })
        );
    }
}
