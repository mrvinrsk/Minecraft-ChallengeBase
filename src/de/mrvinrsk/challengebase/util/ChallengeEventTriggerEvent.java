package de.mrvinrsk.challengebase.util;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class ChallengeEventTriggerEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private Player player;
    private ChallengeEvent event;
    private ChallengeEventManager eventManager;
    private Plugin plugin;

    public ChallengeEventTriggerEvent(Player player, ChallengeEvent event, Plugin plugin) {
        this.player = player;
        this.event = event;
        this.plugin = plugin;

        this.eventManager = ChallengeEventManager.getManager();
    }

    public Player getPlayer() {
        return player;
    }

    public ChallengeEvent getEvent() {
        return event;
    }

    public ChallengeEventManager getEventManager() {
        return eventManager;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}
