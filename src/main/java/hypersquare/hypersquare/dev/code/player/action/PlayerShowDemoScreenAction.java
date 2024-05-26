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

public class PlayerShowDemoScreenAction implements Action {

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            p.showDemoScreen();
        }
    }

    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.NAME_TAG)
            .setName(Component.text(this.getName()).color(NamedTextColor.YELLOW))
            .setDescription(Component.text("Shows the demo screen to the"),
                Component.text("player."))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data);
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{};
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    public String getId() {
        return "show_demo_screen";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    public String getSignName() {
        return "DemoScreen";
    }

    @Override
    public String getName() {
        return "Show Demo Screen";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.PLAYER_ACTION_COMMUNICATION;
    }
}
