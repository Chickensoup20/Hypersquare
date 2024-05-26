package hypersquare.hypersquare.play.execution;

import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.event.Event;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.CodeVariableScope;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CodeStacktrace {
    public final Event event;
    public final List<Frame> frames;
    public final org.bukkit.event.Event bukkitEvent;
    public final CodeVariableScope lineScope = new CodeVariableScope();
    public boolean halt = false;

    public CodeStacktrace(Event event, org.bukkit.event.Event bukkitEvent, Frame frame) {
        this.event = event;
        frames = new LinkedList<>();
        frames.add(frame);
        this.bukkitEvent = bukkitEvent;
    }

    public Frame next() {
        Frame next = frames.getLast();
        if (next != null) return next;
        frames.removeLast();
        if (isDone()) return null;
        return next();
    }

    public boolean isDone() {
        return frames.isEmpty();
    }

    public void popFrame() {
        frames.removeLast();
    }

    public void pushFrame(Frame frame) {
        frames.add(frame);
    }

    public static class Frame {
        public final List<CodeActionData> actions;
        public final HashMap<String, Object> tempData = new HashMap<>();
        public final CodeSelection selection;
        public int position = 0;

        public Frame(List<CodeActionData> actions, CodeSelection selection) {
            this.actions = actions;
            this.selection = selection;
        }

        public CodeActionData next() {
            if (position < actions.size()) {
                CodeActionData action = actions.get(position);
                position++;
                return action;
            }
            return null;
        }

        public CodeActionData current() {
            return actions.get(position - 1);
        }
    }
}
