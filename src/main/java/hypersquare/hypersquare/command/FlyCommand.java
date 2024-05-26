package hypersquare.hypersquare.command;

import com.mojang.brigadier.CommandDispatcher;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements HyperCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        cd.register(literal("fly")
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getBukkitSender();
                if (sender instanceof Player player) {
                    if (player.getAllowFlight()) {
                        player.setAllowFlight(false);
                        Utilities.sendInfo(player, Component.text("Flight disabled."));
                    } else {
                        player.setAllowFlight(true);
                        Utilities.sendInfo(player, Component.text("Flight enabled."));
                    }
                } else {
                    sender.sendMessage("This command can only be used by players.");
                }
                return DONE;
            })
        );
    }
}
