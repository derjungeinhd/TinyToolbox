package de.derjungeinhd.tinytoolbox.commands.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpecPlusTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("tinytoolbox.specplus") && player.hasPermission("tinytoolbox.specplus.others")) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            list.add(p.getName());
                        }
                }
            }
        } else if (args.length == 1) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    list.add(p.getName());
                }
        }
        return list;
    }
}
