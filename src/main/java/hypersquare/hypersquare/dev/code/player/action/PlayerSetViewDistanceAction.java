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

public class PlayerSetViewDistanceAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            int distance = ctx.args().getOr("distance", new DecimalNumber(10, 0)).toInt();
            p.setViewDistance(Math.clamp(distance, 2, 32));
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(
                DisplayValue.NUMBER, false, false, Component.text("Distance in chunks (2-32)"), "distance"),
            new BarrelParameter(
                DisplayValue.OR, false, false, Component.empty(), ""),
            new BarrelParameter(
                DisplayValue.NONE, false, false, Component.text("(Resets view distance)"), "")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "set_view_distance";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "ViewDistance";
    }

    @Override
    public String getName() {
        return "Set Player View Distance";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.WORLD_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.SPYGLASS)
            .setName(Component.text("Set View Distance").color(NamedTextColor.YELLOW))
            .setDescription(Component.text("Sets the view distance"),
                Component.text("limit for a player."))
            .addAdditionalInfo(Component.text("The distance cannot exceed the"),
                Component.text("client's render distance."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("distance", 13);
    }
}
