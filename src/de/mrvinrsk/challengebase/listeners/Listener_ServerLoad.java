package de.mrvinrsk.challengebase.listeners;

import de.mrvinrsk.challengebase.main.ChallengeBase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

public class Listener_ServerLoad implements Listener {

    @EventHandler
    public void onLoad(ServerLoadEvent e) {
        for(Plugin pl : ChallengeBase.getInstance().getChallengePlugins()) {
            //Bukkit.getPluginManager().disablePlugin(pl);
        }

        Bukkit.broadcastMessage("§eAufgrund eines Reloads wurden alle Challenges deaktiviert!");
    }

}
