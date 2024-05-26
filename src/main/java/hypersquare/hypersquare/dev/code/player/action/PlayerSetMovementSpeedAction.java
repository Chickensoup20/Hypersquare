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

public class PlayerSetMovementSpeedAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            float speed = ctx.args().<DecimalNumber>allNonNull("speed").getFirst().toFloat();
            float groundSpeed = Math.clamp(speed / 500, 0, 1);
            float flightSpeed = Math.clamp(speed / 1000, 0, 1);

            PlayerSetMovementSpeedAction.SpeedTypes speedType = ctx.getTag("speed_type", PlayerSetMovementSpeedAction.SpeedTypes::valueOf);
            if (speedType == SpeedTypes.GROUND) p.setWalkSpeed(groundSpeed);
            if (speedType == SpeedTypes.FLIGHT) p.setFlySpeed(flightSpeed);
            if (speedType == SpeedTypes.BOTH) {
                p.setWalkSpeed(groundSpeed);
                p.setFlySpeed(flightSpeed);
            }
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{new BarrelParameter(DisplayValue.NUMBER, false, false, Component.text("Movement speed percentage"), "speed")};
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{new BarrelTag("speed_type", "Speed Type", SpeedTypes.GROUND, new BarrelTag.Option(SpeedTypes.GROUND, "Ground speed", Material.IRON_BOOTS), new BarrelTag.Option(SpeedTypes.FLIGHT, "Flight speed", Material.ELYTRA), new BarrelTag.Option(SpeedTypes.BOTH, "Both", Material.SUGAR))};
    }

    @Override
    public String getId() {
        return "set_movement_speed";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "SetSpeed";
    }

    @Override
    public String getName() {
        return "Set Movement Speed";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.STATISTICS_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem().setMaterial(Material.NETHERITE_BOOTS).setName(Component.text("Set Movement Speed").color(NamedTextColor.GOLD)).setDescription(Component.text("Sets a player's walking"), Component.text("and/or flight speed.")).addAdditionalInfo(Component.text("Fly speed maxes at 1000%, whilst"), Component.text("walk speed maxes at 500%.")).addAdditionalInfo(Component.text("Values above or below the max"), Component.text("or min will get clamped accordingly.")).setParameters(parameters()).setTagAmount(tags().length).build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data).parameter("speed", 11).tag("speed_type", 14);
    }

    private enum SpeedTypes {
        GROUND, FLIGHT, BOTH
    }
}
