package hypersquare.hypersquare.listener;


import hypersquare.hypersquare.item.MiscItems;
import hypersquare.hypersquare.menu.MyPlotsMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerRightClickListener implements Listener {
    @EventHandler
    public void onRightClick(@NotNull PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null && event.getItem().getItemMeta().equals(MiscItems.MY_PLOTS.build().getItemMeta())) {
                MyPlotsMenu.open(event.getPlayer());
            }
        }
    }
}
