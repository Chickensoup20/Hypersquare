package hypersquare.hypersquare.dev.code.player.action;

import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public enum PlayerStatEnum {
    HEAL_PLAYER(new PlayerNumericalStatTemplate(
        new ActionItem()
            .setMaterial(Material.CARROT)
            .setName(Component.text("Heal Player").color(Colors.RED_LIGHT))
            .setDescription(Component.text("Heals the player the given amount.")),
        "Set Player Health",
        "Health to set to",
        (p, health) -> p.setHealth(p.getHealth() + health)
    )),
    // Have to customize DAMAGE_PLAYER because it can have a source.
    SET_PLAYER_HEALTH(new PlayerNumericalStatTemplate(
        new ActionItem()
            .setMaterial(Material.APPLE)
            .setName(Component.text("Set Current Health").color(Colors.RED_LIGHT))
            .setDescription(Component.text("Sets the player's current health"),
                Component.text("to the specified number")),
        "Set Player Health",
        "Health to set to",
        Player::setHealth
    )),
    SET_PLAYER_INVUL_TICKS(new PlayerNumericalStatTemplate(
        new ActionItem()
            .setMaterial(Material.DIAMOND_HELMET)
            .setName(Component.text("Set Invulnerability Ticks").color(Colors.MUSTARD))
            .setDescription(Component.text("Sets the player's currently"),
                Component.text("remaining ticks until a"),
                Component.text("player can next be hurt."))
            .addAdditionalInfo(Component.text("This value is set to 10"),
                Component.text("upon taking damage.")),
        "Set Invulnerability Ticks",
        "Ticks",
        (player, value) -> player.setNoDamageTicks(value.intValue())
    )),
    SET_PLAYER_FALL_DISTANCE(new PlayerNumericalStatTemplate(
        new ActionItem()
            .setMaterial(Material.HAY_BLOCK)
            .setName(Component.text("Set Fall Distance").color(Colors.MUSTARD))
            .setDescription(Component.text("Sets the player's fall distance,"),
                Component.text("affecting fall damage upon"),
                Component.text("landing.")),
        "Set Fall Distance",
        "Fall distance (blocks)",
        (player, value) -> player.setFallDistance(value.floatValue())
    )),
    SET_PLAYER_REMAINING_AIR(new PlayerNumericalStatTemplate(
        new ActionItem()
            .setMaterial(Material.PUFFERFISH)
            .setName(Component.text("Set Remaining Air").color(Colors.MUSTARD))
            .setDescription(Component.text("Sets the player's remaining"),
                Component.text("breath ticks."))
            .addAdditionalInfo(Component.text("Each bubble is equal"),
                Component.text("to 30 air ticks.")),
        "Set Remaining Air",
        "Breath ticks",
        (player, value) -> player.setRemainingAir(value.intValue())
    )),
    SET_PLAYER_FREEZE_TICKS(new PlayerNumericalStatTemplate(
        new ActionItem()
            .setMaterial(Material.POWDER_SNOW_BUCKET)
            .setName(Component.text("Set Freeze Ticks").color(Colors.MUSTARD))
            .setDescription(Component.text("Sets how long a player"),
                Component.text("is frozen for.")),
        "Set Freeze Ticks",
        "Ticks (0-140)",
        (player, value) -> player.setFreezeTicks(Math.clamp(value.intValue(), 0, 140))
    )),
    SET_PLAYER_FIRE_TICKS(new PlayerNumericalStatTemplate(
        new ActionItem()
            .setMaterial(Material.BLAZE_POWDER)
            .setName(Component.text("Set Fire Ticks").color(Colors.MUSTARD))
            .setDescription(Component.text("Sets the remaining time a"),
                Component.text("player is on fire for."))
            .addAdditionalInfo(Component.text("0 ticks mean the target is"),
                Component.text("not on fire.")),
        "Set Fire Ticks",
        "Ticks",
        (player, value) -> player.setFireTicks(value.intValue())
    )),
    SET_PLAYER_HOTBAR_SLOT(new PlayerNumericalStatTemplate(
        new ActionItem()
            .setMaterial(Material.SLIME_BALL)
            .setName(Component.text("Set Hotbar Slot").color(Colors.MUSTARD))
            .setDescription(Component.text("Sets a player's selected"),
                Component.text("hotbar slot.")),
        "Set Hotbar Slot",
        "New Slot (1-9)",
        (player, value) -> player.getInventory().setHeldItemSlot(Math.clamp(value.intValue(), 1, 9) - 1)
    )),
    SET_PLAYER_MAX_HEALTH(new PlayerNumericalStatTemplate(
        new ActionItem()
            .setMaterial(Material.GOLDEN_APPLE)
            .setName(Component.text("Set Player Max Health").color(Colors.RED_LIGHT))
            .setDescription(Component.text("Sets the player's max health"),
                Component.text("to the specified number")),
        "Set Player Max Health",
        "Max health to set to",
        (player, value) -> player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(value)
    )),
    ;

    final PlayerNumericalStatTemplate instance;

    PlayerStatEnum(PlayerNumericalStatTemplate instance) {
        this.instance = instance;
    }

    public PlayerNumericalStatTemplate getInstance() {
        return instance;
    }
}
