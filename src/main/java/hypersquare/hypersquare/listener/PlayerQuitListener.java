package hypersquare.hypersquare.listener;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.Events;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.plot.UnloadPlotsSchedule;
import hypersquare.hypersquare.util.PlotUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void playerQuitEvent(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Trigger leave and possibly game unload
        if (Hypersquare.mode.get(player).equals("playing")) {
            PlotUtilities.getExecutor(player).trigger(Events.PLAYER_LEAVE_EVENT, event, new CodeSelection(player));
            UnloadPlotsSchedule.tryGameUnload(player.getWorld());
        }

        // Remove player from the hashmaps
        Hypersquare.lastDeathLoc.remove(player);
        Hypersquare.cooldownMap.remove(player.getUniqueId());
        Hypersquare.localPlayerData.remove(player.getUniqueId());
        Hypersquare.mode.remove(player);
        Hypersquare.plotData.remove(player);
        Hypersquare.lastSwapHands.remove(player);
        Hypersquare.lastDevLocation.remove(player);
        Hypersquare.lastBuildLocation.remove(player);

        event.quitMessage(Component.text(player.getName() + " left.").color(NamedTextColor.GRAY));
    }
}
