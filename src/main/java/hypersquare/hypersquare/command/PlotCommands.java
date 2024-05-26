package hypersquare.hypersquare.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import hypersquare.hypersquare.HSKeys;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.codefile.CodeFile;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.play.execution.CodeExecutor;
import hypersquare.hypersquare.plot.*;
import hypersquare.hypersquare.util.PlotUtilities;
import hypersquare.hypersquare.util.Utilities;
import hypersquare.hypersquare.util.color.Colors;
import hypersquare.hypersquare.util.color.HSColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static hypersquare.hypersquare.Hypersquare.minimalMM;

public class PlotCommands implements HyperCommand {
    public static void givePlotPermission(CommandContext<CommandSourceStack> ctx, String permission) {

        Player sender = (Player) ctx.getSource().getBukkitSender();
        OfflinePlayer recipient = Bukkit.getOfflinePlayer(ctx.getArgument("player", String.class));

        int plotID = PlotUtilities.getPlotId(sender.getWorld());
        if (!sender.getUniqueId().toString().equals(PlotManager.getPlotOwner(plotID))) {
            HSException.sendError(sender, "You are not allowed to do this.");
            return;
        }
        String recipientName = recipient.getName();
        if (!sender.getName().equalsIgnoreCase(recipientName)) {
            if (recipient.hasPlayedBefore() || Utilities.playerOnline(recipientName)) {
                if (permission.equalsIgnoreCase("builder")) {
                    if (PlotDatabase.getRawBuilders(plotID).contains(recipient.getUniqueId().toString())) { // this has a vulnerability
                        HSException.sendError(sender, "That player is already a builder.");
                    } else PlotDatabase.addBuilder(plotID, recipient.getUniqueId());
                }
                if (permission.equalsIgnoreCase("dev")) {
                    if (PlotDatabase.getRawDevs(plotID).contains(recipient.getUniqueId().toString())) {
                        HSException.sendError(sender, "That player is already a dev.");
                    } else PlotDatabase.addDev(plotID, recipient.getUniqueId());
                }
                Utilities.sendInfo(sender, (Hypersquare.minimalMM.deserialize("<reset>" + recipientName + " <gray>now has " + permission + " permissions for " + PlotDatabase.getRawPlotName(plotID))));
                if (Utilities.playerOnline(recipientName)) {
                    Utilities.sendInfo((CommandSender) recipient, minimalMM.deserialize(("<reset>You now have " + permission + " permissions for " + PlotDatabase.getRawPlotName(plotID))));
                }
            } else {
                HSException.sendError(sender, "Could not find that player.");
            }
        } else {
            HSException.sendError(sender, "You cannot add yourself as a " + permission + ".");
        }
    }

    public static void removePlotPermission(CommandContext<CommandSourceStack> ctx, String permission) {

        Player sender = (Player) ctx.getSource().getBukkitSender();
        OfflinePlayer recipient = Bukkit.getOfflinePlayer(ctx.getArgument("player", String.class));

        int plotID = PlotUtilities.getPlotId(sender.getWorld());
        if (!sender.getUniqueId().toString().equals(PlotManager.getPlotOwner(plotID))) {
            HSException.sendError(sender, "You are not allowed to do this.");
            return;
        }
        String recipientName = recipient.getName();
        if (!sender.getName().equalsIgnoreCase(recipientName)) {
            if (recipient.hasPlayedBefore() || Utilities.playerOnline(recipientName)) {
                if (permission.equalsIgnoreCase("builder")) {
                    if (!PlotDatabase.getRawBuilders(plotID).contains(recipient.getUniqueId().toString())) {
                        HSException.sendError(sender, "That player is not a builder.");
                    } else {
                        PlotDatabase.removeBuilder(plotID, recipient.getUniqueId());
                    }
                }
                if (permission.equalsIgnoreCase("dev")) {
                    if (!PlotDatabase.getRawDevs(plotID).contains(recipient.getUniqueId().toString())) {
                        HSException.sendError(sender, "That player is not a dev.");
                    } else {
                        PlotDatabase.removeDev(plotID, recipient.getUniqueId());
                    }
                }
                Utilities.sendInfo(sender, (Hypersquare.minimalMM.deserialize("<reset>" + recipientName + " <gray>no longer has " + permission + " permissions for " + PlotDatabase.getRawPlotName(plotID))));
                if (Utilities.playerOnline(recipientName)) {
                    Utilities.sendInfo((CommandSender) recipient, minimalMM.deserialize(("<reset>You no longer have " + permission + " permissions for " + PlotDatabase.getRawPlotName(plotID))));
                }
            } else {
                HSException.sendError(sender, "Could not find that player.");
            }
        } else {
            HSException.sendError(sender, "You cannot remove yourself as a " + permission + ".");
        }
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> cd) {
        for (String alias : List.of("plot", "p")) {
            cd.register(literal(alias)
                .then(literal("name")
                    .then(argument("name", StringArgumentType.greedyString())
                        .executes(this::setName)))
                .then(literal("icon").executes(this::setIcon))
                .then(literal("unclaim").executes(this::unclaimPlot))
                .then(literal("dev")
                    .then(literal("add")
                        .then(argument("player", StringArgumentType.string()).executes(ctx -> {
                            givePlotPermission(ctx, "dev");
                            return DONE;
                        }))
                    )
                    .then(literal("remove")
                        .then(argument("player", StringArgumentType.string()).executes(ctx -> {
                            removePlotPermission(ctx, "dev");
                            return DONE;
                        }))
                    )
                    .then(literal("list").executes(this::listDevs))
                )
                .then(literal("builder")
                    .then(literal("add")
                        .then(argument("player", StringArgumentType.string()).executes(ctx -> {
                            givePlotPermission(ctx, "builder");
                            return DONE;
                        }))
                    )
                    .then(literal("remove")
                        .then(argument("player", StringArgumentType.string()).executes(ctx -> {
                            removePlotPermission(ctx, "builder");
                            return DONE;
                        }))
                    )
                )
                .then(literal("stats").executes(this::getStats))
                .then(literal("debug").executes(this::plotDebug))
            );
        }
    }

    private int listDevs(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getBukkitSender() instanceof Player player) {
            int plotID = PlotUtilities.getPlotId(player.getWorld());
            if (!player.getUniqueId().toString().equals(PlotManager.getPlotOwner(plotID))) {
                HSException.sendError(ctx.getSource().getBukkitSender(), "You are not allowed to do this.");
                return DONE;
            }
            StringBuilder devs = new StringBuilder("&a");
            for (String name : PlotDatabase.getPlotDevs(plotID)) {
                devs.append(Bukkit.getOfflinePlayer(UUID.fromString(name)).getName()).append(", ");
            }
            if (devs.length() > 2) devs = new StringBuilder(devs.substring(0, devs.length() - 2));
            player.sendMessage(Component.text("Plot Devs: ").color(NamedTextColor.AQUA)
                .append(Component.text(devs.toString()).color(NamedTextColor.GREEN)));
        }
        return DONE;
    }

    private int addDev(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getBukkitSender() instanceof Player player) {
            int plotID = PlotUtilities.getPlotId(player.getWorld());
            if (!player.getUniqueId().toString().equals(PlotManager.getPlotOwner(plotID))) {
                HSException.sendError(ctx.getSource().getBukkitSender(), "You are not allowed to do this.");
                return DONE;
            }
            String recipient = ctx.getArgument("player", String.class);
            if (!ctx.getArgument("player", String.class).equalsIgnoreCase(player.getName())) {
                OfflinePlayer offlineRecipentPlayer = Bukkit.getOfflinePlayer(recipient);
                if (offlineRecipentPlayer.hasPlayedBefore()) {
                    if (Objects.requireNonNull(PlotDatabase.getRawDevs(plotID)).contains(offlineRecipentPlayer.getUniqueId().toString())) {
                        // TODO: don't use getRawDevs
                        HSException.sendError(player, "That player is already a dev.");
                    } else {
                        PlotDatabase.addDev(plotID, offlineRecipentPlayer.getUniqueId());
                        Utilities.sendInfo(player, (Hypersquare.fullMM.deserialize("<reset>" + offlineRecipentPlayer.getName() + " <gray>now has dev permissions for " + PlotDatabase.getPlotName(plotID))));
                    }
                } else HSException.sendError(player, "Could not find that player.");
            } else HSException.sendError(player, "You cannot add yourself as a dev.");
        }
        return DONE;
    }

    private int getStats(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getBukkitSender() instanceof Player player) {
            int plotID = PlotUtilities.getPlotId(player.getWorld());
            if (Hypersquare.mode.get(player).equals("coding")) {
                List<String> messages = new ArrayList<>();
                messages.add(HSColor.PRIMARY_INFO + "Plot stats for:");
                messages.add("");
                messages.add(HSColor.DECORATION + "→ <reset>" + PlotManager.getPlotName(plotID) + "<reset>" + HSColor.PRIMARY_INFO + " by <white>" + Bukkit.getOfflinePlayer(UUID.fromString(PlotManager.getPlotOwner(plotID))).getName() + " <dark_gray>[" + HSColor.PRIMARY_INFO + plotID + "<dark_gray>]");
                messages.add(HSColor.DECORATION + "→ " + HSColor.SECONDARY_INFO + "Total unique joins: <white>" + PlotStats.getTotalUniquePlayers(plotID));
                messages.add(HSColor.DECORATION + "→ " + HSColor.SECONDARY_INFO + "Total playtime: <white>" + PlotStats.calculateTotalTimePlayed(plotID));

                Utilities.sendMultiMiniMessage(player, messages);
            }
        }
        return DONE;
    }

    private int plotDebug(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getBukkitSender() instanceof Player player) {
            int plotID = PlotUtilities.getPlotId(player.getWorld());
            if (plotID == 0) {
                HSException.sendError(player, "You must be on a plot to run this command!");
                return DONE;
            }
            if (!PlotDatabase.getRawDevs(plotID).contains(player.getUniqueId().toString())) {
                HSException.sendError(player, "You do not have dev permissions for this plot!");
                return DONE;
            }

            Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
            CodeFile file = new CodeFile(plotID);
            Utilities.sendInfo(player, Component.text("Debugging plot " + file.plotId() + "."));
            player.sendMessage(Component.text("Code Data:").color(Colors.YELLOW_LIGHT));
            player.sendMessage(Component.text(gson.toJson(file.getCodeData().toJson())).color(Colors.YELLOW));

            CodeExecutor executor = PlotUtilities.getExecutor(player);
            var threadsMap = executor.running;
            player.sendMessage(Component.text(" ".repeat(12)).color(Colors.GRAY_DARK).decoration(TextDecoration.STRIKETHROUGH, true));
            player.sendMessage(Component.text("Threads: (" + threadsMap.size() + ")").color(Colors.YELLOW_LIGHT));
            for (Event e : threadsMap.keySet()) {
                int count = threadsMap.get(e).size();
                player.sendMessage(Component.text(" • "
                    + e.getSignName()
                    + (count == 1 ? "" : ": " + count)
                ).color(Colors.GOLD));
            }
        }
        return DONE;
    }

    private int unclaimPlot(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getBukkitSender() instanceof Player player) {
            int plotID = PlotUtilities.getPlotId(player.getWorld());
            if (player.getUniqueId().toString().equals(PlotManager.getPlotOwner(plotID))) {
                World world = player.getWorld();
                if (world.getName().startsWith("hs.code."))
                    world = Bukkit.getWorld("hs." + PlotUtilities.getPlotId(world));
                assert world != null;
                PlayerDatabase.removePlot(player.getUniqueId(), Objects.requireNonNull(world.getPersistentDataContainer().get(HSKeys.PLOT_TYPE, PersistentDataType.STRING)).toLowerCase());
                for (Player playerInWorld : player.getWorld().getPlayers()) {
                    ChangeGameMode.spawn(playerInWorld);
                    Utilities.sendRedInfo(playerInWorld, Component.text("The plot that you were currently on was unclaimed."));
                }

                Utilities.sendInfo(player, Component.text("Plot " + plotID + " has been unclaimed."));
                PlotDatabase.deletePlot(plotID);
                try {
                    Plot.deletePlot(plotID);
                } catch (UnknownWorldException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else
                HSException.sendError(player, "Only the plot owner can do that!");
        }
        return DONE;
    }

    private int setName(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getBukkitSender() instanceof Player player) {
            int plotID = PlotUtilities.getPlotId(player.getWorld());
            if (player.getUniqueId().toString().equals(PlotManager.getPlotOwner(plotID))) {
                String name = ctx.getArgument("name", String.class).strip();
                PlotDatabase.changePlotName(PlotUtilities.getPlotId((player).getWorld()), name);
                Utilities.sendInfo(player, Component.text("Successfully changed the plot name to " + name + "."));
                PlotManager.loadPlot(plotID);
            } else HSException.sendError(player, "Only the plot owner can do that!");
        } else {
            HSException.sendError(ctx.getSource().getBukkitSender(), "Only a player can do this");
        }
        return DONE;
    }

    private int setIcon(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getBukkitSender() instanceof Player player) {
            int plotID = PlotUtilities.getPlotId(player.getWorld());
            if (!player.getUniqueId().toString().equals(PlotManager.getPlotOwner(plotID))) {
                HSException.sendError(ctx.getSource().getBukkitSender(), "You are not allowed to do this.");
                return DONE;
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getItemMeta() == null) {
                HSException.sendError(player, "Hold the item you want to use as your plot icon, then use /plot icon.");
                return DONE;
            }
            String icon = item.getType().toString();

            PlotDatabase.changePlotIcon(plotID, icon);
            Utilities.sendInfo(player, Component.text("Successfully changed the plot icon to " + icon + "."));
            PlotManager.loadPlot(plotID);
        } else HSException.sendError(ctx.getSource().getBukkitSender(), "Only a player can do this");
        return DONE;
    }
}




