package de.derjungeinhd.tinytoolbox.commands;

import de.derjungeinhd.tinytoolbox.Main;
import de.derjungeinhd.tinytoolbox.serverrestart.ServerRestart;
import de.derjungeinhd.tinytoolbox.serverrestart.ServerRestartTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRestartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            ServerRestart srManager = Main.getServerRestartManager();
            if (args.length == 1) {
                if (args[0].equals("stop")) {
                    if (srManager.getState().equals("none")) {
                        sender.sendMessage(Main.lang.getString("restart-timer-not-active"));
                        return true;
                    }
                    srManager.cancelRestart();
                    return true;
                } else {
                    if (ServerRestartTimer.isRunning()) {
                        player.sendMessage(Main.lang.getString("restart-timer-already-started"));
                    }
                    try {
                        int srt = Integer.parseInt(args[0].substring(0,args[0].length()-1));
                        char timeformat = args[0].charAt(args[0].length()-1);
                        String msgForPlayers;
                        String msgForConsole;

                        if (timeformat == 'm') {
                            msgForPlayers = "§c" + Main.lang.getString("restart-scheduled-in") + "§6" + srt + "§c" + Main.lang.getString("restart-scheduled-in-min");
                            msgForConsole = Main.lang.getString("restart-scheduled-in") + srt + Main.lang.getString("restart-scheduled-in-min");
                            srt *= 60;
                        } else {
                            msgForPlayers = "§c" + Main.lang.getString("restart-scheduled-in") + "§6" + srt + "§c" + Main.lang.getString("restart-scheduled-in-sec");
                            msgForConsole = Main.lang.getString("restart-scheduled-in") + srt + Main.lang.getString("restart-scheduled-in-sec");
                        }
                        Main.logger.log(Level.INFO, msgForConsole);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            srManager.getServerRestartBossbar().addPlayer(p);
                            p.sendMessage(msgForPlayers);
                        }
                        ServerRestartTimer.run(srt);
                    } catch (Exception e) {
                        player.sendMessage(Main.lang.getString("wrong-format-time-till-restart-error"));
                        return true;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
