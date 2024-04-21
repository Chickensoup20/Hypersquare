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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;


public class PlayerNumericalStatTemplate implements Action {
    protected String name;
    protected Component argDescription;
    protected ActionItem actionItem;
    protected BiConsumer<Player, Double> fun;
    // protected DecimalNumber defaultVal;

    public PlayerNumericalStatTemplate(@NotNull ActionItem actionItem, @NotNull String name, @NotNull String argDescription, BiConsumer<Player, Double> fun) {
        this.name = name;
        this.fun = fun;
        this.actionItem = actionItem;
        this.argDescription = Component.text(argDescription);
        // this.defaultVal = defaultVal;
    }

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            fun.accept(p, ctx.args().<DecimalNumber>single("value").toDouble());
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.NUMBER, false, false, argDescription, "value")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return name.replace("Set", "").replace(" ", "_").toLowerCase();
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return name.replace("Set", "").replace(" ", "");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.STATISTICS_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return actionItem
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("value", 13);
    }
}

