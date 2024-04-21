package hypersquare.hypersquare.plot;

import hypersquare.hypersquare.listener.CodePlacement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class PlotEvents implements Listener {

    //Stopping blocks indirectly being placed outside plot
    @EventHandler
    public void BlockDispense(BlockDispenseEvent event) {
        Location blockLocation = event.getBlock().getLocation();
        Location dispenseLocation = blockLocation.clone().add(blockLocation.getDirection().multiply(-1));
        if (!CodePlacement.blockInPlot(dispenseLocation)) event.setCancelled(true);
    }

    @EventHandler
    public void BlockMultiPlace(BlockMultiPlaceEvent event) {
        for (BlockState blockState : event.getReplacedBlockStates()) {
            if (!CodePlacement.blockInPlot(blockState.getLocation())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void StructureGrow(StructureGrowEvent event) {
        for (BlockState blockState : event.getBlocks()) {
            if (!CodePlacement.blockInPlot(blockState.getLocation())) blockState.setType(Material.AIR);
        }
    }


    @EventHandler
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        if (!CodePlacement.blockInPlot(event.getBlock().getLocation())) event.setCancelled(true);
    }
}
