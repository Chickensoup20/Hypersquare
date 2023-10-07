package hypersquare.hypersquare.menus;

import com.infernalsuite.aswm.api.SlimePlugin;
import hypersquare.hypersquare.*;
import hypersquare.hypersquare.plot.PlayerDatabase;
import hypersquare.hypersquare.utils.managers.ItemManager;
import hypersquare.hypersquare.plot.ChangeGameMode;
import hypersquare.hypersquare.plot.PlotDatabase;
import hypersquare.hypersquare.plot.Plot;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class CreatePlotsMenu extends Gui{

        public static Logger logger = getLogger();
        SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        public CreatePlotsMenu(Player player) {
            super(player, "createPlot", "Choose a plot size", 2);
        }
        @Override
        public void onOpen(InventoryOpenEvent event) {
            int usedBasic = PlayerDatabase.getUsedPlots(player.getUniqueId(),"basic");
            int usedLarge = PlayerDatabase.getUsedPlots(player.getUniqueId(),"large");
            int usedMassive = PlayerDatabase.getUsedPlots(player.getUniqueId(),"massive");
            int usedHuge = PlayerDatabase.getUsedPlots(player.getUniqueId(),"huge");
            int usedGigantic = PlayerDatabase.getUsedPlots(player.getUniqueId(),"gigantic");
            int maxBasic = PlayerDatabase.getMaxPlots(player.getUniqueId(),"basic");
            int maxLarge = PlayerDatabase.getMaxPlots(player.getUniqueId(),"large");
            int maxMassive = PlayerDatabase.getMaxPlots(player.getUniqueId(),"massive");
            int maxHuge = PlayerDatabase.getMaxPlots(player.getUniqueId(),"huge");
            int maxGigantic = PlayerDatabase.getMaxPlots(player.getUniqueId(),"gigantic");

            Icon basic = basicPlot(usedBasic,maxBasic);
            Icon large = largePlot(usedLarge,maxLarge);
            Icon massive = massivePlot(usedMassive,maxMassive);
            Icon huge = hugePlot(usedHuge,maxHuge);
            Icon gigantic = giganticPlot(usedGigantic,maxGigantic);

            addItem(0, basic);
            addItem(2, large);
            addItem(4, massive);
            addItem(6, huge);
            addItem(8, gigantic);


            basic.onClick(e -> {
                e.setCancelled(true);
                if (usedBasic != maxBasic) {
                    int plotID = Hypersquare.lastUsedWorldNumber;
                    Plot.createPlot(plotID, plugin, player.getUniqueId().toString(), "plot_template_basic");
                    ChangeGameMode.devMode((Player) event.getPlayer(), plotID);
                    PlayerDatabase.addPlot(player.getUniqueId(),"basic");
                    Hypersquare.lastUsedWorldNumber++;
                    Hypersquare.plotData.put(player, PlotDatabase.getPlot(player.getUniqueId().toString()));
                }
            });

            large.onClick(e -> {
                e.setCancelled(true);
                if (usedLarge != maxLarge) {
                    int plotID = Hypersquare.lastUsedWorldNumber;
                    Plot.createPlot(plotID, plugin, player.getUniqueId().toString(), "plot_template_large");
                    ChangeGameMode.devMode((Player) event.getPlayer(), plotID);
                    PlayerDatabase.addPlot(player.getUniqueId(),"large");
                    Hypersquare.lastUsedWorldNumber++;
                    Hypersquare.plotData.put(player, PlotDatabase.getPlot(player.getUniqueId().toString()));
                }
            });

            massive.onClick(e -> {
                e.setCancelled(true);
                if (usedMassive != maxMassive) {
                    int plotID = Hypersquare.lastUsedWorldNumber;
                    Plot.createPlot(plotID, plugin, player.getUniqueId().toString(), "plot_template_massive");
                    ChangeGameMode.devMode((Player) event.getPlayer(), plotID);
                    PlayerDatabase.addPlot(player.getUniqueId(),"massive");
                    Hypersquare.lastUsedWorldNumber++;
                    Hypersquare.plotData.put(player, PlotDatabase.getPlot(player.getUniqueId().toString()));
                }
            });

            huge.onClick(e -> {
                e.setCancelled(true);
                if (usedHuge != maxHuge) {
                    int plotID = Hypersquare.lastUsedWorldNumber;
                    Plot.createPlot(plotID, plugin, player.getUniqueId().toString(), "plot_template_huge");
                    ChangeGameMode.devMode((Player) event.getPlayer(), plotID);
                    PlayerDatabase.addPlot(player.getUniqueId(),"huge");
                    Hypersquare.lastUsedWorldNumber++;
                    Hypersquare.plotData.put(player, PlotDatabase.getPlot(player.getUniqueId().toString()));
                }
            });

            gigantic.onClick(e -> {
                e.setCancelled(true);
                if (usedGigantic != maxGigantic) {
                    int plotID = Hypersquare.lastUsedWorldNumber;
                    Plot.createPlot(plotID, plugin, player.getUniqueId().toString(), "plot_template_gigantic");
                    ChangeGameMode.devMode((Player) event.getPlayer(), plotID);
                    PlayerDatabase.addPlot(player.getUniqueId(),"gigantic");
                    Hypersquare.lastUsedWorldNumber++;
                    Hypersquare.plotData.put(player, PlotDatabase.getPlot(player.getUniqueId().toString()));
                }
            });

        }
        public static Icon basicPlot(int usedBasic, int maxBasic){
            ItemStack item = ItemManager.getItem("menu.basic_plot");
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Size: 64x64");
            lore.add("");
            if (usedBasic != maxBasic) {
                lore.add(ChatColor.GRAY + "You have used " + ChatColor.GREEN + usedBasic + "/" + maxBasic + ChatColor.GRAY + " of your Basic plots.");
            } else {
                lore.add(ChatColor.GRAY + "You have used " + ChatColor.RED + usedBasic + "/" + maxBasic + ChatColor.GRAY + " of your Basic plots.");
            }
            lore.add("");
            lore.add(ChatColor.GREEN + "Click to create!");
            meta.setLore(lore);
            item.setItemMeta(meta);
            Icon basic = new Icon(item);
            return basic;
        }
    public static Icon largePlot(int usedPlot, int maxPlot){
        ItemStack item = ItemManager.getItem("menu.large_plot");
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Size: 128x128");
        lore.add("");
        if (usedPlot != maxPlot) {
            lore.add(ChatColor.GRAY + "You have used " + ChatColor.GREEN + usedPlot + "/" + maxPlot + ChatColor.GRAY + " of your Large plots.");
        } else {
            lore.add(ChatColor.GRAY + "You have used " + ChatColor.RED + usedPlot + "/" + maxPlot + ChatColor.GRAY + " of your Large plots.");
        }
        lore.add("");
        lore.add(ChatColor.GREEN + "Click to create!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        Icon basic = new Icon(item);
        return basic;
    }
    public static Icon massivePlot(int usedPlot, int maxPlot){
        ItemStack item = ItemManager.getItem("menu.massive_plot");
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Size: 256x256");
        lore.add("");
        if (usedPlot != maxPlot) {
            lore.add(ChatColor.GRAY + "You have used " + ChatColor.GREEN + usedPlot + "/" + maxPlot + ChatColor.GRAY + " of your Massive plots.");
        } else {
            lore.add(ChatColor.GRAY + "You have used " + ChatColor.RED + usedPlot + "/" + maxPlot + ChatColor.GRAY + " of your Massive plots.");
        }
        lore.add("");
        lore.add(ChatColor.GREEN + "Click to create!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        Icon basic = new Icon(item);
        return basic;
    }
    public static Icon hugePlot(int usedPlot, int maxPlot){
        ItemStack item = ItemManager.getItem("menu.huge_plot");
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Size: 512x512");
        lore.add("");
        if (usedPlot != maxPlot) {
            lore.add(ChatColor.GRAY + "You have used " + ChatColor.GREEN + usedPlot + "/" + maxPlot + ChatColor.GRAY + " of your Huge plots.");
        } else {
            lore.add(ChatColor.GRAY + "You have used " + ChatColor.RED + usedPlot + "/" + maxPlot + ChatColor.GRAY + " of your Huge plots.");
        }
        lore.add("");
        lore.add(ChatColor.GREEN + "Click to create!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        Icon basic = new Icon(item);
        return basic;
    }
    public static Icon giganticPlot(int usedPlot, int maxPlot){
        ItemStack item = ItemManager.getItem("menu.gigantic_plot");
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Size: 1024x1024");
        lore.add("");
        if (usedPlot != maxPlot) {
            lore.add(ChatColor.GRAY + "You have used " + ChatColor.GREEN + usedPlot + "/" + maxPlot + ChatColor.GRAY + " of your Gigantic plots.");
        } else {
            lore.add(ChatColor.GRAY + "You have used " + ChatColor.RED + usedPlot + "/" + maxPlot + ChatColor.GRAY + " of your Gigantic plots.");
        }
        lore.add("");
        lore.add(ChatColor.GREEN + "Click to create!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        Icon basic = new Icon(item);
        return basic;
    }
    }


