package hypersquare.hypersquare;

import hypersquare.hypersquare.Commands.*;
import hypersquare.hypersquare.Listeners.PlayerDeathListener;
import hypersquare.hypersquare.Listeners.PlayerJoinListener;
import hypersquare.hypersquare.Listeners.PlayerRightClickListener;
import hypersquare.hypersquare.Listeners.PlayerRespawnListener;
import lombok.Getter;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Hypersquare extends JavaPlugin {
    public static int lastUsedWorldNumber;
    private CommandManager commandManager;
    public static HashMap<Player, World> lastDeathLoc = new HashMap<>();
    public static HashMap<Player,String> mode = new HashMap<>();

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerRightClickListener(), this);
        pm.registerEvents(new PlayerRespawnListener(), this);
        pm.registerEvents(new PlayerDeathListener(), this);
        new InventoryAPI(this).init();
        ItemManager.initializeKey(this);
        ItemManager.registerItems();
        loadLastUsedWorldNumber();
        commandManager = new CommandManager(this);
        registerCommands(commandManager);

    }

    @Override
    public void onDisable() {
        saveLastUsedWorldNumber();
    }

    public static void registerCommands(CommandManager commandManager){

        commandManager.registerCommand("join", new JoinCommand());
        commandManager.registerCommand("dev", new DevCommand());
        commandManager.registerCommand("locate", new LocateCommand());
        commandManager.registerCommand("spawn", new SpawnCommand());
        commandManager.registerCommand("s", new SpawnCommand());
        commandManager.registerCommand("build", new BuildCommand());
        commandManager.registerCommand("play", new PlayCommand());
        commandManager.registerCommand("fs", new FlightSpeedCommand());
    }


    private void loadLastUsedWorldNumber() {
        // Load the last used world number from the configuration file
        FileConfiguration config = getConfig();
        if (config.contains("lastUsedWorldNumber")) {
            lastUsedWorldNumber = config.getInt("lastUsedWorldNumber");
        }
    }

    private void saveLastUsedWorldNumber() {
        // Save the last used world number to the configuration file
        FileConfiguration config = getConfig();
        config.set("lastUsedWorldNumber", lastUsedWorldNumber);
        saveConfig();
    }


}
