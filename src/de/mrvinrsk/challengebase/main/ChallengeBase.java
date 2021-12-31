package de.mrvinrsk.challengebase.main;

import de.chatvergehen.spigotapi.util.filemanaging.FolderBuilder;
import de.mrvinrsk.challengebase.commands.Command_Challenge;
import de.mrvinrsk.challengebase.commands.Command_Event;
import de.mrvinrsk.challengebase.commands.Command_Leaderboard;
import de.mrvinrsk.challengebase.commands.Command_Points;
import de.mrvinrsk.challengebase.listeners.Listener_EventTrigger;
import de.mrvinrsk.challengebase.listeners.Listener_ServerLoad;
import de.mrvinrsk.challengebase.util.Gameplay;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChallengeBase extends JavaPlugin {

    private static ChallengeBase instance;

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
        getCommand("points").setExecutor(new Command_Points());
        getCommand("leaderboard").setExecutor(new Command_Leaderboard());

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Listener_ServerLoad(), this);
        pm.registerEvents(new Listener_EventTrigger(), this);
        pm.registerEvents(new Command_Event(), this);
        pm.registerEvents(new Gameplay(), this);
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
