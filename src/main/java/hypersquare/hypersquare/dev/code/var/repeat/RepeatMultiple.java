package hypersquare.hypersquare.dev.code.var.repeat;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.RepeatAction;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.repeat.RepeatItems;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RepeatMultiple implements RepeatAction {

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.NUMBER, Component.text("Amount to repeat executing code."), "amount")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[0];
    }

    @Override
    public String getId() {
        return "multiple";
    }

    @Override
    public String getCodeblockId() {
        return "repeat";
    }

    @Override
    public String getSignName() {
        return "Multiple";
    }

    @Override
    public String getName() {
        return "Multiple";
    }

    @Override
    public ActionMenuItem getCategory() {
        return RepeatItems.NUMERIC;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.PRISMARINE_SHARD)
            .setName(Component.text("Repeat Multiple Times").color(NamedTextColor.DARK_AQUA))
            .setDescription(Component.text("Repeats the code a"),
                Component.text("certain number of times."))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("amount", 13);
    }

    @Override
    public boolean check(@NotNull ExecutionContext ctx, boolean initial) {
        HashMap<String, Object> data = ctx.trace().next().tempData;
        int remaining;
        if (initial) remaining = ctx.args().<DecimalNumber>single("amount").toInt();
        else remaining = ((int) data.get("repeat_multiple")) - 1;
        data.put("repeat_multiple", remaining);
        return remaining > 0;
    }

}
