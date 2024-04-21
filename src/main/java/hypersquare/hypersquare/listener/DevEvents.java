package hypersquare.hypersquare.listener;

import com.google.gson.JsonObject;
import hypersquare.hypersquare.HSKeys;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.Actions;
import hypersquare.hypersquare.dev.CodeBlocks;
import hypersquare.hypersquare.dev.CodeItems;
import hypersquare.hypersquare.dev.Events;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.CodeFile;
import hypersquare.hypersquare.dev.codefile.CodeFileHelper;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.codefile.data.CodeData;
import hypersquare.hypersquare.dev.codefile.data.CodeLineData;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.dev.target.TargetType;
import hypersquare.hypersquare.dev.value.CodeValues;
import hypersquare.hypersquare.dev.value.impl.*;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.menu.ActionTargetsMenu;
import hypersquare.hypersquare.menu.CodeblockMenu;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.menu.barrel.MenuParameter;
import hypersquare.hypersquare.play.error.HSException;
import hypersquare.hypersquare.plot.ChangeGameMode;
import hypersquare.hypersquare.plot.MoveEntities;
import hypersquare.hypersquare.util.PlotUtilities;
import hypersquare.hypersquare.util.Utilities;
import hypersquare.hypersquare.util.ValueDisplay;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class DevEvents implements Listener {

    public static Location basic = null;
    public static Location large = null;
    public static Location huge = null;
    public static Location massive = null;
    public static Location gigantic = null;
    public static Location commonStart = null;

    private static void handleCodeblockRClick(@NotNull Block clickedBlock, @NotNull PlayerInteractEvent event) {
        event.setCancelled(true);
        Location codeblockLocation = clickedBlock.getType() == Material.OAK_WALL_SIGN ?
            clickedBlock.getLocation().clone().add(1, 0, 0) : clickedBlock.getLocation();
        CodeFile codeFile = new CodeFile(event.getPlayer());
        CodeActionData actionData = CodeFileHelper.getActionAt(codeblockLocation, codeFile.getCodeData());
        CodeLineData line = codeFile.getCodeData().codelines.get(CodeFileHelper.getCodelineListIndex(codeblockLocation, codeFile.getCodeData()));
        String id = actionData == null ? line.type : actionData.codeblock;
        Event hsEvent = Events.getEvent(line.event);

        // TODO: if (line.type.equals("func") || line.type.equals("proc")) { find all sources of the func/proc being called }

        if (event.getPlayer().isSneaking() && actionData != null && hsEvent != null) {
            ArrayList<Target> targets = new ArrayList<>(Arrays.stream(hsEvent.compatibleTargets()).filter(t -> {
                TargetType acceptedType = TargetType.ofCodeblock(actionData.codeblock);
                if (acceptedType == null) return false;
                return acceptedType == t.targetType;
            }).toList());
            ActionTargetsMenu.open(event.getPlayer(), targets, clickedBlock.getLocation());
        } else CodeblockMenu.open(id, event.getPlayer(), clickedBlock.getLocation());
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1.75f);
    }

    private static void handleBarrelRClick(@NotNull Block clickedBlock, @NotNull PlayerInteractEvent event) {
        event.setCancelled(true);
        CodeData codeData = new CodeFile(event.getPlayer()).getCodeData();
        Location blockLoc = clickedBlock.getLocation().subtract(0, 1, 0);
        CodeLineData lineData = CodeFileHelper.getLineAt(blockLoc, codeData);
        if (lineData == null) {
            HSException.sendError(event.getPlayer(), "Couldn't find this line in the plot (corrupted plot?)");
            return;
        }
        CodeBlocks lineCodeblock = CodeBlocks.getById(lineData.type);
        if (lineCodeblock == null) {
            HSException.sendError(event.getPlayer(), "Couldn't find this codeblock in the plot (corrupted plot?)");
            return;
        }
        CodeActionData actionData = CodeFileHelper.getActionAt(blockLoc, lineData);
        if (actionData == null && lineCodeblock.hasBarrel) {
            lineData.menu().open(event.getPlayer(), blockLoc);
        } else if (actionData == null) {
            HSException.sendError(event.getPlayer(), "Couldn't find this action in the plot (corrupted plot?)");
            return;
        } else {
            Action action = Actions.getAction(actionData.action, actionData.codeblock);
            if (action == null) {
                HSException.sendError(event.getPlayer(), "Couldn't find this action in the registry (outdated plot?)");
                throw new NullPointerException("Bad CodeFile! (Invalid action ID: " + actionData.action + ")");
            }
            action.actionMenu(actionData).open(event.getPlayer(), blockLoc);
        }
        event.getPlayer().playSound(clickedBlock.getLocation(), Sound.BLOCK_BARREL_OPEN, 0.75f, 1);
    }

    private static void setLocation(Player player, Location newLocation) {
        if (Hypersquare.mode.get(player).equals("building")) {
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                if (CodeValues.LOCATION.fromItem(player.getInventory().getItemInMainHand()) != null) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    LocationValue.HSLocation locationValue = new LocationValue.HSLocation(new DecimalNumber(newLocation.getX()), new DecimalNumber(newLocation.getY()), new DecimalNumber(newLocation.getZ()), new DecimalNumber(newLocation.getPitch()), new DecimalNumber(newLocation.getYaw()));
                    ItemStack item = CodeValues.LOCATION.getItem(locationValue);
                    player.getInventory().setItemInMainHand(item);
                }
            }
        }
    }

    public static void commonVars() {
        basic = MoveEntities.basic;
        large = MoveEntities.large;
        huge = MoveEntities.huge;
        massive = MoveEntities.massive;
        gigantic = MoveEntities.gigantic;
        commonStart = MoveEntities.commonStart;
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            ValueDisplay.showLocationDisplay(event.getPlayer(), event.getPlayer().getLocation());
            setLocation(event.getPlayer(), event.getPlayer().getLocation());
            return;
        }
        switch (event.getAction()) {
            case RIGHT_CLICK_BLOCK -> {
                if (event.getHand() != EquipmentSlot.HAND || clickedBlock.getLocation().getX() > -0) return;
                Material clickedMaterial = clickedBlock.getType();
                if (clickedMaterial == Material.AIR) return;
                if (clickedMaterial == Material.OAK_WALL_SIGN || CodeBlocks.getByMaterial(clickedMaterial) != null)
                    handleCodeblockRClick(clickedBlock, event);
                else if (clickedBlock.getType() == Material.BARREL) handleBarrelRClick(clickedBlock, event);
            }
            case RIGHT_CLICK_AIR -> {
                if (event.getHand() != EquipmentSlot.HAND) return;
                ItemStack item = event.getItem();
                if (item == null) return;
                JsonObject varItem = CodeValues.getVarItemData(item);
                CodeValues v = CodeValues.getType(varItem);
                if (v == null) return;
                v.onRightClick(event.getPlayer(), v.fromJson(varItem));
            }
            case LEFT_CLICK_BLOCK -> {
                Player player = event.getPlayer();
                if (Hypersquare.mode.get(player).equals("building")) {
                    if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                        if (CodeValues.LOCATION.fromItem(player.getInventory().getItemInMainHand()) != null) {
                            event.setCancelled(true);
                            ValueDisplay.showLocationDisplay(event.getPlayer(), event.getClickedBlock().getLocation().add(0.5, 0.5, 0.5));
                            setLocation(player, event.getClickedBlock().getLocation().add(0.5, 0.5, 0.5));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChangeSlot(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (Hypersquare.mode.get(player).equals("building")) {
            if (Hypersquare.locationValueDisplays.containsKey(event.getPlayer())) {
                ValueDisplay.removeValueDisplay(Hypersquare.locationValueDisplays.get(event.getPlayer()));
                Hypersquare.locationValueDisplays.remove(event.getPlayer());
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack newItem = player.getInventory().getItemInMainHand();
                    if (newItem != null) {
                        if (newItem.hasItemMeta()) {
                            if (CodeValues.LOCATION.fromItem(newItem) != null) {
                                Location location = (Location) CodeValues.LOCATION.realValue(CodeValues.LOCATION.fromItem(newItem));
                                location.setWorld(player.getWorld());
                                ValueDisplay.showLocationDisplay(event.getPlayer(), location);
                            }
                        }
                    }
                }

            }.runTaskLater(Hypersquare.instance, 3);

        }
    }

    @EventHandler
    public void onBreakBlock(@NotNull BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location brokenLocation = event.getBlock().getLocation();
        Material brokenMaterial = event.getBlock().getType();
        if (brokenMaterial == Material.BARREL) {
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            if (mainHandItem.getType() == Material.AIR) return;
            CodeFile file = new CodeFile(player);
            CodeData data = file.getCodeData();
            CodeActionData actionData = CodeFileHelper.getActionAt(brokenLocation.subtract(0, 1, 0), data);
            if (actionData == null) return;
            Action action = Actions.getByData(actionData);
            if (action == null) return;
            BarrelMenu menu = action.actionMenu(actionData);
            for (int i = 0; i < menu.getSize(); i++) {
                if (!menu.items.containsKey(i)) continue;
                if (menu.items.get(i) instanceof MenuParameter param) {
                    if (!param.isEmpty(actionData)) continue;
                    if (param.notValid(mainHandItem)) continue;
                    int oldAmount = mainHandItem.getAmount();
                    // We only want to insert 1 item (as well as remove 1 item) at a time
                    mainHandItem.setAmount(1);
                    param.replaceValue(actionData, mainHandItem);
                    file.setCode(data.toJson().toString());

                    mainHandItem.setAmount(oldAmount - 1);
                    player.getInventory().setItemInMainHand(mainHandItem);
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onExplode(@NotNull EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(@NotNull EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (!Hypersquare.mode.get(player).equals("playing")) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(@NotNull BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onRightClick(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Hypersquare.mode.get(player).equals("coding") && !Hypersquare.mode.get(player).equals("building")) return;
        ItemStack eventItem = event.getItem();
        if (eventItem == null || eventItem.getType() == Material.AIR) return;

        if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK || event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR) {
            if (eventItem.isSimilar(CodeItems.BLOCKS_SHORTCUT)) {
                event.setCancelled(true);

                Utilities.sendOpenMenuSound(player);
                Inventory inv = Bukkit.createInventory(null, 27, Component.text("Code Blocks"));
                //First row
                inv.setItem(0, CodeItems.PLAYER_EVENT_ITEM);
                inv.setItem(1, CodeItems.IF_PLAYER_ITEM);
                inv.setItem(2, CodeItems.PLAYER_ACTION_ITEM);
                inv.setItem(3, CodeItems.CALL_FUNCTION_ITEM);
                inv.setItem(4, CodeItems.START_PROCESS_ITEM);
                inv.setItem(5, CodeItems.ENTITY_EVENT_ITEM);
                inv.setItem(6, CodeItems.IF_ENTITY_ITEM);
                //Second row
                inv.setItem(9, CodeItems.CONTROL_ITEM);
                inv.setItem(10, CodeItems.SELECT_OBJECT_ITEM);
                inv.setItem(11, CodeItems.REPEAT_ITEM);
                inv.setItem(12, CodeItems.ELSE_ITEM);
                inv.setItem(18, CodeItems.SET_VARIABLE_ITEM);
                //Third row
                inv.setItem(19, CodeItems.IF_VARIABLE_ITEM);
                inv.setItem(20, CodeItems.GAME_ACTION_ITEM);
                inv.setItem(21, CodeItems.IF_GAME_ITEM);
                inv.setItem(22, CodeItems.ENTITY_ACTION_ITEM);
                inv.setItem(23, CodeItems.FUNCTION_ITEM);
                inv.setItem(24, CodeItems.PROCESS_ITEM);
                inv.setItem(26, CodeItems.DEV_ITEM);
                player.openInventory(inv);
            }
            if (eventItem.isSimilar(CodeItems.VALUES_INGOT)) {
                event.setCancelled(true);

                Utilities.sendOpenMenuSound(player);
                Inventory inv = Bukkit.createInventory(null, 18, Component.text("Code Values"));

                inv.setItem(0, new StringValue().emptyValue());
                inv.setItem(1, new TextValue().emptyValue());
                inv.setItem(2, new NumberValue().emptyValue());
                inv.setItem(3, new VariableValue().emptyValue());
                inv.setItem(4, new LocationValue().emptyValue());

                inv.setItem(9, new NullValue().emptyValue());

                player.openInventory(inv);
            }
        }
    }

    @EventHandler
    public void onSpread(@NotNull BlockFromToEvent event) {
        commonVars();
        String plotType = event.getToBlock().getWorld().getPersistentDataContainer().get(HSKeys.PLOT_TYPE, PersistentDataType.STRING);
        if (plotType == null)
            return;
        switch (plotType) {

            case "Basic": {
                if (Utilities.notWithinLocation(event.getToBlock().getLocation(), commonStart, basic)) {
                    event.setCancelled(true);
                }
                break;
            }
            case "Large": {
                if (Utilities.notWithinLocation(event.getToBlock().getLocation(), commonStart, large)) {
                    event.setCancelled(true);

                }
                break;
            }
            case "Huge": {
                if (Utilities.notWithinLocation(event.getToBlock().getLocation(), commonStart, huge)) {
                    event.setCancelled(true);
                }
                break;
            }
            case "Massive": {
                if (Utilities.notWithinLocation(event.getToBlock().getLocation(), commonStart, massive)) {
                    event.setCancelled(true);
                }
                break;
            }
            case "Gigantic": {
                if (Utilities.notWithinLocation(event.getToBlock().getLocation(), commonStart, gigantic)) {
                    event.setCancelled(true);
                }
                break;
            }
        }
    }

    @EventHandler
    public void onBlockPistonExtend(@NotNull BlockPistonExtendEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPistonRetract(@NotNull BlockPistonRetractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void creatureSpawnEvent(@NotNull CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM && event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.COMMAND)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(@NotNull PlayerDropItemEvent event) {
        if (!Hypersquare.mode.get(event.getPlayer()).equals("coding")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onChat(@NotNull AsyncChatEvent event) {
        if (!Hypersquare.mode.get(event.getPlayer()).equals("coding")) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return;

        int originalAmount = item.getAmount();

        JsonObject json = CodeValues.getVarItemData(item);
        if (json == null) return;

        CodeValues value = CodeValues.getType(json);
        if (value == null) return;
        if (value.isUnsetable()) return;
        event.setCancelled(true);
        String raw = PlainTextComponentSerializer.plainText().serialize(event.message());
        Object v;
        try {
            v = value.fromString(raw, value.fromItem(item));
        } catch (Exception ignored) {
            HSException.sendError(event.getPlayer(), "Invalid input: '" + raw + "'");
            return;
        }
        ItemStack newItem = value.getItem(v);
        newItem.setAmount(originalAmount);
        event.getPlayer().getInventory().setItemInMainHand(newItem);
    }

    @EventHandler
    public void onSwapHands(@NotNull PlayerSwapHandItemsEvent event) {
        String playerMode = Hypersquare.mode.get(event.getPlayer());
        if (!(playerMode.equals("coding") || playerMode.equals("building"))) return;

        Player player = event.getPlayer();
        if (Hypersquare.lastSwapHands.containsKey(player)) {

            // Swapping hands twice in 1 second
            if (System.currentTimeMillis() - Hypersquare.lastSwapHands.get(player) < 950
                && System.currentTimeMillis() - Hypersquare.cooldownMap.get(player.getUniqueId()) > 1000) {
                int plotID = PlotUtilities.getPlotId(player);
                if (playerMode.equals("coding")) {
                    Hypersquare.lastDevLocation.put(player, player.getLocation());
                    ChangeGameMode.buildMode(player, plotID, true);
                } else {
                    Hypersquare.lastBuildLocation.put(player, player.getLocation());
                    ChangeGameMode.devMode(player, plotID, true);
                }
                Hypersquare.lastSwapHands.remove(player);
                Hypersquare.cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
                return;
            }
        }
        // Update last swap hands time
        Hypersquare.lastSwapHands.put(player, System.currentTimeMillis());
    }

}
