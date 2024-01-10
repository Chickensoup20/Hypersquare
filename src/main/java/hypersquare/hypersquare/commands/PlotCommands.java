package hypersquare.hypersquare.commands;

import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import hypersquare.hypersquare.dev.CodeItems;
import hypersquare.hypersquare.plot.*;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.utils.Colors;
import hypersquare.hypersquare.utils.Utilities;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlotCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (Hypersquare.mode.get(Bukkit.getPlayer(sender.getName())).equals("coding")) {
                int plotID = Utilities.getPlotID(((Player) sender).getWorld());
                Player player = (Player) sender;
                if (args.length >= 1) {
                    switch (args[0]) {
                        case "glitch":
                        case "g": {
                            ((Player) sender).getInventory().addItem(CodeItems.GLITCH_STICK_ITEM);
                            break;
                        }
                        case "name": {
                            if (PlotManager.getPlotOwner(plotID).equals(((Player) sender).getUniqueId().toString())) {
                                if (args.length >= 2) {
                                    StringBuilder name = new StringBuilder();

                                    for (int i = 1; i <= args.length - 1; i++) {
                                        name.append(args[i]);
                                        if (i < 100) {
                                            name.append(" ");
                                        }
                                    }
                                    PlotDatabase.changePlotName(Utilities.getPlotID(((Player) sender).getWorld()), String.valueOf(name).strip());
                                    Utilities.sendInfo(player,"Successfully changed the plot name to " + String.valueOf(name).strip() + ".");
                                    PlotManager.loadPlot(plotID);
                                } else {
                                    Utilities.sendError(player, "You cannot set the plot name to nothing");
                                }
                            } else
                                Utilities.sendError((Player) sender, "Only the plot owner can do that!");
                            break;
                        }
                        case "icon": {
                            if (PlotManager.getPlotOwner(plotID).equals(((Player) sender).getUniqueId().toString())) {
                                if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                    PlotDatabase.changePlotIcon(Utilities.getPlotID(((Player) sender).getWorld()), ((Player) sender).getInventory().getItemInMainHand().getType().toString());
                                    Utilities.sendInfo(player,"Successfully changed the plot icon to " + Utilities.capitalize(((Player) sender).getInventory().getItemInMainHand().getType().toString()) + ".");
                                } else {
                                    Utilities.sendError(player,"You cannot set the plot's icon to nothing.");
                                }
                            } else
                                Utilities.sendError((Player) sender, "Only the plot owner can do that!");
                            break;
                        }
                        case "setspawn": {
                            if (PlotManager.getPlotOwner(plotID).equals(((Player) sender).getUniqueId().toString())) {
                                if (player.getLocation().getX() > 0){
                                    PlotDatabase.setPlotSpawnLocation(plotID,player.getLocation());
                                    Utilities.sendInfo(player,"Set plot spawn location to " + player.getLocation().getBlock() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "");
                                }
                            } else
                                Utilities.sendError((Player) sender, "Only the plot owner can do that!");
                            break;
                        }
                        case "spawn": {
                            player.teleport(PlotDatabase.getPlotSpawnLocation(plotID));
                            break;
                        }
                        case "unclaim": {
                            if (PlotManager.getPlotOwner(plotID).equals(((Player) sender).getUniqueId().toString())) {
                                World world = player.getWorld();
                                PlayerDatabase.removePlot(player.getUniqueId(),world.getPersistentDataContainer().get(new NamespacedKey(Hypersquare.instance,"plotType"), PersistentDataType.STRING).toLowerCase());
                                for (Player player1 : player.getWorld().getPlayers()) {
                                    ChangeGameMode.spawn(player1);
                                    Utilities.sendRedInfo(player1, "The plot that you were currently on was unclaimed.");
                                }

                                Utilities.sendInfo(player, "Plot "+ plotID + " has been unclaimed.");
                                PlotDatabase.deletePlot(plotID);
                                try {
                                    Plot.deletePlot(plotID);
                                } catch (UnknownWorldException | IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else
                                Utilities.sendError((Player) sender, "Only the plot owner can do that!");
                            break;
                        }
                        case "dev": {
                            if (PlotManager.getPlotOwner(plotID).equals(((Player) sender).getUniqueId().toString())) {
                                try {
                                    switch (args[1]) {
                                        case "add": {
                                            if (!args[2].equalsIgnoreCase(sender.getName())) {
                                                if (Bukkit.getOfflinePlayer(args[2]).hasPlayedBefore()) {
                                                    if (PlotDatabase.getRawDevs(plotID).contains(Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString())) {
                                                        Utilities.sendError((Player) sender, "That player is already a dev.");
                                                    } else {
                                                        PlotDatabase.addDev(plotID, Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                                                        Utilities.sendInfo((Player) sender, ("<reset>" + Bukkit.getOfflinePlayer(args[2]).getName() + " <gray>now has dev permissions for " + PlotDatabase.getPlotName(plotID)));
                                                        if (Utilities.playerOnline(args[2])) {
                                                            Utilities.sendInfo(Bukkit.getPlayer(args[2]), "You now have dev permissions for " + Utilities.convertToChatColor(PlotDatabase.getPlotName(plotID)));
                                                        }
                                                    }
                                                } else {
                                                    Utilities.sendError((Player) sender, "Could not find that player.");
                                                }
                                            } else {
                                                Utilities.sendError((Player) sender, "You cannot add yourself as a dev.");
                                            }
                                            break;

                                        }
                                        case "list": {
                                            String devs = "&a";
                                            for (String name : PlotDatabase.getPlotDevs(plotID)) {
                                                devs = devs + Bukkit.getOfflinePlayer(UUID.fromString(name)).getName() + ", ";
                                            }

                                            if (devs.length() > 2) {
                                                devs = devs.substring(0, devs.length() - 2);
                                            }

                                            sender.sendMessage(ChatColor.AQUA + "Plot Devs: " + ChatColor.translateAlternateColorCodes('&', String.valueOf(devs)));

                                            break;
                                        }
                                        case "remove": {
                                            if (!args[2].equalsIgnoreCase(sender.getName())) {
                                                if (Bukkit.getOfflinePlayer(args[2]).hasPlayedBefore()) {
                                                    if (!PlotDatabase.getRawDevs(plotID).contains(Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString())) {
                                                        Utilities.sendError((Player) sender, "That player is not a dev.");
                                                    } else {
                                                        PlotDatabase.removeDev(plotID, Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                                                        Utilities.sendInfo((Player) sender, ("&f" + Bukkit.getOfflinePlayer(args[2]).getName() + " &7no longer has dev permissions for " + Utilities.convertToChatColor(PlotDatabase.getPlotName(plotID))));
                                                        if (Utilities.playerOnline(args[2])) {
                                                            Utilities.sendRedInfo(Bukkit.getPlayer(args[2]), "You no longer have dev permissions for " + Utilities.convertToChatColor(PlotDatabase.getPlotName(plotID)));
                                                            if (Hypersquare.mode.get(Bukkit.getPlayer(args[2])).equals("coding")) {
                                                                ChangeGameMode.spawn(Bukkit.getPlayer(args[2]));
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    Utilities.sendError((Player) sender, "Could not find that player.");
                                                }
                                            } else {
                                                Utilities.sendError((Player) sender, "You cannot remove yourself as a dev.");
                                            }
                                            break;

                                        }
                                    }
                                } catch (IndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                    Utilities.sendUsageError(player, "/plot <dev/builder> <add/remove/list/clear> [player]");
                                }


                            } else
                                Utilities.sendError((Player) sender, "Only the plot owner can do that!");
                            break;
                        }
                        case "stats":{
                            if (Hypersquare.mode.get(player).equals("coding")){
                                List<String> messages = new ArrayList<>();
                                messages.add(Colors.PRIMARY_INFO + "Plot stats for:");
                                messages.add("");
                                messages.add(Colors.DECORATION + "→ <reset>" + PlotManager.getPlotName(plotID) + "<reset>" + Colors.PRIMARY_INFO + " by <white>" + Bukkit.getOfflinePlayer(UUID.fromString(PlotManager.getPlotOwner(plotID))).getName() + " <dark_gray>[" + Colors.PRIMARY_INFO + plotID + "<dark_gray>]");
                                messages.add(Colors.DECORATION + "→ " + Colors.SECONDARY_INFO + "Total unique joins: <white>" + PlotStats.getTotalUniquePlayers(plotID));
                                messages.add(Colors.DECORATION + "→ " + Colors.SECONDARY_INFO + "Total playtime: <white>" + PlotStats.calculateTotalTimePlayed(plotID));

                                Utilities.sendMultiMiniMessage(player,messages);
                            }
                            break;
                        }
                        case "ad":{
                            if (PlotManager.getPlotOwner(plotID).equals(((Player) sender).getUniqueId().toString()) || player.hasPermission("hypersquare.override.ad")) {
                                    List<String> messages = new ArrayList<>();
                                    player.sendMessage(args);
                                        if (args[1].equals("message")) {
                                            String finalAd = getString(args);
                                            if (finalAd.isEmpty()){
                                                finalAd = PlotDatabase.getPlotDescription(plotID);
                                            }
                                            StringBuilder devs = new StringBuilder();
                                            for (String name : PlotDatabase.getPlotDevs(plotID)) {
                                                devs.append(Bukkit.getOfflinePlayer(UUID.fromString(name)).getName()).append(", ");
                                            }
                                            if (devs.length() > 2) {
                                                devs = new StringBuilder(devs.substring(0, devs.length() - 2));
                                            }


                                            String authors = String.valueOf(devs);
                                            messages.add(PlotDatabase.getPlotName(plotID) + " <gray>by " + authors + "<reset>: " + finalAd);
                                            Utilities.sendMultiMiniMessage(player,messages);
                                            Hypersquare.admap.put(plotID,finalAd);
                                        }
                                        if (args[1].equals("confirm")) {
                                            String finalAd = Hypersquare.admap.get(plotID);
                                            if (finalAd == null){
                                                finalAd = PlotDatabase.getPlotDescription(plotID);
                                            }
                                            StringBuilder devs = new StringBuilder();
                                            for (String name : PlotDatabase.getPlotDevs(plotID)) {
                                                devs.append(Bukkit.getOfflinePlayer(UUID.fromString(name)).getName()).append(", ");
                                            }
                                            if (devs.length() > 2) {
                                                devs = new StringBuilder(devs.substring(0, devs.length() - 2));
                                            }


                                            String authors = String.valueOf(devs);
                                            messages.add(PlotDatabase.getPlotName(plotID) + " <gray>by " + authors + "<reset>: " + finalAd);
                                            for (Player player1 : Bukkit.getOnlinePlayers()) {
                                                player1.playSound(player1.getLocation(),Sound.ITEM_LODESTONE_COMPASS_LOCK,1,2);
                                                player1.playSound(player1.getLocation(),Sound.UI_TOAST_IN,1,2);
                                                Utilities.sendMultiMiniMessage(player1, messages);
                                            }
                                            if (!PlotDatabase.isPlotPublished(plotID)){
                                                PlotDatabase.addPlotToNew(plotID);
                                                PlotDatabase.setPlotPublishedStatus(plotID,true);
                                                if (PlotDatabase.getPlotDescription(plotID) == null){
                                                    PlotDatabase.changePlotDescription(plotID,finalAd);
                                                }
                                            }
                                        }

                                } else {
                                Utilities.sendError((Player) sender, "Only the plot owner can do that!");
                            }
                                break;
                            }
                        case "description": {
                            if (PlotManager.getPlotOwner(plotID).equals(((Player) sender).getUniqueId().toString()) || player.hasPermission("hypersquare.override.description")) {
                                if (args.length >= 2) {
                                    StringBuilder name = new StringBuilder();

                                    for (int i = 1; i <= args.length - 1; i++) {
                                        name.append(args[i]);
                                        if (i < 100) {
                                            name.append(" ");
                                        }
                                    }
                                    PlotDatabase.changePlotDescription(plotID,name.toString().strip());
                                    Utilities.sendInfo(player,"Successfully changed the plot description to " + String.valueOf(name).strip() + ".");
                                    PlotManager.loadPlot(plotID);
                                } else {
                                    Utilities.sendError(player, "You cannot set the plot description to nothing.");
                                }
                            }
                            }
                        }

                } else {
                    Utilities.sendError(player, "Invalid command usage.");
                }
            } else {
                Utilities.sendError((Player) sender,"You must be in dev mode on a plot.");
            }
            Hypersquare.plotData.put((Player) sender, PlotDatabase.getPlot(String.valueOf(Bukkit.getPlayer(sender.getName()).getUniqueId().toString())));
        }
         else {
            sender.sendMessage("This command can only be used by players.");
        }
        return true;
    }

    @NotNull
    private static String getString(@NotNull String @NotNull [] args) {
        StringBuilder name = new StringBuilder();

        for (int i = 2; i <= args.length - 1; i++) {
            name.append(args[i]);
            if (i < 100) {
                name.append(" ");
            }
        }
        String finalAd = String.valueOf(name).strip();
        return finalAd;
    }
}
