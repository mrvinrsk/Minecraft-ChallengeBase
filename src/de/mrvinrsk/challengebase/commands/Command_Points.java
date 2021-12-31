package de.mrvinrsk.challengebase.commands;

import de.mrvinrsk.challengebase.main.ChallengeBase;
import de.mrvinrsk.challengebase.util.Gameplay;
import de.mrvinrsk.challengebase.util.GameplayMessageType;
import de.mrvinrsk.challengebase.util.PointManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Command_Points implements CommandExecutor {

    private Gameplay gameplay = Gameplay.getInstance();

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        PointManager pm = PointManager.getInstance(UUID.randomUUID(), ChallengeBase.getInstance());

        if (args.length == 1) {
            if (cs instanceof Player) {
                Player p = (Player) cs;
                Plugin plugin = Bukkit.getPluginManager().getPlugin(args[0]);

                if (plugin != null) {
                    pm.setPlugin(plugin);
                    pm.changeUUID(p.getUniqueId());
                    int points = pm.getPoints();

                    gameplay.sendMessage(p, GameplayMessageType.SYSTEM, "Du hast in der Challenge §e" + plugin.getName() + " §a" + points + " " + (points == 1 ? "Punkt" : "Punkte") + "§r.");
                } else {
                    gameplay.sendMessage(p, GameplayMessageType.SYSTEM, "§cDie angegebene Challenge existiert nicht!");
                }
            } else {
                cs.sendMessage("§cDas geht nur InGame!");
            }
        } else if (args.length == 2) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(args[0]);

            if (plugin != null) {
                OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);

                pm.setPlugin(plugin);
                pm.changeUUID(op.getUniqueId());
                int points = pm.getPoints();

                if (cs instanceof Player) {
                    Player p = (Player) cs;
                    gameplay.sendMessage(p, GameplayMessageType.SYSTEM, "§e" + op.getName() + " §rhat in der Challenge §e" + plugin.getName() + " §a" + points + " " + (points == 1 ? "Punkt" : "Punkte") + "§r.");
                }else {
                    Bukkit.getConsoleSender().sendMessage("§e" + op.getName() + " §7hat in der Challenge §e" + plugin.getName() + " §a" + points + " " + (points == 1 ? "Punkt" : "Punkte") + "§7.");
                }
            } else {
                if (cs instanceof Player) {
                    Player p = (Player) cs;
                    gameplay.sendMessage(p, GameplayMessageType.SYSTEM, "§cDie angegebene Challenge existiert nicht!");
                }else {
                    Bukkit.getConsoleSender().sendMessage("§cDie angegebene Challenge existiert nicht!");
                }
            }
        }else {
            cs.sendMessage("§f/" + label + " <Challenge> [Spieler]");
        }
        return true;
    }

}
