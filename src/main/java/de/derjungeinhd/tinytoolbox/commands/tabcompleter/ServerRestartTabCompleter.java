package de.derjungeinhd.tinytoolbox.commands.tabcompleter;

import de.derjungeinhd.tinytoolbox.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ServerRestartTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<String>();
        if (args.length == 1) {
            if (sender instanceof Player player && player.hasPermission("tinytoolbox.restartin")) {
                if (Main.getServerRestartManager().getState().equals("none")) {
                    try {
                        list.add(Integer.parseInt(args[0]) + "m");
                        list.add(Integer.parseInt(args[0]) + "s");
                    } catch (NumberFormatException e) {
                        list.add("30s");
                        list.add("1m");
                        list.add("2m");
                        list.add("3m");
                        list.add("5m");
                        list.add("10m");
                    }
                } else if (Main.getServerRestartManager().getState().equals("active")) {
                    list.add("stop");
                }

            }
        }
        return list;
    }
}
