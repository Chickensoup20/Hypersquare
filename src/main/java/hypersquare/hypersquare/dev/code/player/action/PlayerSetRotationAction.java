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

public class PlayerSetRotationAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            DecimalNumber pitch = ctx.args().<DecimalNumber>allNonNull("pitch").getFirst();
            DecimalNumber yaw = ctx.args().<DecimalNumber>allNonNull("yaw").getFirst();
            p.setRotation(yaw.toFloat(), pitch.toFloat());

        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.NUMBER, false, false, Component.text("Pitch (-90 to 90)"), "pitch"),
            new BarrelParameter(DisplayValue.NUMBER, false, false, Component.text("Yaw (-180 to 180)"), "yaw"),
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "set_rotation";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "SetRotation";
    }

    @Override
    public String getName() {
        return "Set Rotation";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.MOVEMENT_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.HEART_OF_THE_SEA)
            .setName(Component.text("Set Rotation").color(NamedTextColor.GOLD))
            .setDescription(Component.text("Sets the player's rotation."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("pitch", 0)
            .parameter("yaw", 1);
    }
}
