package hypersquare.hypersquare.menu.barrel;

import com.google.gson.JsonObject;
import hypersquare.hypersquare.dev.ArgumentsData;
import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.value.CodeValues;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.system.MenuItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MenuParameter extends MenuItem {

    /*
       Example:
       ----------
       String(s)*
       String to change

       *Optional
     */
    private static final Component optionalDesc = Component.text("*Optional").color(NamedTextColor.GRAY);
    private final int slotId;
    private final BarrelParameter p;

    public MenuParameter(@NotNull BarrelParameter p, int slotId, @NotNull ArgumentsData argsData) {
        super(DisplayValue.menuPaneColor.get(p.type()));
        this.slotId = slotId;
        this.p = p;

        List<JsonObject> current = argsData.getArguments().computeIfAbsent(p.id(), _ -> new ArrayList<>());

        Component typeName = p.type().getName();
        if (p.plural()) typeName = typeName.append(Component.text("(s)"));
        if (p.optional()) typeName = typeName.append(Component.text("*").color(NamedTextColor.GRAY));

        name(typeName.decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(p.description().color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        if (p.optional()) {
            lore.add(Component.empty());
            lore.add(optionalDesc);
        }
        lore(lore);

        if (current.size() > slotId) {
            JsonObject data = current.get(slotId);
            if (data.has("type")) {
                CodeValues v = CodeValues.getType(data);
                if (v != null) {
                    ItemStack item = v.getItem(v.fromJson(data));
                    ItemMeta meta = item.getItemMeta();
                    List<Component> itemLore = meta.lore();
                    if (itemLore == null) itemLore = new ArrayList<>();
                    lore.addAll(0, itemLore);
                    lore.add(itemLore.size(), Component.empty());
                    lore.add(itemLore.size() + 1, typeName.decoration(TextDecoration.ITALIC, false));

                    lore(lore);
                    material(item.getType());
                    name(meta.displayName());
                    amount(item.getAmount());
                }
            }
        }
    }

    public boolean notValid(ItemStack item) {
        if (item == null) return true;
        JsonObject data = CodeValues.getVarItemData(item);
        if (data == null) return p.type().notValid(CodeValues.ITEM, item);
        CodeValues v = CodeValues.getType(data);
        if (v == CodeValues.VARIABLE) return false;
        return p.type().notValid(v, item);
    }

    public ItemStack replaceValue(ArgumentsData argsData, ItemStack newItem) {
        ItemStack oldItem = getValue(argsData, true);
        List<JsonObject> previous = argsData.getArguments().computeIfAbsent(p.id(), _ -> new ArrayList<>());

        JsonObject data = CodeValues.getVarItemData(newItem);
        CodeValues v;
        if (data == null) v = CodeValues.ITEM; // has no varitem means it's a regular item
        else v = CodeValues.getType(data);

        if (v == null) return oldItem;
        if (p.type().notValid(v, newItem) && p.type().codeVal != null && v != CodeValues.VARIABLE) {
            v = p.type().codeVal;
            if (CodeValues.getType(data) != v) {
                Object newVal = v.coerce(data);
                if (newVal == null) return newItem;
                newItem = v.getItem(newVal);
            } else {
                newItem = v.getItem(v.fromJson(data));
            }
        }

        JsonObject json = v.getVarItemData(v.fromItem(newItem));
        json.addProperty("type", v.getTypeId());
        while (previous.size() <= slotId) previous.add(new JsonObject());
        previous.set(slotId, json);

        return oldItem;
    }

    public MenuParameter updated(ArgumentsData argsData) {
        return new MenuParameter(p, slotId, argsData);
    }

    public boolean isEmpty(@NotNull CodeActionData action) {
        List<JsonObject> previous = action.getArguments().computeIfAbsent(p.id(), _ -> new ArrayList<>());

        return previous.size() <= slotId || !previous.get(slotId).has("type");
    }

    public void reset(@NotNull List<JsonObject> previous) {
        previous.set(slotId, new JsonObject());
        while (!previous.isEmpty() && !previous.getLast().has("type")) {
            previous.removeLast();
        }
    }

    public ItemStack getValue(@NotNull ArgumentsData argsData, boolean reset) {
        List<JsonObject> previous = argsData.getArguments().computeIfAbsent(p.id(), _ -> new ArrayList<>());
        if (previous.size() > slotId) {
            JsonObject data = previous.get(slotId);
            if (data != null && data.has("type")) {
                CodeValues v = CodeValues.getType(data);
                if (v == null) return null;
                if (reset) reset(previous);
                return v.getItem(v.fromJson(data));
            }
        }
        return null;
    }
}
