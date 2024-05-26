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

public class PlayerSetHotbarItemsAction implements Action {

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        List<ItemStack> items = ctx.args().allNullable("items");

        if (items.isEmpty()) {
            for (int i = 0; i < 9; i++) items.add(null);
        }

        for (Player p : targetSel.players()) {
            for (int i = 0; i < 9; i++) {
                if (items.size() <= i) break;

                ItemStack item = items.get(i);
                boolean notNull = item != null;

                switch (ctx.getTag("replacestyle", ReplaceOption::valueOf)) {
                    case REPLACE_EVERYTHING -> {
                        p.getInventory().setItem(i, notNull ? item : new ItemStack(Material.AIR));
                    }
                    case ONLY_ITEMS_REPLACE -> {
                        if (notNull)
                            p.getInventory().setItem(i, item);
                    }
                    case ONLY_REPLACE_AIR -> {
                        if (p.getInventory().getItem(i) == null)
                            p.getInventory().setItem(i, item);
                    }
                }

            }
        }
    }

    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.DISPENSER)
            .setName(Component.text(this.getName()).color(NamedTextColor.GOLD))
            .setDescription(Component.text("Sets the hotbar items of the player."))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameterRange("items", 9, 17)
            .tag("replacestyle", 26);
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(
                DisplayValue.ITEM, true, true, Component.text("Item(s) to set"), "items"),
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("replacestyle", "Replacement", ReplaceOption.ONLY_ITEMS_REPLACE,
                new BarrelTag.Option(ReplaceOption.REPLACE_EVERYTHING, "Replace Each Slot (Including air)", Material.IRON_BLOCK),
                new BarrelTag.Option(ReplaceOption.ONLY_ITEMS_REPLACE, "Replace Each Slot (Excluding air)", Material.RAW_IRON_BLOCK),
                new BarrelTag.Option(ReplaceOption.ONLY_REPLACE_AIR, "Replace Empty Slots", Material.IRON_ORE)
            )
        };
    }

    public String getId() {
        return "set_hotbar_items";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    public String getSignName() {
        return "HotbarItems";
    }

    @Override
    public String getName() {
        return "Set Hotbar Items";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.ITEM_MANAGEMENT_CATEGORY;
    }

    private enum ReplaceOption {
        REPLACE_EVERYTHING,
        ONLY_ITEMS_REPLACE,
        ONLY_REPLACE_AIR
    }
}
