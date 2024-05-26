package hypersquare.hypersquare.util;

import com.flowpowered.nbt.CompoundMap;
import net.minecraft.nbt.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_20_R3.persistence.CraftPersistentDataContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static hypersquare.hypersquare.util.NBTUtils.Converter.convertMap;

public class NBTUtils {

    public static CompoundMap dataToCompoundMap(PersistentDataContainer container) {
        Map<String, net.minecraft.nbt.Tag> serialized = ((CraftPersistentDataContainer) container).getRaw();

        return convertMap(serialized);
    }

    public static class Converter {

        private static final Logger LOGGER = LogManager.getLogger("SB NBT Converter");

        public static @NotNull Tag convertTag(com.flowpowered.nbt.Tag<?> tag) {
            try {
                return switch (tag.getType()) {
                    case TAG_BYTE -> ByteTag.valueOf(((com.flowpowered.nbt.ByteTag) tag).getValue());
                    case TAG_SHORT -> ShortTag.valueOf(((com.flowpowered.nbt.ShortTag) tag).getValue());
                    case TAG_INT -> IntTag.valueOf(((com.flowpowered.nbt.IntTag) tag).getValue());
                    case TAG_LONG -> LongTag.valueOf(((com.flowpowered.nbt.LongTag) tag).getValue());
                    case TAG_FLOAT -> FloatTag.valueOf(((com.flowpowered.nbt.FloatTag) tag).getValue());
                    case TAG_DOUBLE -> DoubleTag.valueOf(((com.flowpowered.nbt.DoubleTag) tag).getValue());
                    case TAG_BYTE_ARRAY -> new ByteArrayTag(((com.flowpowered.nbt.ByteArrayTag) tag).getValue());
                    case TAG_STRING -> StringTag.valueOf(((com.flowpowered.nbt.StringTag) tag).getValue());
                    case TAG_LIST -> {
                        net.minecraft.nbt.ListTag list = new net.minecraft.nbt.ListTag();
                        ((com.flowpowered.nbt.ListTag<?>) tag).getValue().stream().map(Converter::convertTag).forEach(list::add);
                        yield list;
                    }
                    case TAG_COMPOUND -> {
                        CompoundTag compound = new CompoundTag();
                        ((com.flowpowered.nbt.CompoundTag) tag).getValue().forEach((key, value) -> compound.put(key, convertTag(value)));
                        yield compound;
                    }
                    case TAG_INT_ARRAY -> new IntArrayTag(((com.flowpowered.nbt.IntArrayTag) tag).getValue());
                    case TAG_LONG_ARRAY -> new LongArrayTag(((com.flowpowered.nbt.LongArrayTag) tag).getValue());
                    default -> throw new IllegalArgumentException("Invalid tag type " + tag.getType().name());
                };
            } catch (Exception var3) {
                LOGGER.error("Failed to convert NBT object:");
                LOGGER.error(tag.toString());
                throw var3;
            }
        }

        static com.flowpowered.nbt.Tag<?> convertTag(String name, Tag base) {
            switch (base.getId()) {
                case Tag.TAG_BYTE:
                    return new com.flowpowered.nbt.ByteTag(name, ((ByteTag) base).getAsByte());
                case Tag.TAG_SHORT:
                    return new com.flowpowered.nbt.ShortTag(name, ((ShortTag) base).getAsShort());
                case Tag.TAG_INT:
                    return new com.flowpowered.nbt.IntTag(name, ((IntTag) base).getAsInt());
                case Tag.TAG_LONG:
                    return new com.flowpowered.nbt.LongTag(name, ((LongTag) base).getAsLong());
                case Tag.TAG_FLOAT:
                    return new com.flowpowered.nbt.FloatTag(name, ((FloatTag) base).getAsFloat());
                case Tag.TAG_DOUBLE:
                    return new com.flowpowered.nbt.DoubleTag(name, ((DoubleTag) base).getAsDouble());
                case Tag.TAG_BYTE_ARRAY:
                    return new com.flowpowered.nbt.ByteArrayTag(name, ((ByteArrayTag) base).getAsByteArray());
                case Tag.TAG_STRING:
                    return new com.flowpowered.nbt.StringTag(name, base.getAsString());
                case Tag.TAG_LIST:
                    List<com.flowpowered.nbt.Tag<?>> list = new ArrayList<>();
                    ListTag originalList = ((ListTag) base);

                    for (Tag entry : originalList) {
                        list.add(convertTag("", entry));
                    }

                    return new com.flowpowered.nbt.ListTag<>(name, com.flowpowered.nbt.TagType.getById(originalList.getElementType()), list);
                case Tag.TAG_COMPOUND:
                    CompoundTag originalCompound = ((CompoundTag) base);
                    com.flowpowered.nbt.CompoundTag compound = new com.flowpowered.nbt.CompoundTag(name, new CompoundMap());

                    for (String key : originalCompound.getAllKeys()) {
                        compound.getValue().put(key, convertTag(key, Objects.requireNonNull(originalCompound.get(key))));
                    }

                    return compound;
                case Tag.TAG_INT_ARRAY:
                    return new com.flowpowered.nbt.IntArrayTag(name, ((IntArrayTag) base).getAsIntArray());
                case Tag.TAG_LONG_ARRAY:
                    return new com.flowpowered.nbt.LongArrayTag(name, ((LongArrayTag) base).getAsLongArray());
                default:
                    throw new IllegalArgumentException("Invalid tag type " + base.getId());
            }
        }

        public static CompoundMap convertMap(Map<String, Tag> nbtMap) {
            CompoundMap data = new CompoundMap();

            nbtMap.forEach((key, obj) -> data.put(key, Converter.convertTag(key, obj)));

            return data;
        }
    }
}
