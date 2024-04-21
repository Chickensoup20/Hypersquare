package hypersquare.hypersquare.item;

import hypersquare.hypersquare.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static hypersquare.hypersquare.Hypersquare.cleanMM;

public enum CreatePlotMenuItems {
    BASIC_PLOT(Material.POLISHED_ANDESITE, "<#7070FF>", "Basic Plot", "<gray>Size: 64x64%n<gray>You have used %s <gray>of your Basic plots.%n<green>Click to create!"),
    LARGE_PLOT(Material.IRON_BLOCK, "<#7FFF7F>", "Large Plot", "<gray>Size: 128x128%n<gray>You have used %s <gray>of your Large plots.%n<green>Click to create!"),
    HUGE_PLOT(Material.GOLD_BLOCK, "<#FFFFAA>", "Huge Plot", "<gray>Size: 256x256%n<gray>You have used %s <gray>of your Huge plots.%n<green>Click to create!"),
    MASSIVE_PLOT(Material.DIAMOND_BLOCK, "<#00E8FF>", "Massive Plot", "<gray>Size: 512x512%n<gray>You have used %s <gray>of your Massive plots.%n<green>Click to create!"),
    GIGANTIC_PLOT(Material.NETHERITE_BLOCK, "<#333232>", "Gigantic Plot", "<gray>Size: 1024x1024%n<gray>You have used %s <gray>of your Gigantic plots.%n<green>Click to create!");

    public final Material material;
    public final String color;
    public final String name;
    public final String lore;

    CreatePlotMenuItems(Material material, String color, String name, String lore) {
        this.material = material;
        this.color = color;
        this.name = name;
        this.lore = lore;
    }

    public ItemStack build(int usedPlots, int maxPlots) {
        String tempLore = lore.replace("%s", (usedPlots != maxPlots ? "<green>" : "<red>") + usedPlots + "/" + maxPlots);
        String[] parts = tempLore.split("%n");
        List<Component> list = new ArrayList<>(List.of());
        for (String part : parts) {
            list.add(cleanMM.deserialize(part));
        }

        return new ItemBuilder(material)
            .name(cleanMM.deserialize(color + name))
            .lore(list)
            .hideFlags()
            .build();
    }
}
