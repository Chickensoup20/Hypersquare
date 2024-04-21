package hypersquare.hypersquare.dev.code.player.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.player.PlayerActionItems;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerRemoveItemsAction implements Action {

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        List<ItemStack> items = new ArrayList<>();
        double multiplier = ctx.args().getOr("multiplier", new DecimalNumber(1, 0)).toDouble();
        for (ItemStack item : ctx.args().<ItemStack>allNonNull("items")) {
            int amount = (int) (item.getAmount() * multiplier);
            int max = item.getMaxStackSize();
            item.setAmount(max);
            while (amount > max && items.size() < 100) {
                items.add(item.clone());
                amount -= max;
            }
            item.setAmount(amount);
            items.add(item);
        }

        for (Player p : targetSel.players()) {
            for (ItemStack item : items) {
                p.getInventory().removeItem(item);
            }
        }
    }

    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.FLINT_AND_STEEL)
            .setName(Component.text(this.getName()).color(NamedTextColor.RED))
            .setDescription(Component.text("Removes given amount of item"),
                Component.text("in the barrel from the player"))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 4, data)
            .parameterRange("items", 9, 26)
            .parameter("multiplier", 35);
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(
                DisplayValue.ITEM, true, false, Component.text("Item(s) to remove"), "items"),
            new BarrelParameter(
                DisplayValue.NUMBER, false, true, Component.text("Amount to multiply by"), "multiplier"),
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    public String getId() {
        return "remove_items";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    public String getSignName() {
        return "RemoveItems";
    }

    @Override
    public String getName() {
        return "Remove Items";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.ITEM_MANAGEMENT_CATEGORY;
    }
}
