package de.mrvinrsk.challengebase.util;

import org.bukkit.craftbukkit.libs.org.eclipse.sisu.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * This event gets fired, whenever a {@link ChallengeEvent} is getting triggered.
 */
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

    public ChallengeEventTriggerEvent(@Nullable Player player, ChallengeEvent event, Plugin plugin) {
        this.player = player;
        this.event = event;
        this.plugin = plugin;

        this.eventManager = ChallengeEventManager.getManager();
    }

    /**
     * Get the player who triggered the event.
     *
     * @return the player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the event which was triggered.
     *
     * @return the event.
     */
    public ChallengeEvent getEvent() {
        return event;
    }

    /**
     * Get an instance from {@link ChallengeEventManager}.
     *
     * @return the instance.
     */
    public ChallengeEventManager getEventManager() {
        return eventManager;
    }

    /**
     * Get the plugin from which the event was registered.
     *
     * @return the plugin.
     */
    public Plugin getPlugin() {
        return plugin;
    }

}
