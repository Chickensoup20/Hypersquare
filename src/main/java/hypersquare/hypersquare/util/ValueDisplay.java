package hypersquare.hypersquare.util;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.value.CodeValues;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class ValueDisplay {

    private static MagmaCube display;

    public static void spawnValueDisplay(Location location, int timeout, NamedTextColor color) {
        display = location.getWorld().spawn(new Location(location.getWorld(), 0, 300, 0), MagmaCube.class);
        display.setSize(1);
        display.teleport(location.clone().add(0, -0.25, 0));

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.registerNewTeam(display.getUniqueId() + "");
        team.displayName(Component.text("displayValues"));
        team.color(color);
        team.addEntry(display.getUniqueId().toString());
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        display.setAI(false);
        display.setInvisible(true);
        display.setInvulnerable(true);
        display.setGlowing(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                display.remove();
            }
        }.runTaskLater(Hypersquare.instance, 20L * timeout);
    }

    public static void spawnValueDisplay(Location location, NamedTextColor color) {
        display = location.getWorld().spawn(new Location(location.getWorld(), 0, 1000, 0, location.getYaw(), location.getPitch()), MagmaCube.class);
        display.setAI(false);
        display.setInvisible(true);
        display.setSize(1);
        display.setInvulnerable(true);
        display.teleport(location.clone().add(0, -0.25, 0));
        display.setGlowing(true);


        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.registerNewTeam(display.getUniqueId() + "");
        team.displayName(Component.text("displayValues"));
        team.color(color);
        team.addEntry(display.getUniqueId().toString());
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);


    }

    public static UUID getID() {
        return display.getUniqueId();
    }

    public static void removeValueDisplay(UUID uuid) {
        if (uuid != null) {
            if (Bukkit.getEntity(uuid) != null) Bukkit.getEntity(uuid).remove();
        }

    }

    public static void showLocationDisplay(Player player, Location newLocation) {
        if (Hypersquare.mode.get(player).equals("building")) {
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                if (CodeValues.LOCATION.fromItem(player.getInventory().getItemInMainHand()) != null) {
                    if (Hypersquare.locationValueDisplays.containsKey(player)) {
                        ValueDisplay.removeValueDisplay(Hypersquare.locationValueDisplays.get(player));
                        Hypersquare.locationValueDisplays.remove(player);
                    }
                    spawnValueDisplay(newLocation, NamedTextColor.GREEN);
                    Hypersquare.locationValueDisplays.put(player, getID());
                }
            }
        }
    }
}
