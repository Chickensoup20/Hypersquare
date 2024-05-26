package hypersquare.hypersquare.plot;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.play.execution.CodeExecutor;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

import static hypersquare.hypersquare.Hypersquare.*;

public class PlotManager {

    public static void loadPlot(int plotId) {
        if (loadedPlots.containsKey(plotId)) return;
        List<Object> plotData = PlotDatabase.getPlotData(plotId);
        if (plotData != null) loadedPlots.put(plotId, plotData);
        CodeExecutor executor = new CodeExecutor(plotId);
        // TODO: Plot load event for running code on first player join (Game Event)
        // executor.trigger(Events.GAME_INITIALIZE_EVENT, new CodeSelection())
        codeExecMap.put(plotId, executor);
    }

    public static void unloadPlot(int plotId, World devWorld, World buildWorld) {
        loadedPlots.remove(plotId);
        codeExecMap.get(plotId).halt();
        codeExecMap.remove(plotId);

        if (buildWorld == null || devWorld == null) {
            Hypersquare.logger().warning("Tried unloading plot " + plotId + " but one of the worlds were null");
            return;
        }

        Bukkit.unloadWorld(buildWorld, true);
        Bukkit.unloadWorld(devWorld, true);
        Hypersquare.loadedPlots.remove(plotId);
    }

    public static List<Object> getPlotData(int plotID) {
        return loadedPlots.get(plotID);
    }

    public static String getPlotName(int plotID) {
        List<Object> plotData = getPlotData(plotID);
        if (plotData != null && !plotData.isEmpty()) {
            return minimalMM.serialize(minimalMM.deserialize((String) plotData.getFirst()));
        }
        return null;
    }

    public static String getPlotOwner(int plotID) {
        List<Object> plotData = getPlotData(plotID);
        if (plotData != null && plotData.size() > 1) {
            return (String) plotData.get(1);
        }
        return null;
    }

    public static int getPlotNode(int plotID) {
        List<Object> plotData = getPlotData(plotID);
        if (plotData != null && plotData.size() > 2) {
            return (int) plotData.get(2);
        }
        return -1;
    }

    public static int getPlotVotes(int plotID) {
        List<Object> plotData = getPlotData(plotID);
        if (plotData != null && plotData.size() > 8) {
            return (int) plotData.get(8);
        }
        return 0;
    }
}
