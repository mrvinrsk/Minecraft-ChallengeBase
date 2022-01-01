package de.mrvinrsk.challengebase.util;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ChallengeEvent extends Listener {

    String getEventName();
    String getConfigName();
    List<String> getDescription(Player p);
    ItemStack getIcon();
    ChallengeEventType getType();

}
