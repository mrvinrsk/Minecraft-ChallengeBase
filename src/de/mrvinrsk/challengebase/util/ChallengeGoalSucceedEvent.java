package de.mrvinrsk.challengebase.util;

import org.bukkit.craftbukkit.libs.org.eclipse.sisu.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * This event gets fired, whenever a {@link ChallengeEvent} is getting triggered.
 */
public class ChallengeGoalSucceedEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private Player player;
    private Goal goal;
    private GoalManager goalManager;

    public ChallengeGoalSucceedEvent(@Nullable Player player, Goal goal) {
        this.player = player;
        this.goal = goal;

        this.goalManager = GoalManager.getInstance();
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
    public Goal getGoal() {
        return goal;
    }

    /**
     * Get an instance from {@link ChallengeEventManager}.
     *
     * @return the instance.
     */
    public GoalManager getGoalManager() {
        return goalManager;
    }

    /**
     * Get the plugin from which the event was registered.
     *
     * @return the plugin.
     */
    public Plugin getPlugin() {
        return goal.getPlugin();
    }

}
