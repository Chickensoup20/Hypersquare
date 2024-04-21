package hypersquare.hypersquare.dev.code.player.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
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

import java.util.List;

public class PlayerBoostElytraAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            if (p.isGliding()) {
                ItemStack boost = ctx.args().<ItemStack>allNonNull("item").getFirst();
                p.fireworkBoost(boost);
            }
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.ITEM, false, false, Component.text("Item to boost elytra with"), List.of(Component.text("Player must be wearing an elytra.")), "item")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
        };
    }

    @Override
    public String getId() {
        return "boost_elytra";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "BoostElytra";
    }

    @Override
    public String getName() {
        return "Boost Elytra";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.MOVEMENT_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.FIREWORK_ROCKET)
            .setName(Component.text("Boost Elytra").color(NamedTextColor.AQUA))
            .setDescription(Component.text("Boosts a player's elytra."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("item", 4);
    }
}
