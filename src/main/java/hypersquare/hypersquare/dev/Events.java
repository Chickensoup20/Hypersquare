package hypersquare.hypersquare.dev;

import hypersquare.hypersquare.dev.code.player.event.*;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public enum Events implements Event {
    // Plot and server events
    PLAYER_JOIN_EVENT(new PlayerJoinEvent()),
    PLAYER_LEAVE_EVENT(new PlayerLeaveEvent()),
    PLAYER_REJOIN_EVENT(new PlayerRejoinEvent()),
    PLAYER_CHAT_EVENT(new PlayerChatEvent()),
    // Click Events
    PLAYER_RIGHT_CLICK_EVENT(new PlayerRightClickEvent()),
    PLAYER_LEFT_CLICK_EVENT(new PlayerLeftClickEvent()),
    PLAYER_CLICK_PLAYER_EVENT(new PlayerClickPlayerEvent()),
    PLAYER_CLICK_ENTITY_EVENT(new PlayerClickEntityEvent()),
    PLAYER_PLACE_BLOCK_EVENT(new PlayerPlaceBlockEvent()),
    PLAYER_BREAK_BLOCK_EVENT(new PlayerBreakBlockEvent()),
    PLAYER_SWAP_HANDS_EVENT(new PlayerSwapHandsEvent()),
    PLAYER_CHANGE_SLOT_EVENT(new PlayerChangeSlotEvent()),
    PLAYER_TAME_MOB_EVENT(new PlayerTameMobEvent()),
    // Movement Events
    PLAYER_WALK_EVENT(new PlayerWalkEvent()),
    PLAYER_MOVE_EVENT(new PlayerMoveEvent()),
    PLAYER_JUMP_EVENT(new PlayerJumpEvent()),
    PLAYER_VELOCITY_EVENT(new PlayerVelocityEvent()),
    PLAYER_SNEAK_EVENT(new PlayerSneakEvent()),
    PLAYER_UNSNEAK_EVENT(new PlayerUnsneakEvent()),
    PLAYER_START_SPRINT_EVENT(new PlayerStartSprintEvent()),
    PLAYER_STOP_SPRINT_EVENT(new PlayerStopSprintEvent()),
    PLAYER_START_FLY_EVENT(new PlayerStartFlyEvent()),
    PLAYER_STOP_FLY_EVENT(new PlayerStopFlyEvent()),
    PLAYER_RIPTIDE_EVENT(new PlayerRiptideEvent()),
    PLAYER_DISMOUNT_EVENT(new PlayerDismountEvent()),
    PLAYER_HORSE_JUMP_EVENT(new PlayerHorseJumpEvent()),
    PLAYER_TELEPORT_EVENT(new PlayerTeleportEvent()),
    // Item Events
    PLAYER_CLICK_MENU_SLOT_EVENT(new PlayerClickMenuSlotEvent()),
    PLAYER_CLICK_INV_SLOT_EVENT(new PlayerClickInvSlotEvent()),
    PLAYER_DROP_ITEM_EVENT(new PlayerDropItemEvent()),
    PLAYER_DAMAGE_ITEM_EVENT(new PlayerItemDamageEvent()),
    PLAYER_BREAK_ITEM_EVENT(new PlayerBreakItemEvent()),
    PLAYER_MEND_ITEM_EVENT(new PlayerMendItemEvent()),
    PLAYER_RESPAWN_EVENT(new PlayerRespawnEvent()),
    PLAYER_TAKE_DAMAGE_EVENT(new PlayerTakeDamageEvent()),
    PLAYER_DAMAGE_PLAYER_EVENT(new PlayerDamagePlayerEvent()),
    PLAYER_DAMAGE_ENTITY_EVENT(new PlayerDamageEntityEvent()),
    ENTITY_DAMAGE_PLAYER_EVENT(new EntityDamagePlayerEvent());

    final Event v;

    Events(Event v) {
        this.v = v;
    }

    public static Event getEvent(String id) {
        for (Event event : Events.values()) {
            if (Objects.equals(event.getId(), id)) return event;
        }
        return null;
    }

    @Override
    public String getId() {
        return v.getId();
    }

    @Override
    public String getCodeblockId() {
        return v.getCodeblockId();
    }

    @Override
    public String getSignName() {
        return v.getSignName();
    }

    @Override
    public PlayerEventItems getCategory() {
        return v.getCategory();
    }

    @Override
    public ItemStack item() {
        return v.item();
    }

    @Override
    public Target[] compatibleTargets() {
        return v.compatibleTargets();
    }
}
