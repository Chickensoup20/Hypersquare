package hypersquare.hypersquare.listener;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.Events;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.CodeExecutor;
import hypersquare.hypersquare.util.PlotUtilities;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class PlayModeListener implements Listener {

    private boolean cannotExecute(Player player) {
        return !Hypersquare.mode.get(player).equals("playing");
    }

    /* PLOT AND SERVER EVENTS */

    @EventHandler
    public void playerChatEvent(@NotNull AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_CHAT_EVENT, event, new CodeSelection(player));
    }



    /* TODO: Uncomment once resource pack action is implemented
    @EventHandler
    public void playerPackLoadEvent(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            executor.trigger(Events.PLAYER_PACK_LOAD_EVENT, new CodeSelection(player));
        }
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            executor.trigger(Events.PLAYER_PACK_DECLINE_EVENT, new CodeSelection(player));
        }
    }
    */

    /* CLICK EVENTS */

    @EventHandler
    public void playerInteractEvent(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            if (event.getHand() == EquipmentSlot.HAND)
                executor.trigger(Events.PLAYER_RIGHT_CLICK_EVENT, event, new CodeSelection(player));
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
            executor.trigger(Events.PLAYER_LEFT_CLICK_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerInteractAtEntityEvent(@NotNull PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        // TODO: assign getRightClicked() to the event victim
        if (event.getRightClicked() instanceof Player) {
            if (event.getHand() == EquipmentSlot.HAND)
                executor.trigger(Events.PLAYER_CLICK_PLAYER_EVENT, event, new CodeSelection(player));
        } else executor.trigger(Events.PLAYER_CLICK_ENTITY_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerPlaceBlockEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_PLACE_BLOCK_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerBreakBlockEvent(@NotNull BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_BREAK_BLOCK_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerSwapHandsEvent(@NotNull PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_SWAP_HANDS_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerChangeSlotEvent(@NotNull PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_CHANGE_SLOT_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerTameEvent(@NotNull EntityTameEvent event) {
        Player player = (Player) event.getOwner();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_TAME_MOB_EVENT, event, new CodeSelection(player));
    }

    /* MOVEMENT EVENTS */

    @EventHandler
    public void playerWalkEvent(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_MOVE_EVENT, event, new CodeSelection(player));
        if (event.getFrom().getBlock() != event.getTo().getBlock() && !player.isSprinting() && !player.isFlying() && !player.isSwimming()) {
            executor.trigger(Events.PLAYER_WALK_EVENT, event, new CodeSelection(player));
        }
    }

    @EventHandler
    public void playerJumpEvent(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getBlockY() < event.getTo().getBlockY()) {
            if (cannotExecute(player)) return;
            CodeExecutor executor = PlotUtilities.getExecutor(player);
            executor.trigger(Events.PLAYER_JUMP_EVENT, event, new CodeSelection(player));
        }
    }

    @EventHandler
    public void playerVelocityEvent(@NotNull PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_VELOCITY_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerSneakEvent(@NotNull PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        if (event.isSneaking()) executor.trigger(Events.PLAYER_SNEAK_EVENT, event, new CodeSelection(player));
        else executor.trigger(Events.PLAYER_UNSNEAK_EVENT, event, new CodeSelection(player));
    }


    @EventHandler
    public void playerSprintEvent(@NotNull PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        if (event.isSprinting()) executor.trigger(Events.PLAYER_START_SPRINT_EVENT, event, new CodeSelection(player));
        else executor.trigger(Events.PLAYER_STOP_SPRINT_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerFlightEvent(@NotNull PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        if (event.isFlying()) executor.trigger(Events.PLAYER_START_FLY_EVENT, event, new CodeSelection(player));
        else executor.trigger(Events.PLAYER_STOP_FLY_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerRiptideEvent(@NotNull PlayerRiptideEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_RIPTIDE_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerDismountEvent(@NotNull VehicleExitEvent event) {
        if (!(event.getExited() instanceof Player player)) return;
        // We can cast because we already made sure it's a player
        if (cannotExecute((Player) event.getExited())) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_DISMOUNT_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void playerHorseJumpEvent(@NotNull HorseJumpEvent event) {
        Entity entity = event.getEntity().getPassengers().getFirst();
        if (!(entity instanceof Player player)) return;
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_HORSE_JUMP_EVENT, event, new CodeSelection(player));
    }

    // TODO: Vehicle Jump

    @EventHandler
    public void playerTeleportEvent(@NotNull PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_TELEPORT_EVENT, event, new CodeSelection(player));
    }

    /* ITEM EVENTS */

    @EventHandler
    public void playerClickMenuSlotEvent(@NotNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        if (event.getClickedInventory() != player.getInventory())
            executor.trigger(Events.PLAYER_CLICK_MENU_SLOT_EVENT, event, new CodeSelection(player));

        if (event.getClickedInventory() == player.getInventory())
            executor.trigger(Events.PLAYER_CLICK_INV_SLOT_EVENT, event, new CodeSelection(player));

    }

    @EventHandler
    public void playerDropItemListener(@NotNull PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (cannotExecute(p)) return;
        PlotUtilities.getExecutor(p).trigger(Events.PLAYER_DROP_ITEM_EVENT, event, new CodeSelection(p));
    }

    // TODO: Consume Item
    @EventHandler
    public void playerBreakItemEvent(@NotNull PlayerItemBreakEvent event) {
        Player p = event.getPlayer();
        if (cannotExecute(p)) return;
        PlotUtilities.getExecutor(p).trigger(Events.PLAYER_BREAK_ITEM_EVENT, event, new CodeSelection(p));
    }

    @EventHandler
    public void playerDamageItemEvent(@NotNull PlayerItemDamageEvent event) {
        Player p = event.getPlayer();
        if (cannotExecute(p)) return;
        PlotUtilities.getExecutor(p).trigger(Events.PLAYER_DAMAGE_ITEM_EVENT, event, new CodeSelection(p));
    }

    @EventHandler
    public void playerMendItemEvent(@NotNull PlayerItemMendEvent event) {
        Player p = event.getPlayer();
        if (cannotExecute(p)) return;
        PlotUtilities.getExecutor(p).trigger(Events.PLAYER_MEND_ITEM_EVENT, event, new CodeSelection(p));

    }

    @EventHandler
    public void playerRespawnEvent(@NotNull PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (cannotExecute(player)) return;
        CodeExecutor executor = PlotUtilities.getExecutor(player);
        executor.trigger(Events.PLAYER_RESPAWN_EVENT, event, new CodeSelection(player));
    }

    @EventHandler
    public void entityTakeDamageEvent(@NotNull EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (cannotExecute(player)) return;
            CodeExecutor executor = PlotUtilities.getExecutor(player);
            executor.trigger(Events.PLAYER_TAKE_DAMAGE_EVENT, event, new CodeSelection(player));
        }
    }

    @EventHandler
    public void entityDamageEntityEvent(@NotNull EntityDamageByEntityEvent event) {
        CodeExecutor executor;
        if (event.getDamager() instanceof Player player) {
            if (cannotExecute(player)) return;
            executor = PlotUtilities.getExecutor(player);
            if (event.getEntity() instanceof Player victim) {
                executor.trigger(Events.PLAYER_DAMAGE_PLAYER_EVENT, event, new CodeSelection(player));
            } else {
                System.out.println("victim is not player");
                executor.trigger(Events.PLAYER_DAMAGE_ENTITY_EVENT, event, new CodeSelection(player));
            }
        } else if (event.getEntity() instanceof Player victim) {
            if (cannotExecute(victim)) return;
            executor = PlotUtilities.getExecutor(victim);
            executor.trigger(Events.ENTITY_DAMAGE_PLAYER_EVENT, event, new CodeSelection(victim));
        }
    }

    // TODO: Close Inventory
    // TODO: Fish Event

    /* DAMAGE EVENTS */

    // TODO: Take Damage
    // TODO: Damage Player
    // TODO: Damage Entity
    // TODO: Entity Damage Player
    // TODO: Heal
    // TODO: Shoot Bow
    // TODO: Shoot Projectile
    // TODO: Projectile Hit (block or entity)
    // TODO: Projectile Damage Player
    // TODO: Potion Cloud Applies Potion to Player

    /* DEATH EVENTS */
    // TODO: Death
    // TODO: Kill Player
    // TODO: Revived by Totem
    // TODO: Kill Mob
    // TODO: Mob Kill Player
    // TODO: Respawn
}
