package de.mrvinrsk.challengebase.main;

import de.chatvergehen.spigotapi.util.filemanaging.FolderBuilder;
import de.mrvinrsk.challengebase.commands.*;
import de.mrvinrsk.challengebase.listeners.Listener_Death;
import de.mrvinrsk.challengebase.listeners.Listener_EventTrigger;
import de.mrvinrsk.challengebase.listeners.Listener_Join;
import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.Gameplay;
import de.mrvinrsk.challengebase.util.Goal;
import de.mrvinrsk.challengebase.util.GoalManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChallengeBase extends JavaPlugin {

    private static ChallengeBase instance;
    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    private Gameplay gameplay = Gameplay.getInstance();
    private GoalManager goalManager = GoalManager.getInstance();

    public static ChallengeBase getInstance() {
        return instance;
    }

    public static FolderBuilder getPluginFolder(Plugin plugin) {
        return new FolderBuilder("plugins//" + plugin.getDataFolder().getName());
    }

    public static FolderBuilder getUserFolder(Plugin plugin, UUID uuid) {
        return new FolderBuilder(getPluginFolder(plugin).getPath() + "//Users//" + uuid.toString());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        getCommand("challenge").setExecutor(new Command_Challenge());
        getCommand("event").setExecutor(new Command_Event());
        getCommand("goal").setExecutor(new Command_Goal());
        getCommand("points").setExecutor(new Command_Points());
        getCommand("leaderboard").setExecutor(new Command_Leaderboard());
        getCommand("vanish").setExecutor(new Command_Vanish());

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new Listener_EventTrigger(), this);
        pm.registerEvents(new Listener_Join(), this);
        pm.registerEvents(new Command_Event(), this);
        pm.registerEvents(new Command_Goal(), this);
        pm.registerEvents(new Listener_Death(), this);
        pm.registerEvents(new Gameplay(), this);


        new BukkitRunnable() {
            @Override
            public void run() {
                for (Plugin plugin : getChallengePlugins()) {
                    if (goalManager.getPluginGoals(plugin) == null || goalManager.getPluginGoals(plugin).isEmpty()) {
                        getLogger().info(plugin.getName() + " registriert keine Ziele!");
                    } else {
                        getLogger().info(plugin.getName() + " registriert " + goalManager.getPluginGoals(plugin).size() + " Ziel" + (goalManager.getPluginGoals(plugin).size() != 1 ? "e" : "") + "!");
                    }
                }
            }
        }.runTaskLater(this, 25L);


        // Scheduler
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    Listener_Join.updateBoard(all);
                }
            }
        }.runTaskTimer(this, 0, 3);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public List<Plugin> getChallengePlugins() {
        List<Plugin> plugins = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getDescription().getDepend().contains(getDescription().getName())) {
                plugins.add(plugin);
            }
        }
        return plugins;
    }

}
