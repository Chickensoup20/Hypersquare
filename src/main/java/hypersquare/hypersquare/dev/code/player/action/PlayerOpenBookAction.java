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
import hypersquare.hypersquare.play.error.CodeErrorType;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;

public class PlayerOpenBookAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            ItemStack item = ctx.args().single("item");
            if (item.getType() != Material.WRITTEN_BOOK) {
                if (item.getType() == Material.WRITABLE_BOOK) {
                    BookMeta bookMeta = (BookMeta) item.getItemMeta();
                    bookMeta.setAuthor(p.getName());
                    bookMeta.setTitle("Book");
                    item.setType(Material.WRITTEN_BOOK);
                    item.setItemMeta(bookMeta);
                } else {
                    throw new HSException(CodeErrorType.INVALID_PARAM, new InvalidParameterException("Item must be either a written book or a writable book"));
                }
            }
            p.openBook(item);
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(
                DisplayValue.ITEM, false, false, Component.text("Book item"), "item"),
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "open_book";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "OpenBook";
    }

    @Override
    public String getName() {
        return "Open Book";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.PLAYER_ACTION_COMMUNICATION;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.WRITABLE_BOOK)
            .setName(Component.text(this.getName()).color(NamedTextColor.YELLOW))
            .setDescription(Component.text("Opens a written book"),
                Component.text("menu for a player."))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("item", 13);
    }
}
