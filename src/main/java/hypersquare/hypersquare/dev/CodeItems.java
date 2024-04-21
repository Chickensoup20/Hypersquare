package hypersquare.hypersquare.dev;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CodeItems {
    public static ItemStack PLAYER_EVENT_ITEM;
    public static ItemStack IF_PLAYER_ITEM;
    public static ItemStack PLAYER_ACTION_ITEM;
    public static ItemStack CALL_FUNCTION_ITEM;
    public static ItemStack START_PROCESS_ITEM;
    public static ItemStack ENTITY_EVENT_ITEM;
    public static ItemStack IF_ENTITY_ITEM;
    public static ItemStack ENTITY_ACTION_ITEM;
    public static ItemStack FUNCTION_ITEM;
    public static ItemStack PROCESS_ITEM;
    public static ItemStack SET_VARIABLE_ITEM;
    public static ItemStack IF_VARIABLE_ITEM;
    public static ItemStack GAME_ACTION_ITEM;
    public static ItemStack IF_GAME_ITEM;
    public static ItemStack CONTROL_ITEM;
    public static ItemStack SELECT_OBJECT_ITEM;
    public static ItemStack REPEAT_ITEM;
    public static ItemStack ELSE_ITEM;
    public static ItemStack DEV_ITEM;
    public static ItemStack BLOCKS_SHORTCUT;
    public static ItemStack VALUES_INGOT;

    public static void register() {
        PLAYER_EVENT_ITEM = new CodeItem(Material.DIAMOND_BLOCK)
            .name("<aqua>Player Event")
            .description("Used to execute code when something",
                "is done by (or happens to) a player.")
            .examples(
                "Detect when a player joins the plot",
                "Detect when a player right clicks",
                "Detect when a player dies"
            ).build();

        IF_PLAYER_ITEM = new CodeItem(Material.OAK_PLANKS)
            .name("<gold>If Player")
            .description("Used to execute the code inside it",
                "if a certain condition related to a",
                "player is met.")
            .examples(
                "Check if a player is swimming",
                "Check if a player has an item",
                "Check a player's username"
            ).build();

        PLAYER_ACTION_ITEM = new CodeItem(Material.COBBLESTONE)
            .name("<green>Player Action")
            .description("Used to do something related to",
                "a player or multiple players.")
            .examples(
                "Send a message to a player",
                "Damage or heal a player",
                "Clear a player's inventory"
            ).build();

        CALL_FUNCTION_ITEM = new CodeItem(Material.LAPIS_ORE)
            .name("<blue>Call Function")
            .description("Used to call functions declared by",
                "a Function block.",
                "Code will not continue past this block",
                "until the Function completes/returns.")
            .build();

        START_PROCESS_ITEM = new CodeItem(Material.EMERALD_ORE)
            .name("<dark_green>Start Process")
            .description("Used to start processes declared by",
                "a Process block.",
                "Waits inside the Process will not",
                "stop this code line, unlike Functions.")
            .build();

        ENTITY_EVENT_ITEM = new CodeItem(Material.GOLD_BLOCK)
            .name("<yellow>Entity Event")
            .description("Used to execute code when an entity",
                "does something or when something",
                "happens to an entity.")
            .examples(
                "Detect when a mob takes damage",
                "Detect when a projectile kills a mob",
                "Detect when a mob kills another mob"
            ).build();


        IF_ENTITY_ITEM = new CodeItem(Material.BRICKS)
            .name("<dark_green>If Entity")
            .description("Used to execute the code inside it",
                "if a certain condition related to an",
                "entity or multiple entities is met.")
            .examples(
                "Check if an entity is a projectile",
                "Check if an entity is a certain type%nof mob",
                "Check the name of an entity"
            ).build();

        ENTITY_ACTION_ITEM = new CodeItem(Material.MOSSY_COBBLESTONE)
            .name("<red>Entity Action")
            .description("Used to do something related to",
                "an entity or multiple entities.")
            .examples(
                "Set the age or size of a mob",
                "Give a mob a potion effect",
                "Delete an entity from the plot"
            ).build();

        FUNCTION_ITEM = new CodeItem(Material.LAPIS_BLOCK)
            .name("<aqua>Function")
            .description("Used to define a line of code that can be",
                "called with a Call Function block.")
            .additionalInfo(
                "Set the function name by right clicking%n" +
                    "the code block sign with a <aqua>string</aqua>.",
                "Set the name color, icon, and lore of the%n" +
                    "function by putting an item in the chest.",
                "Put <#aaffaa>parameter</#aaffaa> items to specify the%n" +
                    "parameters of the function.",
                "Functions can also be used for continuing%n" +
                    "lines of code that are out of space."
            )
            .build();

        PROCESS_ITEM = new CodeItem(Material.EMERALD_BLOCK)
            .name("<green>Process")
            .description("Used to execute code when the process",
                "is started using a Start Process block.")
            .additionalInfo(
                "Set the process name by right clicking%n" +
                    "the code block sign with a <aqua>string</aqua>.",
                "Set the name color, icon, and lore of the%n" +
                    "process by putting an item in the chest.",
                "Processes are not a replacement for%n" +
                    "Functions. Processes are much more CPU%n" +
                    "intensive than functions, and should only%n" +
                    "be used when needed."
            )
            .build();

        SET_VARIABLE_ITEM = new CodeItem(Material.IRON_BLOCK)
            .name("<yellow>Set Variable")
            .description("Used to set the value of a variable.")
            .examples(
                "Set a variable to a sum of numbers",
                "Remove all instances of a certain%n" +
                    "string from a string variable",
                "Set a variable to a random value"
            ).build();

        IF_VARIABLE_ITEM = new CodeItem(Material.OBSIDIAN)
            .name("<gold>If Variable")
            .description("Used to execute the code inside it",
                "if a certain condition related to the",
                "value of a variable is met.")
            .examples(
                "Check if a number variable is less%n" +
                    "than or equal to another number",
                "Check if a string variable contains%n" +
                    "a certain string",
                "Check if a variable is equal%n" +
                    "to something"
            ).build();

        GAME_ACTION_ITEM = new CodeItem(Material.NETHERRACK)
            .name("<red>Game Action")
            .description("Used to do something related to",
                "the plot and everyone playing it.")
            .examples(
                "Spawn a mob or other entity",
                "Change a block or block region",
                "Change the weather or time"
            ).build();

        IF_GAME_ITEM = new CodeItem(Material.RED_NETHER_BRICKS)
            .name("<red>If Game")
            .description("Used to execute the code inside",
                "it if a certain condition related",
                "to the plot is met.")
            .examples(
                "Check if a container has all of%n" +
                    "a certain set of items",
                "Check if a sign's text contains%n" +
                    "a certain text",
                "Check if a block at a certain%n" +
                    "location is a certain block"
            ).build();

        CONTROL_ITEM = new CodeItem(Material.COAL_BLOCK)
            .name("<blue>Control")
            .description("Used to control the execution of",
                "some or all code blocks after it.")
            .examples(
                "Pause the code sequence for a%n" +
                    "certain duration",
                "Stop the code sequence",
                "Skip a repeat iteration"
            ).build();

        SELECT_OBJECT_ITEM = new CodeItem(Material.PURPUR_BLOCK)
            .name("<light_purple>Select Object")
            .description("Used to change the selection on the",
                "current line of code, which will affect",
                "the targets of most code blocks.")
            .examples(
                "Select all players on the plot",
                "Select a player with a certain username",
                "Select all players on the plot that meet%n" +
                    "a certain condition"
            ).build();

        REPEAT_ITEM = new CodeItem(Material.PRISMARINE)
            .name("<green>Repeat")
            .description("Used to repeat the code inside it.")
            .examples(
                "Repeat code forever",
                "Repeat code a certain number%n" +
                    "of times",
                "Repeat code until a certain%n" +
                    "condition is met"
            ).build();

        ELSE_ITEM = new CodeItem(Material.END_STONE)
            .name("<dark_aqua>Else")
            .description("Used to execute the code inside it",
                "if the condition directly before it",
                "was not met. Else must be placed directly",
                "after any If block's closing bracket.")
            .build();

        DEV_ITEM = new CodeItem(Material.DIORITE)
            .name("<#FF7FAA>Dev Action")
            .description("Used for debugging and development.",
                "These actions are not intended for",
                "use in normal code.")
            .build();

        BLOCKS_SHORTCUT = new CodeItem(Material.DIAMOND)
            .name("<aqua>Code Blocks")
            .description("Right click to access a menu",
                "containing all code blocks.")
            .build();

        VALUES_INGOT = new CodeItem(Material.IRON_INGOT)
            .name("<white>Values")
            .description("Right click to access a menu",
                "containing all values.")
            .build();
    }
}
