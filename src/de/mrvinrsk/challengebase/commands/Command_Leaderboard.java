package de.mrvinrsk.challengebase.commands;

import de.mrvinrsk.challengebase.util.PointManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Command_Leaderboard implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(args[0]);

            if (plugin != null) {
                HashMap<UUID, Integer> points = PointManager.getAll(plugin);

                if (!points.isEmpty()) {
                    Object[] a = points.entrySet().toArray();
                    Arrays.sort(a, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            return ((Map.Entry<UUID, Integer>) o2).getValue()
                                    .compareTo(((Map.Entry<UUID, Integer>) o1).getValue());
                        }
                    });

                    if (a.length >= 2) {
                        cs.sendMessage("§fLeaderboard der Challenge §a" + plugin.getName());
                        cs.sendMessage("");

                        int i = 1;
                        for (Object e : a) {
                            if (i <= 10) {
                                OfflinePlayer user = Bukkit.getOfflinePlayer(((Map.Entry<UUID, Integer>) e).getKey());
                                int punkte = ((Map.Entry<UUID, Integer>) e).getValue();

                                cs.sendMessage("§ePlatz §6" + i + "§7: §f" + user.getName() + " §7mit §a" + punkte + " " + (punkte == 1 ? "Punkt" : "Punkten"));
                                i++;
                            } else {
                                break;
                            }
                        }
                    }else {
                        cs.sendMessage("§cEs gibt bisher nur einen Wert!");
                    }
                }else {
                    cs.sendMessage("§cEs gibt aktuell noch keine Werte!");
                }
            } else {
                cs.sendMessage("§cDie angegebene Challenge existiert nicht!");
            }
        } else {
            cs.sendMessage("§f/" + label + " <Challenge>");
        }
        return true;
    }

}
