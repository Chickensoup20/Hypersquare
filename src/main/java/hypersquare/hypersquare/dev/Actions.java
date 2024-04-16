package hypersquare.hypersquare.dev;

import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.action.EmptyAction;
import hypersquare.hypersquare.dev.code.control.WaitAction;
import hypersquare.hypersquare.dev.code.dev.PrintStackTraceAction;
import hypersquare.hypersquare.dev.code.player.action.*;
import hypersquare.hypersquare.dev.code.player.condition.IfPlayerHolding;
import hypersquare.hypersquare.dev.code.var.action.AssignVariableAction;
import hypersquare.hypersquare.dev.code.var.repeat.RepeatMultiple;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public enum Actions implements Action {
    EMPTY(new EmptyAction()),
    PRINT_STACKTRACE(new PrintStackTraceAction()),

    PLAYER_GIVE_ITEMS(new PlayerGiveItemsAction()),
    PLAYER_SET_HOTBAR_ITEMS(new PlayerSetHotbarItems()),
    PLAYER_SET_INVENTORY_ITEMS(new PlayerSetInventoryItems()),
    PLAYER_SET_INVENTORY_SLOT(new PlayerSetInventorySlot()),
    PLAYER_REMOVE_ITEMS(new PlayerRemoveItemsAction()),
    PLAYER_CLEAR_INV(new PlayerClearInventory()),
    PLAYER_SET_ITEM_COOLDOWN(new PlayerSetItemCooldown()),
    PLAYER_SEND_MESSAGE(new PlayerSendMessageAction()),
    PLAYER_SHOW_ACTION_BAR_TEXT(new PlayerShowActionBarText()),
    PLAYER_GAMEMODE(new PlayerGamemodeAction()),
    PLAYER_TELEPORT(new PlayerTeleportAction()),
    PLAYER_LAUNCH(new PlayerLaunchAction()),
    PLAYER_SET_FLYING(new PlayerSetFlyingAction()),
    PLAYER_SET_GLIDING(new PlayerSetGlidingAction()),
    PLAYER_BOOST_ELYTRA(new PlayerBoostElytraAction()),
    PLAYER_SET_ROTATION(new PlayerSetRotationAction()),
    PLAYER_FACE_LOCATION(new PlayerFaceLocationAction()),
    IF_PLAYER_HOLDING(new IfPlayerHolding()),

    // Player Statistics
    SET_MOVEMENT_SPEED(new PlayerSetMovementSpeed()),


    ASSIGN_VARIABLE(new AssignVariableAction()),
    REPEAT_MULTIPLE(new RepeatMultiple()),
    CONTROL_WAIT(new WaitAction()),
    ;

    public final Action a;
    Actions(Action a) {
        this.a = a;
    }

    public static @Nullable Actions getAction(String id, String codeblockId) {
        if (id == null || codeblockId == null || id.equals("empty")) return Actions.EMPTY;

        for (Actions action : values()) {
            if (Objects.equals(action.getCodeblockId(), codeblockId) && Objects.equals(action.getId(), id)) return action;
        }
        return null;
    }

    public static @Nullable Action getByData(CodeActionData data) {
        for (Action action : Actions.values()) {
            if (Objects.equals(action.getId(), data.action) && Objects.equals(action.getCodeblockId(), data.codeblock)) return action;
        }
        return null;
    }

    @Override
    public BarrelParameter[] parameters() {
        return a.parameters();
    }

    @Override
    public BarrelTag[] tags() {
        return a.tags();
    }

    @Override
    public String getId() {
        return a.getId();
    }

    @Override
    public String getCodeblockId() {
        return a.getCodeblockId();
    }

    @Override
    public String getSignName() {
        return a.getSignName();
    }

    @Override
    public String getName() {
        return a.getName();
    }

    @Override
    public ActionMenuItem getCategory() {
        return a.getCategory();
    }

    @Override
    public ItemStack item() {
        return a.item();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return a.actionMenu(data);
    }

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        a.execute(ctx, targetSel);
    }
}
