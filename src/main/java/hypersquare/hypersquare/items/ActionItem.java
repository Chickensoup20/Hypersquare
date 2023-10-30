package hypersquare.hypersquare.items;


import hypersquare.hypersquare.utils.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hypersquare.hypersquare.Hypersquare.mm;

public class ActionItem {

    private ItemStack actionItem;
    String name;
    Material material;
    String[] description;
    ActionArgument[] arguments;
    List<String[]> additionalInfo = List.of();
    boolean enchanted;

    public ActionItem setMaterial(Material material){
        this.material = material;
        return this;
    }
    public ActionItem setName(String name){
        this.name = name;
        return this;
    }

    public ActionItem setDescription(String... lore){
        this.description = lore;
        return this;
    }
    public ActionItem setArguments(ActionArgument... arguments) {
        this.arguments = arguments;
        return this;
    }

    public ActionItem addAdditionalInfo(String... info){
        List<String[]> infoList = new ArrayList<>(additionalInfo);
        info[0] = "<!italic><blue>⏵ <gray>" + info[0];
        infoList.add(info);
        this.additionalInfo = infoList;
        return this;
    }

    public ActionItem setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
        return this;
    }

    public ItemStack build(){
        actionItem = new ItemStack(material);
        ItemMeta meta = actionItem.getItemMeta();
        List<Component> lore = new ArrayList<>(List.of());

        //Name
        meta.displayName(mm.deserialize("<!italic>" + name));

        // Description
        for (String part : description) {
            lore.add(mm.deserialize("<!italic><gray>" + part));
        }

        // Arguments

        StringBuilder builder = new StringBuilder();
        builder.append("%n<white>Chest Parameters:%n");
        if (arguments != null) {
            for (ActionArgument actionArgument : arguments) {
                builder.append(mm.serialize(actionArgument.getType().getName(actionArgument.type)));
                builder.append(actionArgument.plural ? "(s)" : "");
                builder.append(actionArgument.optional ? "*" : "");
                builder.append(" <dark_gray>- <gray>").append(actionArgument.description).append("%n");
            }
        } else
            builder.append("<dark_gray>None");
        String builderString = String.valueOf(builder);

        String[] parts = builderString.split("%n");
        for (String part : parts) {
            lore.add(mm.deserialize("<!italic>" + part));
        }

        // Additional Info
        if (additionalInfo != null) {
            lore.add(Component.text(""));
            lore.add(mm.deserialize("<!italic><blue>Additional info:"));
            for (String[] info : additionalInfo) {
                for (String text : info) {
                    lore.add(mm.deserialize("<!italic><gray>" + text));
                }
            }
        }

        // Enchanted
        if (enchanted) meta.addEnchant(Enchantment.LURE, 1, true);

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS,  ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        actionItem.setItemMeta(meta);
        return actionItem;
    }
}
