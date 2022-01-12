package de.mrvinrsk.challengebase.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRecipeManager {

    private static ChallengeRecipeManager INSTANCE;

    public static ChallengeRecipeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChallengeRecipeManager();
        }

        return INSTANCE;
    }


    private List<ChallengeRecipe> recipes = new ArrayList<>();

    public List<ChallengeRecipe> getRecipes() {
        return recipes;
    }

    public void registerRecipe(ChallengeRecipe recipe, Plugin plugin) {
        Bukkit.addRecipe(recipe.getRecipe());
        recipes.add(recipe);

        Bukkit.getPluginManager().registerEvents(recipe, plugin);
    }

}
