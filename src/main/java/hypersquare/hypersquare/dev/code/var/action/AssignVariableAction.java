package hypersquare.hypersquare.dev.code.var.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.var.SetVariableItems;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.CodeVariable;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AssignVariableAction implements Action {
    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.VARIABLE, Component.text("The variable to set"), "variable"),
            new BarrelParameter(DisplayValue.ANY, Component.text("The value to set the variable to."), "value")
        };
    }

    @Override
    public String getId() {
        return "assign";
    }

    @Override
    public String getCodeblockId() {
        return "set_variable";
    }

    @Override
    public String getSignName() {
        return "=";
    }

    @Override
    public String getName() {
        return "Assign Variable (=)";
    }

    @Override
    public ActionMenuItem getCategory() {
        return SetVariableItems.VARIABLE_SETTING;
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.IRON_INGOT)
            .setName(Component.text("Assign Variable").color(NamedTextColor.WHITE))
            .setDescription(Component.text("Assigns a value to a variable."))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("variable", 12)
            .parameter("value", 14);
    }

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        ctx.args().<CodeVariable>single("variable").set(ctx.args().single("value"));
    }
}
