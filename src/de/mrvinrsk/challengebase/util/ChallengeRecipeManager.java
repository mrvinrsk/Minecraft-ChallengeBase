package de.mrvinrsk.challengebase.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is being used as a singleton for managing custom crafting recipes.
 * If you need to access a function from this class please use the provided instance by {@link #getInstance()}.
 */
public class ChallengeRecipeManager {

    private static ChallengeRecipeManager INSTANCE;

    /**
     * Get the singleton instance of this class.
     *
     * @return the instance.
     */
    public static ChallengeRecipeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChallengeRecipeManager();
        }

        return INSTANCE;
    }


    private List<ChallengeRecipe> recipes = new ArrayList<>();

    /**
     * Get a list of all currently registered recipes.
     *
     * @return the list.
     */
    public List<ChallengeRecipe> getRecipes() {
        return recipes;
    }

    /**
     * Add a new crafting recipes. Listeners will be registered automatically by calling this function.
     *
     * @param recipe the recipe.
     * @param plugin the associated plugin.
     */
    public void registerRecipe(ChallengeRecipe recipe, Plugin plugin) {
        Bukkit.addRecipe(recipe.getRecipe());
        recipes.add(recipe);

        Bukkit.getPluginManager().registerEvents(recipe, plugin);
    }

}
