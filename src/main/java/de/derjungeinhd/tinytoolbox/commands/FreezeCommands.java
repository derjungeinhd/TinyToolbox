package de.derjungeinhd.tinytoolbox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FreezeCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName();
        switch (cmd) {
            case "freeze":
                Bukkit.getServer().dispatchCommand(sender, "tick freeze");
                break;
            case "unfreeze":
                Bukkit.getServer().dispatchCommand(sender, "tick unfreeze");
                break;
        }
        return true;
    }
}
