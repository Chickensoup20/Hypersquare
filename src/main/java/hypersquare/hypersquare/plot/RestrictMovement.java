package hypersquare.hypersquare.plot;

import hypersquare.hypersquare.HSKeys;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.util.LocationInitializer;
import hypersquare.hypersquare.util.PlotUtilities;
import hypersquare.hypersquare.util.Utilities;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class RestrictMovement {

    public static Location basic = null;
    public static Location large = null;
    public static Location huge = null;
    public static Location massive = null;
    public static Location gigantic = null;
    public static Location commonStart = null;
    public static Location code = null;
    public static Location codeStart = null;

    public static void commonVars(Location location) {
        World world = location.getWorld();
        basic = LocationInitializer.getBasicLocation(world);
        large = LocationInitializer.getLargeLocation(world);
        huge = LocationInitializer.getHugeLocation(world);
        massive = LocationInitializer.getMassiveLocation(world);
        gigantic = LocationInitializer.getGiganticLocation(world);
        commonStart = LocationInitializer.getCommonStartLocation(world);

        code = new Location(location.getWorld(), -111, 0, 256);
        codeStart = new Location(location.getWorld(), 1, 255, 0);
    }


    public static void movementCheck(Player player) {
        if (Hypersquare.mode.get(player).equals("coding")) {
            commonVars(player.getLocation());
            if (Utilities.notWithinLocationIgnoreY(player.getLocation(), codeStart, code)) {
                PlotUtilities.moveEntityInsidePlot(player, codeStart, code);
            }
        }
        if (Hypersquare.mode.get(player).equals("building") || Hypersquare.mode.get(player).equals("playing")) {
            commonVars(player.getLocation());
            String plotType = player.getWorld().getPersistentDataContainer().get(HSKeys.PLOT_TYPE, PersistentDataType.STRING);

            switch (plotType) {
                case null:
                    break;
                case "Basic": {
                    if (Utilities.notWithinLocationIgnoreY(player.getLocation(), commonStart, basic)) {
                        PlotUtilities.moveEntityInsidePlot(player, commonStart, basic);
                    }
                    break;
                }
                case "Large": {
                    if (Utilities.notWithinLocationIgnoreY(player.getLocation(), commonStart, large)) {
                        PlotUtilities.moveEntityInsidePlot(player, commonStart, large);
                    }
                    break;
                }
                case "Huge": {
                    if (Utilities.notWithinLocationIgnoreY(player.getLocation(), commonStart, huge)) {
                        PlotUtilities.moveEntityInsidePlot(player, commonStart, huge);
                    }
                    break;
                }
                case "Massive": {
                    if (Utilities.notWithinLocationIgnoreY(player.getLocation(), commonStart, massive)) {
                        PlotUtilities.moveEntityInsidePlot(player, commonStart, massive);
                    }
                    break;
                }
                case "Gigantic": {
                    if (Utilities.notWithinLocationIgnoreY(player.getLocation(), commonStart, gigantic)) {
                        PlotUtilities.moveEntityInsidePlot(player, commonStart, gigantic);
                    }
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + plotType);
            }
        }
    }
}
