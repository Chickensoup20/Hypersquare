package hypersquare.hypersquare.listener;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.Events;
import hypersquare.hypersquare.play.CodeExecutor;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.plot.UnloadPlotsSchedule;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Trigger leave and possibly game unload
        if (Hypersquare.mode.get(player).equals("playing")) {
            UnloadPlotsSchedule.tryGameUnload(player.getWorld());
            CodeExecutor.trigger(Utilities.getPlotID(player.getWorld()), Events.PLAYER_LEAVE_EVENT, new CodeSelection(player));
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
