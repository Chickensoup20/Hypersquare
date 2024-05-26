package hypersquare.hypersquare;

import org.bukkit.NamespacedKey;

public class HSKeys {
    public final static NamespacedKey CODE = create("code");
    public final static NamespacedKey PLOT_TYPE = create("plotType");

    private static NamespacedKey create(String key) {
        return new NamespacedKey(Hypersquare.instance, key);
    }
}
