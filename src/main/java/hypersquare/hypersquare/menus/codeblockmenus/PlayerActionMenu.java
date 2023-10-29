package hypersquare.hypersquare.menus.codeblockmenus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import hypersquare.hypersquare.items.PlayerActionItems;
import hypersquare.hypersquare.items.actions.GiveItemsAction;
import hypersquare.hypersquare.utils.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hypersquare.hypersquare.Hypersquare.mm;

public class PlayerActionMenu {
    public static Gui create() {
        Gui gui = Gui.gui()
                .title(Component.text("Player Actions"))
                .rows(5)
                .create();

        // Loop through all categories
        for (PlayerActionItems playerActionItem : PlayerActionItems.values()) {
            if (playerActionItem.getCategory() != null) continue; // Skip if not a category
            int slot = playerActionItem.getSlot();

            // Clicking a category
            GuiItem item = ItemBuilder.from(playerActionItem.build()).asGuiItem(event -> {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                Utilities.sendSecondaryMenuSound(player);

                if (PlayerActionItems.getActions(playerActionItem).isEmpty()) return; // In case there are no actions

                // Strip Color Codes
                Pattern pattern = Pattern.compile("<[^>]+?>");
                Matcher matcher = pattern.matcher(mm.serialize(playerActionItem.getName()));

                // Create a new gui for the category
                Gui categoryGui = Gui.gui()
                        .title(mm.deserialize("Player Actions <dark_gray>> ").append(mm.deserialize(matcher.replaceAll(""))))
                        .rows(5)
                        .create();

                // Loop through all actions in the category
                for (PlayerActionItems action : PlayerActionItems.getActions(playerActionItem)) {
                    GuiItem actionItem = ItemBuilder.from(action.build()).asGuiItem(event2 -> {
                        event.setCancelled(true);
                        Utilities.sendSuccessClickMenuSound(player);
                        Block block = player.getTargetBlock(null, 5);
                        Utilities.setAction(block, action.getId(), player);
                    });
                    categoryGui.addItem(actionItem);
                }

                // Open the category GUI
                categoryGui.open(player);
            });

            gui.setItem(slot, item);
        }

        return gui;
    }
}
