package hypersquare.hypersquare.dev.code.player.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.player.PlayerActionItems;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerSetFlyingAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            Flying flying = ctx.getTag("flying", Flying::valueOf);
            boolean fly = flying == Flying.TRUE;
            p.setFlying(fly && p.getAllowFlight());
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{};
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{new BarrelTag("flying", "Flying", Flying.TRUE, new BarrelTag.Option(Flying.TRUE, "True", Material.LIME_DYE), new BarrelTag.Option(Flying.FALSE, "False", Material.RED_DYE))};
    }

    @Override
    public String getId() {
        return "set_flying";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "SetFlying";
    }

    @Override
    public String getName() {
        return "Set Flying";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.MOVEMENT_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem().setMaterial(Material.FEATHER).setName(Component.text("Set Flying").color(NamedTextColor.GOLD)).setDescription(Component.text("Sets the player's flying state.")).setParameters(parameters()).setTagAmount(tags().length).build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data).tag("flying", 4);
    }

    private enum Flying {
        TRUE, FALSE,
    }
}
