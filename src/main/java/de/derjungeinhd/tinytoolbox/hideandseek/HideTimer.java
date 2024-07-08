package de.derjungeinhd.tinytoolbox.hideandseek;

import de.derjungeinhd.tinytoolbox.Main;

public class HideTimer {
    private static boolean running;

    private static int time;

    private static int leftTime;

    private static HideTimerRunnable timer = new HideTimerRunnable(60);;

    public HideTimer() {
        running = false;
        time = 60;
        leftTime = time;
    }

    public static boolean isRunning() {
        return running;
    }

    public static int getTime() {
        return time;
    }

    public static void setTime(int t) {
        time = t;
    }

    public static int getLeftTime() {
        return leftTime;
    }

    public static void setLeftTime(int t) {
        leftTime = t;
    }

    public static void cancel() {
        if (running) {
            running = false;
            timer.cancel();
        }
    }

    public static void run(int t) {
        HideAndSeek hnsManager = Main.getHideAndSeekManager();
        running = true;
        time = t;
        leftTime = t;
        hnsManager.getHnsHider().setProgress(1);
        hnsManager.getHnsHider().setTitle(Main.lang.getString("hider-bossbar-title-hidephase"));
        hnsManager.getHnsSeeker().setTitle(Main.lang.getString("seeker-bossbar-title-hidephase"));

        timer = new HideTimerRunnable(time);
        timer.runTaskTimer(Main.getInstance(), 20, 20);
    }
}
