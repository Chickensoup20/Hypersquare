package hypersquare.hypersquare.dev;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public record BarrelTag(String id, String name, Enum<?> defaultOption, Option... options) {

    public @Nullable Option getOption(String id) {
        for (Option o : options) {
            if (o.id.name().equals(id)) return o;
        }
        return null;
    }

    public record Option(Enum<?> id, String text, Material icon) {
    }
}
