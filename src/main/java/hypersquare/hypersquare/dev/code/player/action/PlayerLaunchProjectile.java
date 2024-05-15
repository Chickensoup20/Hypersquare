package hypersquare.hypersquare.dev.code.player.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.player.PlayerActionItems;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PlayerLaunchProjectile implements Action {
    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        ProjectileType projectileType = ctx.getTag("projectile", ProjectileType::valueOf);
        float projectileSpeed = ctx.args().getOr("speed", new DecimalNumber(1, 0)).toFloat();
        float projectileInaccuracy = ctx.args().getOr("inaccuracy", new DecimalNumber(0, 0)).toFloat();
        for (Player p : targetSel.players()) {
            Location launchPoint = ctx.args().getOr("location", p.getEyeLocation().add(p.getEyeLocation().getDirection()));
            Vector direction = launchPoint.getDirection();
            direction.rotateAroundX((Math.random()*2-1) * projectileInaccuracy/5);
            direction.rotateAroundZ((Math.random()*2-1) * projectileInaccuracy/5);
            Projectile projectile = p.getWorld().spawn(launchPoint, projectileType.get());

            if(projectileType == ProjectileType.CHARGED_WITHER_SKULL) ((WitherSkull) projectile).setCharged(true);
            if (projectile instanceof AbstractArrow) ((AbstractArrow) projectile).setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            if(projectile instanceof Explosive) ((Explosive) projectile).setIsIncendiary(false);

            projectile.setVelocity(direction.normalize().multiply(projectileSpeed));
            projectile.setShooter(p);
        }
    }
    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.LOCATION, false, true, Component.text("Launch point"), "location"),
            new BarrelParameter(DisplayValue.NUMBER, false, true, Component.text("Speed"), "speed"),
            new BarrelParameter(DisplayValue.NUMBER, false, true, Component.text("Inaccuracy"), "inaccuracy")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("projectile", "Projectile", ProjectileType.ARROW,
                new BarrelTag.Option(ProjectileType.ARROW, "Arrow", Material.ARROW),
                new BarrelTag.Option(ProjectileType.SPECTRAL_ARROW, "Spectral arrow", Material.SPECTRAL_ARROW),
                new BarrelTag.Option(ProjectileType.SNOWBALL, "Snowball", Material.SNOWBALL),
                new BarrelTag.Option(ProjectileType.EGG, "Egg", Material.EGG),
                new BarrelTag.Option(ProjectileType.FIREBALL, "Fireball", Material.FIRE_CHARGE),
                new BarrelTag.Option(ProjectileType.SMALL_FIREBALL, "Small fireball", Material.FIRE_CHARGE),
                new BarrelTag.Option(ProjectileType.DRAGON_FIREBALL, "Dragon fireball", Material.DRAGON_BREATH),
                new BarrelTag.Option(ProjectileType.WITHER_SKULL, "Wither skull", Material.WITHER_SKELETON_SKULL),
                new BarrelTag.Option(ProjectileType.CHARGED_WITHER_SKULL, "Charged wither skull", Material.WITHER_SKELETON_SKULL),
                new BarrelTag.Option(ProjectileType.ENDER_PEARL, "Ender pearl", Material.ENDER_PEARL),
                new BarrelTag.Option(ProjectileType.EXPERIENCE_BOTTLE, "Experience bottle", Material.EXPERIENCE_BOTTLE),
                new BarrelTag.Option(ProjectileType.TRIDENT, "Trident", Material.TRIDENT),
                new BarrelTag.Option(ProjectileType.LLAMA_SPIT, "Llama spit", Material.MILK_BUCKET),
                new BarrelTag.Option(ProjectileType.SHULKER_BULLET, "Shulker bullet", Material.SHULKER_SHELL)
            )
        };
    }

    @Override
    public String getId() {
        return "launch_projectile";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "LaunchProj";
    }

    @Override
    public String getName() {
        return "Launch Projectile";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.WORLD_CATEGORY;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.ARROW)
            .setName(Component.text("Launch Projectile").color(NamedTextColor.LIGHT_PURPLE))
            .setDescription(Component.text("Launches a projectile from"),
                Component.text("a player."))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .tag("projectile", 11)
            .parameter("location",13)
            .parameter("speed", 14)
            .parameter("inaccuracy", 15);



    }

    private enum ProjectileType {
        ARROW(org.bukkit.entity.Arrow.class),
        SPECTRAL_ARROW(SpectralArrow.class),
        SNOWBALL(Snowball.class),
        EGG(Egg.class),
        FIREBALL(Fireball.class),
        SMALL_FIREBALL(SmallFireball.class),
        DRAGON_FIREBALL(DragonFireball.class),
        WITHER_SKULL(WitherSkull.class),
        CHARGED_WITHER_SKULL(WitherSkull.class),
        ENDER_PEARL(EnderPearl.class),
        EXPERIENCE_BOTTLE(ThrownExpBottle.class),
        TRIDENT(Trident.class),
        LLAMA_SPIT(LlamaSpit.class),
        SHULKER_BULLET(ShulkerBullet.class)
        ;

        private final Class<? extends org.bukkit.entity.Projectile> projectileClass;

        ProjectileType(Class<? extends org.bukkit.entity.Projectile> projectileClass) {
            this.projectileClass = projectileClass;
        }

        public Class<? extends org.bukkit.entity.Projectile> get() {
            return projectileClass;
        }
    }
}
