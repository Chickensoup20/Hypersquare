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

public class PlayerSetArrowsStuckAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            DecimalNumber arrowCount = ctx.args().single("arrowCount");
            p.setArrowsInBody(arrowCount.toInt());
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(
                DisplayValue.NUMBER, false, false, Component.text("Arrow Count"), "arrowCount")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "set_arrows";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "SetArrowsStuck";
    }

    @Override
    public String getName() {
        return "Set Player Arrows Stuck";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.APPEARANCE_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.ARROW)
            .setName(Component.text("Set Arrows Stuck").color(NamedTextColor.GOLD))
            .setDescription(Component.text("Sets the amount of arrows"),
                Component.text("sticking out of a player's"),
                Component.text("character model."))
            .addAdditionalInfo(Component.text("These arrows cannot be"),
                Component.text("used or picked up."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("arrowCount", 13);
    }
}
