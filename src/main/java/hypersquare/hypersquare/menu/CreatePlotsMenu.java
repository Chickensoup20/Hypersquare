package hypersquare.hypersquare.menu;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.item.CreatePlotMenuItems;
import hypersquare.hypersquare.menu.system.Menu;
import hypersquare.hypersquare.menu.system.MenuItem;
import hypersquare.hypersquare.plot.Plot;
import hypersquare.hypersquare.plot.PlotDatabase;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static hypersquare.hypersquare.Hypersquare.lastUsedWorldNumber;

public class CreatePlotsMenu {
    public static void open(@NotNull Player player) {
        HashMap<String, Integer> playerData = Hypersquare.localPlayerData.get(player.getUniqueId());

        Menu menu = new Menu(Component.text("Choose a plot size"), 1);

        int usedBasic = playerData.get("used_basic");
        int maxBasic = playerData.get("max_basic");
        int usedLarge = playerData.get("used_large");
        int maxLarge = playerData.get("max_large");
        int usedHuge = playerData.get("used_huge");
        int maxHuge = playerData.get("max_huge");
        int usedMassive = playerData.get("used_massive");
        int maxMassive = playerData.get("max_massive");
        int usedGigantic = playerData.get("used_gigantic");
        int maxGigantic = playerData.get("max_gigantic");

        MenuItem basic = new MenuItem(CreatePlotMenuItems.BASIC_PLOT.build(usedBasic, maxBasic));
        MenuItem large = new MenuItem(CreatePlotMenuItems.LARGE_PLOT.build(usedLarge, maxLarge));
        MenuItem huge = new MenuItem(CreatePlotMenuItems.HUGE_PLOT.build(usedHuge, maxHuge));
        MenuItem massive = new MenuItem(CreatePlotMenuItems.MASSIVE_PLOT.build(usedMassive, maxMassive));
        MenuItem gigantic = new MenuItem(CreatePlotMenuItems.GIGANTIC_PLOT.build(usedGigantic, maxGigantic));

        menu.slot(0, basic)
            .slot(2, large)
            .slot(4, huge)
            .slot(6, massive)
            .slot(8, gigantic);

        basic.onClick(() -> {
            if (usedBasic < maxBasic) {
                player.closeInventory();
                int plotID = lastUsedWorldNumber;
                Plot.createPlot(player, plotID, Hypersquare.slimePlugin, player.getUniqueId().toString(), "plot_template_basic");
                lastUsedWorldNumber++;
                PlotDatabase.setRecentPlotID(lastUsedWorldNumber);
            }
        });

        large.onClick(() -> {
            if (usedLarge < maxLarge) {
                player.closeInventory();
                int plotID = lastUsedWorldNumber;
                Plot.createPlot(player, plotID, Hypersquare.slimePlugin, player.getUniqueId().toString(), "plot_template_large");
                lastUsedWorldNumber++;
                PlotDatabase.setRecentPlotID(lastUsedWorldNumber);
            }
        });

        huge.onClick(() -> {
            if (usedHuge < maxHuge) {
                player.closeInventory();
                int plotID = lastUsedWorldNumber;
                Plot.createPlot(player, plotID, Hypersquare.slimePlugin, player.getUniqueId().toString(), "plot_template_massive");
                lastUsedWorldNumber++;
                PlotDatabase.setRecentPlotID(lastUsedWorldNumber);
            }
        });

        massive.onClick(() -> {
            if (usedMassive < maxMassive) {
                player.closeInventory();
                int plotID = lastUsedWorldNumber;
                Plot.createPlot(player, plotID, Hypersquare.slimePlugin, player.getUniqueId().toString(), "plot_template_huge");
                lastUsedWorldNumber++;
                PlotDatabase.setRecentPlotID(lastUsedWorldNumber);
            }
        });

        gigantic.onClick(() -> {
            if (usedGigantic < maxGigantic) {
                player.closeInventory();
                int plotID = lastUsedWorldNumber;
                Plot.createPlot(player, plotID, Hypersquare.slimePlugin, player.getUniqueId().toString(), "plot_template_gigantic");
                lastUsedWorldNumber++;
                PlotDatabase.setRecentPlotID(lastUsedWorldNumber);
            }
        });

        menu.open(player);
    }
}
