package de.derjungeinhd.tinytoolbox.serverrestart;

import de.derjungeinhd.tinytoolbox.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerRestartTimerRunnable extends BukkitRunnable {

    private int time;

    private int leftTime;

    private ServerRestart srManager;

    public ServerRestartTimerRunnable(int t) {
        this.time = t;
        this.leftTime = t;
        this.srManager = Main.getServerRestartManager();
    }


    @Override
    public void run() {
        this.leftTime -= 1;
        double percentage = (double) this.leftTime / this.time;
        srManager.getServerRestartBossbar().setProgress(percentage);

        if (this.leftTime <= 10) {
            if (this.leftTime <= 0) {
                srManager.restart();
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle("§c§l" + this.leftTime, "", 10, 80, 10);
            }
        }
    }
}
