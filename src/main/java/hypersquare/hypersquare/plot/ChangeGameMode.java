package hypersquare.hypersquare.plot;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.CodeItems;
import hypersquare.hypersquare.dev.Events;
import hypersquare.hypersquare.item.MiscItems;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.play.execution.CodeExecutor;
import hypersquare.hypersquare.util.PlotUtilities;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

import static hypersquare.hypersquare.Hypersquare.cleanMM;
import static hypersquare.hypersquare.Hypersquare.codeExecMap;

public class ChangeGameMode {
    @Contract("_ -> new")
    private static @NotNull PlayerQuitEvent quitEvent(Player p) {
        return new PlayerQuitEvent(p, Component.text("Switching mode"), PlayerQuitEvent.QuitReason.DISCONNECTED);
    }

    public static void devMode(@NotNull Player player, int plotID, boolean keepState) {
        if (PlotDatabase.getRawDevs(plotID).contains(player.getUniqueId().toString()) || player.hasPermission("hypersquare.ignore.developers")) {
            String worldName = "hs.code." + plotID;
            Plot.loadPlot(plotID, player, () -> {
                if (Hypersquare.mode.get(player).equals("playing")) {
                    codeExecMap.get(plotID).trigger(Events.PLAYER_LEAVE_EVENT, quitEvent(player), new CodeSelection(player));
                }
                Utilities.resetPlayerStats(player, !keepState);
                Bukkit.getWorld(worldName).setTime(1000);
                if (!keepState) {
                    Inventory inv = player.getInventory();
                    inv.setItem(7, CodeItems.BLOCKS_SHORTCUT);
                    inv.setItem(8, CodeItems.VALUES_INGOT);
                }
                player.setGameMode(GameMode.CREATIVE);
                player.setFlying(true);
                Hypersquare.mode.put(player, "coding");

                if (keepState && Hypersquare.lastDevLocation.containsKey(player)
                    && Hypersquare.lastDevLocation.get(player).getWorld().getName().equals(worldName)) {
                    player.teleport(Hypersquare.lastDevLocation.get(player));
                } else {
                    player.teleport(new Location(Bukkit.getWorld(worldName), -3.5, 0, 2.5, -90, 0));
                }
                UnloadPlotsSchedule.tryGameUnload(plotID);
                Utilities.sendInfo(player, Component.text("You are now in dev mode."));
                Hypersquare.plotData.put(player, PlotDatabase.getPlot(player.getUniqueId().toString()));
                PlotDatabase.updateLocalData(plotID);
                PlotManager.loadPlot(plotID);
            });
        } else {
            HSException.sendError(player, "You do not have dev permissions for this plot.");
        }
    }

    public static void devMode(Player player, int plotID) {
        devMode(player, plotID, false);
    }

    public static void playMode(Player player, int plotID) {
        String worldName = "hs." + plotID;
        Plot.loadPlot(plotID, player, () -> {
            int oldPlotID = PlotUtilities.getPlotId(player.getWorld());
            String oldMode = Hypersquare.mode.get(player);

            PlotManager.loadPlot(plotID);
            CodeExecutor executor = codeExecMap.get(plotID);
            PlayerQuitEvent leaveEvent = quitEvent(player);
            if (oldMode.equals("playing"))
                executor.trigger(Events.PLAYER_LEAVE_EVENT, leaveEvent, new CodeSelection(player));
            if (oldPlotID == plotID)
                executor.trigger(Events.PLAYER_REJOIN_EVENT, leaveEvent, new CodeSelection(player));

            Utilities.resetPlayerStats(player);
            player.closeInventory();
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(Objects.requireNonNull(Bukkit.getWorld(worldName)).getSpawnLocation());
            UnloadPlotsSchedule.tryGameUnload(plotID);
            PlayerJoinEvent joinEvent = new PlayerJoinEvent(player, Component.text("Switching plots"));
            executor.trigger(Events.PLAYER_JOIN_EVENT, joinEvent, new CodeSelection(player));
            Hypersquare.mode.put(player, "playing");

            String ownerName;
            try { // Edge case where the owner of the plot is not in the database
                ownerName = Bukkit.getServer().getOfflinePlayer(UUID.fromString(Objects.requireNonNull(PlotManager.getPlotOwner(plotID)))).getName();
            } catch (NullPointerException e) {
                ownerName = "Unknown"; // This should never happen!
            }
            Utilities.sendInfo(player, cleanMM.deserialize("Joined game: <white>" + PlotManager.getPlotName(plotID) + " <gray>by <reset>" + ownerName));
            Hypersquare.plotData.put(player, PlotDatabase.getPlot(player.getUniqueId().toString()));
            PlotDatabase.updateLocalData(plotID);
            PlotManager.loadPlot(plotID);
            PlotStats.addPlayer(plotID, player);
        });
    }


    public static void buildMode(@NotNull Player player, int plotID, boolean keepState) {
        if (PlotDatabase.getRawBuilders(plotID).contains(player.getUniqueId().toString()) || player.hasPermission("hypersquare.ignore.builders")) {
            Plot.loadPlot(plotID, player, () -> {
                String worldName = "hs." + plotID;
                if (Hypersquare.mode.get(player).equals("playing")) {
                    codeExecMap.get(plotID).trigger(Events.PLAYER_LEAVE_EVENT, quitEvent(player), new CodeSelection(player));
                }
                Utilities.resetPlayerStats(player, !keepState);
                player.closeInventory();
                player.setGameMode(GameMode.CREATIVE);
                player.setFlying(true);
                Hypersquare.mode.put(player, "building");
                Utilities.sendInfo(player, Component.text("You are now in build mode."));
                Hypersquare.plotData.put(player, PlotDatabase.getPlot(player.getUniqueId().toString()));
                PlotDatabase.updateLocalData(plotID);
                PlotManager.loadPlot(plotID);

                if (keepState && Hypersquare.lastBuildLocation.containsKey(player)
                    && Hypersquare.lastBuildLocation.get(player).getWorld().getName().equals(worldName)) {
                    player.teleport(Hypersquare.lastBuildLocation.get(player));
                } else {
                    player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
                }
                UnloadPlotsSchedule.tryGameUnload(plotID);
            });
        } else {
            HSException.sendError(player, "You do not have build permissions for this plot");
        }
    }

    public static void buildMode(Player player, int plotId) {
        buildMode(player, plotId, false);
    }

    public static void editSpawn(Player player) {
        Utilities.resetPlayerStats(player);
        player.setGameMode(GameMode.CREATIVE);
        Hypersquare.mode.put(player, "editing spawn");
    }

    public static void spawn(Player player) {
        if (Hypersquare.mode.get(player).equals("playing")) {
            UnloadPlotsSchedule.tryGameUnload(player.getWorld());
            PlotUtilities.getExecutor(player).trigger(Events.PLAYER_LEAVE_EVENT, quitEvent(player), new CodeSelection(player));
        }
        Utilities.resetPlayerStats(player);
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().setItem(0, MiscItems.MY_PLOTS.build());
        player.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
        Hypersquare.mode.put(player, "at spawn");
        PlayerDatabase.updateLocalPlayerData(player);
        player.setHealth(20);
        player.setFoodLevel(20);
    }
}
