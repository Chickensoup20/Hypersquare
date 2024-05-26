package hypersquare.hypersquare.plot;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Piston;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeBlockManagement {
    public static @Nullable Location findCorrespondingBracket(@NotNull Location location) {
        int i = 0;
        while (location.clone().add(0, 0, 1).getBlock().getType() != Material.AIR || location.clone().add(0, 0, 2).getBlock().getType() != Material.AIR || location.clone().getBlock().getType() != Material.AIR) {
            location.add(0, 0, 1);
            if (location.getBlock().getType() == Material.PISTON) {
                Piston piston = (Piston) location.getBlock().getBlockData();
                if (piston.getFacing() == BlockFace.SOUTH)
                    i--;
                if (piston.getFacing() == BlockFace.NORTH)
                    i++;
                if (i == 0) {
                    return location;
                }
            }

        }
        return null;
    }

    @Contract("_ -> param1")
    public static Location findCodeEnd(@NotNull Location location) {
        while (
            location.clone().add(0, 0, 1).getBlock().getType() != Material.AIR ||
                location.clone().add(0, 0, 2).getBlock().getType() != Material.AIR ||
                location.clone().getBlock().getType() != Material.AIR
        ) {
            location.add(0, 0, 1);
        }
        return location;
    }

    public static void moveCodeLine(@NotNull Location copyLoc, int amount) {
        Location endLoc = findCodeEnd(copyLoc.clone()).add(-1, 1, 0);
        World world = FaweAPI.getWorld(copyLoc.getWorld().getName());

        Region region = new CuboidRegion(
            world,
            BlockVector3.at(copyLoc.getBlockX(), copyLoc.getBlockY(), copyLoc.getBlockZ()),
            BlockVector3.at(endLoc.getBlockX(), endLoc.getBlockY(), endLoc.getBlockZ())
        );
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
            Operations.complete(forwardExtentCopy);
        }

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            BlockState block = BukkitAdapter.adapt(Material.AIR.createBlockData());
            editSession.setBlocks(region, block);
        }

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            Operation operation = new ClipboardHolder(clipboard)
                .createPaste(editSession)
                .to(region.getMinimumPoint().add(0, 0, amount))
                .build();

            Operations.complete(operation);
        }
    }
}
