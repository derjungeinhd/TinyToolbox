package de.derjungeinhd.tinytoolbox.serverrestart;

import de.derjungeinhd.tinytoolbox.Main;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ServerRestart {

    private int time;

    private BossBar srBossbar;

    private String state;

    public ServerRestart() {
        this.time = 0;
        this.srBossbar = Bukkit.createBossBar(Main.lang.getString("restart-approaching"), BarColor.RED, BarStyle.SEGMENTED_20);
        this.state = "none";
    }

    public void cancelRestart() {
        this.state = "none";
        this.srBossbar.removeAll();
        ServerRestartTimer.cancel();
    }

    public void restart() {
        this.state = "none";
        this.srBossbar.removeAll();
        ServerRestartTimer.cancel();
        Main.logger.log(Level.INFO, Main.lang.getString("plugin-trigger-server-restart"));
        Bukkit.getServer().spigot().restart();
    }

    public BossBar getServerRestartBossbar() {
        return srBossbar;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
