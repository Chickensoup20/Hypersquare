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
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerSetTimeAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            long time = ctx.args().getOr("time", new DecimalNumber(1000, 0)).toLong();
            p.setPlayerTime(time, false);
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(
                DisplayValue.NUMBER, false, true, Component.text("Daylight ticks"), "time"),
            new BarrelParameter(
                DisplayValue.OR, false, false, Component.empty(), ""),
            new BarrelParameter(
                DisplayValue.NONE, false, false, Component.text("(Resets player time)"), "")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "set_time";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "SetPlayerTime";
    }

    @Override
    public String getName() {
        return "Set Player Time";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.WORLD_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.CLOCK)
            .setName(Component.text("Set Player Time").color(NamedTextColor.BLUE))
            .setDescription(Component.text("Sets the time of day visible"),
                Component.text("to a player."))
            .addAdditionalInfo(Component.text("Day: ")
                .append(Component.text("1000").color(Colors.RED_LIGHT)))
            .addAdditionalInfo(Component.text("Noon: ")
                .append(Component.text("6000").color(Colors.RED_LIGHT)))
            .addAdditionalInfo(Component.text("Night: ")
                .append(Component.text("13000").color(Colors.RED_LIGHT)))
            .addAdditionalInfo(Component.text("Midnight: ")
                .append(Component.text("18000").color(Colors.RED_LIGHT)))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("time", 13);
    }
}
