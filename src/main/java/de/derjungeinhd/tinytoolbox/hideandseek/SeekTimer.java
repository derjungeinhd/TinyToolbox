package de.derjungeinhd.tinytoolbox.hideandseek;

import de.derjungeinhd.tinytoolbox.Main;

public class SeekTimer {
    private static boolean running;

    private static int time;

    private static int leftTime;

    private static SeekTimerRunnable timer = new SeekTimerRunnable(60);

    public SeekTimer() {
        running = false;
        time = 0;
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
        hnsManager.getHnsHider().setTitle(Main.lang.getString("hider-bossbar-title-searchphase"));
        hnsManager.getHnsSeeker().setTitle(Main.lang.getString("seeker-bossbar-title-searchphase"));

        timer = new SeekTimerRunnable(time);
        timer.runTaskTimer(Main.getInstance(), 20, 20);
    }
}
