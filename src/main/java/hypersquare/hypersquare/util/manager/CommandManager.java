package hypersquare.hypersquare.util.manager;

import com.mojang.brigadier.CommandDispatcher;
import hypersquare.hypersquare.command.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;

import java.lang.reflect.Field;

public class CommandManager {

    public static void registerCommands() {
        register(
            new FlyCommand(),
            new BuildCommand(),
            new DeleteAllPlotsCommand(),
            new DevCommand(),
            new EditSpawn(),
            new FlightSpeedCommand(),
            new GivePlotsCommand(),
            new JoinCommand(),
            new LocateCommand(),
            new PlayCommand(),
            new SpawnCommand(),
            new PlotCommands(),
            new ValueCommands(),
            new ColorPaletteCommand()
        );
    }

    private static CommandDispatcher<CommandSourceStack> getDispatcher() {
        try {
            Field f = CraftServer.class.getDeclaredField("console");
            f.setAccessible(true);
            DedicatedServer nmsServer = (DedicatedServer) f.get(Bukkit.getServer());
            return nmsServer.vanillaCommandDispatcher.getDispatcher();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void register(HyperCommand... cmds) {
        CommandDispatcher<CommandSourceStack> cd = getDispatcher();
        for (HyperCommand cmd : cmds) {
            cmd.register(cd);
        }
    }
}
