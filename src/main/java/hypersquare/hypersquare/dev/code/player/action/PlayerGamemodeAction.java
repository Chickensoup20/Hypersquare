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
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerGamemodeAction implements Action {

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            p.setGameMode(ctx.getTag("gamemode", GameMode::valueOf));
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{};
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("gamemode", "Gamemode", GameMode.CREATIVE,
                new BarrelTag.Option(GameMode.CREATIVE, "Creative", Material.COMMAND_BLOCK),
                new BarrelTag.Option(GameMode.SURVIVAL, "Survival", Material.OAK_LOG),
                new BarrelTag.Option(GameMode.ADVENTURE, "Adventure", Material.GRASS_BLOCK),
                new BarrelTag.Option(GameMode.SPECTATOR, "Spectator", Material.GLASS)
            )
        };
    }

    @Override
    public String getId() {
        return "gamemode";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "Gamemode";
    }

    @Override
    public String getName() {
        return "Set Gamemode";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.SETTINGS_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.GRASS_BLOCK)
            .setName(Component.text(this.getName()).color(Colors.LIME))
            .setDescription(Component.text("Sets the gamemode of the player."))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .tag("gamemode", 13);
    }
}
