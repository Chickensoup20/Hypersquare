package hypersquare.hypersquare.plot;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import hypersquare.hypersquare.HSKeys;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.util.LocationInitializer;
import hypersquare.hypersquare.util.PlotUtilities;
import hypersquare.hypersquare.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class MoveEntities {
    public static Location basic = null;
    public static Location large = null;
    public static Location huge = null;
    public static Location massive = null;
    public static Location gigantic = null;
    public static Location commonStart = null;

    public static void commonVars(@NotNull Location location) {
        World world = location.getWorld();
        basic = LocationInitializer.getBasicLocation(world);
        large = LocationInitializer.getLargeLocation(world);
        huge = LocationInitializer.getHugeLocation(world);
        massive = LocationInitializer.getMassiveLocation(world);
        gigantic = LocationInitializer.getGiganticLocation(world);
        commonStart = LocationInitializer.getCommonStartLocation(world);
    }

    public static void entityLoop() {
        SlimePlugin plugin = Hypersquare.slimePlugin;
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Hypersquare.instance, () -> {
            assert plugin != null;
            for (SlimeWorld world : plugin.getLoadedWorlds()) {
                World bukkitWorld = Bukkit.getWorld(world.getName());
                if (bukkitWorld == null) return;
                for (Entity entity : bukkitWorld.getEntities()) {
                    if (entity == null) return;
                    if (entity.getType() != EntityType.PLAYER) {
                        commonVars(entity.getLocation());
                        String plotType = (entity).getWorld().getPersistentDataContainer().get(HSKeys.PLOT_TYPE, PersistentDataType.STRING);
                        if (plotType == null) return;

                        switch (plotType) {

                            case "Basic": {
                                if (Utilities.notWithinLocation(entity.getLocation(), commonStart, basic)) {
                                    PlotUtilities.moveEntityInsidePlot(entity, commonStart, basic);
                                }
                                break;
                            }
                            case "Large": {
                                if (Utilities.notWithinLocation(entity.getLocation(), commonStart, large)) {
                                    PlotUtilities.moveEntityInsidePlot(entity, commonStart, large);
                                }
                                break;
                            }
                            case "Huge": {
                                if (Utilities.notWithinLocation(entity.getLocation(), commonStart, huge)) {
                                    PlotUtilities.moveEntityInsidePlot(entity, commonStart, huge);
                                }
                                break;
                            }
                            case "Massive": {
                                if (Utilities.notWithinLocation(entity.getLocation(), commonStart, massive)) {
                                    PlotUtilities.moveEntityInsidePlot(entity, commonStart, massive);
                                }
                                break;
                            }
                            case "Gigantic": {
                                if (Utilities.notWithinLocation(entity.getLocation(), commonStart, gigantic)) {
                                    PlotUtilities.moveEntityInsidePlot(entity, commonStart, gigantic);
                                }
                                break;
                            }

                        }
                    }
                }
            }
        }, 0, 1);
    }
}
