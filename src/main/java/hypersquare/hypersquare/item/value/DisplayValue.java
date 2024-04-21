package hypersquare.hypersquare.item.value;


import hypersquare.hypersquare.dev.value.CodeValues;
import hypersquare.hypersquare.util.Utilities;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.BiFunction;

public enum DisplayValue {
    NUMBER(CodeValues.NUMBER, Colors.RED_LIGHT, (v, _) -> v == CodeValues.NUMBER),
    ITEM(CodeValues.ITEM, Colors.GOLD, (v, _) -> v == CodeValues.ITEM),
    BLOCK(CodeValues.ITEM, Colors.GOLD, (v, t) -> v == CodeValues.ITEM && t.getType().isBlock()),
    SPAWN_EGG(CodeValues.ITEM, Colors.GOLD, (v, t) -> v == CodeValues.ITEM && t.getType().getKey().getKey().endsWith("_spawn_egg")),
    ANY(null, Colors.GOLD_LIGHT, (_, _) -> true),
    VARIABLE(CodeValues.VARIABLE, Colors.YELLOW, (v, _) -> v == CodeValues.VARIABLE),
    TEXT(CodeValues.TEXT, Colors.LIME_DARK, (v, _) -> v == CodeValues.TEXT),
    LOCATION(CodeValues.LOCATION, Colors.GREEN, (v, _) -> v == CodeValues.LOCATION),
    LIST(null, Colors.GREEN_DARK_2, (_, _) -> false),
    VECTOR(null, Colors.TEAL, (_, _) -> false),
    STRING(null, Colors.AQUA_LIGHT, (v, _) -> v == CodeValues.STRING),
    NULL(CodeValues.NULL, Colors.AQUA_DESATURATED, (v, _) -> v == CodeValues.NULL),
    DICTIONARY(null, Colors.SKY, (_, _) -> false),
    SOUND(null, Colors.BLUE, (_, _) -> false),
    PARTICLE(null, Colors.PURPLE, (_, _) -> false),
    POTION(null, Colors.ROSE_DARK, (_, _) -> false),

    OR(null, Colors.ROSE_DARK, (_, _) -> false, true, "OR"),
    NONE(null, Colors.GRAY_DARK, (_, _) -> false, true),
    ;

    public static final HashMap<DisplayValue, Material> menuPaneColor = new HashMap<>() {{
        put(DisplayValue.NUMBER, Material.RED_STAINED_GLASS_PANE);
        put(DisplayValue.ITEM, Material.ORANGE_STAINED_GLASS_PANE);
        put(DisplayValue.BLOCK, Material.ORANGE_STAINED_GLASS_PANE);
        put(DisplayValue.SPAWN_EGG, Material.ORANGE_STAINED_GLASS_PANE);
        put(DisplayValue.ANY, Material.WHITE_STAINED_GLASS_PANE);
        put(DisplayValue.VARIABLE, Material.YELLOW_STAINED_GLASS_PANE);
        put(DisplayValue.TEXT, Material.LIME_STAINED_GLASS_PANE);
        put(DisplayValue.LIST, Material.GREEN_STAINED_GLASS_PANE);
        put(DisplayValue.VECTOR, Material.CYAN_STAINED_GLASS_PANE);
        put(DisplayValue.STRING, Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        put(DisplayValue.DICTIONARY, Material.BLUE_STAINED_GLASS);
        put(DisplayValue.SOUND, Material.BLUE_STAINED_GLASS_PANE);
        put(DisplayValue.PARTICLE, Material.PURPLE_STAINED_GLASS_PANE);
        put(DisplayValue.POTION, Material.MAGENTA_STAINED_GLASS_PANE);
        put(DisplayValue.LOCATION, Material.LIME_STAINED_GLASS_PANE);
    }};

    public final CodeValues codeVal;
    public final TextColor color;
    private final BiFunction<CodeValues, ItemStack, Boolean> isValid;
    public final boolean isIndicator;
    public final String overridenName;

    DisplayValue(@Nullable CodeValues codeVal, TextColor color, BiFunction<CodeValues, ItemStack, Boolean> isValid, boolean isIndicator, String overridenName) {
        this.codeVal = codeVal;
        this.color = color;
        this.isValid = isValid;
        this.isIndicator = isIndicator;
        this.overridenName = overridenName;
    }

    DisplayValue(@Nullable CodeValues codeVal, TextColor color, BiFunction<CodeValues, ItemStack, Boolean> isValid, boolean isIndicator) {
        this.codeVal = codeVal;
        this.color = color;
        this.isValid = isValid;
        this.isIndicator = isIndicator;
        this.overridenName = null;
    }

    DisplayValue(@Nullable CodeValues codeVal, TextColor color, BiFunction<CodeValues, ItemStack, Boolean> isValid) {
        this.codeVal = codeVal;
        this.color = color;
        this.isValid = isValid;
        this.isIndicator = false;
        this.overridenName = null;
    }

    public @NotNull Component getName() {
        return Component.text(overridenName == null ? Utilities.capitalize(toString().toLowerCase()) : overridenName).color(color);
    }

    public boolean notValid(CodeValues v, @Nullable ItemStack t) {
        return !isValid.apply(v, t);
    }
}
