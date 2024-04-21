package hypersquare.hypersquare.play;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeSelection {

    private final List<Entity> selection;

    public CodeSelection(List<Entity> selection) {
        this.selection = selection;
    }

    public CodeSelection(Entity... selection) {
        this.selection = Arrays.asList(selection);
    }

    public CodeSelection() {
        this.selection = new ArrayList<>();
    }

    public CodeSelection add(List<Entity> entity) {
        List<Entity> newSelection = new ArrayList<>(selection);
        newSelection.addAll(entity);
        return new CodeSelection(newSelection);
    }

    /**
     * All players in the selection
     *
     * @return selection of players
     */
    public List<Player> players() {
        List<Player> filtered = new ArrayList<>();
        for (Entity e : selection) {
            if (e instanceof Player p) filtered.add(p);
        }
        return filtered;
    }

    /**
     * All entities except players in the selection
     *
     * @return selection of entities
     */
    public List<Entity> noPlayers() {
        List<Entity> filtered = new ArrayList<>();
        for (Entity e : selection) {
            if (!(e instanceof Player)) filtered.add(e);
        }
        return filtered;
    }

    /**
     * All entities of a certain type in the selection
     *
     * @param type type of entity
     * @return selection of the given type
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> ofType(Class<? extends Entity> type) {
        List<T> filtered = new ArrayList<>();
        for (Entity e : selection) {
            if (type.isInstance(e)) filtered.add((T) e);
        }
        return filtered;
    }

    public List<Entity> all() {
        return selection;
    }
}
