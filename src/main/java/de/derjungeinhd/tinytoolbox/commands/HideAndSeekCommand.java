package de.derjungeinhd.tinytoolbox.commands;


import de.derjungeinhd.tinytoolbox.Main;
import de.derjungeinhd.tinytoolbox.hideandseek.HideAndSeek;
import de.derjungeinhd.tinytoolbox.hideandseek.HideTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HideAndSeekCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        HideAndSeek hnsManager = Main.getHideAndSeekManager();
        if (sender instanceof Player player) {
            if (args.length == 0) {
                return false;
            }
            switch (args[0]) {
                case "join": {
                    if (!player.hasPermission("tinytoolbox.hideandseek.participate")) {
                        sender.sendMessage(Main.lang.getString("no-perm-to-participate"));
                        return true;
                    }
                    if (hnsManager.getState().equals("invite")) {
                        if (!hnsManager.getParticipatingPlayers().contains(player.getName())) {
                            hnsManager.addParticipatingPlayer(player.getName());
                            sender.sendMessage(Main.lang.getString("player-joins-hide-and-seek"));
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendMessage(player.getDisplayName() + Main.lang.getString("player-has-joined-hide-and-seek"));
                            }
                        }
                    } else if (hnsManager.getState().equals("none")) {
                        sender.sendMessage(Main.lang.getString("no-active-invitation"));
                        return true;
                    } else {
                        sender.sendMessage(Main.lang.getString("hide-and-seek-already-active"));
                        return true;
                    }
                    return true;
                }
                case "exit": {
                    if (hnsManager.getParticipatingPlayers().contains(player.getName())) {
                        hnsManager.removeParticipatingPlayer(player.getName());
                        if (hnsManager.getState().equals("seeking") || hnsManager.getState().equals("hiding")) {
                            player.teleport(hnsManager.getStartPos());
                        }
                        for (String pname : hnsManager.getParticipatingPlayers()) {
                            Player p = Bukkit.getPlayerExact(pname);
                            if (p != null) {
                                p.sendMessage(player.getDisplayName() + Main.lang.getString("player-has-exited-hide-and-seek"));
                            }
                        }
                        sender.sendMessage(Main.lang.getString("player-exits-hide-and-seek"));
                        return true;
                    } else {
                        sender.sendMessage(Main.lang.getString("player-not-participating"));
                        return true;
                    }
                }
                case "invite": {
                    if (!player.hasPermission("tinytoolbox.hideandseek.organize")) {
                        sender.sendMessage(Main.lang.getString("no-perm-for-invite"));
                        return true;
                    }
                    if (hnsManager.getState().equals("invite")) {
                        sender.sendMessage(Main.lang.getString("hide-and-seek-already-invited"));
                        return true;
                    } else if (!hnsManager.getState().equals("none")) {
                        sender.sendMessage(Main.lang.getString("hide-and-seek-already-active"));
                        return true;
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p != player && p.hasPermission("tinytoolbox.hideandseek.participate")) {
                            p.sendTitle(Main.lang.getString("hide-and-seek-invite-title"), Main.lang.getString("hide-and-seek-invite-subtitle"), 10, 80, 10);
                            p.sendMessage(Main.lang.getString("hide-and-seek-invite-msg"));
                        }
                    }
                    hnsManager.setState("invite");
                    sender.sendMessage(Main.lang.getString("hide-and-seek-invite-success"));
                    return true;
                }
                case "start": {
                    player.sendMessage(Main.lang.getString("hide-and-seek-starting"));
                    if (!player.hasPermission("tinytoolbox.hideandseek.organize")) {
                        sender.sendMessage(Main.lang.getString("no-perm-for-start"));
                        return true;
                    }
                    if (args.length >= 3 && hnsManager.getState().equals("invite")) {
                        if (hnsManager.getParticipatingPlayers().isEmpty()) {
                            player.sendMessage(Main.lang.getString("not-enough-players-for-hide-and-seek"));
                            return true;
                        }
                        return hnsManager.startGame(player, args);
                    } else if (args.length >= 3 && hnsManager.getState().equals("none")) {
                        player.sendMessage(Main.lang.getString("no-active-invitation-organizer"));
                        return true;
                    } else if (args.length >= 3) {
                        player.sendMessage(Main.lang.getString("hide-and-seek-already-active"));
                        return true;
                    } else {
                        return false;
                    }
                }
                case "stop": {
                    player.sendMessage(Main.lang.getString("hide-and-seek-stopping"));
                    if (!player.hasPermission("tinytoolbox.hideandseek.organize")) {
                        sender.sendMessage(Main.lang.getString("no-perm-for-stop"));
                        return true;
                    }
                    hnsManager.stopGame();
                    player.sendMessage(Main.lang.getString("hide-and-seek-stop-success"));
                    return true;
                }
                case "kick": {
                    if (!player.hasPermission("tinytoolbox.hideandseek.organize")) {
                        sender.sendMessage(Main.lang.getString("no-perm-for-kick"));
                        return true;
                    } else if (hnsManager.getState().equals("none")) {
                        sender.sendMessage(Main.lang.getString("no-active-hide-and-seek"));
                        return true;
                    } else if (args.length < 2) {
                        return false;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        sender.sendMessage(Main.lang.getString("player-not-found"));
                        return true;
                    }
                    if (hnsManager.getParticipatingPlayers().contains(target.getName())) {
                        hnsManager.removeParticipatingPlayer(target.getName());
                        if (hnsManager.getState().equals("seeking") || hnsManager.getState().equals("hiding")) {
                            target.teleport(hnsManager.getStartPos());
                        }
                        sender.sendMessage(target.getDisplayName() + Main.lang.getString("player-was-kicked"));
                        target.sendMessage(Main.lang.getString("player-got-kicked"));
                        return true;
                    } else {
                        sender.sendMessage(target.getDisplayName() + Main.lang.getString("target-not-participating"));
                        return true;
                    }
                }
                case "found": {
                    if (!player.hasPermission("tinytoolbox.hideandseek.participate")) {
                        sender.sendMessage(Main.lang.getString("no-perm-to-participate"));
                        return true;
                    } else if (!hnsManager.getState().equals("seeking")) {
                        sender.sendMessage(Main.lang.getString("player-not-able-to-find-player"));
                        return true;
                    }  else if (!hnsManager.getParticipatingPlayers().contains(player.getName())) {
                        sender.sendMessage(Main.lang.getString("player-not-participating"));
                        return true;
                    }  else if (args.length < 2) {
                        return false;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        sender.sendMessage(Main.lang.getString("player-not-found"));
                        return true;
                    } else if (hnsManager.getFoundHiders().contains(target.getName()))
                    target.sendTitle(Main.lang.getString("player-was-found-title"), Main.lang.getString("player-was-found-subtitle") + player.getDisplayName() + ".", 10, 80, 10);
                    for (String pname : hnsManager.getParticipatingPlayers()) {
                        Player p = Bukkit.getPlayerExact(pname);
                        if (p != null) {
                            p.sendMessage(target.getDisplayName() + Main.lang.getString("player-was-found") + player.getDisplayName() + ".");
                        }
                    }
                    target.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(Main.lang.getString("player-was-found-seeker-msg-1") + target.getDisplayName() + Main.lang.getString("player-was-found-seeker-msg-2"));
                    hnsManager.getFoundHiders().add(target.getName());
                    if (hnsManager.getHiders().size() == hnsManager.getFoundHiders().size()) {
                        hnsManager.endGame(true);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
