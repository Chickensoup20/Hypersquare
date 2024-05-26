package hypersquare.hypersquare.dev.value.impl;

import com.google.gson.JsonObject;
import hypersquare.hypersquare.dev.value.CodeValue;
import hypersquare.hypersquare.item.value.DisplayValue;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class ItemValue implements CodeValue<ItemStack, ItemStack> {

    @Override
    public Component getName() {
        return Component.text("Item").color(DisplayValue.ITEM.color);
    }

    @Override
    public Material getMaterial() {
        return Material.ITEM_FRAME;
    }

    @Override
    public String getTypeId() {
        return "item";
    }

    @Override
    public List<Component> getDescription() {
        return List.of();
    }

    @Override
    public List<Component> getHowToSet() {
        return List.of();
    }

    @Override
    public JsonObject getVarItemData(ItemStack item) {
        JsonObject obj = new JsonObject();
        CompoundTag nbt = CraftItemStack.asNMSCopy(item).save(new CompoundTag());
        byte[] bytes;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            NbtIo.writeCompressed(nbt, stream);
            bytes = stream.toByteArray();
        } catch (Exception err) {
            bytes = new byte[0];
        }
        obj.addProperty("nbt", new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8));
        return obj;
    }

    @Override
    public Component getValueName(ItemStack value) {
        return value.displayName();
    }

    @Override
    public ItemStack fromJson(JsonObject obj) {
        String b64 = obj.get("nbt").getAsString();
        try (ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(b64))) {
            if (stream.available() == 0) return new ItemStack(Material.AIR);
            CompoundTag nbt = NbtIo.readCompressed(stream, NbtAccounter.unlimitedHeap()); // TODO: find optimal max size
            var nmsItem = net.minecraft.world.item.ItemStack.of(nbt);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch (Exception ignored) {
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public ItemStack defaultValue() {
        return new ItemStack(Material.AIR);
    }

    @Override
    public ItemStack fromString(String data, ItemStack previous) {
        Material mat = Material.matchMaterial(data);
        if (mat == null && previous != null) return previous;
        if (mat == null) throw new RuntimeException("Not an item!");
        return new ItemStack(mat);
    }

    @Override
    public ItemStack fromItem(ItemStack item) {
        return item;
    }

    @Override
    public ItemStack realValue(ItemStack value) {
        return value;
    }

    @Override
    public JsonObject serialize(Object obj) {
        if (obj instanceof ItemStack item) return getVarItemData(item);
        return null;
    }

    @Override
    public ItemStack getItem(ItemStack value) {
        return value;
    }

}
