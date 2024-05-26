package hypersquare.hypersquare.dev.codefile;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.dev.ArgumentsData;
import hypersquare.hypersquare.dev.CodeBlocks;
import hypersquare.hypersquare.dev.TagOptionsData;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.codefile.data.CodeData;
import hypersquare.hypersquare.dev.codefile.data.CodeLineData;
import hypersquare.hypersquare.dev.target.Target;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.plot.CodeBlockManagement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Piston;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CodeFileHelper {
    private static int getCodelineWorldIndex(@NotNull Location location) {
        return Math.abs(location.getBlockX() / 3) - 1;
    }

    public static int getCodelineListIndex(@NotNull Location location, @NotNull CodeData plotCode) {
        int position = getCodelineWorldIndex(location);

        for (CodeLineData element : plotCode.codelines) {
            if (element.position == position) return plotCode.codelines.indexOf(element);
        }
        return -1;
    }

    /**
     * Set index to -1 for the codeblock to be appended instead of inserted.
     */
    public static CodeData addCodeblock(@NotNull Location location, @NotNull CodeBlocks codeblock, @NotNull CodeFile code) {
        CodeData plotCode = code.getCodeData();

        if (codeblock.isThreadStarter) {
            CodeLineData event = new CodeLineData();
            event.type = codeblock.id;
            event.event = "empty";
            event.position = getCodelineWorldIndex(location);
            plotCode.codelines.add(event);
            return plotCode;
        }
        CodeLineData codeline = plotCode.codelines.get(getCodelineListIndex(location, plotCode));

        List<Integer> positions;
        try {
            positions = findCodeIndex(location);
        } catch (Exception e) {
            Hypersquare.logger().warning("Failed to find code positions");
            return plotCode;
        }

        CodeActionData action = new CodeActionData();
        action.codeblock = codeblock.id;
        action.action = codeblock.defaultAction.isEmpty() ? "empty" : codeblock.defaultAction;

        if (positions.size() == 1) {
            codeline.actions.add(positions.get(0), action);
            return plotCode;
        }

        CodeActionData parent = codeline.actions.get(positions.get(0));
        for (int i = 1; i < positions.size() - 1; i++) {
            parent = parent.actions.get(positions.get(i));
        }

        parent.actions.add(positions.get(positions.size() - 1), action);

        return plotCode;
    }

    public static CodeData removeCodeBlock(@NotNull Location location, @NotNull CodeFile code, boolean breakAll) {
        CodeData plotCode = code.getCodeData();

        int codelineListIndex = getCodelineListIndex(location, plotCode);
        CodeLineData codeline = plotCode.codelines.get(codelineListIndex);

        List<Integer> positions;
        try {
            positions = findCodeIndex(location);
        } catch (Exception e) {
            Hypersquare.logger().warning("Failed to find code positions");
            return plotCode;
        }

        if (positions.isEmpty()) {
            plotCode.codelines.remove(codelineListIndex);
            return plotCode;
        }

        if (positions.size() == 1) {
            int pos = positions.get(0);
            CodeActionData prev = codeline.actions.remove(pos);
            if (!breakAll) for (CodeActionData action : prev.actions) codeline.actions.add(pos, action);
            return plotCode;
        }

        CodeActionData parent = codeline.actions.get(positions.get(0));
        for (int i = 1; i < positions.size() - 1; i++) {
            parent = parent.actions.get(positions.get(i));
        }

        int pos = positions.get(positions.size() - 1);
        CodeActionData prev = parent.actions.remove(pos);
        if (!breakAll) for (CodeActionData action : prev.actions) {
            parent.actions.add(pos, action);
        }
        return plotCode;
    }

    public static void updateAction(@NotNull Location location, @NotNull CodeFile code, Action newAction) {
        CodeData plotCode = code.getCodeData();
        int codelineListIndex = getCodelineListIndex(location, plotCode);

        if (codelineListIndex == -1) {
            // We are updating a non-existent codeline (got deleted by another player)
            // Logging just in case for debug purposes
            Hypersquare.logger().warning("[Update Action] Tried updating a non existent codeline @ " + code.world());
            code.getCodeData();
            return;
        }

        CodeLineData codeline = new CodeLineData();
        if (!plotCode.codelines.isEmpty()) codeline = plotCode.codelines.get(codelineListIndex);
        else plotCode.codelines.set(codelineListIndex, codeline);


        List<Integer> positions;
        try {
            positions = CodeFileHelper.findCodeIndex(location);
        } catch (Exception e) {
            // We are updating a non-existent codeblock
            Hypersquare.logger().info("[Update Action] Couldn't find the codeblock the player was editing @ " + code.world());
            return;
        }

        CodeActionData actionData = codeline.actions.get(positions.get(0));
        for (int i = 1; i < positions.size(); i++) {
            actionData = actionData.actions.get(positions.get(i));
        }
        actionData.action = newAction.getId();
        actionData.getArguments().clear();

        code.setCode(plotCode.toJson().toString());
    }

    public static void updateEvent(@NotNull Location location, @NotNull CodeFile code, Event newEvent) {
        CodeData plotCode = code.getCodeData();
        int codelineListIndex = getCodelineListIndex(location, plotCode);

        if (codelineListIndex == -1) {
            // We are updating a non-existent codeline (got deleted by another player)
            // Logging just in case for debug purposes
            Hypersquare.logger().warning("[Update Event] Tried updating a non existent codeline @ " + code.world());
            code.getCodeData();
            return;
        }

        CodeLineData codeline = new CodeLineData();
        if (!plotCode.codelines.isEmpty()) codeline = plotCode.codelines.get(codelineListIndex);
        else plotCode.codelines.set(codelineListIndex, codeline);

        codeline.event = newEvent.getId();
        plotCode.codelines.set(codelineListIndex, codeline);
        code.setCode(plotCode.toJson().toString());
    }

    public static void updateTarget(@NotNull Location location, @NotNull CodeFile code, Target newTarget) {
        CodeData plotCode = code.getCodeData();
        int codelineListIndex = getCodelineListIndex(location, plotCode);

        if (codelineListIndex == -1) {
            Hypersquare.logger().warning("[Update Target] Tried updating a non existent codeline @ " + code.world());
            code.getCodeData();
            return;
        }

        CodeLineData codeline = new CodeLineData();
        if (!plotCode.codelines.isEmpty()) codeline = plotCode.codelines.get(codelineListIndex);
        else plotCode.codelines.set(codelineListIndex, codeline);

        List<Integer> positions;
        try {
            positions = CodeFileHelper.findCodeIndex(location);
        } catch (Exception e) {
            // We are updating a non-existent codeblock
            Hypersquare.logger().info("[Update Target] Couldn't find the codeblock the player was editing @ " + code.world());
            return;
        }

        CodeActionData actionData = codeline.actions.get(positions.get(0));
        for (int i = 1; i < positions.size(); i++) {
            actionData = actionData.actions.get(positions.get(i));
        }
        actionData.target = newTarget.name();

        code.setCode(plotCode.toJson().toString());
    }

    /**
     * @return List of integers representing the path to the codeblock,
     * if the codeblock is the thread starter it returns an empty list
     */
    public static List<Integer> findCodeIndex(@NotNull Location queryLoc) throws Exception {
        List<Integer> trace = new ArrayList<>();
        trace.add(0);

        // Thread starter special treatment
        if (queryLoc.getBlockZ() == 1) return List.of();

        Location end = CodeBlockManagement.findCodeEnd(queryLoc.clone());
        Location location = queryLoc.clone().set(queryLoc.getBlockX(), queryLoc.getBlockY(), 1);

        while (location.getBlockZ() <= end.getBlockZ() + 5) { // +5 for extra padding
            if (location.getBlockZ() == queryLoc.getBlockZ()) return trace;

            if (location.getBlock().getType() == Material.STONE || location.getBlock().getType() == Material.AIR) {
                location.add(0, 0, 1);
                continue;
            }

            if (location.getBlock().getType() != Material.PISTON && location.getBlock().getType() != Material.STICKY_PISTON) {
                CodeBlocks codeblock = CodeBlocks.getByMaterial(location.getBlock().getType());
                if (codeblock != null && !(codeblock.hasBrackets || codeblock.isThreadStarter)) {
                    trace.set(trace.size() - 1, trace.get(trace.size() - 1) + 1); // ^ Skip the bracket codeblock or the thread starter
                }
                location.add(0, 0, 1);
                continue;
            }

            Piston piston = (Piston) location.getBlock().getBlockData();
            BlockFace face = piston.getFacing();
            if (face == BlockFace.SOUTH) {
                trace.add(0);
            } else if (face == BlockFace.NORTH) {
                trace.remove(trace.size() - 1);
                trace.set(trace.size() - 1, trace.get(trace.size() - 1) + 1); // Increment the last value
            }

            location.add(0, 0, 1);
        }

        throw new Exception("Somehow never reached the query location");
    }

    public static @Nullable CodeLineData getLineAt(@NotNull Location location, CodeData data) {
        int codelineIndex = CodeFileHelper.getCodelineListIndex(location, data);
        if (codelineIndex < 0 || codelineIndex >= data.codelines.size()) return null;
        return data.codelines.get(codelineIndex);
    }

    public static @Nullable CodeActionData getActionAt(@NotNull Location location, CodeData data) {
        CodeLineData line = getLineAt(location, data);
        return getActionAt(location, line);
    }

    public static @Nullable CodeActionData getActionAt(@NotNull Location location, CodeLineData line) {
        if (line == null) return null;
        try {
            List<Integer> positions = CodeFileHelper.findCodeIndex(location);

            if (positions.isEmpty()) return null;

            CodeActionData action = line.actions.get(positions.get(0));
            for (int i = 1; i < positions.size(); i++) {
                if (positions.get(i) < 0 || positions.get(i) >= action.actions.size()) return null;
                action = action.actions.get(positions.get(i));
            }

            return action;
        } catch (Exception err) {
            return null;
        }
    }

    public static @Nullable ArgumentsData getArgsDataAt(@NotNull Location location, CodeData data) {
        CodeBlocks codeblock = CodeBlocks.getByMaterial(location.getBlock().getType());
        if (codeblock == null || !codeblock.hasBarrel) return null;
        CodeLineData line = getLineAt(location, data);
        CodeActionData action = getActionAt(location, line);
        return action != null ? action : line;
    }

    public static @Nullable TagOptionsData getTagsDataAt(@NotNull Location location, CodeData data) {
        ArgumentsData argsData = getArgsDataAt(location, data);
        if (argsData == null) return null;
        try {
            return (TagOptionsData) argsData;
        } catch (Exception _) {
            return null;
        }
    }
}
