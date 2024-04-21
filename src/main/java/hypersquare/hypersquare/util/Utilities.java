package hypersquare.hypersquare.util;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.google.gson.JsonParser;
import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import hypersquare.hypersquare.HSKeys;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.plot.PlotDatabase;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hypersquare.hypersquare.Hypersquare.cleanMM;
import static hypersquare.hypersquare.Hypersquare.fullMM;

public class Utilities {

    private static final Pattern firstWordLetter = Pattern.compile("\\b\\w");

    public static int findLengthOfLongestString(List<String> strings) {
        if (strings == null || strings.isEmpty()) return 0; // Return 0 if the list is null or empty


        int maxLength = PlainTextComponentSerializer.plainText().serialize(cleanMM.deserialize(strings.getFirst())).length();  // Initialize maxLength to the length of the first string

        for (String s : strings) {
            if (PlainTextComponentSerializer.plainText().serialize(cleanMM.deserialize(s)).length() > maxLength) {
                maxLength = PlainTextComponentSerializer.plainText().serialize(cleanMM.deserialize(s)).length();
                // Update maxLength if a longer string is found
            }
        }

        return maxLength;
    }

    public static void sendMultiMiniMessage(CommandSender recipient, List<String> messages, boolean full) {
        int size = findLengthOfLongestString(messages);
        size = (int) (size * 1.5);
        if (size > 50) {
            size = 50;
        }
        String before = "<#AAAAFF><strikethrough>" + " ".repeat((int) (size * 1.5)) + "<#AAAAFF>";
        String message = String.join("<newline><reset>", messages);
        message = before + "<newline><reset>" + message + "<newline>" + before;
        recipient.sendMessage((full ? fullMM : cleanMM).deserialize(message));
    }

    public static void sendMultiMiniMessage(CommandSender recipient, List<String> messages) {
        {
            sendMultiMiniMessage(recipient, messages, false);
        }
    }

    public static ItemStack formatItem(@NotNull String lore, Material material, String name) {
        String[] parts = lore.split("%n");
        List<Component> list = new ArrayList<>(List.of());
        for (String part : parts) {
            list.add(cleanMM.deserialize(part));
        }

        return new ItemBuilder(material)
            .name(cleanMM.deserialize(name))
            .lore(list)
            .hideFlags()
            .build();
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static @NotNull String capitalizeAll(String text) {
        Matcher m = firstWordLetter.matcher(text);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(sb, m.group().toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static void sendInfo(@NotNull CommandSender sender, Component message, TextColor color) {
        sender.sendMessage(Component.text("»")
            .color(color)
            .decoration(TextDecoration.BOLD, true)
            .append(Component.text(" ")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.BOLD, false)
                .append(message)
            )
        );
    }

    public static void sendInfo(@NotNull CommandSender sender, Component message) {
        sendInfo(sender, message, NamedTextColor.GREEN);
    }

    public static void sendRedInfo(@NotNull CommandSender sender, Component message) {
        sendInfo(sender, message, NamedTextColor.RED);
    }

    public static void sendOpenMenuSound(@NotNull Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 2);
    }

    public static void sendClickMenuSound(@NotNull Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1f, 1.5f);
    }

    public static void sendSecondaryMenuSound(@NotNull Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
    }

    public static void sendSuccessClickMenuSound(@NotNull Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
    }

    public static boolean notWithinLocation(@NotNull Location targetLocation, @NotNull Location location1, Location location2) {
        return !targetLocation.getWorld().equals(location1.getWorld()) ||
            !targetLocation.getWorld().equals(location2.getWorld()) ||
            !(targetLocation.getX() >= Math.min(location1.getX(), location2.getX())) ||
            !(targetLocation.getX() <= Math.max(location1.getX(), location2.getX())) ||
            !(targetLocation.getY() >= Math.min(location1.getY(), location2.getY())) ||
            !(targetLocation.getY() <= Math.max(location1.getY(), location2.getY())) ||
            !(targetLocation.getZ() >= Math.min(location1.getZ(), location2.getZ())) ||
            !(targetLocation.getZ() <= Math.max(location1.getZ(), location2.getZ()));
    }

    public static boolean notWithinLocationIgnoreY(@NotNull Location targetLocation, @NotNull Location location1, Location location2) {
        return !targetLocation.getWorld().equals(location1.getWorld()) ||
            !targetLocation.getWorld().equals(location2.getWorld()) ||
            !(targetLocation.getX() >= Math.min(location1.getX(), location2.getX())) ||
            !(targetLocation.getX() <= Math.max(location1.getX(), location2.getX())) ||
            !(targetLocation.getZ() >= Math.min(location1.getZ(), location2.getZ())) ||
            !(targetLocation.getZ() <= Math.max(location1.getZ(), location2.getZ()));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static CompoundTag getChunkData(SlimeWorld slimeWorld) {
        if (slimeWorld == null) {
            return null;
        }

        CompoundMap extraData = slimeWorld.getExtraData().getValue();
        Optional<CompoundTag> optionalChunkData = extraData.getOrDefault("worldData", new CompoundTag("worldData", new CompoundMap())).getAsCompoundTag();
        return optionalChunkData.orElse(null);
    }

    public static void savePersistentData(@NotNull World world, @NotNull SlimePlugin plugin) {
        SlimeWorld slimeWorld = plugin.getWorld(world.getName());
        CompoundTag chunkData = getChunkData(slimeWorld);
        if (chunkData == null) return;
        CompoundMap compoundMap = NBTUtils.dataToCompoundMap(world.getPersistentDataContainer());
        chunkData.getValue().put("worldData", new CompoundTag("worldData", compoundMap));
        slimeWorld.getExtraData().getValue().put("worldData", chunkData);
    }

    public static void getWorldDataFromSlimeWorlds(@NotNull World world) {
        SlimeWorld slimeWorld = Hypersquare.slimePlugin.getWorld(world.getName());
        CompoundTag chunkData = getChunkData(slimeWorld);
        if (chunkData == null) return;
        String pType1 = JsonParser.parseString(NBTUtils.Converter.convertTag(chunkData.getValue().get("worldData")).getAsString()).getAsJsonObject().get("hypersquare:plottype").getAsString();
        world.getPersistentDataContainer().set(HSKeys.PLOT_TYPE, PersistentDataType.STRING, pType1);
    }

    public static @NotNull String randomHSVHex(float minHue, float maxHue, float saturation, float value) {
        Random random = new Random();

        // Generate a random hue within the specified range
        float hue = minHue + random.nextFloat() * (maxHue - minHue);

        // Create a Color object using the specified HSV values
        Color color = Color.fromRGB(java.awt.Color.getHSBColor(hue / 360, saturation, value).getRed(), java.awt.Color.getHSBColor(hue / 360, saturation, value).getGreen(), java.awt.Color.getHSBColor(hue / 360, saturation, value).getBlue());

        // Convert the Color object to hex text
        return colorToHex(color);
    }

    public static @NotNull String colorToHex(@NotNull Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        String redHex = Integer.toHexString(red);
        String greenHex = Integer.toHexString(green);
        String blueHex = Integer.toHexString(blue);
        redHex = padWithZeroes(redHex);
        greenHex = padWithZeroes(greenHex);
        blueHex = padWithZeroes(blueHex);
        return "#" + redHex + greenHex + blueHex;
    }

    @Contract(pure = true)
    public static @NotNull String padWithZeroes(@NotNull String input) {
        if (input.length() == 1) {
            return "0" + input;
        }
        return input;
    }

    public static @NotNull Boolean playerOnline(String player) {
        return Bukkit.getPlayerExact(player) != null;
    }

    public static void sendUsageError(@NotNull CommandSender sender, String usage) {
        sender.sendMessage(Component.text("Usage: ")
            .color(NamedTextColor.DARK_AQUA)
            .append(Component.text(usage).color(NamedTextColor.GRAY))
        );
    }

    public static void resetPlayerStats(@NotNull Player player, boolean clearInventory) {
        for (Attribute attribute : Attribute.values()) {
            AttributeInstance attributeInstance = player.getAttribute(attribute);
            if (attributeInstance != null) {
                double defaultValue = attributeInstance.getDefaultValue();
                attributeInstance.setBaseValue(defaultValue);
                Collection<AttributeModifier> modifiers = attributeInstance.getModifiers();
                for (AttributeModifier existingModifier : modifiers) {
                    attributeInstance.removeModifier(existingModifier);
                }
            }
        }
        player.setHealth(20);
        player.setHealthScale(20);
        player.setLevel(0);
        player.setExp(0);
        player.setFreezeTicks(0);
        player.setFireTicks(0);
        player.setNoDamageTicks(0);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.sendActionBar(Component.empty());
        player.sendTitle("", "", 0, 0, 0);
        player.setBeeStingersInBody(0);
        player.setArrowsInBody(0);
        player.setVisualFire(false);
        player.setViewDistance(10);
        player.setSimulationDistance(10);
        player.setPlayerWeather(WeatherType.CLEAR);
        player.setPlayerTime(1000, false);
        if (clearInventory) player.getInventory().clear();
        player.clearActivePotionEffects();
        player.closeInventory();
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setFlySpeed(0.1f);
        player.setWalkSpeed(0.2f);
        player.setFallDistance(0);
        player.setGameMode(GameMode.SURVIVAL);
    }

    public static void resetPlayerStats(Player player) {
        resetPlayerStats(player, true);
    }

    public static void setAction(@NotNull Block block, String id, Player player) {
        if (block.getType() == Material.OAK_WALL_SIGN) {
            Sign sign = (Sign) block.getState();
            sign.getSide(Side.FRONT).line(1, Component.text(id).color(Colors.WHITE));
            sign.update();
            int plotID = PlotUtilities.getPlotId(player.getWorld());
            HashMap<String, String> map = new HashMap<>();
            map.put(LocationToString(block.getLocation().add(1, 0, 0)), id);
            PlotDatabase.addEvents(plotID, map);
        }
        player.closeInventory();
    }

    public static @NotNull String LocationToString(@NotNull Location location) {
        return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }
}
