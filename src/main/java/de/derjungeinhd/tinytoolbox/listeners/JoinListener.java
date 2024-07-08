package de.derjungeinhd.tinytoolbox.listeners;

import de.derjungeinhd.tinytoolbox.Main;
import de.derjungeinhd.tinytoolbox.serverrestart.ServerRestart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("leftWhileHns")) {
            player.removeMetadata("leftWhileHns", Main.getInstance());
            player.sendMessage(Main.lang.getString("left-while-hide-and-seek"));
        }

        ServerRestart srManager = Main.getServerRestartManager();
        if (srManager.getState().equals("active")) {
            srManager.getServerRestartBossbar().addPlayer(player);
        }
    }
}
