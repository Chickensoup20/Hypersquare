package hypersquare.hypersquare.listeners;

import hypersquare.hypersquare.Hypersquare;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerGoToSpawnEvent implements Listener {

    @EventHandler
    public void damagePlayer(EntityDamageByEntityEvent event){
        if (event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();
            if (Hypersquare.mode.get(player).equals("at spawn")) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void looseFood(FoodLevelChangeEvent event){
        Player player = (Player) event.getEntity();
        if (Hypersquare.mode.get(player).equals("at spawn")){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void dropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if (Hypersquare.mode.get(player).equals("at spawn")){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void Interaction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (Hypersquare.mode.get(player).equals("at spawn")){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void Interaction(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        if (Hypersquare.mode.get(player).equals("at spawn")){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void Interaction(PlayerInteractAtEntityEvent event){
        Player player = event.getPlayer();
        if (Hypersquare.mode.get(player).equals("at spawn")){
            event.setCancelled(true);
        }
    }
}
