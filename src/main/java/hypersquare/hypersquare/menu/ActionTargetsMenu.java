package hypersquare.hypersquare.menu;

import hypersquare.hypersquare.dev.codefile.CodeFile;
import hypersquare.hypersquare.dev.codefile.CodeFileHelper;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.menu.system.Menu;
import hypersquare.hypersquare.menu.system.MenuItem;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.util.component.BasicComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActionTargetsMenu {
    public static void open(Player player, @NotNull ArrayList<Target> targets, Location loc) {
        if (targets.isEmpty()) {
            HSException.sendErrorSound(player);
            return;
        }
        Sign sign;
        try {
            sign = (Sign) player.getWorld().getBlockState(loc);
        } catch (Exception ignored) {
            return;
        }
        Menu menu = new Menu(Component.text("Select Target"), 1);
        SignSide side = sign.getSide(Side.FRONT);
        targets.addFirst(Target.SELECTED);

        for (int slot = 0; slot < targets.size(); slot++) {
            Target t = targets.get(slot);
            List<Component> lore = new ArrayList<>();
            for (String line : t.description) lore.add(BasicComponent.gray(line));
            menu.slot(slot, new MenuItem(t.mat).onClick(() -> {
                CodeFileHelper.updateTarget(loc, new CodeFile(player.getWorld()), t);
                // DEFAULT_PLAYER -> Default Player
                side.line(2, Component.text(StringUtils.capitaliseAllWords(t.name().replace('_', ' ').toLowerCase())).color(t.color));
                sign.update();
                player.closeInventory();
            }).name(Component.text(t.name).color(t.color).decoration(TextDecoration.ITALIC, false)).lore(lore));
        }
        menu.open(player);
    }
}
