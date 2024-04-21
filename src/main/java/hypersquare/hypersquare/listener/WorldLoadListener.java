package hypersquare.hypersquare.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.jetbrains.annotations.NotNull;

public class WorldLoadListener implements Listener {
    @EventHandler
    public void onLoadWorld(@NotNull WorldLoadEvent event) {
        event.getWorld().setKeepSpawnInMemory(false);
    }
}
