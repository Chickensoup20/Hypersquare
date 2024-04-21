package hypersquare.hypersquare.dev.code.player.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.value.impl.TextValue;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.player.PlayerActionItems;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

public class PlayerShowTitleAction implements Action {

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            Component title = ctx.args().<Component>allNonNull("title").getFirst();
            List<Component> subtitleParam = ctx.args().allNonNull("subtitle");
            long duration = 60;
            List<DecimalNumber> durationParam = ctx.args().allNonNull("duration");
            if (!durationParam.isEmpty()) {
                duration = durationParam.getFirst().toLong();
            }

            List<DecimalNumber> inLengthParam = ctx.args().allNonNull("inLength");
            long inLength = 20;
            if (!inLengthParam.isEmpty()) {
                inLength = inLengthParam.getFirst().toLong();
            }

            List<DecimalNumber> outLengthParam = ctx.args().allNonNull("outLength");
            long outLength = 20;
            if (!outLengthParam.isEmpty()) {
                outLength = outLengthParam.getFirst().toLong();
            }

            Title.Times times = Title.Times.times(
                Duration.ofSeconds(inLength / 20),
                Duration.ofSeconds(duration / 20),
                Duration.ofSeconds(outLength / 20)
            );

            Component subtitle = Component.text("");
            if (!subtitleParam.isEmpty()) {
                subtitle = subtitleParam.getFirst();
            }

            Title showtitle = Title.title(title, subtitle, times);
            p.showTitle(showtitle);
        }

    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.TEXT, false, false, Component.text("Title text."), "title"),
            new BarrelParameter(DisplayValue.TEXT, false, true, Component.text("Subtitle text."), "subtitle"),
            new BarrelParameter(DisplayValue.NUMBER, false, true, Component.text("Title duration."),List.of(Component.text("Default = 60 ticks")), "duration"),
            new BarrelParameter(DisplayValue.NUMBER, false, true, Component.text("Fade in length."), "inLength"),
            new BarrelParameter(DisplayValue.NUMBER, false, true, Component.text("Fade out length."), "outLength")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    @Override
    public String getId() {
        return "show_title";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "ShowTitle";
    }

    @Override
    public String getName() {
        return "Show Title";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.PLAYER_ACTION_COMMUNICATION;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.BIRCH_SIGN)
            .setName(Component.text(this.getName()).color(NamedTextColor.GREEN))
            .setDescription(Component.text("Show title text to a player."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("title", 0)
            .parameter("subtitle",1)
            .parameter("duration",2)
            .parameter("inLength",3)
            .parameter("outLength",4);
    }
    
}
