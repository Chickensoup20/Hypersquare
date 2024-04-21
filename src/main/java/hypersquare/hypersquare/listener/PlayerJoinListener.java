package hypersquare.hypersquare.listener;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.plot.ChangeGameMode;
import hypersquare.hypersquare.plot.PlayerDatabase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import static hypersquare.hypersquare.Hypersquare.fullMM;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Hypersquare.mode.put(player, "at spawn");
        if (!PlayerDatabase.playerExists(player.getUniqueId())) {
            PlayerDatabase.addPlayer(player.getUniqueId(), 2, 1, 0, 0, 0);
        }
        ChangeGameMode.spawn(player);
        player.showTitle(Title.title(
            Component.text("◆").color(NamedTextColor.BLUE)
                .append(Component.text(" Hypersquare ").color(NamedTextColor.AQUA))
                .append(Component.text("◆").color(NamedTextColor.BLUE)),
            Component.text("mchypersquare.xyz").color(NamedTextColor.AQUA),
            Title.Times.times(Ticks.duration(20), Ticks.duration(60), Ticks.duration(20))
        ));
        player.sendMessage(
            Component.text("◆").color(NamedTextColor.BLUE)
                .append(Component.text(" Welcome back to Hypersquare. ").color(NamedTextColor.AQUA))
                .append(Component.text("◆").color(NamedTextColor.BLUE)));
        event.joinMessage(Component.text(player.getName() + " joined.").color(NamedTextColor.GRAY));

        player.sendMessage(fullMM.deserialize("""
            <#00CED1><b>Hypersquare public beta</b>
                    
            <#00FFFF>Hey there,
                    
            <#00CED1>Welcome to our Beta Minecraft Server!
                    
            Just a quick heads-up:
                    
            <#87CEEB>- Work in Progress: <white>Not all code blocks and events are in place yet. We're actively adding new features to enhance your experience.
                    
            <#87CEEB>- Report Bugs: <white>If you spot any bugs, please let us know at our <click:open_url:'https://discord.gg/Angjk6F3W'><#4169E1><u><b>Discord</b><u></click>. Your input helps us improve!
                                            
            <#00FFFF>Thanks for being a part of our community. Let's make this server awesome together!
            """));

        Hypersquare.cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());

    }
}
