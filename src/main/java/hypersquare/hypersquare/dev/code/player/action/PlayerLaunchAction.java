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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerLaunchAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            List<DecimalNumber> powers = ctx.args().allNonNull("power");

            if (powers.isEmpty()) {
                return;
            }
            DecimalNumber power = powers.get(0);


            LaunchingTypes types = ctx.getTag("launch_type", LaunchingTypes::valueOf);
            ApplyCurrentVelocity applyCurrentVelocity = ctx.getTag("launch_settings", ApplyCurrentVelocity::valueOf);
            IgnoreDistance ignoreDistance = ctx.getTag("ignore_distance", IgnoreDistance::valueOf);

            Vector appliedForce = new Vector();
            if (applyCurrentVelocity == ApplyCurrentVelocity.TRUE) {
                appliedForce = p.getVelocity();
            }

            switch (types) {
                case LAUNCH_UPWARD -> appliedForce.setY(appliedForce.getY() + power.toFloat());
                case LAUNCH_FORWARD -> appliedForce.add(p.getLocation().getDirection().multiply(power.toFloat()));
                case LAUNCH_TOWARD -> {
                    List<Location> locations = ctx.args().allNullable("location");
                    if (locations.isEmpty()) return;
                    Location location = locations.getFirst();
                    Location playerLoc = p.getLocation();
                    switch (ignoreDistance) {
                        case TRUE ->
                            appliedForce.add(((location.toVector().subtract(playerLoc.toVector())).normalize()).multiply(power.toFloat()));
                        case FALSE ->
                            appliedForce.add((location.toVector().subtract(playerLoc.toVector())).multiply(power.toFloat()));
                    }
                }
            }
            p.setVelocity(appliedForce);
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.LOCATION, false, true, Component.text("Location to launch toward."), "location"),
            new BarrelParameter(DisplayValue.NUMBER, false, false, Component.text("Amount of force to launch player with."), "power")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("ignore_distance", "Ignore Distance", IgnoreDistance.FALSE,
                new BarrelTag.Option(IgnoreDistance.TRUE, "True", Material.LIME_DYE),
                new BarrelTag.Option(IgnoreDistance.FALSE, "False", Material.RED_DYE)
            ),
            new BarrelTag("launch_type", "Launch Type", LaunchingTypes.LAUNCH_FORWARD,
                new BarrelTag.Option(LaunchingTypes.LAUNCH_UPWARD, "Launch upward", Material.STICKY_PISTON),
                new BarrelTag.Option(LaunchingTypes.LAUNCH_FORWARD, "Launch forward", Material.PISTON),
                new BarrelTag.Option(LaunchingTypes.LAUNCH_TOWARD, "Launch toward", Material.TARGET)
            ),
            new BarrelTag("launch_settings", "Apply Current Velocity", ApplyCurrentVelocity.FALSE,
                new BarrelTag.Option(ApplyCurrentVelocity.TRUE, "True", Material.LIME_DYE),
                new BarrelTag.Option(ApplyCurrentVelocity.FALSE, "False", Material.RED_DYE)
            )
        };
    }

    @Override
    public String getId() {
        return "launch";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "Launch";
    }

    @Override
    public String getName() {
        return "Launch";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.MOVEMENT_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.PISTON)
            .setName(Component.text("Launch").color(NamedTextColor.AQUA))
            .setDescription(Component.text("Launches a player"))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("location", 12)
            .parameter("power", 13)
            .tag("ignore_distance", 24)
            .tag("launch_type", 25)
            .tag("launch_settings", 26);
    }

    private enum LaunchingTypes {
        LAUNCH_UPWARD,
        LAUNCH_FORWARD,
        LAUNCH_TOWARD
    }

    private enum IgnoreDistance {
        TRUE,
        FALSE
    }

    private enum ApplyCurrentVelocity {
        TRUE,
        FALSE
    }
}
