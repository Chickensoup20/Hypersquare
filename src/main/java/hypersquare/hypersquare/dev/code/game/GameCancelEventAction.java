package hypersquare.hypersquare.dev.code.game;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.action.CancellableEvent;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.game.GameActionItems;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GameCancelEventAction implements Action {

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        Bukkit.broadcastMessage(ctx.trace().bukkitEvent.getEventName());
        if (ctx.trace().bukkitEvent instanceof Cancellable c) {
            ((Cancellable) ctx.trace().bukkitEvent).setCancelled(true);
            Bukkit.broadcastMessage("it aparently succeeded");
        } else {
            Bukkit.broadcastMessage("hi it faliled");
        }
    }

    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.BARRIER)
            .setName(Component.text(this.getName()).color(NamedTextColor.GREEN))
            .setDescription(Component.text("Cancels the event the action is called with."))
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
        return "cancel_event";
    }

    @Override
    public String getCodeblockId() {
        return "game_action";
    }

    public String getSignName() {
        return "CancelEvent";
    }

    @Override
    public String getName() {
        return "Cancel Event";
    }

    @Override
    public ActionMenuItem getCategory() {
        return GameActionItems.EVENT_MANIPULATION_CATEGORY;
    }
}
