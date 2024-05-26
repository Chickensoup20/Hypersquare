package hypersquare.hypersquare.dev.target;

/**
 * Target priority when null / unspecified
 */
public class TargetPriority {
    public final static TargetSet PLAYER_SET = new TargetSet(TargetType.PLAYER,
        Target.SELECTED,
        Target.DEFAULT_PLAYER,
        Target.KILLER,
        Target.DAMAGER,
        Target.SHOOTER,
        Target.VICTIM,
        Target.RANDOM_PLAYER
    );

    public final static TargetSet ENTITY_SET = new TargetSet(TargetType.ENTITY,
        Target.SELECTED,
        Target.DEFAULT_ENTITY,
        Target.KILLER,
        Target.DAMAGER,
        Target.SHOOTER,
        Target.PROJECTILE,
        Target.VICTIM,
        Target.RANDOM_ENTITY
    );

    public static TargetSet ofType(TargetType type) {
        return switch (type) {
            case ALL -> null;
            case PLAYER -> PLAYER_SET;
            case ENTITY -> ENTITY_SET;
        };
    }
}
