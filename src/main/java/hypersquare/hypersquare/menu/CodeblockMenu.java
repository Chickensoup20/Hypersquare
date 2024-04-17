package hypersquare.hypersquare.menu;

import hypersquare.hypersquare.dev.Actions;
import hypersquare.hypersquare.dev.Events;
import hypersquare.hypersquare.dev.codefile.CodeFile;
import hypersquare.hypersquare.dev.codefile.CodeFileHelper;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.game.GameActionItems;
import hypersquare.hypersquare.item.action.player.IfPlayerItems;
import hypersquare.hypersquare.item.action.player.PlayerActionItems;
import hypersquare.hypersquare.item.action.player.PlayerEventItems;
import hypersquare.hypersquare.item.action.repeat.RepeatItems;
import hypersquare.hypersquare.item.action.var.SetVariableItems;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.menu.system.Menu;
import hypersquare.hypersquare.menu.system.MenuItem;
import hypersquare.hypersquare.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hypersquare.hypersquare.Hypersquare.cleanMM;

public class CodeblockMenu {
    public static void open(
        Player player, Location targetLocation,
        @NotNull Menus codeblockMenu
    ) {
        Menu menu = new Menu(Component.text(codeblockMenu.fullTitle + (codeblockMenu.items == null ? "" : " Categories")), codeblockMenu.rows);

        if (codeblockMenu.items == null) {
            for (Action action : Actions.values()) {
                if (!Objects.equals(action.getCodeblockId(), codeblockMenu.id) || action.getId() == null) continue;
                addActionToMenu(player, targetLocation, menu, action);
            }
            menu.open(player);
            return;
        }

        // Loop through all categories
        for (ActionMenuItem item : codeblockMenu.items) {
            menu.slot(item.getSlot(), new MenuItem(item.build()).onClick(() -> {
                Utilities.sendSecondaryMenuSound(player);

                // Strip Color Codes
                Pattern pattern = Pattern.compile("<[^>]+?>");
                Matcher matcher = pattern.matcher(cleanMM.serialize(item.getName()));

                // Create a new gui for the category
                Menu categoryGui = new Menu(Component.text(codeblockMenu.shortTitle + " > " + matcher.replaceAll("")), 5);

                if (codeblockMenu.actions) {
                    // Loop through all actions in the category
                    for (Action action : Actions.values()) {
                        if (action.getCategory() != item || action.getId() == null) continue;
                        addActionToMenu(player, targetLocation, categoryGui, action);
                    }
                } else {
                    // Loop through all event actions in the category
                    for (Event event : Events.values()) {
                        if (event.getCategory() != item || event.getId() == null) continue;
                        MenuItem actionItem = new MenuItem(event.item()).onClick(() -> {
                            Utilities.sendSuccessClickMenuSound(player);
                            Utilities.setAction(targetLocation.getBlock(), event.getSignName(), player);
                            CodeFileHelper.updateEvent(targetLocation.clone().add(1, 0, 0), new CodeFile(player.getWorld()), event);
                        });
                        categoryGui.addItem(actionItem);
                    }
                }
                // Open the category GUI
                categoryGui.open(player);
            }));
        }

        menu.open(player);
    }

    private static void addActionToMenu(Player player, Location targetLocation, Menu menu, Action action) {
        MenuItem actionItem = new MenuItem(action.item()).onClick(() -> {
            Utilities.sendSuccessClickMenuSound(player);
            Utilities.setAction(targetLocation.getBlock(), action.getSignName(), player);
            Location codeblockLoc = targetLocation.clone();
            // If the player clicked the sign we need to add 1 to the x coordinate
            CodeFileHelper.updateAction(
                codeblockLoc.getBlock().getType() == Material.OAK_WALL_SIGN ?
                    targetLocation.add(1, 0, 0) : targetLocation,
                new CodeFile(player.getWorld()), action
            );
        });
        menu.addItem(actionItem);
    }

    public static void open(String id, Player p, Location loc) {
        for (Menus m : Menus.values()) {
            if (m.id.equals(id)) open(p, loc, m);
        }
    }

    public enum Menus {
        PLAYER_EVENT("player_event", "Player Event", "Events", 5, PlayerEventItems.values(), false),
        PLAYER_ACTION("player_action", "Player Action", "Actions", 5, PlayerActionItems.values(), true),
        IF_PLAYER("if_player", "If Player", "Conditions", 3, IfPlayerItems.values(), true),
        SET_VARIABLE("set_variable", "Set Variable", "Variables", 5, SetVariableItems.values(), true),
        REPEAT("repeat", "Repeat", "Repeats", 3, RepeatItems.values(), true),
        CONTROL("control", "Control", "Controls", 3, null, true),
        DEV("dev", "Dev Actions", null, 3, null, true),
        GAME_ACTION("game_action","Game Action","Game",5,GameActionItems.values(), true)
        ;

        public final String id, fullTitle, shortTitle;
        public final int rows;
        public final ActionMenuItem[] items;
        public final boolean actions;
        Menus(String id, String fullTitle, String shortTitle, int rows, ActionMenuItem[] items, boolean actions) {
            this.id = id;
            this.fullTitle = fullTitle;
            this.shortTitle = shortTitle;
            this.rows = rows;
            this.items = items;
            this.actions = actions;
        }
    }
}
