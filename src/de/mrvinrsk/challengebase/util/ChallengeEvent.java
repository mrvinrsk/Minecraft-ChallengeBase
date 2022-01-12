package de.mrvinrsk.challengebase.util;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ChallengeEvent extends Listener {

    /**
     * Get the Displayname of the Event.
     *
     * @return
     */
    String getEventName();

    /**
     * Get the name of the event for YAML files.
     *
     * @return
     */
    String getConfigName();

    /**
     * Get the event description.
     *
     * @param p the player who sees the description.
     * @return the description for the given player.
     */
    List<String> getDescription(Player p);

    /**
     * Get the icon for the {@link de.mrvinrsk.challengebase.commands.Command_Event} Command.
     *
     * @return the icon.
     */
    ItemStack getIcon();

    /**
     * Get the {@link ChallengeEventType} of this event.
     *
     * @return the type.
     */
    ChallengeEventType getType();

    /**
     * Get the amount of points a player will get when he discoveres the event.
     *
     * @return the amount of points.
     */
    int getDiscoverPoints();

}
