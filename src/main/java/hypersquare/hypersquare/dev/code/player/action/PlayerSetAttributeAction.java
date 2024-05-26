package hypersquare.hypersquare.dev.code.player.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.player.PlayerActionItems;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PlayerSetAttributeAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        List<DecimalNumber> modifierDecimal = ctx.args().allNonNull("modifier");
        double modifier = 0;
        if (!modifierDecimal.isEmpty()) modifier = modifierDecimal.getFirst().toDouble();
        Operations operations = ctx.getTag("operation", Operations::valueOf);
        Attributes attributes = ctx.getTag("attribute", Attributes::valueOf);

        modifier = Math.clamp(modifier, attributes.min, attributes.max);
        Attribute attribute = attributes.attribute;

        for (Player p : targetSel.players()) {
            if (!modifierDecimal.isEmpty()) {
                switch (operations) {
                    case SET_BASE -> p.getAttribute(attribute).setBaseValue(modifier);
                    case ADD_NUMBER -> {
                        AttributeModifier modifierToAdd = new AttributeModifier(UUID.randomUUID(), "AddNumber", modifier, AttributeModifier.Operation.ADD_NUMBER);
                        p.getAttribute(attribute).addModifier(modifierToAdd);
                    }
                    case MULTIPLY_BASE ->
                        p.getAttribute(attribute).setBaseValue(p.getAttribute(attribute).getBaseValue() * modifier);
                    case MULTIPLY_MOD -> {
                        Collection<AttributeModifier> modifiers = p.getAttribute(attribute).getModifiers();
                        for (AttributeModifier existingModifier : modifiers) {
                            double existingValue = existingModifier.getAmount();
                            String name = existingModifier.getName();
                            p.getAttribute(attribute).removeModifier(existingModifier);
                            AttributeModifier modifiedModifier = new AttributeModifier(UUID.randomUUID(), name, existingValue * modifier, existingModifier.getOperation());
                            p.getAttribute(attribute).addModifier(modifiedModifier);
                        }
                    }
                }
            } else {
                Collection<AttributeModifier> modifiers = p.getAttribute(attribute).getModifiers();
                for (AttributeModifier existingModifier : modifiers) {
                    p.getAttribute(attribute).removeModifier(existingModifier);
                }
                p.getAttribute(attribute).setBaseValue(p.getAttribute(attribute).getDefaultValue());
            }
            p.setHealth(Math.clamp(p.getHealth(), 1, p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(
                DisplayValue.NUMBER, false, true, Component.text("Modifier Amount"), "modifier"),
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("attribute", "Attribute", Attributes.ARMOR,
                new BarrelTag.Option(Attributes.ARMOR, "Armor", Material.IRON_CHESTPLATE),
                new BarrelTag.Option(Attributes.ARMOR_TOUGHNESS, "Armor toughness", Material.GOLDEN_CHESTPLATE),
                new BarrelTag.Option(Attributes.ATTACK_DAMAGE, "Attack damage", Material.IRON_SWORD),
                new BarrelTag.Option(Attributes.ATTACK_SPEED, "Attack speed", Material.GOLDEN_SWORD),
                new BarrelTag.Option(Attributes.KNOCKBACK_RESISTANCE, "Knockback resistance", Material.IRON_LEGGINGS),
                new BarrelTag.Option(Attributes.LUCK, "Luck", Material.SCUTE),
                new BarrelTag.Option(Attributes.MAX_HEALTH, "Max health", Material.GOLDEN_APPLE),
                new BarrelTag.Option(Attributes.MAX_ABSORPTION, "Max absorption", Material.ENCHANTED_GOLDEN_APPLE)
            ),
            new BarrelTag("operation", "Operation", Operations.SET_BASE,
                new BarrelTag.Option(Operations.SET_BASE, "Set base", Material.LIME_DYE),
                new BarrelTag.Option(Operations.ADD_NUMBER, "Add to base", Material.LIGHT_BLUE_DYE),
                new BarrelTag.Option(Operations.MULTIPLY_BASE, "Multiply base", Material.CYAN_DYE),
                new BarrelTag.Option(Operations.MULTIPLY_MOD, "Multiply modifier", Material.ORANGE_DYE)
            )
        };
    }

    @Override
    public String getId() {
        return "set_player_attribute";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "SetAttribute";
    }

    @Override
    public String getName() {
        return "Set Player Attribute";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.STATISTICS_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.TOTEM_OF_UNDYING)
            .setName(Component.text("Set Player Attribute").color(NamedTextColor.GOLD))
            .setDescription(Component.text("Sets an attribute"),
                Component.text("of a player."))
            .addAdditionalInfo(Component.text("Resets a player's attribute if"),
                Component.text("no parameter was provided."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("modifier", 11)
            .tag("attribute", 13)
            .tag("operation", 15);

    }

    private enum Attributes {
        ARMOR(Attribute.GENERIC_ARMOR, 0, 30),
        ARMOR_TOUGHNESS(Attribute.GENERIC_ARMOR_TOUGHNESS, 0, 20),
        ATTACK_DAMAGE(Attribute.GENERIC_ATTACK_DAMAGE, 0, 2048),
        ATTACK_SPEED(Attribute.GENERIC_ATTACK_SPEED, 0, 1024),
        KNOCKBACK_RESISTANCE(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 0, 1),
        LUCK(Attribute.GENERIC_LUCK, -1024, 1024),
        MAX_ABSORPTION(Attribute.GENERIC_MAX_ABSORPTION, 0, 2048),
        MAX_HEALTH(Attribute.GENERIC_MAX_HEALTH, 1, 1024);

        private final Attribute attribute;
        private final int min;
        private final int max;

        Attributes(Attribute literalAttribute, int min, int max) {
            this.attribute = literalAttribute;
            this.min = min;
            this.max = max;
        }
    }

    private enum Operations {
        SET_BASE,
        ADD_NUMBER,
        MULTIPLY_BASE,
        MULTIPLY_MOD
    }
}
