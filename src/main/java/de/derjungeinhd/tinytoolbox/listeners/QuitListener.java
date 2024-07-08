package de.derjungeinhd.tinytoolbox.listeners;

import de.derjungeinhd.tinytoolbox.Main;
import de.derjungeinhd.tinytoolbox.hideandseek.HideAndSeek;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        HideAndSeek hnsManager = Main.getHideAndSeekManager();

        if (hnsManager != null) {
            if (!hnsManager.getState().equals("none") && hnsManager.getParticipatingPlayers().contains(player.getName())) {
                hnsManager.removeParticipatingPlayer(player.getName());
                if (hnsManager.getState().equals("seeking") || hnsManager.getState().equals("hiding")) {
                    player.teleport(hnsManager.getStartPos());
                }
                player.setMetadata("leftWhileHns", new FixedMetadataValue(Main.getInstance(), 1));
            }
        }
    }
}
