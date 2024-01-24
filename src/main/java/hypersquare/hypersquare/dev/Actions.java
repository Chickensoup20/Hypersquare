package hypersquare.hypersquare.dev;

import hypersquare.hypersquare.dev.action.EmptyAction;
import hypersquare.hypersquare.dev.action.player.PlayerCreativeModeAction;
import hypersquare.hypersquare.dev.action.player.PlayerSendMessageAction;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.Action;
import hypersquare.hypersquare.dev.action.player.PlayerGiveItemsAction;
import hypersquare.hypersquare.item.PlayerActionItems;
import hypersquare.hypersquare.menu.actions.ActionMenu;
import hypersquare.hypersquare.play.ActionArguments;
import hypersquare.hypersquare.play.CodeSelection;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public enum Actions implements Action {
    EMPTY(new EmptyAction()),

    PLAYER_GIVE_ITEMS(new PlayerGiveItemsAction()),
    PLAYER_SEND_MESSAGE(new PlayerSendMessageAction()),

    PLAYER_CREATIVE_MODE(new PlayerCreativeModeAction())
    ;

    final Action a;
    Actions(Action a) {
        this.a = a;
    }

    public static Action getAction(String id) {
        for (Action action : values()) {
            if (Objects.equals(action.getId(), id)) return action;
        }
        return null;
    }

    @Override
    public ActionParameter[] parameters() {
        return a.parameters();
    }

    @Override
    public String getId() {
        return a.getId();
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
    public PlayerActionItems getCategory() {
        return a.getCategory();
    }

    @Override
    public ItemStack item() {
        return a.item();
    }

    @Override
    public ActionMenu actionMenu(CodeActionData data) {
        return a.actionMenu(data);
    }

    @Override
    public void execute(CodeSelection selection, ActionArguments args) {
        a.execute(selection, args);
    }
}
