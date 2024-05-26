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
import hypersquare.hypersquare.play.error.CodeErrorType;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerSetInventorySlotAction implements Action {

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        ItemStack item = ctx.args().single("item");
        int slot = ctx.args().<DecimalNumber>single("slot").toInt() - 1;

        for (Player plr : targetSel.players()) {
            if (slot > plr.getInventory().getSize())
                throw new HSException(CodeErrorType.INDEX_OUT_OF_BOUNDS, new Exception());

            plr.getInventory().setItem(slot, item);
        }
    }

    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.ITEM_FRAME)
            .setName(Component.text(this.getName()).color(NamedTextColor.GOLD))
            .setDescription(Component.text("Set the item in a slot of the player."))
            .setParameters(parameters())
            .addAdditionalInfo(Component.text("Slots 1-9 are hotbar slots."))
            .addAdditionalInfo(Component.text("Slots 10-36 are inventory slots."))
            .addAdditionalInfo(Component.text("Slots 37-40 are armor slots, boots to helmet."))
            .addAdditionalInfo(Component.text("Slot 40 is the offhand slot."))
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("item", 12)
            .parameter("slot", 14);
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(
                DisplayValue.ITEM, false, false, Component.text("Item to set"), "item"),
            new BarrelParameter(
                DisplayValue.NUMBER, false, false, Component.text("Item slot"), "slot")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    public String getId() {
        return "set_inv_slot";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    public String getSignName() {
        return "SetInvSlot";
    }

    @Override
    public String getName() {
        return "Set Inventory Slot";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.ITEM_MANAGEMENT_CATEGORY;
    }
}
