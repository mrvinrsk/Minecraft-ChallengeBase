package de.mrvinrsk.challengebase.util;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface ChallengeRecipe extends Listener {

    String getNamespace();
    ItemStack getResult();
    Recipe getRecipe();

}
