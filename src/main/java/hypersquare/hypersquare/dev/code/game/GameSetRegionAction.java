package hypersquare.hypersquare.dev.code.game;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.dev.value.type.DecimalNumber;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.game.GameActionItems;
import hypersquare.hypersquare.item.action.player.PlayerActionItems;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

public class GameSetRegionAction implements Action {

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        List<ItemStack> block = ctx.args().allNonNull("block");
        ItemStack setBlock;
        if (block.isEmpty())
            setBlock = new ItemStack(Material.AIR);
        else setBlock = block.getFirst();
        //TODO: make the locations not able to go out the plot
        Location corner1 = ctx.args().<Location>allNonNull("corner1").getFirst();
        Location corner2 = ctx.args().<Location>allNonNull("corner2").getFirst();
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(corner1.getWorld()))) {
            Region region = new CuboidRegion(BukkitAdapter.asBlockVector(corner1),BukkitAdapter.asBlockVector(corner2));
            editSession.setBlocks(region,BukkitAdapter.adapt(setBlock.getType().createBlockData()));
        }
    }

    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.BLACKSTONE)
            .setName(Component.text(this.getName()).color(NamedTextColor.GREEN))
            .setDescription(Component.text("Sets a specified region with a specified block."))
            .setParameters(parameters())
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 3, data)
            .parameter("block", 0)
            .parameter("corner1", 1)
            .parameter("corner2", 2);
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.ITEM,false,true,Component.text("Block to set"),"block"),
            new BarrelParameter(DisplayValue.LOCATION,false,false,Component.text("Corner 1"),"corner1"),
            new BarrelParameter(DisplayValue.LOCATION,false,false,Component.text("Corner 2"),"corner2")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{};
    }

    public String getId() {
        return "set_region";
    }

    @Override
    public String getCodeblockId() {
        return "game_action";
    }

    public String getSignName() {
        return "SetRegion";
    }

    @Override
    public String getName() {
        return "Set Region";
    }

    @Override
    public ActionMenuItem getCategory() {
        return GameActionItems.BLOCK_MANAGEMENT_CATEGORY;
    }
}
