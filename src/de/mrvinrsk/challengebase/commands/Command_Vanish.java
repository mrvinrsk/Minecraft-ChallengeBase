package de.mrvinrsk.challengebase.commands;

import de.mrvinrsk.challengebase.main.ChallengeBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Command_Vanish implements CommandExecutor {

    List<UUID> vanished = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;

            if (!vanished.contains(p.getUniqueId())) {
                vanished.add(p.getUniqueId());
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1, false, false));
            } else {
                vanished.remove(p.getUniqueId());
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
            }

            updateVanish();
        }
        return true;
    }

    private void updateVanish() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (vanished.contains(p.getUniqueId())) {
                    all.hidePlayer(ChallengeBase.getInstance(), p);
                } else {
                    all.showPlayer(ChallengeBase.getInstance(), p);
                }
            }
        }
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        updateVanish();
    }

}
