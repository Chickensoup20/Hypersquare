package hypersquare.hypersquare.items;

import hypersquare.hypersquare.utils.ItemBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hypersquare.hypersquare.Hypersquare.mm;

public enum CreatePlotMenuItems {
    BASIC_PLOT(Material.POLISHED_ANDESITE, "<#7070FF>", "Basic Plot", "<gray>Size: 64x64%n<gray>You have used %s<gray>of your Basic plots.%n<green>Click to create!"),
    LARGE_PLOT(Material.IRON_BLOCK, "<#7FFF7F>", "Large Plot", "<gray>Size: 128x128%n<gray>You have used %s<gray>of your Large plots.%n<green>Click to create!"),
    HUGE_PLOT(Material.GOLD_BLOCK, "<#FFFFAA>", "Huge Plot", "<gray>Size: 256x256%n<gray>You have used %s<gray>of your Huge plots.%n<green>Click to create!"),
    MASSIVE_PLOT(Material.DIAMOND_BLOCK, "<#00E8FF>", "Massive Plot", "<gray>Size: 512x512%n<gray>You have used %s<gray>of your Massive plots.%n<green>Click to create!"),
    GIGANTIC_PLOT(Material.NETHERITE_BLOCK, "<#333232>", "Gigantic Plot", "<gray>Size: 1024x1024%n<gray>You have used %s<gray>of your Gigantic plots.%n<green>Click to create!");

    @Getter Material material;
    @Getter final String color;
    @Getter final String name;
    @Getter
    String lore;
    CreatePlotMenuItems(Material material, String color, String name, String lore) {
        this.material = material;
        this.color = color;
        this.name = name;
        this.lore = lore;
    }

    public ItemStack build(int usedPlots, int maxPlots) {
        lore = lore.replace("%s", (usedPlots != maxPlots ? "<green>" : "<red>") + " " + usedPlots);
        String[] parts = lore.split("%n");
        List<Component> list = new ArrayList<>(List.of());
        for (String part : parts) {
            list.add(mm.deserialize(part));
        }

        return new ItemBuilder(material)
                .name(mm.deserialize(color + name))
                .lore(list)
                .hideFlags()
                .build();
    }
}
