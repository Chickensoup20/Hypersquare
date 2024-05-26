package hypersquare.hypersquare.menu.barrel;

import com.google.gson.JsonObject;
import hypersquare.hypersquare.dev.*;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.CodeFile;
import hypersquare.hypersquare.dev.codefile.CodeFileHelper;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.codefile.data.CodeData;
import hypersquare.hypersquare.dev.codefile.data.CodeLineData;
import hypersquare.hypersquare.dev.value.CodeValues;
import hypersquare.hypersquare.dev.value.impl.VariableValue;
import hypersquare.hypersquare.menu.system.Menu;
import hypersquare.hypersquare.menu.system.MenuItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

public class BarrelMenu extends Menu {

    private final BarrelParamSupplier paramSupplier;
    private final BarrelTagSupplier tagSupplier;
    private final TagOptionsData tagsData;
    private final ArgumentsData argsData;
    private Location block;

    public BarrelMenu(
        @NotNull Component name, @NotNull BarrelParamSupplier paramSupplier, @NotNull BarrelTagSupplier tagSupplier,
        int rows, @NotNull TagOptionsData tagsData, @NotNull ArgumentsData argsData
    ) {
        super(name, rows);
        this.paramSupplier = paramSupplier;
        this.tagSupplier = tagSupplier;
        this.tagsData = tagsData;
        this.argsData = argsData;
        for (int i = 0; i < rows * 9; i++) {
            slot(i, new MenuItem(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()));
        }
    }

    public BarrelMenu(@NotNull Action action, int rows, @NotNull CodeActionData data) {
        this(Component.text(action.getName()), action, action, rows, data, data);
    }

    public BarrelMenu(@NotNull String name, int rows, @NotNull CodeLineData data) {
        this(Component.text(name), data, data, rows, data, data);
    }

    public BarrelMenu parameter(String id, int @NotNull ... slots) {
        int slotId = 0;
        for (int slot : slots) {
            BarrelParameter param = paramSupplier.getParameter(id);
            if (param == null) continue;
            this.slot(slot, new MenuParameter(param, slotId, argsData));
            slotId++;
        }
        return this;
    }

    public BarrelMenu parameterRange(String id, int startSlot, int endSlot) {
        int slotId = 0;
        for (int slot = startSlot; slot <= endSlot; slot++) {
            BarrelParameter param = paramSupplier.getParameter(id);
            if (param == null) continue;
            this.slot(slot, new MenuParameter(param, slotId, argsData));
            slotId++;
        }
        return this;
    }

    public BarrelMenu tag(String id, int slot) {
        this.slot(slot, new MenuTag(tagSupplier.getTag(id), tagsData));
        return this;
    }

    @Override
    public void performClick(@NotNull InventoryClickEvent event) {
        MenuItem menuItem = items.get(event.getSlot());
        if (menuItem == null) return;
        CodeFile file = new CodeFile(block.getWorld());
        CodeData data = file.getCodeData();
        if (menuItem instanceof MenuParameter param) {
            HumanEntity p = event.getWhoClicked();
            ArgumentsData argsData = CodeFileHelper.getArgsDataAt(block, data);
            if (argsData == null) return;
            ItemStack cursor = p.getItemOnCursor();
            if (cursor.isEmpty()) p.setItemOnCursor(param.getValue(argsData, true));
            else p.setItemOnCursor(param.replaceValue(argsData, cursor));
            slot(event.getSlot(), param.updated(argsData));
        } else if (menuItem instanceof MenuTag tag) {
            TagOptionsData tagsData = CodeFileHelper.getTagsDataAt(block, data);
            if (tagsData == null) return;
            VariableValue.HSVar varValue = tagsData.getTags().getOrDefault(tag.tag.id(), new Pair<>(null, null)).getB();

            ItemStack clickedItem = event.getWhoClicked().getItemOnCursor();
            JsonObject itemData = CodeValues.getVarItemData(clickedItem);
            int direction = event.isRightClick() ? -1 : 1;

            if (CodeValues.VARIABLE.isType(itemData)) {
                varValue = (VariableValue.HSVar) CodeValues.VARIABLE.fromItem(clickedItem);
                direction = 0;
                ItemStack item = event.getWhoClicked().getItemOnCursor();
                item.setAmount(item.getAmount() - 1);
                event.getWhoClicked().setItemOnCursor(item);
            }

            int position = -1;
            for (int i = 0; i < tag.tag.options().length; i++) {
                if (tag.tag.options()[i] == MenuTag.currentOption(tag.tag, tagsData)) {
                    position = i;
                    break;
                }
            }
            if (position == -1) throw new IllegalStateException(); //TODO Add error message
            position += direction;
            position = (position + tag.tag.options().length) % tag.tag.options().length;

            tagsData.getTags().put(tag.tag.id(), new Pair<>(
                tag.tag.options()[position].id().name(),
                varValue
            ));
            slot(event.getSlot(), new MenuTag(tag.tag, tagsData));
            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1.5f);
        }
        file.setCode(data.toJson().toString());
    }

    @Override
    public void open(@NotNull Player player) {
        throw new IllegalStateException("Regular open call on BarrelMenu");
    }

    public void open(Player player, Location block) {
        this.block = block;
        super.open(player);
    }

    @Override
    public void shiftClick(InventoryClickEvent event) {
        HumanEntity p = event.getWhoClicked();
        CodeFile file = new CodeFile(block.getWorld());
        CodeData data = file.getCodeData();
        CodeActionData action = CodeFileHelper.getActionAt(block, data);
        if (action == null) return;
        if (event.getClickedInventory() == p.getInventory()) {
            ItemStack item = p.getInventory().getItem(event.getSlot());
            for (int i = 0; i < getSize(); i++) {
                if (!items.containsKey(i)) continue;
                if (items.get(i) instanceof MenuParameter param) {
                    if (!param.isEmpty(action)) continue;
                    if (param.notValid(item)) continue;
                    param.replaceValue(action, item);
                    slot(i, param.updated(action));
                    file.setCode(data.toJson().toString());
                    p.getInventory().setItem(event.getSlot(), null);
                    return;
                }
            }
        } else if (items.get(event.getSlot()) instanceof MenuParameter param) {
            ItemStack item = param.getValue(action, true);
            if (item != null) p.getInventory().addItem(item);
            slot(event.getSlot(), param.updated(action));
            file.setCode(data.toJson().toString());
        } else if (items.get(event.getSlot()) instanceof MenuTag tag) {
            VariableValue.HSVar varValue = action.getTags().getOrDefault(tag.tag.id(), new Pair<>(null, null)).getB();
            if (varValue == null) return;

            p.setItemOnCursor(CodeValues.VARIABLE.getItem(varValue));

            action.getTags().put(tag.tag.id(), new Pair<>(
                action.getTags().getOrDefault(tag.tag.id(), new Pair<>(tag.tag.defaultOption().name(), null)).getA(),
                null
            ));
            file.setCode(data.toJson().toString());
            slot(event.getSlot(), new MenuTag(tag.tag, action));
            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1.5f);
        }
    }

    @Override
    public void middleClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        CodeFile file = new CodeFile(block.getWorld());
        CodeData data = file.getCodeData();
        CodeActionData action = CodeFileHelper.getActionAt(block, data);
        if (action == null) return;
        if (event.getClickedInventory() == player.getInventory()) {
            event.setCancelled(false);
        } else if (items.get(event.getSlot()) instanceof MenuParameter param) {
            ItemStack item = param.getValue(action, false);
            item.setAmount(64);
            player.setItemOnCursor(item);
        }
    }
}
