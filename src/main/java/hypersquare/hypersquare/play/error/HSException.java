package hypersquare.hypersquare.play.error;

import hypersquare.hypersquare.util.PlotUtilities;
import hypersquare.hypersquare.util.Utilities;
import hypersquare.hypersquare.util.component.BasicComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static hypersquare.hypersquare.Hypersquare.cleanMM;

public class HSException extends RuntimeException {
    private final List<CommandSender> senders;
    private final String msg;

    public HSException(@NotNull CodeErrorType error, Throwable cause) {
        this(List.of(), error.message, cause);
    }

    public HSException(CommandSender sender, @NotNull CodeErrorType error) {
        this(List.of(sender), error.message, null);
    }

    public HSException(int plotId, @NotNull CodeErrorType error, Throwable cause) {
        //noinspection unchecked
        this((List<CommandSender>) (List<?>) PlotUtilities.getOnlinePlotDevs(plotId), error.message, cause);
    }

    public HSException(List<CommandSender> senders, String msg, Throwable cause) {
        super(msg, cause, false, false);
        this.senders = senders;
        this.msg = msg;
    }

    public static void sendErrorSound(@NotNull Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_SHULKER_HURT_CLOSED, 1, 1);
    }

    public static void sendError(CommandSender s, String m) {
        new HSException(List.of(s), m, null).sendMessage();
    }

    public static @NotNull List<Component> getStackTrace(@NotNull Throwable cause) {
        StringWriter s = new StringWriter();
        cause.printStackTrace(new PrintWriter(s));
        String[] split = s.toString().split("\n");
        List<Component> components = new ArrayList<>(List.of());
        for (String line : split) {
            components.add(BasicComponent.gray(line.replaceAll("[\r\t]", "")));
        }
        return components;
    }

    public void sendMessage() {
        senders.forEach(s -> {
            if (s instanceof Player p) sendErrorSound(p);
        });
        int i = 0;
        Throwable cause = this;
        while (cause != null) {
            String errMsg = (cause instanceof HSException hsCause) ? hsCause.msg : CodeErrorType.INTERNAL_ERROR.message;
            errMsg = errMsg.replaceAll("[\r\t]", "");
            Component msg;
            if (i == 0) msg = cleanMM.deserialize(" <red>Error: <gray>" + errMsg);
            else msg = cleanMM.deserialize(" <red>Caused by: <gray>" + errMsg);
            List<Component> stackTrace = getStackTrace(cause);
            Component stackTraceComponent = BasicComponent.gray("Stack Trace:");
            for (Component line : stackTrace) {
                stackTraceComponent = stackTraceComponent.appendNewline().append(line);
            }
            msg = msg.hoverEvent(HoverEvent.showText(stackTraceComponent));
            final Component finalMsg = msg;
            senders.forEach(s -> Utilities.sendRedInfo(s, finalMsg));
            cause = cause.getCause();
            i++;
        }
    }
}
