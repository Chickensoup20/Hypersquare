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

public class PlayerSendAttackAnimationAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            AnimationArm arm = ctx.getTag("arm", AnimationArm::valueOf);
            if (arm == AnimationArm.MAIN) p.swingMainHand();
            if (arm == AnimationArm.OFF) p.swingOffHand();
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{};
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("arm", "Animation Arm", AnimationArm.MAIN,
                new BarrelTag.Option(AnimationArm.MAIN, "Swing main arm", Material.DIAMOND_SWORD),
                new BarrelTag.Option(AnimationArm.OFF, "Swing off arm", Material.SHIELD)
            )
        };
    }

    @Override
    public String getId() {
        return "send_attack_animation";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "AttackAnimation";
    }

    @Override
    public String getName() {
        return "Send Player Attack Animation";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.APPEARANCE_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.GOLDEN_SWORD)
            .setName(Component.text("Send Player Attack Animation").color(NamedTextColor.YELLOW))
            .setDescription(Component.text("Makes a player perform"),
                Component.text("an attack animation."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .tag("arm", 13);
    }

    private enum AnimationArm {
        MAIN,
        OFF
    }
}
