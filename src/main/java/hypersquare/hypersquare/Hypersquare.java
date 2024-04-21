package hypersquare.hypersquare;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import hypersquare.hypersquare.dev.CodeItems;
import hypersquare.hypersquare.listener.*;
import hypersquare.hypersquare.menu.system.MenuListeners;
import hypersquare.hypersquare.play.execution.CodeExecutor;
import hypersquare.hypersquare.plot.*;
import hypersquare.hypersquare.util.manager.CommandManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.luckperms.api.LuckPerms;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class Hypersquare extends JavaPlugin {
    // Last death location - Used for respawning players where they died when not in play mode
    public static final HashMap<Player, World> lastDeathLoc = new HashMap<>();
    // Last swap hands timestamp - Used for quick swapping between dev and build mode (checking time diff)
    public static final HashMap<Player, Long> lastSwapHands = new HashMap<>();
    // Plot data - Contains plot size, owners, devs, etc.
    public static final HashMap<Player, List<Document>> plotData = new HashMap<>();
    // Last dev location - Used for teleporting back to the last dev location
    public static final HashMap<Player, Location> lastDevLocation = new HashMap<>();
    // Last build location - Used for teleporting back to the last build location
    public static final HashMap<Player, Location> lastBuildLocation = new HashMap<>();
    // Mode - Contains each player's mode (playing, coding, building, editing spawn, etc.)
    // TODO: Use enum instead of strings composed of verbs
    public static final HashMap<Player, String> mode = new HashMap<>();
    // Loaded plots - Contains all world-loaded plots
    public static final HashMap<Integer, List<Object>> loadedPlots = new HashMap<>();
    // Game unload timestamp - A plot's last game unload, used for world-unloading
    public static final HashMap<Integer, Long> gameUnloadTimestamp = new HashMap<>();
    // Local player data - Contains each player's data like max plot count for each type
    public static final HashMap<UUID, HashMap<String, Integer>> localPlayerData = new HashMap<>();
    // Cooldown - Generic per player cooldown
    public static final HashMap<UUID, Long> cooldownMap = new HashMap<>();
    // Code execution - Contains each plot's code execution instance
    public static final HashMap<Integer, CodeExecutor> codeExecMap = new HashMap<>();
    public static final int PLOT_VERSION = 5;
    public static final Random RANDOM = new Random(PLOT_VERSION * 12345L);
    public static final MiniMessage cleanMM = MiniMessage.builder()
        .tags(TagResolver.resolver(
            StandardTags.decorations(), StandardTags.color(), StandardTags.font(),
            StandardTags.gradient(), StandardTags.hoverEvent(), StandardTags.rainbow(),
            StandardTags.transition(), StandardTags.reset(), StandardTags.newline()
        )).build();
    public static final MiniMessage minimalMM = MiniMessage.builder()
        .tags(TagResolver.resolver(
            StandardTags.decorations(), StandardTags.color(), StandardTags.font(),
            StandardTags.gradient(), StandardTags.rainbow(),
            StandardTags.transition(), StandardTags.reset()
        )).build();
    public static final String pluginName = "hypersquare";
    public static final MiniMessage fullMM = MiniMessage.miniMessage();
    // Displayed locations - Contains current location display per player
    public static HashMap<Player, UUID> locationValueDisplays = new HashMap<>();
    public static String DB_PASS;
    public static String DB_NAME;
    public static MongoClient mongoClient;
    public static int lastUsedWorldNumber;

    public static Plugin instance;
    public static LuckPerms lpPlugin;
    public static SlimePlugin slimePlugin;

    public static Logger logger() {
        if (instance == null) return Bukkit.getLogger();
        return instance.getLogger();
    }

    /**
     * @deprecated Use Hypersquare.logger() instead.
     */
    @Deprecated
    @Override
    public @NotNull Logger getLogger() {
        return super.getLogger();
    }

    @Override
    public void onEnable() {
        getLogger().info("Hypersquare starting up...");
        instance = this;
        // Register Dependencies
        slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager"); // Slime
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            lpPlugin = provider.getProvider(); // LuckPerms
        }

        saveDefaultConfig();
        getConfig().addDefault("DB_PASS", "");
        DB_PASS = (String) getConfig().get("DB_PASS");
        getConfig().addDefault("DB_NAME", "devdb");
        DB_NAME = (String) getConfig().get("DB_NAME");
        mongoClient = MongoClients.create(Hypersquare.DB_PASS);
        PlotDatabase.init();
        PlayerDatabase.init();
        PlotStats.init();

        PluginManager pm = getServer().getPluginManager();
        List<Listener> listeners = List.of(
            new PlayerJoinListener(),
            new PlayerQuitListener(),

            new PlayerRightClickListener(),
            new PlayerMoveListener(),

            new PlayerRespawnListener(),
            new PlayerDeathListener(),

            new PaperServerListPingListener(),
            new MenuListeners(),
            new WorldLoadListener(),

            new PlayerGoToSpawnEvent(),
            new DevEvents(),
            new CodePlacement(),
            new PlayModeListener(),
            new PlotEvents()
        );

        for (Listener l : listeners) pm.registerEvents(l, this);

        loadLastUsedWorldNumber();
        CommandManager.registerCommands();

        CodeItems.register();
        MoveEntities.entityLoop();
        getServer().getScheduler().runTaskTimer(Hypersquare.instance, UnloadPlotsSchedule::run, 0L, TimeUnit.SECONDS.toMillis(2L));

        // Make sure no world's spawn chunks are kept in memory
        for (World world : Bukkit.getWorlds()) {
            world.setKeepSpawnInMemory(false);
        }

        getLogger().info("Hypersquare is ready!");
    }

    @Override
    public void onDisable() {
        saveLastUsedWorldNumber();
        getLogger().info("Byeee!!");
    }

    private void loadLastUsedWorldNumber() {
        // Load the last used world number from the configuration file
        lastUsedWorldNumber = PlotDatabase.getRecentPlotID();
    }

    private void saveLastUsedWorldNumber() {
        // Save the last used world number to the configuration file
        PlotDatabase.setRecentPlotID(lastUsedWorldNumber);
    }
}
