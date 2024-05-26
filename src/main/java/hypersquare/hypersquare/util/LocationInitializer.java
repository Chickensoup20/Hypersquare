package hypersquare.hypersquare.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class LocationInitializer {

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Location getBasicLocation(World world) {
        return new Location(world, 64, -640, 64);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Location getLargeLocation(World world) {
        return new Location(world, 128, -640, 128);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Location getHugeLocation(World world) {
        return new Location(world, 256, -640, 256);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Location getMassiveLocation(World world) {
        return new Location(world, 512, -640, 512);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Location getGiganticLocation(World world) {
        return new Location(world, 1024, -640, 1024);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Location getCommonStartLocation(World world) {
        return new Location(world, 0, 255, 0);
    }
}
