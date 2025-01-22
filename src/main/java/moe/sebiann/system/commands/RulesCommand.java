package moe.sebiann.system.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class RulesCommand extends BaseCommand {

    @CommandAlias("rules")
    public static void onRules(Player player){
        if(player.hasPlayedBefore()){
            ItemStack customBook = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) customBook.getItemMeta();

            bookMeta.setTitle("Welcome to Cloudie!");
            bookMeta.setAuthor("CloudieSMP");
            bookMeta.addPages(Component.text("Welcome to Cloudie SMP").color(TextColor.fromHexString("#5555FF"))
                    .append(Component.text("\n-------------------").color(TextColor.fromHexString("#AAAAAA")))
                    .append(Component.text("\n\nContents:").color(TextColor.fromHexString("#000000")))
                    .append(Component.text("\n- Rules").color(TextColor.fromHexString("#000000")).clickEvent(ClickEvent.changePage(2)))
                    .append(Component.text("\n- Commands").color(TextColor.fromHexString("#000000")).clickEvent(ClickEvent.changePage(4))));

            bookMeta.addPages(Component.text("RULES").color(TextColor.fromHexString("#5555FF"))
                    .append(Component.text("\n-------------------").color(TextColor.fromHexString("#AAAAAA")))
                    .append(Component.text("\nAll of the rules listed on the Discord are included.\n\n. No hacking, this includes X-raying, Seed Breakers and Glitch Exploits").color(TextColor.fromHexString("#000000")))
                    .append(Component.text("\n\n-> next page").color(TextColor.fromHexString("#000000")).clickEvent(ClickEvent.changePage(3))));

            bookMeta.addPages(Component.text("2. No Trolling, this includes Killing people without their permission, Stealing from houses and inventories, Greifing people by adding inappropriate aspects to someone's build and also destroying parts\n\nFailure to follow these rules will lead to consequences."));
            bookMeta.addPages(Component.text("COMMANDS").color(TextColor.fromHexString("#5555FF"))
                    .append(Component.text("\n-------------------").color(TextColor.fromHexString("#AAAAAA")))
                    .append(Component.text("\n/tpa (name), be able to tpa to friends\n/set home (name), to set a home at your base\n/homes, lists all your homes\n/kit color, color codes book\n/trigger as_help, getting armor stand book").color(TextColor.fromHexString("#000000"))));

            bookMeta.addPages(Component.text("/rules, for all rules\n/help, general help\n/warp spawn, warps you to spawn"));

            customBook.setItemMeta(bookMeta);
            player.openBook(customBook);
            player.getInventory().addItem(customBook);
        }
    }
}
