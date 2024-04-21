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

public class IfPlayerItemCooldown implements IfAction {
    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.ITEM, true, false, Component.text("Item type to check for."), "item")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "item_cooldown";
    }

    @Override
    public String getCodeblockId() {
        return "if_player";
    }

    @Override
    public String getSignName() {
        return "ItemCooldown";
    }

    @Override
    public String getName() {
        return "Item Is On Cooldown";
    }

    @Override
    public ActionMenuItem getCategory() {
        return IfPlayerItems.ITEM_CONDITIONS_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.CLOCK)
            .setName(Component.text("Item Is On Cooldown").color(NamedTextColor.BLUE))
            .setDescription(Component.text("Checks if a player has a cooldown"),
                Component.text("applied to an item type."))
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
            for (ItemStack item : ctx.args().<ItemStack>allNonNull("item")) {
                if (!p.hasCooldown(item.getType())) return true;
            }
        }
        return false;
    }
}
