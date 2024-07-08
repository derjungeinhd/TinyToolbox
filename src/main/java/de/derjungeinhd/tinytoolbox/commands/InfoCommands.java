package de.derjungeinhd.tinytoolbox.commands;

import de.derjungeinhd.tinytoolbox.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfoCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "tinytoolbox":
                sender.sendMessage(Main.lang.getString("plugin-info-output"));
                break;
            case "discord":
                sender.sendMessage(Main.mainConfig.getString("discord-output"));
                break;
            case "twitch":
                sender.sendMessage(Main.mainConfig.getString("twitch-output"));
                break;
            case "youtube":
                sender.sendMessage(Main.mainConfig.getString("youtube-output"));
                break;
            case "twitter":
                sender.sendMessage(Main.mainConfig.getString("twitter-output"));
                break;
            case "facebook":
                sender.sendMessage(Main.mainConfig.getString("facebook-output"));
                break;
            case "threads":
                sender.sendMessage(Main.mainConfig.getString("threads-output"));
                break;
            case "tiktok":
                sender.sendMessage(Main.mainConfig.getString("tiktok-output"));
                break;
            case "reddit":
                sender.sendMessage(Main.mainConfig.getString("reddit-output"));
                break;
            case "instagram":
                sender.sendMessage(Main.mainConfig.getString("instagram-output"));
                break;
            default:
                sender.sendMessage(Main.lang.getString("plugin-confusion"));
                break;
        }
        return true;
    }
}
