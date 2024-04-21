package hypersquare.hypersquare.plot;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.infernalsuite.aswm.api.exceptions.WorldLockedException;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import hypersquare.hypersquare.HSKeys;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.codefile.CodeFile;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.util.Utilities;
import hypersquare.hypersquare.util.WorldUtilities;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static hypersquare.hypersquare.util.Utilities.savePersistentData;

public class Plot {

    public static ItemStack getPlotItem(int plotID) {
        ItemStack plotItem = new ItemStack(Material.matchMaterial(PlotDatabase.getPlotIcon(plotID)));
        ItemMeta meta = plotItem.getItemMeta();
        if (meta == null) {
            plotItem.setType(Material.PAPER); // Edge case
            meta = Bukkit.getItemFactory().getItemMeta(Material.PAPER);
        }
        if (Hypersquare.PLOT_VERSION == PlotDatabase.getPlotVersion(plotID)) {
            meta.displayName(PlotDatabase.getPlotName(plotID));
        } else {
            String name = PlotDatabase.getRawPlotName(plotID);
            meta.displayName(Hypersquare.minimalMM.deserialize(name + "<red>" + " (Out of date)"));
        }
        List<Component> lore = new ArrayList<>();
        lore.add(MiniMessage.miniMessage()
            .deserialize("<dark_gray>" + PlotDatabase.getPlotSize(plotID) + " Plot")
            .decoration(TextDecoration.ITALIC, false));
        lore.add(MiniMessage.miniMessage().deserialize(""));
        lore.add(MiniMessage.miniMessage().deserialize(""));
        lore.add(MiniMessage.miniMessage()
            .deserialize("<dark_gray>ID: " + plotID)
            .decoration(TextDecoration.ITALIC, false));
        if (Hypersquare.PLOT_VERSION == PlotDatabase.getPlotVersion(plotID)) {
            lore.add(MiniMessage.miniMessage()
                .deserialize("<dark_gray>Plot version: " + PlotDatabase.getPlotVersion(plotID))
                .decoration(TextDecoration.ITALIC, false));
        } else {
            Component aa = MiniMessage.miniMessage()
                .deserialize("<red>Plot version: " + PlotDatabase.getPlotVersion(plotID))
                .decoration(TextDecoration.ITALIC, false);
            lore.add(aa);
        }

        // Make sure absolutely nothing is italic
        lore.replaceAll(component -> component.decoration(TextDecoration.ITALIC, false));

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        meta.displayName(Component.empty().color(Colors.WHITE).decoration(TextDecoration.ITALIC, false)
            .append(meta.displayName()));
        plotItem.setItemMeta(meta);
        return plotItem;
    }

    public static void createPlot(Player player, int plotID, SlimePlugin plugin, String ownerUUID, final String plotType) {
        PlayerDatabase.addPlot(player.getUniqueId(), plotType.replace("plot_template_", ""));
        Utilities.sendInfo(player, Component.text("Starting creation of new " + Utilities.capitalize(plotType.replace("plot_template_", "")) + " plot."));
        String worldName = "hs." + plotID;
        WorldUtilities.cloneWorld(plotType, worldName, _ -> {
            World w = Bukkit.getWorld(worldName);
            assert w != null;
            String capitalized = Utilities.capitalize(plotType.replace("plot_template_", ""));
            w.getPersistentDataContainer().set(HSKeys.PLOT_TYPE, PersistentDataType.STRING, capitalized);
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            w.setDifficulty(Difficulty.EASY);
            w.setSpawnLocation(10, 0, 10);

            WorldUtilities.cloneWorld("dev_template", "hs.code." + plotID, _ -> {
                PlotDatabase.addPlot(plotID, ownerUUID, "map", "<" + Utilities.randomHSVHex(0, 360, 97, 62) + ">" + Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID)).getName() + "'s Game", 1, "None", 0, Utilities.capitalize(plotType.replace("plot_template_", "")), Hypersquare.PLOT_VERSION);
                Objects.requireNonNull(Bukkit.getWorld("hs.code." + plotID)).getPersistentDataContainer().set(HSKeys.PLOT_TYPE, PersistentDataType.STRING, "Code");
                new CodeFile(Bukkit.getWorld("hs.code." + plotID)).setCode("[]");
                savePersistentData(w, plugin);
                PlotManager.loadPlot(plotID);
                ChangeGameMode.devMode(player, plotID);
                Hypersquare.plotData.put(player, PlotDatabase.getPlot(player.getUniqueId().toString()));
                PlotDatabase.setPlotSpawnLocation(plotID, new Location(Bukkit.getWorld(worldName), 10, 0, 10, 0, 0));
            });
        });
    }


    public static void loadPlot(int plotID, Player player, Runnable callback) {
        String worldName = "hs." + plotID;
        String codeWorldName = "hs.code." + plotID;
        player.closeInventory();

        SlimePlugin plugin = Hypersquare.slimePlugin;
        SlimeLoader file = plugin.getLoader("mongodb");
        SlimePropertyMap properties = new SlimePropertyMap();
        SlimeWorld buildTest = plugin.getWorld(worldName);
        SlimeWorld codeTest = plugin.getWorld(codeWorldName);
        if (!plugin.getLoadedWorlds().contains(buildTest)) {
            new Thread(() -> {
                Utilities.sendInfo(player, Component.text("Loading plot please wait..."));
                try {
                    // Load both dev and build worlds
                    if (!plugin.getLoadedWorlds().contains(buildTest)) {
                        SlimeWorld world = plugin.loadWorld(file, worldName, false, properties);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // Configure worlds
                                try {
                                    plugin.loadWorld(world);
                                } catch (UnknownWorldException | WorldLockedException | IOException e) {
                                    if (e instanceof UnknownWorldException)
                                        HSException.sendError(player, "That plot is vacant.");
                                    throw new RuntimeException(e);
                                }
                                Utilities.getWorldDataFromSlimeWorlds(player.getWorld());
                            }
                        }.runTask(Hypersquare.instance);

                    }
                    if (!plugin.getLoadedWorlds().contains(codeTest)) {
                        SlimeWorld world = plugin.loadWorld(file, codeWorldName, false, properties);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // Configure worlds
                                try {
                                    plugin.loadWorld(world);
                                } catch (Exception e) {
                                    if (e instanceof UnknownWorldException)
                                        HSException.sendError(player, "That plot is vacant.");
                                    if (e instanceof NullPointerException)
                                        HSException.sendError(player, "Cannot find the world.");
                                }
                                Utilities.getWorldDataFromSlimeWorlds(player.getWorld());
                            }
                        }.runTask(Hypersquare.instance);
                    }
                } catch (Exception e) {
                    HSException.sendError(player, "Error loading plot. Please try again later.");
                    if (e instanceof UnknownWorldException) HSException.sendError(player, "That plot is vacant.");
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Configure worlds
                        Location spawn = PlotDatabase.getPlotSpawnLocation(plotID);
                        if (Bukkit.getWorld(worldName) != null) {
                            Bukkit.getWorld(worldName).setSpawnLocation(spawn);
                        }
                        loadRules(worldName);
                        loadRules(codeWorldName);

                        callback.run();
                    }
                }.runTask(Hypersquare.instance);
            }).start();
        } else {
            callback.run();
        }
    }


    public static void loadRules(String worldName) {
        World w = Bukkit.getWorld(worldName);
        assert w != null;
        w.setTime(1000);
        w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        w.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        w.setGameRule(GameRule.DO_FIRE_TICK, false);
        w.setGameRule(GameRule.DO_VINES_SPREAD, false);
        w.setDifficulty(Difficulty.EASY);
    }

    public static void deletePlot(int plotID) throws UnknownWorldException, IOException {
        String worldName = "hs." + plotID;
        String codeWorldName = "hs.code." + plotID;
        SlimeLoader file = Hypersquare.slimePlugin.getLoader("mongodb");
        Bukkit.unloadWorld(Objects.requireNonNull(Bukkit.getWorld(worldName)), true);
        Bukkit.unloadWorld(Objects.requireNonNull(Bukkit.getWorld(codeWorldName)), true);
        file.deleteWorld(worldName);
        file.deleteWorld(codeWorldName);
    }
}
