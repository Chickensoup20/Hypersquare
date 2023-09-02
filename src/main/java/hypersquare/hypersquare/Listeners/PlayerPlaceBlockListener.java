package hypersquare.hypersquare.Listeners;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hypersquare.hypersquare.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.sign.Side;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Locale;

public class PlayerPlaceBlockListener implements Listener {
    Plugin plugin = Hypersquare.getPlugin(Hypersquare.class);
    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        if (Hypersquare.mode.get(event.getPlayer()).equals("coding")) {
            event.setCancelled(true);
            if (event.getItemInHand().getType() == Material.ENDER_CHEST) {
                String data = LoadCodeTemplate.load(event.getItemInHand());
                JsonArray blocksArray = JsonParser.parseString(Utilities.decode(data)).getAsJsonObject().getAsJsonArray("blocks");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int move = 0;
                        Location location = event.getBlock().getLocation();
                        Location loc = location;
                        for (JsonElement blockElement : blocksArray) {
                            JsonObject blockObject = blockElement.getAsJsonObject();
                            if (blockObject.has("direct")){
                                String bracket = blockObject.get("direct").getAsString();
                                if (bracket.equals("close"))
                                    move++;
                                CodeBlockManagement.placeCodeBlock(loc.add(0,0,move),"bracket",bracket,"");
                                loc = event.getBlock().getLocation();
                                move++;
                            }
                            if (blockObject.has("block")) {
                                String id = blockObject.get("block").getAsString();
                                ItemStack item = ItemManager.getItem("dev." + id);
                                if (!item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin,"brackets"),PersistentDataType.STRING).equals("true")) {

                                    JsonElement action = blockObject.get("data");
                                    if (action == null)
                                        action = blockObject.get("action");


                                    CodeBlockManagement.placeCodeBlock(loc.add(0, 0, move), id, "stone",action.getAsString());
                                    loc = event.getBlock().getLocation();;
                                    move+=2;

                                } else {
                                    CodeBlockManagement.placeCodeBlock(loc.add(0, 0, move), id, "none",blockObject.get("action").getAsString());
                                    loc = event.getBlock().getLocation();;
                                    move++;
                                }


                            }
                        }
                    }
                }.runTaskLater(plugin,1);



            }
            if (ItemManager.getItemID(event.getItemInHand()).startsWith("dev.")) {
                Location location = event.getBlock().getLocation();

                if (event.getBlockAgainst().getType() == Material.STONE || event.getBlockAgainst().getType() == Material.PISTON){
                    CodeBlockManagement.moveCodeLine(event.getBlockAgainst().getLocation().add(0,0,1), 2);
                    Vector against = event.getBlockAgainst().getLocation().toVector();
                    Vector original = event.getBlock().getLocation().toVector();
                    Vector difference = against.subtract(original);
                    location.add(difference);
                    location.add(0,0,1);
                } else {
                    CodeBlockManagement.moveCodeLine(event.getBlock().getLocation().add(0,0,0), 1);

                }


                Location signLocation = location.clone().add(-1,0,0);

                String signText = ChatColor.stripColor(event.getItemInHand().getItemMeta().getDisplayName().toUpperCase());
                signLocation.getBlock().setType(Material.OAK_WALL_SIGN);
                BlockData blockData = signLocation.getBlock().getBlockData();
                ((Directional) blockData).setFacing(BlockFace.WEST);
                Sign sign = (Sign) signLocation.getBlock().getState();
                sign.setEditable(true);
                sign.getSide(Side.FRONT).setLine(0, signText);
                sign.update();
                signLocation.getBlock().setBlockData(blockData);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location codeblockLocation = location.clone();
                        codeblockLocation.getBlock().setType(event.getItemInHand().getType());

                        Location stoneLocation = location.clone().add(0, 0, 1);
                        Location closeBracketLocation = location.clone().add(0, 0, 3);
                        Location chestLocation = location.add(0, 1, 0);
                        Location openBracketLocation = location.clone().add(0, -1, 1);


                        if (event.getItemInHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "brackets"), PersistentDataType.STRING).equals("true")) {

                            //Open Bracket
                            openBracketLocation.getBlock().setType(Material.PISTON);
                            BlockData pistonData = openBracketLocation.getBlock().getBlockData();
                            ((Directional) pistonData).setFacing(BlockFace.SOUTH);
                            openBracketLocation.getBlock().setBlockData(pistonData);

                            //Close bracket
                            closeBracketLocation.getBlock().setType(Material.PISTON);
                            pistonData = closeBracketLocation.getBlock().getBlockData();
                            ((Directional) pistonData).setFacing(BlockFace.NORTH);
                            closeBracketLocation.getBlock().setBlockData(pistonData);


                        } else
                            //Stone Bracket

                            stoneLocation.getBlock().setType(Material.STONE);
                        if (event.getItemInHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "chest"), PersistentDataType.STRING).equals("true")) {
                            chestLocation.getBlock().setType(Material.CHEST);
                            BlockData blockData = chestLocation.getBlock().getBlockData();
                            ((Directional) blockData).setFacing(BlockFace.WEST);
                            chestLocation.getBlock().setBlockData(blockData);
                        }
                    }
                }.runTaskLater(plugin, 1);


            }

        }
    }

}
