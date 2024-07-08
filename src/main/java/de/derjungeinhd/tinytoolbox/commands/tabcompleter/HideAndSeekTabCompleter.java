package de.derjungeinhd.tinytoolbox.commands.tabcompleter;

import de.derjungeinhd.tinytoolbox.Main;
import de.derjungeinhd.tinytoolbox.hideandseek.HideAndSeek;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HideAndSeekTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<String>();
        HideAndSeek hnsManager = Main.getHideAndSeekManager();
        ArrayList<String> fh = hnsManager.getFoundHiders();
        ArrayList<String> h = hnsManager.getHiders();
        ArrayList<String> s = hnsManager.getSeekers();

        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("tinytoolbox.hideandseek.participate")) {
                    list.add("join");
                    if (hnsManager.getSeekers().contains(player.getName())) {
                        list.add("found");
                    }
                }
                list.add("exit");
                if (player.hasPermission("tinytoolbox.hideandseek.organize")) {
                    list.add("kick");
                    list.add("invite");
                    list.add("start");
                    if (!hnsManager.getState().equals("none")) {
                        list.add("stop");
                    }
                }

            } else if (args.length == 2) {
                if (args[0].equals("found") && s.contains(player.getName())) {
                    for (String pname : h) {
                        Player p = Bukkit.getPlayerExact(pname);
                        if (p != null && !fh.contains(pname)) {
                            list.add(pname);
                        }
                    }
                } else if (args[0].equals("kick") && player.hasPermission("tinytoolbox.hideandseek.organize")) {
                    for (String pname : hnsManager.getParticipatingPlayers()) {
                        Player p = Bukkit.getPlayerExact(pname);
                        if (p != null) {
                            list.add(pname);
                        }
                    }
                } else if (args[0].equals("start")) {
                    if (player.hasPermission("tinytoolbox.hideandseek.organize")) {
                        try {
                            list.add(Integer.parseInt(args[1]) + "m");
                            list.add(Integer.parseInt(args[1]) + "s");
                        } catch (NumberFormatException e) {
                            list.add("<time to hide>");
                        }
                    }
                }
            } else if (args.length == 3) {
                if (player.hasPermission("tinytoolbox.hideandseek.organize") && args[0].equals("start")) {
                    try {
                        list.add(Integer.parseInt(args[2]) + "m");
                        list.add(Integer.parseInt(args[2]) + "s");
                    } catch (NumberFormatException e) {
                        list.add("<time to play>");
                    }
                } else if (args[0].equals("found") && s.contains(player.getName())) {
                    for (String pname : h) {
                        Player p = Bukkit.getPlayerExact(pname);
                        if (p != null) {
                            list.add(pname);
                        }
                    }
                }
            } else if (args.length == 4 && args[0].equals("start")) {
                if (player.hasPermission("tinytoolbox.hideandseek.organize")) {
                    list.add("[<seeker count>]");
                }
            }
        }
        return list;
    }
}
