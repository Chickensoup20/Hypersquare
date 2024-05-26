package hypersquare.hypersquare.item.event;

import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EventItem {
    Component name;
    Material material;
    Component[] description;
    List<Component[]> additionalInfo = List.of();
    boolean cancellable;

    public EventItem setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public EventItem setName(Component name) {
        this.name = name;
        return this;
    }

    public EventItem setDescription(Component... lore) {
        this.description = lore;
        return this;
    }

    public EventItem addAdditionalInfo(Component @NotNull ... info) {
        List<Component[]> infoList = new ArrayList<>(additionalInfo);
        info[0] = Component.text("⏵")
            .color(NamedTextColor.BLUE)
            .append(Component.text(" ")
                .color(NamedTextColor.GRAY)
                .append(info[0]
                    .color(NamedTextColor.GRAY)));
        for (int i = 1; i < info.length; i++) {
            info[i] = info[i].color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false);
        }
        infoList.add(info);
        this.additionalInfo = infoList;
        return this;
    }

    public EventItem setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    public ItemStack build() {
        ItemStack eventItem = new ItemStack(material);
        ItemMeta meta = eventItem.getItemMeta();
        List<Component> lore = new ArrayList<>(List.of());

        // Name
        meta.displayName(name.decoration(TextDecoration.ITALIC, false));

        // Description
        for (Component part : description) {
            lore.add(part.color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        }

        // Additional Info
        if (!additionalInfo.isEmpty()) {
            lore.add(Component.text("").decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("Additional Info").color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false));
            for (Component[] info : additionalInfo) {
                for (Component text : info) {
                    lore.add(text.decoration(TextDecoration.ITALIC, false));
                }
            }
        }

        // Cancellable
        if (cancellable) {
            lore.add(Component.empty());
            lore.add(Component.text("∅").decoration(TextDecoration.ITALIC, false)
                .color(Colors.RED_DARK)
                .append(Component.text(" Cancellable")
                    .color(Colors.RED)
                )
            );
        }

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        eventItem.setItemMeta(meta);
        return eventItem;
    }
}
