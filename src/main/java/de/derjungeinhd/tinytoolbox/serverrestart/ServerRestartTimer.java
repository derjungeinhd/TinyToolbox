package de.derjungeinhd.tinytoolbox.serverrestart;

import de.derjungeinhd.tinytoolbox.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ServerRestartTimer {

    private static boolean running;

    private static int time;

    private static int leftTime;

    private static ServerRestartTimerRunnable timer;

    public ServerRestartTimer() {
        running = false;
        time = 60;
        leftTime = time;
        timer = new ServerRestartTimerRunnable(60);
    }

    public static boolean isRunning() {
        return running;
    }

    public static int getTime() {
        return time;
    }

    public static int getLeftTime() {
        return leftTime;
    }

    public static void cancel() {
        if (running) {
            timer.cancel();
            running = false;
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("§7" + Main.lang.getString("server-restart-abandoned"));
            }
            Main.logger.log(Level.INFO, Main.lang.getString("server-restart-abandoned"));
        }
    }

    public static void run(int t) {
        ServerRestart srManager = Main.getServerRestartManager();
        running = true;
        time = t;
        leftTime = t;
        srManager.getServerRestartBossbar().setProgress(1);
        srManager.setState("active");

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(Main.lang.getString("restart-announcement-title"), Main.lang.getString("restart-announcement-subtitle"), 10, 80, 10);
        }

        timer = new ServerRestartTimerRunnable(time);
        timer.runTaskTimer(Main.getInstance(), 20, 20);
    }
}
