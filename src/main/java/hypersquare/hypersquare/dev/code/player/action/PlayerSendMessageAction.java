package hypersquare.hypersquare.dev.code.player.action;

import hypersquare.hypersquare.dev.BarrelParameter;
import hypersquare.hypersquare.dev.BarrelTag;
import hypersquare.hypersquare.dev.action.Action;
import hypersquare.hypersquare.dev.codefile.data.CodeActionData;
import hypersquare.hypersquare.item.action.ActionItem;
import hypersquare.hypersquare.item.action.ActionMenuItem;
import hypersquare.hypersquare.item.action.player.PlayerActionItems;
import hypersquare.hypersquare.item.value.DisplayValue;
import hypersquare.hypersquare.menu.barrel.BarrelMenu;
import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerSendMessageAction implements Action {

    @Override
    public void execute(@NotNull ExecutionContext ctx, @NotNull CodeSelection targetSel) {
        for (Player p : targetSel.players()) {
            List<Component> messages = ctx.args().allNonNull("messages");

            if (messages.isEmpty()) {
                p.sendMessage(Component.empty());
                return;
            }

            MergingOptions merging = ctx.getTag("text_merging", MergingOptions::valueOf);

            switch (merging) {
                case SPACES ->
                    p.sendMessage(Component.join(JoinConfiguration.builder().separator(Component.text(" ")).build(), messages));
                case NO_SPACES -> p.sendMessage(Component.join(JoinConfiguration.noSeparators(), messages));
                case SEPARATE_MESSAGES -> {
                    for (Component c : messages) p.sendMessage(c);
                }
            }
        }
    }

    @Override
    public BarrelParameter[] parameters() {
        return new BarrelParameter[]{
            new BarrelParameter(DisplayValue.TEXT, true, true, Component.text("The message(s) to send."), "messages")
        };
    }

    @Override
    public BarrelTag[] tags() {
        return new BarrelTag[]{
            new BarrelTag("text_merging", "Text Value Splitting", MergingOptions.SPACES,
                new BarrelTag.Option(MergingOptions.NO_SPACES, "No spaces", Material.PAPER),
                new BarrelTag.Option(MergingOptions.SPACES, "Add spaces", Material.MAP),
                new BarrelTag.Option(MergingOptions.SEPARATE_MESSAGES, "Separate messages", Material.FILLED_MAP)
            )
        };
    }

    @Override
    public String getId() {
        return "send_message";
    }

    @Override
    public String getCodeblockId() {
        return "player_action";
    }

    @Override
    public String getSignName() {
        return "SendMessage";
    }

    @Override
    public String getName() {
        return "Send Message";
    }

    @Override
    public ActionMenuItem getCategory() {
        return PlayerActionItems.PLAYER_ACTION_COMMUNICATION;
    }

    @Override
    public ItemStack item() {
        return new ActionItem()
            .setMaterial(Material.OAK_SIGN)
            .setName(Component.text(this.getName()).color(NamedTextColor.GREEN))
            .setDescription(Component.text("Sends the player all of the"),
                Component.text("messages in the barrel"))
            .setParameters(parameters())
            .setTagAmount(tags().length)
            .build();
    }

    @Override
    public BarrelMenu actionMenu(CodeActionData data) {
        return new BarrelMenu(this, 4, data)
            .parameter("messages", 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)
            .tag("text_merging", 35);
    }

    private enum MergingOptions {
        SPACES,
        NO_SPACES,
        SEPARATE_MESSAGES
    }
}
