package hypersquare.hypersquare.plot;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.play.execution.CodeExecutor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class UnloadPlotsSchedule {

    public static void run() {
        for (Document plotDocument : PlotDatabase.getAllPlots()) {
            int plotId = plotDocument.getInteger("plotID");
            if (!Hypersquare.loadedPlots.containsKey(plotId)) continue;

            // Game Unloading - Purges variables and entities
            // World Unloading - Unloads plot worlds from memory

            World buildWorld = Bukkit.getWorld("hs." + plotId);
            World devWorld = Bukkit.getWorld("hs.code." + plotId);

            if (buildWorld != null && buildWorld.getPlayerCount() <= 0 && devWorld != null && devWorld.getPlayerCount() <= 0) {
                if (
                    Hypersquare.gameUnloadTimestamp.containsKey(plotId)
                        && System.currentTimeMillis() - Hypersquare.gameUnloadTimestamp.get(plotId) < 1000 * 60 * 2) {
                    continue;
                }
                Hypersquare.gameUnloadTimestamp.remove(plotId);

                // Game unload should have already happened
                PlotManager.unloadPlot(plotId, devWorld, buildWorld);

                Hypersquare.logger().info("Unloaded worlds for ID " + plotId);
                //TODO: non saved variables will be purged in the gameUnload method
            }
        }
    }

    /**
     * Tries to game-unload the plot, should be called on leave events
     *
     * @param plotId Plot ID to unload
     */
    public static void tryGameUnload(int plotId) {
        World buildWorld = Bukkit.getWorld("hs." + plotId);
        if (buildWorld == null || buildWorld.getPlayerCount() > 0) return;
        gameUnload(plotId, buildWorld);
    }

    public static void tryGameUnload(@NotNull World world) {
        tryGameUnload(Integer.parseInt(world.getName().replace("hs.", "")));
    }

    private static void gameUnload(int plotId, @NotNull World buildWorld) {
        Bukkit.getScheduler().runTaskLater(Hypersquare.instance, () -> {
                Hypersquare.gameUnloadTimestamp.put(plotId, System.currentTimeMillis());
                buildWorld.getEntities().listIterator().forEachRemaining(Entity::remove);
                CodeExecutor executor = Hypersquare.codeExecMap.get(plotId);
                if (executor != null) executor.halt();
            },
            1);
    }
}
