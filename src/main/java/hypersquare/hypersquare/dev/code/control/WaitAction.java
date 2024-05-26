package hypersquare.hypersquare.dev.code.control;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WaitAction implements Action {
    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.NUMBER, false, true, Component.text("Wait duration"), "duration")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("unit", "Time Unit", WaitTimeUnit.TICKS,
                new BarrelTag.Option(WaitTimeUnit.TICKS, "Ticks", Material.REPEATER),
                new BarrelTag.Option(WaitTimeUnit.SECONDS, "Seconds", Material.CLOCK),
                new BarrelTag.Option(WaitTimeUnit.MINUTES, "Minutes", Material.RED_BED)
            )
        };
    }

    @Override
    public String getId() {
        return "wait";
    }

    @Override
    public String getCodeblockId() {
        return "control";
    }

    @Override
    public String getSignName() {
        return "Wait";
    }

    @Override
    public String getName() {
        return "Wait";
    }

    @Override
    public ActionMenuItem getCategory() {
        return null;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.CLOCK)
            .setName(Component.text(getName()).color(Colors.YELLOW))
            .setDescription(Component.text("Waits a certain amount of time."))
            .setParameters(parameters())
            .setTagAmount(1)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("duration", 12)
            .tag("unit", 14);
    }

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        WaitTimeUnit unit = ctx.getTag("unit", WaitTimeUnit::valueOf);
        DecimalNumber duration = ctx.args().getOr("duration", new DecimalNumber(1L));
        ctx.sleep(unit.get(duration.toDouble()));
    }

    enum WaitTimeUnit {
        TICKS(1),
        SECONDS(20),
        MINUTES(20 * 60),
        ;

        public final long ticks;

        WaitTimeUnit(long ticks) {
            this.ticks = ticks;
        }

        public int get(double dur) {
            return (int) (dur * ticks);
        }
    }
}
