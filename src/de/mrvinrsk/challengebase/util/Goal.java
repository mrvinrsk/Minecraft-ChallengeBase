package de.mrvinrsk.challengebase.util;


import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface Goal extends Listener {

    /**
     * Get the name of this goal.
     * @return the name.
     */
    String getName();

    /**
     * Get the description of this goal.
     * @return the description.
     */
    List<String> getDescription();

    /**
     * Get the amount of points gained by reaching this goal.
     * @return the amount of points.
     */
    int getPoints();

    Material getMaterial();

    Plugin getPlugin();

}
