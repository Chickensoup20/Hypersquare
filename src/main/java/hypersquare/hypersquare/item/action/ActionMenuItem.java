package hypersquare.hypersquare.item.action;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public interface ActionMenuItem {
    int getSlot();

    ItemStack build();

    Component getName();
}
