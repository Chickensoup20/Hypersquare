package hypersquare.hypersquare.util;

import hypersquare.hypersquare.play.execution.CodeExecutor;
import hypersquare.hypersquare.plot.PlotDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static hypersquare.hypersquare.Hypersquare.codeExecMap;

public class PlotUtilities implements Listener {
    /**
     * Get plot ID from a world (supports code and build worlds)
     *
     * @param world World to get ID from
     * @return Plot ID
     */
    public static int getPlotId(World world) {
        String name = world.getName();
        if (name.contains("hs.")) {
            name = name.replace("hs.", "");
            if (name.startsWith("code.")) name = name.substring(5);
            return Integer.parseInt(name);
        }
        return 0;
    }

    public static List<Player> getOnlinePlotDevs(int plotId) {
        ArrayList<Player> l = new ArrayList<>();
        for (String uuid : PlotDatabase.getPlotDevs(plotId)) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if (p != null && getPlotId(p.getWorld()) == plotId) l.add(p);
        }
        return l;
    }

    /**
     * Get plot ID from a player's world
     *
     * @param player Player to get plot ID from
     */
    public static int getPlotId(Player player) {
        return getPlotId(player.getWorld());
    }

    /**
     * Get executor instance from a plot id
     *
     * @param plotId Plot ID to get executor from
     * @return {@link CodeExecutor} instance
     */
    public static CodeExecutor getExecutor(int plotId) {
        return codeExecMap.get(plotId);
    }

    /**
     * Get executor instance from a world
     *
     * @param world World to get executor from
     * @return {@link CodeExecutor} instance
     */
    public static CodeExecutor getExecutor(World world) {
        return codeExecMap.get(getPlotId(world));
    }

    /**
     * Get executor instance from a player's world
     *
     * @param player Player to get executor from
     * @return {@link CodeExecutor} instance
     */
    public static CodeExecutor getExecutor(Player player) {
        return getExecutor(player.getWorld());
    }

    /**
     * Moves an entity inside a set of plot boundaries
     *
     * @param entity Entity to teleport
     * @param locA   Boundary #1
     * @param locB   Boundary #2
     */
    public static void moveEntityInsidePlot(Entity entity, Location locA, Location locB) {
        Location entityLocation = entity.getLocation();

        double minX = Math.min(locA.getX(), locB.getX());
        double minY = Math.min(locA.getY(), locB.getY());
        double minZ = Math.min(locA.getZ(), locB.getZ());
        double maxX = Math.max(locA.getX(), locB.getX());
        double maxY = Math.max(locA.getY(), locB.getY());
        double maxZ = Math.max(locA.getZ(), locB.getZ());

        // Check if the entity is even outside the boundaries
        if (entityLocation.getX() < minX || entityLocation.getX() > maxX ||
            entityLocation.getY() < minY || entityLocation.getY() > maxY ||
            entityLocation.getZ() < minZ || entityLocation.getZ() > maxZ) {

            // Calculate a location that is within the boundaries
            double tpX = Utilities.clamp(entityLocation.getX(), minX, maxX);
            double tpY = Utilities.clamp(entityLocation.getY(), minY, maxY);
            double tpZ = Utilities.clamp(entityLocation.getZ(), minZ, maxZ);

            Location tpLocation = new Location(entity.getWorld(), tpX, tpY, tpZ, entityLocation.getYaw(), entityLocation.getPitch());
            entity.teleport(tpLocation);
        }
    }
}
