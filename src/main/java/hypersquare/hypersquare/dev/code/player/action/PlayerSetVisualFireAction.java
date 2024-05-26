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

public class PlayerSetVisualFireAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            OnFire onFire = ctx.getTag("onFire", OnFire::valueOf);
            p.setVisualFire(onFire == OnFire.ON);
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{};
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("onFire", "On Fire", OnFire.ON,
                new BarrelTag.Option(OnFire.ON, "True", Material.LIME_DYE),
                new BarrelTag.Option(OnFire.OFF, "False", Material.RED_DYE)
            )
        };
    }

    @Override
    public String getId() {
        return "set_visual_fire";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "SetVisualFire";
    }

    @Override
    public String getName() {
        return "Set Player Visual Fire";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.APPEARANCE_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.CAMPFIRE)
            .setName(Component.text("Set Visual Fire").color(NamedTextColor.GOLD))
            .setDescription(Component.text("Sets whether a player"),
                Component.text("should appear on fire."))
            .addAdditionalInfo(Component.text("The affected player's fire"),
                Component.text("ticks won't change and they"),
                Component.text("won't take any damage."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .tag("onFire", 13);
    }

    private enum OnFire {
        ON,
        OFF
    }
}
