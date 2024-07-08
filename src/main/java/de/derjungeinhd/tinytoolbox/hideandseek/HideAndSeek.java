package de.derjungeinhd.tinytoolbox.hideandseek;



import de.derjungeinhd.tinytoolbox.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;

public class HideAndSeek {

    private ArrayList<String> seekers;

    private ArrayList<String> hiders;

    private ArrayList<String> foundHiders;

    private ArrayList<String> participatingPlayers;

    private String state;

    private BossBar hnsInvite;

    private BossBar hnsHider;

    private BossBar hnsSeeker;

    private Location startPos;

    private int hideTime;

    private int seekTime;

    private Scoreboard scoreboard;

    private Team hnsTeam;

    public HideAndSeek() {
        this.seekers = new ArrayList<String>();
        this.hiders = new ArrayList<String>();
        this.foundHiders = new ArrayList<String>();
        this.participatingPlayers = new ArrayList<String>();
        this.state = "none";
        this.hnsInvite = Bukkit.createBossBar("Hide & Seek", BarColor.YELLOW, BarStyle.SEGMENTED_20);
        this.hnsHider = Bukkit.createBossBar("Hider", BarColor.GREEN, BarStyle.SEGMENTED_20);
        this.hnsSeeker = Bukkit.createBossBar("Seeker", BarColor.RED, BarStyle.SEGMENTED_20);
        this.hideTime = 60;
        this.seekTime = 60;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.hnsTeam = this.scoreboard.registerNewTeam("HideAndSeek");
        this.hnsTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    public ArrayList<String> getSeekers() {
        return this.seekers;
    }

    public ArrayList<String> getHiders() {
        return this.hiders;
    }

    public ArrayList<String> getFoundHiders() {
        return this.foundHiders;
    }

    public ArrayList<String> getParticipatingPlayers() {
        return this.participatingPlayers;
    }

    public void setParticipatingPlayers(ArrayList<String> newParticipatingPlayers) {
        this.participatingPlayers = newParticipatingPlayers;
    }

    public void addParticipatingPlayer(String participatingPlayer) {
        Player p = Bukkit.getPlayerExact(participatingPlayer);
        if (p != null) {
            this.hnsInvite.addPlayer(p);
            this.participatingPlayers.add(participatingPlayer);
            p.setScoreboard(scoreboard);
            this.hnsTeam.addEntry(participatingPlayer);
        }
    }

    public void removeParticipatingPlayer(String participatingPlayer) {
        Player p = Bukkit.getPlayerExact(participatingPlayer);
        if (p != null) {
            this.hnsInvite.removePlayer(p);
            this.hnsHider.removePlayer(p);
            this.hnsSeeker.removePlayer(p);
        }
        this.hnsTeam.removeEntry(participatingPlayer);
        p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        this.participatingPlayers.remove(participatingPlayer);
        this.seekers.remove(participatingPlayer);
        this.hiders.remove(participatingPlayer);
        this.foundHiders.remove(participatingPlayer);
        if (this.participatingPlayers.isEmpty()) {
            if (this.state.equals("hiding") || this.state.equals("seeking")) {
                this.stopGame();
                Main.logger.log(Level.WARNING, Main.lang.getString("no-participants-error"));
            }
        } else if (this.seekers.isEmpty()) {
            if (this.state.equals("hiding") || this.state.equals("seeking")) {
                this.endGame(false);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Main.lang.getString("no-seekers-anymore"));
                }
            }
        } else if (this.hiders.size() == this.foundHiders.size() || this.hiders.isEmpty()) {
            if (this.state.equals("hiding") || this.state.equals("seeking")) {
                this.endGame(true);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Main.lang.getString("no-hiders-anymore"));
                }
            }
        }
    }

    public String getState() {
        return this.state;
    }

    public void setState(String newState) {
        this.state = newState;
    }

    public BossBar getHnsInvite() {
        return this.hnsInvite;
    }

    public void setHnsInvite(BossBar newHnsInvite) {
        this.hnsInvite = newHnsInvite;
    }

    public BossBar getHnsHider() {
        return this.hnsHider;
    }

    public void setHnsHider(BossBar newHnsHider) {
        this.hnsHider = newHnsHider;
    }

    public BossBar getHnsSeeker() {
        return this.hnsSeeker;
    }

    public void setHnsSeeker(BossBar newHnsSeeker) {
        this.hnsSeeker = newHnsSeeker;
    }

    public Location getStartPos() {
        return this.startPos;
    }

    public void setStartPos(Location startPos) {
        startPos.setYaw(0);
        this.startPos = startPos;
    }

    public int getHideTime() {
        return this.hideTime;
    }

    public void setHideTime(int ht) {
        hideTime = ht;
    }

    public int getSeekTime() {
        return seekTime;
    }

    public void setSeekTime(int st) {
        seekTime = st;
    }

    public Team getHnsTeam() {
        return hnsTeam;
    }

    public void rollSeekers(int count) {
        ArrayList<String> rollablePlayers = new ArrayList<String>(this.participatingPlayers);
        Collections.shuffle(rollablePlayers);
        for (int i = 0; i < count; i++) {
            Player p = Bukkit.getPlayerExact(rollablePlayers.get(i));
            if (p != null) {
                this.hnsSeeker.addPlayer(p);
                this.hnsHider.removePlayer(p);
                this.seekers.add(p.getName());
                this.hiders.remove(p.getName());
            }
        }
    }

    public boolean startGame(Player player, String[] args) {
        try {
            int ht = Integer.parseInt(args[1].substring(0,args[1].length()-1));
            char timeformat = args[1].charAt(args[1].length()-1);
            if (timeformat == 'm') {
                ht *= 60;
            }
            this.hideTime = ht;
        } catch (Exception e) {
            player.sendMessage(Main.lang.getString("wrong-hidetime-error"));
            return true;
        }
        try {
            int st = Integer.parseInt(args[2].substring(0,args[2].length()-1));
            char timeformat = args[2].charAt(args[2].length()-1);
            if (timeformat == 'm') {
                st *= 60;
            }
            this.seekTime = st;
        } catch (Exception e) {
            player.sendMessage(Main.lang.getString("wrong-seektime-error"));
            return true;
        }
        if (args.length == 4) {
            try {
                int seekerCount = Integer.parseInt(args[3]);
                if (seekerCount >= this.participatingPlayers.size()) {
                    player.sendMessage(Main.lang.getString("too-many-seekers-error:"));
                    return true;
                }
                rollSeekers(seekerCount);
            } catch (NumberFormatException e) {
                player.sendMessage(Main.lang.getString("seeker-count-not-an-int"));
                return true;
            }
        } else {
            rollSeekers(1);
        }
        this.state = "hiding";
        this.setStartPos(player.getLocation());
        this.hnsInvite.removeAll();
        for (String pname : this.participatingPlayers) {
            Player p = Bukkit.getPlayerExact(pname);
            if (p != null) {
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(this.startPos);
                this.hnsHider.addPlayer(p);
                this.hiders.add(p.getName());
                if (this.seekers.contains(p.getName())) {
                    this.hnsHider.removePlayer(p);
                    this.hiders.remove(p.getName());
                    p.sendTitle("§c§lSeeker", Main.lang.getString("seeker-start-subtitle"), 10, 80, 10);
                    p.sendMessage( Main.lang.getString("seeker-explanation-message"));
                } else {
                    p.sendTitle("§a§lHider", Main.lang.getString("hider-start-subtitle"), 10, 80, 10);
                }
            }
        }
        HideTimer.run(this.hideTime);
        player.sendMessage(Main.lang.getString("hide-and-seek-start-success"));
        return true;
    }

    public void endHidePhase() {
        HideTimer.cancel();
        this.state = "seeking";

        for (String pname : this.hiders) {
            Player p = Bukkit.getPlayerExact(pname);
            if (p != null) {
                p.sendTitle(Main.lang.getString("seeker-freed-title-hide:"), Main.lang.getString("seeker-freed-subtitle-hider"), 10, 80, 10);
                p.sendMessage(Main.lang.getString("seeker-freed-message-hider"));
            }
        }

        for (String pname : this.seekers) {
            Player p = Bukkit.getPlayerExact(pname);
            if (p != null) {
                p.teleport(this.startPos);
                p.sendTitle(Main.lang.getString("seeker-freed-title-seeker"), Main.lang.getString("seeker-freed-subtitle-seeker"), 10, 80, 10);
            }
        }
        SeekTimer.run(this.seekTime);
    }

    public void stopGame() {
        for (String pname : this.participatingPlayers) {
            Player p = Bukkit.getPlayerExact(pname);
            if (p != null) {
                p.setGameMode(GameMode.SURVIVAL);
                this.hnsTeam.removeEntry(p.getName());
                p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }
        }
        this.hnsInvite.removeAll();
        this.hnsHider.removeAll();
        this.hnsSeeker.removeAll();
        this.seekers.clear();
        this.hiders.clear();
        this.foundHiders.clear();
        this.participatingPlayers.clear();
        this.hnsHider.removeAll();
        this.hnsSeeker.removeAll();
        this.hnsInvite.removeAll();
        if (this.state.equals("hiding")) {
            HideTimer.cancel();
        } if (this.state.equals("seeking")) {
            SeekTimer.cancel();
        }
        setState("none");
    }

    public void endGame(boolean allHidersFound) {
        SeekTimer.cancel();
        if (allHidersFound) {
            for (String pname : this.participatingPlayers) {
                Player p = Bukkit.getPlayerExact(pname);
                if (p != null) {
                    p.teleport(this.startPos);
                    p.sendTitle(Main.lang.getString("hide-and-seek-end-title"), Main.lang.getString("seekers-won-subtitle"), 10, 80, 10);
                }
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(Main.lang.getString("seekers-won-message"));
            }
        } else {
            for (String pname : this.participatingPlayers) {
                Player p = Bukkit.getPlayerExact(pname);
                if (p != null) {
                    p.teleport(this.startPos);
                    p.sendTitle(Main.lang.getString("hide-and-seek-end-title"), Main.lang.getString("hiders-won-subtitle"), 10, 80, 10);
                }
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(Main.lang.getString("hiders-won-message"));
            }
        }

        stopGame();
    }
}
