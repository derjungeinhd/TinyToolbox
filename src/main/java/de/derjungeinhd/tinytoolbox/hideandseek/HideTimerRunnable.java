package de.derjungeinhd.tinytoolbox.hideandseek;

import de.derjungeinhd.tinytoolbox.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


public class HideTimerRunnable extends BukkitRunnable {

    private int time;

    private int leftTime;

    private HideAndSeek hnsManager;

    public HideTimerRunnable(int t) {
        this.time = t;
        this.leftTime = t;
        this.hnsManager = Main.getHideAndSeekManager();
    }


    @Override
    public void run() {
        this.leftTime -= 1;
        double percentage = (double) this.leftTime / this.time;
        this.hnsManager.getHnsHider().setProgress(percentage);
        this.hnsManager.getHnsSeeker().setProgress(percentage);
        for (String pname : this.hnsManager.getHiders()) {
            Player p = Bukkit.getPlayerExact(pname);
            if (p != null) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 255, false, false));
            }
        }
        for (String pname : this.hnsManager.getSeekers()) {
            Player p = Bukkit.getPlayerExact(pname);
            if (p != null) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 255, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 255, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 255, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40, 0, false, false));

            }
        }
        if (this.leftTime <= 0) {
            this.hnsManager.endHidePhase();
        }
    }
}
