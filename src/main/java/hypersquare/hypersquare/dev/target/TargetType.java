package hypersquare.hypersquare.dev.target;

public enum TargetType {
    PLAYER, ENTITY, ALL;

    public static TargetType ofCodeblock(String id) {
        return switch (id) {
            case "player_action", "if_player" -> PLAYER;

            case "entity_action", "if_entity" -> ENTITY;
            default -> null;
        };
    }
}
