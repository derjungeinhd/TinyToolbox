package de.derjungeinhd.tinytoolbox.commands;

import de.derjungeinhd.tinytoolbox.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class SpecPlusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            if (!player.hasPermission("tinytoolbox.specplus")){
                player.sendMessage(Main.lang.getString("no-perm-for-command") + " (tinytoolbox.specplus).");
                return true;
            }
            if (args.length == 0){
                applySpecPlus(sender, player);
                return true;
            }
        }

        if (args.length == 1) {
            Player target = Bukkit.getServer().getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Main.lang.getString("player-not-found"));
                return true;
            } else {
                if (sender instanceof Player s) {
                    if (!s.hasPermission("tinytoolbox.specplus.others")) {
                        s.sendMessage(Main.lang.getString("not-enough-perms-for-command") + " (tinytoolbox.specplus, tinytoolbox.specplus.others).");
                        return true;
                    }
                    applySpecPlus(sender, target);
                    target.sendMessage(Main.lang.getString("specplus-got-activated") + s.getDisplayName() + ".");
                    return true;
                } else {
                    applySpecPlus(sender, target);
                    target.sendMessage(Main.lang.getString("specplus-activated"));
                    return true;
                }
            }

        }
        return false;
    }

    private void applySpecPlus(CommandSender s, Player p) {
        String entergm = Main.mainConfig.getString("set-gm-when-entering-specplus", "none");
        String exitgm = Main.mainConfig.getString("set-gm-when-exiting-specplus", "none");
        if (p.hasMetadata("specPlusActive")) {
            p.setInvisible(false);
            p.setInvulnerable(false);
            p.setCollidable(true);
            p.setCanPickupItems(true);
            p.removeMetadata("specPlusActive", Main.getInstance());
            switch (exitgm) {
                case "creative":
                    p.setGameMode(GameMode.CREATIVE);
                    break;
                case "survival":
                    p.setGameMode(GameMode.SURVIVAL);
                    break;
                case "adventure":
                    p.setGameMode(GameMode.ADVENTURE);
                    break;
                case "spectator":
                    p.setGameMode(GameMode.SPECTATOR);
                    break;
                default:
                    break;
            }
            s.sendMessage(Main.lang.getString("specplus-deactivated") + p.getDisplayName() + ".");
        } else {
            p.setMetadata("specPlusActive", new FixedMetadataValue(Main.getInstance(), 1));
            p.setInvisible(true);
            p.setInvulnerable(true);
            p.setCollidable(false);
            p.setCanPickupItems(false);
            switch (entergm) {
                case "creative":
                    p.setGameMode(GameMode.CREATIVE);
                    break;
                case "survival":
                    p.setGameMode(GameMode.SURVIVAL);
                    break;
                case "adventure":
                    p.setGameMode(GameMode.ADVENTURE);
                    break;
                case "spectator":
                    p.setGameMode(GameMode.SPECTATOR);
                    break;
                default:
                    break;
            }
            s.sendMessage(Main.lang.getString("specplus-activated") + p.getDisplayName() + ".");
        }
    }

}
