package hypersquare.hypersquare.listener;

import com.google.gson.JsonArray;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import hypersquare.hypersquare.HSKeys;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.Actions;
import hypersquare.hypersquare.dev.CodeBlocks;
import hypersquare.hypersquare.dev.codefile.CodeFile;
import hypersquare.hypersquare.dev.codefile.CodeFileHelper;
import hypersquare.hypersquare.dev.codefile.data.CodeData;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.plot.CodeBlockManagement;
import hypersquare.hypersquare.plot.PlotDatabase;
import hypersquare.hypersquare.plot.RestrictMovement;
import hypersquare.hypersquare.util.PlotUtilities;
import hypersquare.hypersquare.util.Utilities;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CodePlacement implements Listener {
    private static boolean checkIfValidAgainst(@NotNull Block againstLocation) {
        Material type = againstLocation.getType();
        return type == Material.STONE || type == Material.PISTON || type == Material.STICKY_PISTON;
    }

    public static boolean blockOutsidePlot(Location location) {
        RestrictMovement.commonVars(location);
        return Utilities.notWithinLocation(location, new Location(location.getWorld(), -3, 0, 0), new Location(location.getWorld(), -108, 0, 256));
    }

    public static boolean blockInPlot(@NotNull Location location) {
        String plotType = location.getWorld().getPersistentDataContainer().get(HSKeys.PLOT_TYPE, PersistentDataType.STRING);
        RestrictMovement.commonVars(location);
        switch (plotType) {
            case null:
                break;
            case "Basic":
                if (Utilities.notWithinLocation(location, RestrictMovement.commonStart, RestrictMovement.basic.clone().add(-1, 0, -1))) {
                    return false;
                }
                break;
            case "Large":
                if (Utilities.notWithinLocation(location, RestrictMovement.commonStart, RestrictMovement.large.clone().add(-1, 0, -1))) {
                    return false;
                }
                break;
            case "Huge":
                if (Utilities.notWithinLocation(location, RestrictMovement.commonStart, RestrictMovement.huge.clone().add(-1, 0, -1))) {
                    return false;
                }
                break;
            case "Massive":
                if (Utilities.notWithinLocation(location, RestrictMovement.commonStart, RestrictMovement.massive.clone().add(-1, 0, -1))) {
                    return false;
                }
                break;
            case "Gigantic":
                if (Utilities.notWithinLocation(location, RestrictMovement.commonStart, RestrictMovement.gigantic.clone().add(-1, 0, -1))) {
                    return false;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + plotType);
        }
        return true;
    }

    private static void placeBlock(ItemStack codeblockItem, @NotNull Location location, CodeBlocks codeblock) {
        Location signLocation = location.clone().add(-1, 0, 0);
        CodeFile code = new CodeFile(location.getWorld());
        JsonArray codeJson = CodeFileHelper.addCodeblock(location.clone(), codeblock, code).toJson();
        code.setCode(codeJson.toString());

        if (!codeblock.name().isEmpty()) { // Either invalid or empty codeblock
            signLocation.getBlock().setType(Material.OAK_WALL_SIGN);
            BlockData blockData = signLocation.getBlock().getBlockData();
            ((Directional) blockData).setFacing(BlockFace.WEST);
            Sign sign = (Sign) signLocation.getBlock().getState();
            sign.setWaxed(true);
            sign.getSide(Side.FRONT).line(0, Component.text(codeblock.name).color(Colors.YELLOW_LIGHT));
            if (!codeblock.defaultAction.isEmpty()) {
                String defaultAction = codeblock.defaultAction;
                Actions action = Actions.getAction(defaultAction, codeblock.id);
                if (action != null && action != Actions.EMPTY) {
                    sign.getSide(Side.FRONT).line(1, Component.text(action.getSignName()).color(Colors.WHITE));
                } else sign.getSide(Side.FRONT).line(1, Component.text("INVALID DEFAULT").color(Colors.RED));
            }
            sign.update();
            signLocation.getBlock().setBlockData(blockData);
        }

        Location codeblockLocation = location.clone();
        codeblockLocation.getBlock().setType(codeblockItem.getType());

        Location stoneLocation = location.clone().add(0, 0, 1);
        Location closeBracketLocation = location.clone().add(0, 0, 3);
        Location barrelLocation = location.add(0, 1, 0);
        Location openBracketLocation = location.clone().add(0, -1, 1);

        if (codeblock.hasBrackets) {
            // Open Bracket
            openBracketLocation.getBlock().setType(Material.PISTON);
            BlockData pistonData = openBracketLocation.getBlock().getBlockData();
            ((Directional) pistonData).setFacing(BlockFace.SOUTH);
            openBracketLocation.getBlock().setBlockData(pistonData);

            // Close bracket
            CodeBlockManagement.moveCodeLine(closeBracketLocation, 1);
            closeBracketLocation.getBlock().setType(Material.PISTON);
            pistonData = closeBracketLocation.getBlock().getBlockData();
            ((Directional) pistonData).setFacing(BlockFace.NORTH);
            closeBracketLocation.getBlock().setBlockData(pistonData);
        } else {
            // Stone Separator, skip if empty codeblock
            if (!codeblock.id.equals("empty")) stoneLocation.getBlock().setType(Material.STONE);
            if (stoneLocation.clone().add(0, 0, 1).getBlock().getType() == Material.PISTON || stoneLocation.clone().add(0, 0, 1).getBlock().getType() == Material.STICKY_PISTON) {
                CodeBlockManagement.moveCodeLine(stoneLocation.clone().add(0, 0, 1), 1);
            }
        }
        if (codeblock.hasBarrel) {
            barrelLocation.getBlock().setType(Material.BARREL);
            BlockData barrelBlockData = barrelLocation.getBlock().getBlockData();
            ((Directional) barrelBlockData).setFacing(BlockFace.UP);
            barrelLocation.getBlock().setBlockData(barrelBlockData);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        String mode = Hypersquare.mode.get(event.getPlayer());
        if (mode.equals("building") || mode.equals("playing")) {
            if (blockInPlot(event.getBlockPlaced().getLocation())) return;
            else event.setCancelled(true);
        }
        if (Hypersquare.mode.get(event.getPlayer()).equals("coding")) processPlace(event);
    }

    public void processPlace(@NotNull BlockPlaceEvent event) {
        Long cooldown = Hypersquare.cooldownMap.get(event.getPlayer().getUniqueId());
        if (cooldown == null) cooldown = 0L;
        event.setCancelled(true);
        if (cooldown <= System.currentTimeMillis()) {
            CodeBlocks codeblock = CodeBlocks.getByMaterial(event.getItemInHand().getType());
            if (codeblock == null) return; // Player placed a block that is not a codeblock
            Hypersquare.cooldownMap.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + 150);
            Bukkit.getScheduler().runTaskLater(Hypersquare.instance, () -> {
                Block againstBlock = event.getBlockAgainst();
                Location location = event.getBlock().getLocation();
                Player player = event.getPlayer();

                int size = codeblock.hasBrackets ? 3 : 2;
                if (checkIfValidAgainst(againstBlock))
                    location = event.getBlockAgainst().getLocation().clone().add(0, 0, 1);

                // Invalid placements
                if (location.getBlockY() % 6 != 0 ||        // Codeblock on top of codeblock
                    location.getBlockX() % 3 != 0 ||        // Codelines not spaced 3 blocks apart
                    (location.getBlockZ() + 1) % 2 != 0     // Codeblocks not spaced 2 blocks apart
                ) {
                    HSException.sendError(player, "Invalid block placement.");
                    return;
                } else if (codeblock.isThreadStarter && location.getBlockZ() != 1) {
                    // Thread starter not at the very start
                    HSException.sendError(player, "Events, Functions, and Processes must be placed at the very start of the code line.");
                    return;
                }
                CodeData code = new CodeFile(event.getPlayer()).getCodeData();
                if (!codeblock.isThreadStarter) {
                    if (CodeFileHelper.getCodelineListIndex(location.clone(), code) == -1) {
                        HSException.sendError(player, "Your code must start with an Event, Function, or Process.");
                        return;
                    }
                }

                if (blockOutsidePlot(CodeBlockManagement.findCodeEnd(location.clone()).add(0, 0, size))) {
                    HSException.sendError(player, "Your code has reached the end of the plot.");
                    return;
                }

                event.setCancelled(false);
                CodeBlockManagement.moveCodeLine(location.clone(), size);
                placeBlock(event.getItemInHand(), location, codeblock);
            }, 2);
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(@NotNull BlockBreakEvent event) {

        if (!Hypersquare.mode.get(event.getPlayer()).equals("coding")) {
            if (Hypersquare.mode.get(event.getPlayer()).equals("building") || Hypersquare.mode.get(event.getPlayer()).equals("playing")) {
                if (!blockInPlot(event.getBlock().getLocation()))
                    event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);


        Location blockLoc = event.getBlock().getLocation();

        if (blockLoc.getY() < 0) {
            return;
        }

        if (event.getBlock().getType() == Material.STONE) return;

        Long cooldown = Hypersquare.cooldownMap.get(event.getPlayer().getUniqueId());
        if (cooldown == null) cooldown = 0L;
        if (cooldown <= System.currentTimeMillis()) {
            Hypersquare.cooldownMap.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + 150);

            if (blockLoc.getBlock().getType() == Material.OAK_WALL_SIGN) {
                blockLoc.add(1, 0, 0);
            }
            Block block = blockLoc.getBlock();
            Block signBlock = blockLoc.clone().add(-1, 0, 0).getBlock();
            CodeBlocks codeblock = CodeBlocks.getByMaterial(block.getType());
            if (codeblock == null) return; // Shouldn't ever happen

            if (codeblock.isThreadStarter) {
                PlotDatabase.removeEventByKey(PlotUtilities.getPlotId(event.getBlock().getWorld()), Utilities.LocationToString(block.getLocation()));

                // Purge the entire codeline
                Location endLoc = CodeBlockManagement.findCodeEnd(blockLoc.clone());
                BlockVector3 loc1 = BlockVector3.at(blockLoc.getBlockX() - 1, blockLoc.getBlockY(), blockLoc.getBlockZ());
                BlockVector3 loc2 = BlockVector3.at(endLoc.getBlockX(), endLoc.getBlockY() + 1, endLoc.getBlockZ());
                Region selection = new CuboidRegion(loc1, loc2);

                World faweWorld = BukkitAdapter.adapt(blockLoc.getWorld());
                try (EditSession editSession = WorldEdit.getInstance().newEditSession(faweWorld)) {
                    editSession.setBlocks(selection, BukkitAdapter.asBlockState(new ItemStack(Material.AIR)));
                } catch (WorldEditException e) {
                    Hypersquare.logger().warning("Failed to remove codeline");
                }
            }

            boolean breakAll = event.getPlayer().isSneaking();

            CodeFile code = new CodeFile(blockLoc.getWorld());
            CodeData codeJson = CodeFileHelper.removeCodeBlock(blockLoc.clone(), code, breakAll);
            code.setCode(codeJson.toJson().toString());

            if (signBlock.getType() == Material.OAK_WALL_SIGN) {
                Location barrelLoc = blockLoc.clone().add(0, 1, 0);
                Location stoneLoc = blockLoc.clone().add(0, 0, 1);

                Location bracketLoc = CodeBlockManagement.findCorrespondingBracket(blockLoc.clone());
                signBlock.setType(Material.AIR);
                block.setType(Material.AIR);
                if (stoneLoc.getBlock().getType() == Material.PISTON || stoneLoc.getBlock().getType() == Material.STICKY_PISTON) {
                    assert bracketLoc != null;
                    bracketLoc.getBlock().setType(Material.AIR);
                    if (breakAll) {
                        BlockVector3 loc1 = BlockVector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ());
                        BlockVector3 loc2 = BlockVector3.at(bracketLoc.getBlockX() - 1, bracketLoc.getBlockY() + 1, bracketLoc.getBlockZ());
                        Region selection = new CuboidRegion(loc1, loc2);

                        World faweWorld = BukkitAdapter.adapt(blockLoc.getWorld());
                        try (EditSession session = WorldEdit.getInstance().newEditSession(faweWorld)) {
                            session.setBlocks(selection, BukkitAdapter.asBlockType(Material.AIR));
                        } catch (WorldEditException e) {
                            Hypersquare.logger().warning("Failed to remove if contents");
                        }
                    }
                }
                stoneLoc.getBlock().setType(Material.AIR);
                barrelLoc.getBlock().setType(Material.AIR);

                if (bracketLoc != null) {
                    if (breakAll) {
                        CodeBlockManagement.moveCodeLine(bracketLoc.clone().add(0, 0, 1), blockLoc.getBlockZ() - bracketLoc.getBlockZ() - 1);
                    } else {
                        CodeBlockManagement.moveCodeLine(bracketLoc.clone().add(0, 0, 1), -2);
                        CodeBlockManagement.moveCodeLine(blockLoc.clone().add(0, 0, 2), -2);
                    }
                } else {
                    CodeBlockManagement.moveCodeLine(blockLoc.clone().add(0, 0, 2), -2);
                }
            }
        }
    }
}
