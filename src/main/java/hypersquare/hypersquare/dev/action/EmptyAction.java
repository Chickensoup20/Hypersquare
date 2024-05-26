package hypersquare.hypersquare.dev.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EmptyAction implements Action {
    @Override
    public BarrelParameter[] parameters() {
        return null;
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "empty";
    }

    @Override
    public String getCodeblockId() {
        return "";
    }

    @Override
    public String getSignName() {
        return "";
    }

    @Override
    public String getName() {
        return "Empty Action";
    }

    @Override
    public ActionMenuItem getCategory() {
        return null;
    }

    @Override
    public ItemStack item() {
        return null;
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data);
    }

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {

    }
}
