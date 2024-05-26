package hypersquare.hypersquare.menu;

import hypersquare.hypersquare.item.MiscItems;
import hypersquare.hypersquare.menu.system.Menu;
import hypersquare.hypersquare.menu.system.MenuItem;
import hypersquare.hypersquare.plot.Plot;
import hypersquare.hypersquare.plot.PlotDatabase;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MyPlotsMenu {

    public static void open(Player player) {
        List<Document> playerPlots = PlotDatabase.getPlotsByOwner(player.getUniqueId().toString());
        int size = (int) Math.ceil((double) playerPlots.size() / 8) + 1;
        Menu menu = new Menu(Component.text("My Plots"), size);

        menu.slot(menu.getSize() - 1, new MenuItem(MiscItems.CLAIM_PLOT.build()).onClick(() -> CreatePlotsMenu.open(player)));

        Utilities.sendOpenMenuSound(player);

        int i = 0;

        for (Document plotDocument : playerPlots) {
            int plotID = plotDocument.getInteger("plotID");
            ItemStack plotItem = Plot.getPlotItem(plotID);

            MenuItem plot = new MenuItem(plotItem);

            menu.slot(i, plot);
            final MenuItem finalPlot = plot;
            plot.onClick(() -> {
                ChangeModeMenu.open(player, finalPlot, plotDocument.getInteger("plotID"));
                Utilities.sendClickMenuSound(player);
            });
            i++;
        }

        menu.open(player);
    }
}
