package hypersquare.hypersquare.dev.target;

import hypersquare.hypersquare.Hypersquare;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.util.color.Colors;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public enum Target {
    DEFAULT_PLAYER(PlayerEvent.class, TargetType.PLAYER, "Default Player", Material.POTATO, Colors.GREEN_LIGHT,
        "The player that triggered\nthe event.", (PlayerEvent e, CodeSelection _) -> of(e.getPlayer())),
    @SuppressWarnings("") ALL_PLAYERS(PlayerEvent.class, TargetType.PLAYER, "All Players", Material.BEACON, Colors.SKY_LIGHT,
        "Every player on this plot.", (PlayerEvent e, CodeSelection _) -> new CodeSelection((List<Entity>) (List<?>) e.getPlayer().getWorld().getPlayers())),
    DEFAULT_ENTITY(EntityEvent.class, TargetType.ENTITY, "Default Entity", Material.POTATO, Colors.GREEN_LIGHT,
        "The entity that triggered\nthe event.", (EntityEvent e, CodeSelection _) -> of(e.getEntity())),
    ALL_ENTITIES(Event.class, TargetType.ENTITY, "All Entities", Material.DIAMOND_BLOCK, Colors.SKY_LIGHT,
        "Every entity on this plot.", (e, _) -> {
        if (e instanceof PlayerEvent pE) return new CodeSelection(pE.getPlayer().getWorld().getEntities());
        else if (e instanceof EntityEvent eE) return new CodeSelection(eE.getEntity().getWorld().getEntities());
        else return null;
    }),

    SELECTED(Event.class, "Current Selection", Material.NETHER_STAR, Colors.GREEN_LIGHT, "The currently selected\nentity.", (_, s) -> s),
    KILLER(
        EntityDeathEvent.class, "Killer", Material.IRON_SWORD, Colors.RED_DARK,
        "The entity that killed the\nevent victim.", (EntityDeathEvent e, CodeSelection _) -> of(e.getEntity().getKiller())),
    DAMAGER(
        EntityDamageByEntityEvent.class, "Damager", Material.STONE_SWORD, Colors.RED,
        "The entity that damaged the\nevent victim.", (EntityDamageByEntityEvent e, CodeSelection _) -> of(e.getDamager())),
    SHOOTER(ProjectileHitEvent.class, "Shooter", Material.BOW, Colors.YELLOW_LIGHT,
        "The player that fired a\nprojectile in this event.", (ProjectileHitEvent e, CodeSelection _) -> of((Entity) e.getEntity().getShooter())),
    PROJECTILE(EntityEvent.class, TargetType.ENTITY, "Projectile", Material.ARROW, Colors.BLUE_LIGHT,
        "The projectile in this event.", (EntityEvent e, CodeSelection _) -> {
        try {
            return of((Projectile) e.getEntity());
        } catch (Exception ignored) {
            return null;
        }
    }),
    VICTIM(EntityDamageEvent.class, "Victim", Material.SKELETON_SKULL, Colors.BLUE_LIGHT,
        "The entity that was damaged\nor killed in this event.", (EntityDamageEvent e, CodeSelection _) -> of(e.getEntity())),

    RANDOM_PLAYER(PlayerEvent.class, "Random Player", Material.HONEYCOMB, Colors.GOLD_LIGHT,
        "A random player from the plot.", (PlayerEvent e, CodeSelection _) -> {
        List<Player> players = e.getPlayer().getWorld().getPlayers();
        return of(players.get(Hypersquare.RANDOM.nextInt(0, players.size())));
    }),
    RANDOM_ENTITY(EntityEvent.class, "Random Entity", Material.HONEYCOMB, Colors.GOLD_LIGHT,
        "A random entity from the plot.", (EntityEvent e, CodeSelection _) -> {
        List<Entity> entities = e.getEntity().getWorld().getEntities();
        return of(entities.get(Hypersquare.RANDOM.nextInt(0, entities.size())));
    }),

    ;

    public final Class<? extends Event> eventType;
    public final String[] description;
    public final TextColor color;
    public final Material mat;
    public final String name;
    public final TargetType targetType;
    private final TargetGetter getter;
    Target(Class<? extends Event> e, TargetType u, String n, Material m, TextColor c, String d, TargetGetter<? extends Event> g) {
        this.description = d.split("\\R");
        this.eventType = e;
        this.targetType = u;
        this.getter = g;
        this.color = c;
        this.name = n;
        this.mat = m;
    }
    Target(Class<? extends Event> e, String n, Material m, TextColor c, String d, TargetGetter<? extends Event> g) {
        this(e, TargetType.ALL, n, m, c, d, g);
    }

    public static Target getTarget(String name) {
        for (Target t : Target.values()) {
            if (t.name().equals(name) || t.name.equals(name)) return t;
        }
        return null;
    }

    private static CodeSelection of(Entity... entities) {
        return new CodeSelection(entities);
    }

    public <T extends Event> CodeSelection get(T event, CodeSelection s) {
        if (!eventType.isAssignableFrom(event.getClass()))
            throw new RuntimeException(event.getClass().getName() + " class is not compatible with target " + name + " (" + eventType.getName() + ")");
        //noinspection unchecked
        return getter.get(eventType.cast(event), s);
    }
}


interface TargetGetter<T extends Event> {
    @Nullable CodeSelection get(T e, CodeSelection s);
}
