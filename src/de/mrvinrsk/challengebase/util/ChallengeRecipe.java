package de.mrvinrsk.challengebase.util;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 * This interface represents a custom crafting recipe
 */
public interface ChallengeRecipe extends Listener {

    /**
     * Get the namespace of this recipe (must be unique).
     *
     * @return the namespace.
     */
    String getNamespace();

    /**
     * Get the result of this recipe.
     *
     * @return the result as {@link ItemStack}.
     */
    ItemStack getResult();

    /**
     * Get the recipe.
     *
     * @return the recipe.
     */
    Recipe getRecipe();

}
