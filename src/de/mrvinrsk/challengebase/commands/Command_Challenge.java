package de.mrvinrsk.challengebase.commands;

import de.mrvinrsk.challengebase.main.ChallengeBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class Command_Challenge implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        PluginManager pm = Bukkit.getPluginManager();
        List<Plugin> plugins = ChallengeBase.getInstance().getChallengePlugins();

        if (cs.isOp()) {
            if (args.length == 1) {
                Plugin plugin = pm.getPlugin(args[0]);

                if (plugin != null) {
                    if (plugins.contains(plugin)) {
                        if (!plugin.isEnabled()) {
                            pm.enablePlugin(plugin);
                            Bukkit.broadcastMessage("§aDie Challenge '§2" + plugin.getName() + "§a' wurde gestartet!");
                        } else {
                            pm.disablePlugin(plugin);
                            Bukkit.broadcastMessage("§cDie Challenge '§e" + plugin.getName() + "§c' wurde gestoppt!");
                        }
                    } else {
                        cs.sendMessage("§cDas angegebene Plugin ist kein Challenge-Plugin!");
                    }
                } else {
                    cs.sendMessage("§cDas Plugin '§e" + args[0] + "§c' konnte nicht gefunden werden!");
                }
            } else {
                cs.sendMessage("Du musst ein Plugin angeben:");

                for (Plugin plugin : plugins) {
                    cs.sendMessage("§7- §" + (plugin.isEnabled() ? "a" : "c") + plugin.getDescription().getName());
                }
            }
        }
        return true;
    }

}
