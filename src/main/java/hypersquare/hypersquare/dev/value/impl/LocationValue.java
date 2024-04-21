package hypersquare.hypersquare.dev.value.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.value.CodeValue;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.util.color.Colors;
import hypersquare.hypersquare.util.component.BasicComponent;
import hypersquare.hypersquare.util.string.ColorWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class LocationValue implements CodeValue<LocationValue.HSLocation, Location> {

    @Override
    public Component getName() {
        return Component.text("Location").color(DisplayValue.LOCATION.color);
    }

    @Override
    public Material getMaterial() {
        return Material.PAPER;
    }

    @Override
    public String getTypeId() {
        return "location";
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
            BasicComponent.gray("Represents a location,"),
            BasicComponent.gray("based on world coordinates")
        );
    }

    @Override
    public List<Component> getHowToSet() {
        return List.of(
            BasicComponent.gray("Right click this item to set it"),
            BasicComponent.gray("to your current location in build mode."),
            BasicComponent.gray("Left click a block to set the location"),
            BasicComponent.gray("of the block in build mode."),
            BasicComponent.gray("Shift left click to teleport to the set location."),
            BasicComponent.gray("Type in chat while holding this item to set the location."),
            Component.text("Example: ").color(Colors.WHITE).decoration(TextDecoration.ITALIC, false)
                .append(ColorWrapper.number("0 64 0"))
        );
    }

    @Override
    public ItemStack getItem(HSLocation value) {
        ItemStack item = CodeValue.super.getItem(value);
        ItemMeta meta = item.getItemMeta();

        List<Component> lore = List.of(
            BasicComponent.gray("X: ")
                .append(BasicComponent.create(value.x)),
            BasicComponent.gray("Y: ")
                .append(BasicComponent.create(value.y)),
            BasicComponent.gray("Z: ")
                .append(BasicComponent.create(value.z)),
            BasicComponent.gray("p: ")
                .append(BasicComponent.create(value.pitch)),
            BasicComponent.gray("y: ")
                .append(BasicComponent.create((value.yaw)))
        );

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public JsonObject getVarItemData(HSLocation type) {
        JsonObject loc = new JsonObject();
        loc.addProperty("x", String.valueOf(type.x));
        loc.addProperty("y", String.valueOf(type.y));
        loc.addProperty("z", String.valueOf(type.z));
        loc.addProperty("pitch", String.valueOf(type.pitch));
        loc.addProperty("yaw", String.valueOf(type.yaw));
        return loc;
    }

    @Override
    public HSLocation fromJson(JsonObject loc) {
        return new HSLocation(
            new DecimalNumber(loc.get("x").getAsDouble()),
            new DecimalNumber(loc.get("y").getAsDouble()),
            new DecimalNumber(loc.get("z").getAsDouble()),
            new DecimalNumber(loc.get("pitch").getAsFloat()),
            new DecimalNumber(loc.get("yaw").getAsFloat())
        );
    }

    @Override
    public HSLocation defaultValue() {
        return new HSLocation(new DecimalNumber(0), new DecimalNumber(0), new DecimalNumber(0), new DecimalNumber(0), new DecimalNumber(0));
    }

    @Override
    public HSLocation fromString(String data, HSLocation previous) {
        String[] dataArray = data.split("\\s+");
        if (dataArray.length < 3 || dataArray.length > 5) return previous;

        long x = Long.parseLong(dataArray[0]);
        long y = Long.parseLong(dataArray[1]);
        long z = Long.parseLong(dataArray[2]);
        DecimalNumber pitch;
        DecimalNumber yaw;
        if (previous != null) {
            pitch = previous.pitch;
            yaw = previous.yaw;
        } else {
            pitch = new DecimalNumber(0);
            yaw = new DecimalNumber(0);
        }

        if (dataArray.length >= 4) pitch = new DecimalNumber(Long.parseLong(dataArray[3]));
        if (dataArray.length == 5) yaw = new DecimalNumber(Long.parseLong(dataArray[4]));

        return new HSLocation(new DecimalNumber(x), new DecimalNumber(y), new DecimalNumber(z), pitch, yaw);
    }

    @Override
    public HSLocation fromItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey varitem = new NamespacedKey(Hypersquare.instance, "varitem");
        if (!meta.getPersistentDataContainer().has(varitem)) return null;
        JsonObject data = JsonParser.parseString(meta.getPersistentDataContainer().get(varitem, PersistentDataType.STRING)).getAsJsonObject();
        return fromJson(data);
    }

    @Override
    public Location realValue(HSLocation value) {
        return new Location(null, value.x.toDouble(), value.y.toDouble(), value.z.toDouble(), value.pitch.toFloat(), value.yaw.toFloat());
    }

    @Override
    public JsonObject serialize(Object obj) {
        if (obj instanceof HSLocation hsLocation) return getVarItemData(hsLocation);
        if (obj instanceof Location location)
            return getVarItemData(new HSLocation(new DecimalNumber(location.getX()), new DecimalNumber(location.getY()), new DecimalNumber(location.getZ()), new DecimalNumber(location.getPitch()), new DecimalNumber(location.getYaw())));
        return null;
    }

    public enum HSDirection {
        NORTH(Direction.NORTH), EAST(Direction.EAST), SOUTH(Direction.SOUTH), WEST(Direction.WEST);

        public final Direction mc;

        HSDirection(Direction mc) {
            this.mc = mc;
        }
    }

    public enum HSAxis {
        X, Y, Z, PITCH, YAW;

        HSAxis() {
        }

        public HSLocation set(HSLocation loc, DecimalNumber val) {
            return switch (this) {
                case X -> new HSLocation(val, loc.y, loc.z, loc.pitch, loc.yaw);
                case Y -> new HSLocation(loc.x, val, loc.z, loc.pitch, loc.yaw);
                case Z -> new HSLocation(loc.x, loc.y, val, loc.pitch, loc.yaw);
                case PITCH -> new HSLocation(loc.x, loc.y, loc.z, val, loc.yaw);
                case YAW -> new HSLocation(loc.x, loc.y, loc.z, loc.pitch, val);
            };
        }
    }

    public record HSLocation(DecimalNumber x, DecimalNumber y, DecimalNumber z, DecimalNumber pitch,
                             DecimalNumber yaw) {
        public HSLocation shift(HSDirection d, double amount) {
            Vec3i dirV = d.mc.getNormal();
            return new HSLocation(
                x.add(new DecimalNumber(amount * (double) dirV.getX())),
                y.add(new DecimalNumber(amount * (double) dirV.getY())),
                z.add(new DecimalNumber(amount * (double) dirV.getZ())),
                pitch, yaw
            );
        }
    }
}
