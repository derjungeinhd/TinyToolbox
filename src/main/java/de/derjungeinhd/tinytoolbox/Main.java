package de.derjungeinhd.tinytoolbox;

import de.derjungeinhd.tinytoolbox.commands.*;
import de.derjungeinhd.tinytoolbox.commands.tabcompleter.EmptyTabCompleter;
import de.derjungeinhd.tinytoolbox.commands.tabcompleter.HideAndSeekTabCompleter;
import de.derjungeinhd.tinytoolbox.commands.tabcompleter.ServerRestartTabCompleter;
import de.derjungeinhd.tinytoolbox.commands.tabcompleter.SpecPlusTabCompleter;
import de.derjungeinhd.tinytoolbox.hideandseek.HideAndSeek;
import de.derjungeinhd.tinytoolbox.listeners.JoinListener;
import de.derjungeinhd.tinytoolbox.listeners.QuitListener;
import de.derjungeinhd.tinytoolbox.serverrestart.ServerRestart;
import de.derjungeinhd.tinytoolbox.serverrestart.ServerRestartTimer;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static Main instance;

    private static HideAndSeek hideAndSeekManager;

    private static ServerRestart srManager;

    public static FileConfiguration mainConfig;

    private static int mainConfigVersion;

    public static Logger logger;

    private static File langFile;

    public static FileConfiguration lang;

    private static int langVersion;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        logger = getLogger();
        mainConfigVersion = 1;
        langVersion = 1;
        loadMainConfig();
        setupLang();

        hideAndSeekManager = new HideAndSeek();
        srManager = new ServerRestart();

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new JoinListener(), this);
        manager.registerEvents(new QuitListener(), this);

        getCommand("specplus").setExecutor(new SpecPlusCommand());
        getCommand("specplus").setTabCompleter(new SpecPlusTabCompleter());

        InfoCommands ic = new InfoCommands();
        getCommand("tinytoolbox").setExecutor(ic);

        getCommand("discord").setExecutor(ic);
        getCommand("twitch").setExecutor(ic);
        getCommand("youtube").setExecutor(ic);
        getCommand("twitter").setExecutor(ic);
        getCommand("facebook").setExecutor(ic);
        getCommand("threads").setExecutor(ic);
        getCommand("tiktok").setExecutor(ic);
        getCommand("reddit").setExecutor(ic);
        getCommand("instagram").setExecutor(ic);

        FreezeCommands fc = new FreezeCommands();
        getCommand("freeze").setExecutor(fc);
        getCommand("unfreeze").setExecutor(fc);

        EmptyTabCompleter etc = new EmptyTabCompleter();
        getCommand("tinytoolbox").setTabCompleter(etc);
        getCommand("discord").setTabCompleter(etc);
        getCommand("twitch").setTabCompleter(etc);
        getCommand("youtube").setTabCompleter(etc);
        getCommand("twitter").setTabCompleter(etc);
        getCommand("facebook").setTabCompleter(etc);
        getCommand("threads").setTabCompleter(etc);
        getCommand("tiktok").setTabCompleter(etc);
        getCommand("reddit").setTabCompleter(etc);
        getCommand("instagram").setTabCompleter(etc);

        getCommand("freeze").setTabCompleter(etc);
        getCommand("unfreeze").setTabCompleter(etc);

        getCommand("hideandseek").setExecutor(new HideAndSeekCommand());
        getCommand("hideandseek").setTabCompleter(new HideAndSeekTabCompleter());

        getCommand("restartin").setExecutor(new ServerRestartCommand());
        getCommand("restartin").setTabCompleter(new ServerRestartTabCompleter());

        logger.log(Level.INFO, lang.getString("plugin-enabled"));
    }

    @Override
    public void onDisable() {
        hideAndSeekManager.stopGame();
        hideAndSeekManager.getHnsTeam().unregister();
        srManager.getServerRestartBossbar().removeAll();
        ServerRestartTimer.cancel();
        logger.log(Level.INFO, lang.getString("plugin-disabled"));
    }
    private void loadMainConfig() {
        saveDefaultConfig();
        mainConfig = getConfig();
        if (mainConfig.getInt("config-ver") != mainConfigVersion) {
            logger.log(Level.WARNING, "MainConfig outdated or wrong configured, reloading config from resources...");
            saveResource("config.yml", true);
        }
        mainConfig = getConfig();
    }

    private void setupLang(){
        String l = mainConfig.getString("language", "en");
        File f = getDataFolder();
        File langFolder = new File(f, "lang");
        if (!langFolder.exists()) {
            try {
                if (!langFolder.mkdirs()) throw new IOException("Could not create directory");
                saveResource("lang/de.yml", false);
                saveResource("lang/en.yml", false);
                logger.log(Level.INFO, "Language Packs could not be found, reloading Packs from resources...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!langFolder.isDirectory()) {
            try {
                FileUtils.forceDelete(langFolder);
                if (!langFolder.mkdirs()) throw new IOException("Could not create directory");
                saveResource("lang/de.yml", false);
                saveResource("lang/en.yml", false);
                logger.log(Level.INFO, "Language Packs could not be found, reloading Packs from resources...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        langFile = new File(getDataFolder()+"/lang/"+ l + ".yml");
        lang = YamlConfiguration.loadConfiguration(langFile);
        if (lang.getInt("lang-ver") != langVersion) {
            logger.log(Level.WARNING, "Language Packs outdated or wrong configured, reloading Packs from resources...");
            saveResource("lang/de.yml", true);
            saveResource("lang/en.yml", true);
        }
        langFile = new File(getDataFolder()+"/lang/"+ l + ".yml");
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public static Main getInstance() {
        return instance;
    }

    public static HideAndSeek getHideAndSeekManager() {
        return hideAndSeekManager;
    }

    public static ServerRestart getServerRestartManager() {
        return srManager;
    }
}
