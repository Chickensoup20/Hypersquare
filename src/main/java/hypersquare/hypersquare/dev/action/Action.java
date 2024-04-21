package hypersquare.hypersquare.dev.action;

import hypersquare.hypersquare.dev.BarrelParamSupplier;
import hypersquare.hypersquare.dev.BarrelTagSupplier;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Action extends BarrelParamSupplier, BarrelTagSupplier {
    void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel);

    String getId();

    String getCodeblockId();

    String getSignName();

    String getName();

    ActionMenuItem getCategory();

    ItemStack item();

    BarrelMenu actionMenu(CodeActionData data);
}
