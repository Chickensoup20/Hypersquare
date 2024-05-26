package hypersquare.hypersquare.dev;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface BarrelParamSupplier {
    BarrelParameter[] parameters();

    default @Nullable BarrelParameter getParameter(String id) {
        for (BarrelParameter param : parameters()) {
            if (Objects.equals(param.id(), id)) return param;
        }
        return null;
    }
}
