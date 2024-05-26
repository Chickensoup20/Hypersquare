package hypersquare.hypersquare.dev;

import org.jetbrains.annotations.Nullable;

public interface BarrelTagSupplier {
    BarrelTag[] tags();

    default @Nullable BarrelTag getTag(String id) {
        for (BarrelTag tag : tags()) {
            if (tag.id().equals(id)) return tag;
        }
        return null;
    }
}
