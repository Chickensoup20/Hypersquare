package hypersquare.hypersquare.dev.code.player.condition;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.IfAction;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.player.IfPlayerItems;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IfPlayerHolding implements IfAction {
    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.ITEM, true, true, Component.text("Item to check for."), "item")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "is_holding";
    }

    @Override
    public String getCodeblockId() {
        return "if_player";
    }

    @Override
    public String getSignName() {
        return "IsHolding";
    }

    @Override
    public String getName() {
        return "Is Holding";
    }

    @Override
    public ActionMenuItem getCategory() {
        return IfPlayerItems.ITEM_CONDITIONS_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.ITEM_FRAME)
            .setName(Component.text("If Player is Holding").color(NamedTextColor.GREEN))
            .setDescription(Component.text("Checks if the player is"),
                Component.text("holding a specific item."))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 4, data)
            .parameter("item", 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);
    }


    @Override
    public boolean check(Entity current, ExecutionContext ctx) {
        if (current instanceof Player p) {
            if (!ctx.args().has("item")) {
                if (p.getInventory().getItemInMainHand().isEmpty()) return true;
                if (p.getInventory().getItemInOffHand().isEmpty()) return true;
            }
            for (ItemStack item : ctx.args().<ItemStack>allNonNull("item")) {
                if (p.getInventory().getItemInMainHand().isSimilar(item)) return true;
                if (p.getInventory().getItemInOffHand().isSimilar(item)) return true;
            }
        }
        return false;
    }
}
