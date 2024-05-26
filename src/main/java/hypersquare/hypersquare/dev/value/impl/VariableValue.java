package hypersquare.hypersquare.dev.value.impl;

import com.google.gson.JsonObject;
import hypersquare.hypersquare.dev.value.CodeValue;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.system.Menu;
import hypersquare.hypersquare.menu.system.MenuItem;
import hypersquare.hypersquare.play.CodeVariable;
import hypersquare.hypersquare.util.ItemBuilder;
import hypersquare.hypersquare.util.Utilities;
import hypersquare.hypersquare.util.color.Colors;
import hypersquare.hypersquare.util.component.BasicComponent;
import hypersquare.hypersquare.util.string.ClickTexts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class VariableValue implements CodeValue<VariableValue.HSVar, CodeVariable> {

    @Override
    public Component getName() {
        return Component.text("Variable").color(DisplayValue.VARIABLE.color);
    }

    @Override
    public Material getMaterial() {
        return Material.MAGMA_CREAM;
    }

    @Override
    public String getTypeId() {
        return "variable";
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
            BasicComponent.gray("Variables can be used in expressions"),
            BasicComponent.gray("to refer to other values.")
        );
    }

    @Override
    public List<Component> getHowToSet() {
        return List.of(
            BasicComponent.gray("Type in chat while holding"),
            BasicComponent.gray("this item to change its name."),
            BasicComponent.gray("Right-click to open the scope"),
            BasicComponent.gray("menu, Shift + Right-click to switch"),
            BasicComponent.gray("between scopes. Add -l -t -g or -s to the"),
            BasicComponent.gray("name to quickly switch scope.")
        );
    }

    @Override
    public JsonObject getVarItemData(HSVar type) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", type.name);
        obj.addProperty("scope", type.scope.name());
        return obj;
    }

    @Override
    public Component getValueName(HSVar value) {
        return BasicComponent.create()
            .append(
                Component.text("[" + value.scope.letter + "] ").color(value.scope().color)
            )
            .append(
                Component.text(value.name).color(NamedTextColor.YELLOW)
            );
    }

    @Override
    public HSVar fromJson(JsonObject obj) {
        return new HSVar(
            obj.get("name").getAsString(),
            Scope.valueOf(obj.get("scope").getAsString())
        );
    }

    public void onRightClick(Player player, HSVar var) {
        Menu scopeSwitcher = new Menu(Component.text("Scope Switcher"), 1);

        MenuItem lineScope = createScopeMenuItem(
            Material.CHEST_MINECART,
            var,
            Scope.LINE,
            player,
            List.of(
                BasicComponent.gray("Only accessible in the current line."),
                BasicComponent.gray("Does not get passed onto function calls.")
            )
        );

        MenuItem threadScope = createScopeMenuItem(
            Material.CHEST,
            var,
            Scope.THREAD,
            player,
            List.of(
                BasicComponent.gray("Only accessible in the current thread."),
                BasicComponent.gray("Passed onto function calls and"),
                BasicComponent.gray("can be passed to processes.")
            )
        );

        MenuItem globalScope = createScopeMenuItem(
            Material.BARREL,
            var,
            Scope.GLOBAL,
            player,
            List.of(
                BasicComponent.gray("Accessible everywhere in the code."),
                BasicComponent.gray("Purged when all players leave the plot.")
            )
        );

        MenuItem saveScope = createScopeMenuItem(
            Material.ENDER_CHEST,
            var,
            Scope.SAVE,
            player,
            List.of(
                BasicComponent.gray("Accessible everywhere in the code."),
                BasicComponent.gray("Persists when all players leave the plot.")
            )
        );

        // [ ][L][ ][T][ ][G][ ][S][ ]
        scopeSwitcher.slot(1, lineScope);
        scopeSwitcher.slot(3, threadScope);
        scopeSwitcher.slot(5, globalScope);
        scopeSwitcher.slot(7, saveScope);
        scopeSwitcher.open(player);
        Utilities.sendOpenMenuSound(player);
    }

    private MenuItem createScopeMenuItem(Material material, HSVar var, Scope scope, Player player, List<Component> lore) {
        lore.add(Component.empty());
        lore.add(
            ClickTexts.LEFT_CLICK.append(
                Component.text(" Click to set scope")
                    .color(Colors.GRAY)
            )
        );

        return new MenuItem(
            new ItemBuilder(material)
                .name(Component.text(scope.name()).color(scope.color))
                .lores(lore)
                .build()
        ).onClick(() -> {
            player.getOpenInventory().close();
            Utilities.sendSuccessClickMenuSound(player);

            player.getInventory().setItemInMainHand(getItem(new HSVar(var.name, scope)));
        });
    }


    @Override
    public HSVar defaultValue() {
        return new HSVar("(unnamed)", Scope.THREAD);
    }

    @Override
    public HSVar fromString(String data, HSVar previous) {
        Scope scope = previous == null ? Scope.THREAD : previous.scope;
        for (Scope s : Scope.values()) {
            String flag = "-" + Character.toLowerCase(s.name().charAt(0));
            if (data.endsWith(flag)) {
                data = data.substring(0, data.length() - 2);
                data = data.trim();
                scope = s;
                break;
            }
            if (data.startsWith(flag)) {
                data = data.substring(2);
                data = data.trim();
                scope = s;
                break;
            }
        }
        return new HSVar(data, scope);
    }

    @Override
    public ItemStack getItem(HSVar value) {
        ItemStack item = CodeValue.super.getItem(value);
        ItemMeta meta = item.getItemMeta();
        meta.lore(List.of(Component.text(value.scope.name())
            .color(value.scope.color)
            .decoration(TextDecoration.ITALIC, false)));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public CodeVariable realValue(HSVar value) {
        return new CodeVariable(value.name, value.scope);
    }

    @Override
    public JsonObject serialize(Object obj) {
        return null;
    }

    public enum Scope {
        GLOBAL(Colors.GREEN_LIGHT, "G"),
        THREAD(Colors.AQUA_LIGHT, "T"),
        LINE(Colors.SKY, "L"),
        SAVE(Colors.YELLOW_LIGHT, "S"),
        ;

        public final TextColor color;
        public final String letter;

        Scope(TextColor color, String letter) {
            this.color = color;
            this.letter = letter;
        }
    }

    public record HSVar(String name, Scope scope) {
    }
}
