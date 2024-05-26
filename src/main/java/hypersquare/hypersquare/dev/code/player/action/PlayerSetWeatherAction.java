package hypersquare.hypersquare.dev.code.player.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.player.PlayerActionItems;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerSetWeatherAction implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            Weather weather = ctx.getTag("weather", Weather::valueOf);
            if (weather == Weather.CLEAR) p.setPlayerWeather(WeatherType.CLEAR);
            if (weather == Weather.DOWNFALL) p.setPlayerWeather(WeatherType.DOWNFALL);
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{};
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("weather", "Weather", Weather.DOWNFALL,
                new BarrelTag.Option(Weather.CLEAR, "Clear", Material.BUCKET),
                new BarrelTag.Option(Weather.DOWNFALL, "Downfall", Material.WATER_BUCKET)
            )
        };
    }

    @Override
    public String getId() {
        return "set_weather";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "SetPlayerWeather";
    }

    @Override
    public String getName() {
        return "Set Player Weather";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.WORLD_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.WATER_BUCKET)
            .setName(Component.text("Set Player Weather").color(NamedTextColor.BLUE))
            .setDescription(Component.text("Sets the type of weather"),
                Component.text("visible to a player."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .tag("weather", 13);
    }

    private enum Weather {
        CLEAR,
        DOWNFALL
    }
}
