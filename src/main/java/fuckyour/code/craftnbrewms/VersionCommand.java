package fuckyour.code.craftnbrewms;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VersionCommand implements CommandExecutor {
    private final CraftNBrewMS plugin;

    public VersionCommand(CraftNBrewMS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s,  String[] strings) {
        commandSender.sendMessage(ChatColor.RED + "Version: " + plugin.getDescription().getVersion());
        return true;
    }
}
