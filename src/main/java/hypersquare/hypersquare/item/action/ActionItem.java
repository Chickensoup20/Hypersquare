package hypersquare.hypersquare.item.action;


import hypersquare.hypersquare.dev.BarrelParameter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActionItem {

    Component name;
    Material material;
    ItemStack itemStack;
    Component[] description;
    BarrelParameter[] params;
    int tags;
    List<Component[]> additionalInfo = List.of();
    boolean enchanted;
    SpecialActionType specialActionType;

    public ActionItem setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ActionItem setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ActionItem setName(Component name) {
        this.name = name;
        return this;
    }

    public ActionItem setDescription(Component... lore) {
        this.description = lore;
        return this;
    }

    public ActionItem setParameters(BarrelParameter... params) {
        this.params = params;
        return this;
    }

    public ActionItem setTagAmount(int tags) {
        this.tags = tags;
        return this;
    }

    public ActionItem addAdditionalInfo(Component @NotNull ... info) {
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

    public ActionItem setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
        return this;
    }

    public ActionItem setSpecialActionType(SpecialActionType specialActionType) {
        this.specialActionType = specialActionType;
        return this;
    }

    public ItemStack build() {
        ItemStack actionItem;
        if (itemStack == null) {
            actionItem = new ItemStack(material);
        } else {
            actionItem = itemStack;
        }
        ItemMeta meta = actionItem.getItemMeta();
        List<Component> lore = new ArrayList<>(List.of());

        //Name
        meta.displayName(name.decoration(TextDecoration.ITALIC, false));

        // Special Action Type
        if (specialActionType != null) {
            lore.add(specialActionType.getComp());
        }

        // Description
        for (Component part : description) {
            lore.add(part.color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        }

        // Arguments
        // Type - Description(s)*
        lore.add(Component.empty());
        lore.add(Component.text("Barrel Parameters:")
            .color(NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, false));
        boolean hasOptional = false;
        if (params != null) {
            for (BarrelParameter actionParameter : params) {
                Component paramComp = actionParameter.type().getName();
                paramComp = paramComp.append(Component.text(actionParameter.plural() ? "(s)" : ""));
                if (actionParameter.optional()) {
                    hasOptional = true;
                    paramComp = paramComp.append(Component.text("*").color(NamedTextColor.GRAY));
                }
                paramComp = paramComp.append(Component.text(actionParameter.type().isIndicator ? " " : " - ").color(NamedTextColor.DARK_GRAY)).append(actionParameter.description().color(NamedTextColor.GRAY));
                lore.add(paramComp.decoration(TextDecoration.ITALIC, false));
            }
        }

        // Tags
        // 1 Tag | 2 Tags
        if (tags != 0) {
            lore.add(Component.text("# ").decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.DARK_AQUA)
                .append(Component.text(tags)
                    .color(NamedTextColor.GRAY)
                    .append(Component.text(" Tag" + (tags == 1 ? "" : "s"))
                    )
                )
            );
        }

        if ((params == null || params.length == 0) & tags == 0)
            lore.add(Component.text("None").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));

        // Additional Info
        if (!additionalInfo.isEmpty()) {
            lore.add(Component.empty());
            lore.add(Component.text("Additional Info").color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false));
            for (Component[] info : additionalInfo) {
                for (Component text : info) {
                    lore.add(text.decoration(TextDecoration.ITALIC, false));
                }
            }
        }

        // *Optional
        if (hasOptional) {
            lore.add(Component.empty());
            lore.add(Component.text("*Optional").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true));
        }

        // Enchanted
        if (enchanted) meta.addEnchant(Enchantment.LURE, 1, true);

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        actionItem.setItemMeta(meta);
        return actionItem;
    }
}
