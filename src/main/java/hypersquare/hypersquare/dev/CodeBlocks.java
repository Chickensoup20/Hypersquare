package hypersquare.hypersquare.dev;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public enum CodeBlocks {
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, "PLAYER EVENT", "player_event", false, false, true),
    COBBLESTONE(Material.COBBLESTONE, "PLAYER ACTION", "player_action", true, false),
    OAK_PLANKS(Material.OAK_PLANKS, "IF PLAYER", "if_player", true, true),
    LAPIS_ORE(Material.LAPIS_ORE, "CALL FUNCTION", "call_function", true, false, false, false),
    EMERALD_ORE(Material.EMERALD_ORE, "CALL PROCESS", "call_process", true, false, false, false),
    GOLD_BLOCK(Material.GOLD_ORE, "ENTITY EVENT", "entity_event", false, false, true),
    MOSSY_COBBLESTONE(Material.MOSSY_COBBLESTONE, "ENTITY ACTION", "entity_action", true, false),
    BRICKS(Material.BRICKS, "IF ENTITY", "if_entity", true, true),
    LAPIS_BLOCK(Material.LAPIS_BLOCK, "FUNCTION", "function", true, false, true, false),
    EMERALD_BLOCK(Material.EMERALD_BLOCK, "PROCESS", "process", true, false, true, false),
    IRON_BLOCK(Material.IRON_BLOCK, "SET VARIABLE", "set_variable", true, false),
    OBSIDIAN(Material.OBSIDIAN, "IF VARIABLE", "if_variable", true, true),
    NETHERRACK(Material.NETHERRACK, "GAME ACTION", "game_action", true, false),
    RED_NETHER_BRICKS(Material.RED_NETHER_BRICKS, "IF GAME", "if_game", true, true),
    COAL_BLOCK(Material.COAL_BLOCK, "CONTROL", "control", true, false, false, true, "wait"),
    PURPUR_BLOCK(Material.PURPUR_BLOCK, "SELECT OBJECT", "select_object", true, false),
    PRISMARINE(Material.PRISMARINE, "REPEAT", "repeat", true, true),
    END_STONE(Material.END_STONE, "ELSE", "else", false, true, false, false),
    DIORITE(Material.DIORITE, "DEV", "dev", false, false, false, true),
    EMPTY(Material.AIR, "empty"),
    ;

    public final Material material;
    public final String name;
    public final String id;
    public final String defaultAction;
    public final boolean isThreadStarter;
    public final boolean hasBarrel;
    public final boolean hasBrackets;
    public final boolean hasActions;

    CodeBlocks(Material material, String id) {
        this(material, id, id, false, false, false, false, "");
    }

    CodeBlocks(Material material, String name, String id, boolean hasBarrel, boolean hasBrackets) {
        this(material, name, id, hasBarrel, hasBrackets, false, true, "");
    }

    CodeBlocks(Material material, String name, String id, boolean hasBarrel, boolean hasBrackets, boolean isThreadStarter) {
        this(material, name, id, hasBarrel, hasBrackets, isThreadStarter, true, "");
    }

    CodeBlocks(Material material, String name, String id, boolean hasBarrel, boolean hasBrackets, boolean isThreadStarter, boolean hasActions) {
        this(material, name, id, hasBarrel, hasBrackets, isThreadStarter, hasActions, "");
    }

    CodeBlocks(@NotNull Material material, @NotNull String name, @NotNull String id, boolean hasBarrel, boolean hasBrackets, boolean isThreadStarter, boolean hasActions, @NotNull String defaultAction) {
        this.material = material;
        this.name = name;
        this.id = id;
        this.hasBarrel = hasBarrel;
        this.hasBrackets = hasBrackets;
        this.isThreadStarter = isThreadStarter;
        this.hasActions = hasActions;
        this.defaultAction = defaultAction;
    }

    public static @Nullable CodeBlocks getByMaterial(Material material) {
        for (CodeBlocks codeBlock : CodeBlocks.values()) {
            if (codeBlock.material == material) return codeBlock;
        }
        return null;
    }

    public static @Nullable CodeBlocks getById(String id) {
        for (CodeBlocks codeBlock : CodeBlocks.values()) {
            if (Objects.equals(codeBlock.id, id)) return codeBlock;
        }
        return null;
    }

    public static @Nullable CodeBlocks getByName(String name) {
        for (CodeBlocks codeBlock : CodeBlocks.values()) {
            if (Objects.equals(codeBlock.name, name)) return codeBlock;
        }
        return null;
    }
}
