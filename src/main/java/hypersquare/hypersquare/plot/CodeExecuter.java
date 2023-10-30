package hypersquare.hypersquare.plot;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;

public class CodeExecuter {
    public static void  runThroughCode(Location startLoc, Entity defaultTarget){
        startLoc.add(0,0,1);
        while (startLoc.clone().add(0,0,1).getBlock().getType() != Material.AIR || startLoc.clone().add(0,0,2).getBlock().getType() != Material.AIR || startLoc.clone().getBlock().getType() != Material.AIR){

                Location signLocation = startLoc.clone().add(-1,0,0);
                if (signLocation.getBlock().getType() == Material.OAK_WALL_SIGN){
                    Sign sign = (Sign) signLocation.getBlock().getState();
                    String[] signInfo = sign.getSide(Side.FRONT).getLines();
                    if (signInfo.length > 0) {

                        List<Entity> targets = new ArrayList<>(List.of());

                        switch (signInfo[3]){
                            case "Selection":{
                                defaultTarget.sendMessage("mf!");
                                break;
                            }
                            case "Default":{
                                targets.add(defaultTarget);
                                break;
                            }
                            case "AllPlayers":{
                                for (Entity entity : startLoc.getWorld().getEntities()){
                                    if (entity instanceof Player){
                                        Player target = (Player) entity;
                                        if (Hypersquare.mode.get(target).equals("playing")){
                                            targets.add(entity);
                                            break;
                                        }
                                    }
                                }
                            }
                            default: {
                                targets.add(defaultTarget);
                            }
                        }
                        Chest chest = (Chest) startLoc.clone().add(0,1,0).getBlock().getState();
                        target.sendMessage(signInfo[1]);
                    }
                }
                startLoc.add(0,0,1);
            }
    }
}
