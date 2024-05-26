package hypersquare.hypersquare.util.string;

import hypersquare.hypersquare.item.value.DisplayValue;
import net.kyori.adventure.text.Component;

public class ColorWrapper {
    public static Component number(String number) {
        return Component.text(number).color(DisplayValue.NUMBER.color);
    }

    public Component number(int number) {
        return number(String.valueOf(number));
    }

    public Component number(float number) {
        return number(String.valueOf(number));
    }

    public Component number(double number) {
        return number(String.valueOf(number));
    }
}
