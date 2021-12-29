package de.mrvinrsk.challengebase.util;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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

    public ChallengeEventTriggerEvent(Player player, ChallengeEvent event) {
        this.player = player;
        this.event = event;

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

}
