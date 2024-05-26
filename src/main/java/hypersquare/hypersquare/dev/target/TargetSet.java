package hypersquare.hypersquare.dev.target;


import hypersquare.hypersquare.item.event.Event;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public record TargetSet(TargetType type, SortedSet<Target> targets) {
    public TargetSet(TargetType type, Target... targets) {
        this(type, new TreeSet<>(Arrays.asList(targets)));
    }

    public static TargetSet getPlayerSet(Event hsEvent) {
        Target[] targets = hsEvent.compatibleTargets();
        TargetSet set = new TargetSet(TargetType.PLAYER);
        for (Target t : targets) {
            if (t.targetType == TargetType.PLAYER || t.targetType == TargetType.ALL) set.targets().add(t);
        }
        return set;
    }

    public static TargetSet getEntitySet(Event hsEvent) {
        Target[] targets = hsEvent.compatibleTargets();
        TargetSet set = new TargetSet(TargetType.PLAYER);
        for (Target t : targets) {
            if (t.targetType == TargetType.ENTITY || t.targetType == TargetType.ALL) set.targets().add(t);
        }
        return set;
    }
}
