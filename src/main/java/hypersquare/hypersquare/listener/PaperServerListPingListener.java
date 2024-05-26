package hypersquare.hypersquare.listener;

import hypersquare.hypersquare.Hypersquare;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.jetbrains.annotations.NotNull;

public class PaperServerListPingListener implements Listener {
    @EventHandler
    public void onServerListPing(@NotNull ServerListPingEvent event) {
        event.setMaxPlayers(event.getNumPlayers() + 1);
        String motd = Hypersquare.instance.getConfig().getString("MOTD");
        if (motd == null) {
            motd = "<gray><i>No MOTD! Whoops.";
        }
        event.motd(Hypersquare.cleanMM.deserialize(motd));
    }
}
