package hypersquare.hypersquare.util;

import com.infernalsuite.aswm.api.exceptions.*;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import hypersquare.hypersquare.Hypersquare;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.function.Consumer;

public class WorldUtilities {

    public static void cloneWorld(String templateName, String newName, Consumer<SlimeWorld> callback) {
        new Thread(() -> {
            SlimePropertyMap properties = new SlimePropertyMap();
            properties.setValue(SlimeProperties.DIFFICULTY, "easy");
            SlimeWorld world;
            SlimeLoader file = Hypersquare.slimePlugin.getLoader("mongodb");

            try {
                world = Hypersquare.slimePlugin.loadWorld(file, templateName, false, properties);
                properties = world.getPropertyMap();
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                     WorldLockedException err) {
                return;
            }
            try {
                world.clone(newName, file);
            } catch (WorldAlreadyExistsException | IOException err) {
                err.printStackTrace();
            }

            SlimePropertyMap finalProperties = properties;
            new BukkitRunnable() {
                @Override
                public void run() {
                    SlimeWorld cloned;
                    try {
                        cloned = Hypersquare.slimePlugin.loadWorld(file, newName, false, finalProperties);
                        Hypersquare.slimePlugin.loadWorld(cloned);
                    } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                             WorldLockedException err) {
                        return;
                    }
                    callback.accept(cloned);
                }
            }.runTaskLater(Hypersquare.instance, 1);
        }).start();
    }

}
