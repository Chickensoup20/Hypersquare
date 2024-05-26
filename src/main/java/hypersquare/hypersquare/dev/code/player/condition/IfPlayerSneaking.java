package hypersquare.hypersquare.dev.code.player.condition;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.IfAction;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.player.IfPlayerItems;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IfPlayerSneaking implements IfAction {
    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{};
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "is_sneaking";
    }

    @Override
    public String getCodeblockId() {
        return "if_player";
    }

    @Override
    public String getSignName() {
        return "IsSneaking";
    }

    @Override
    public String getName() {
        return "Is Sneaking";
    }

    @Override
    public ActionMenuItem getCategory() {
        return IfPlayerItems.TOGGLEABLE_CONDITIONS_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.CHAINMAIL_LEGGINGS)
            .setName(Component.text("Is Sneaking").color(NamedTextColor.BLUE))
            .setDescription(Component.text("Checks if a player is sneaking."))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data);

    }


    @Override
    public boolean check(Entity target, ExecutionContext ctx) {
        if (target instanceof Player) {
            return target.isSneaking();
        }
        return false;
    }
}
