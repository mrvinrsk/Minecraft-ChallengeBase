package de.mrvinrsk.challengebase.listeners;

import de.mrvinrsk.challengebase.main.ChallengeBase;
import de.mrvinrsk.challengebase.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.UUID;

public class Listener_EventTrigger implements Listener {

    private ChallengeBase plugin = ChallengeBase.getInstance();
    private Gameplay gameplay = Gameplay.getInstance();

    @EventHandler
    public void trigger(ChallengeEventTriggerEvent e) {
        Player p = e.getPlayer();
        ChallengeEvent event = e.getEvent();
        ChallengeEventManager eventManager = e.getEventManager();

        if (!eventManager.achieved(event)) {
            eventManager.setAchieved(event, true);

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§6§k00000000000000000000000000000000000000000000000000000");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§r §r §e§o" + p.getName() + " §fhat das Event §a§o" + event.getEventName() + " §fentdeckt:");
            Bukkit.broadcastMessage("");
            for (String desc : event.getDescription(p)) {
                String dsc = desc;

                if (event.getDescription(p).get(0).equalsIgnoreCase(desc)) {
                    dsc = "„" + dsc;
                }

                if (event.getDescription(p).get(event.getDescription(p).size() - 1).equalsIgnoreCase(desc)) {
                    dsc += "“";
                }

                Bukkit.broadcastMessage("§r §r §7§o" + dsc);
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§r §r §7Bisher entdeckte Events: §a" + eventManager.getAchieved().size() + "§7/§a" + eventManager.getEvents().size());
            Bukkit.broadcastMessage("§r §r §7Mehr Infos: §e/event");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§6§k00000000000000000000000000000000000000000000000000000");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("");

            if (eventManager.getAchieved().size() == eventManager.getEvents().size()) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§6§k00000000000000000000000000000000000000000000000000000");
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§6Das letzte Event wurde entdeckt!");
                Bukkit.broadcastMessage("§6Als Belohnung für die Mühe und die Nerven erhalten");
                Bukkit.broadcastMessage("§6§nalle Spieler 20 Diamanten.");
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§6§k00000000000000000000000000000000000000000000000000000");
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("");


                // Online Players
                for (Player all : Bukkit.getOnlinePlayers()) {
                    gameplay.sendMessage(all, GameplayMessageType.SYSTEM, "§6Du hast 20 Diamanten erhalten!");

                    all.getWorld().dropItemNaturally(p.getLocation().add(0, .5, 0), new ItemStack(Material.DIAMOND, 20));
                }

                // Offline Players
                UUID rnd = UUID.randomUUID();
                for (String u : new File(plugin.getUserFolder(plugin, rnd).getPath().replace("//" + rnd, "")).list()) {
                    if (gameplay.getLogoutPosition(UUID.fromString(u)) != null) {
                        gameplay.getLogoutPosition(UUID.fromString(u)).getWorld().getBlockAt(gameplay.getLogoutPosition(UUID.fromString(u))).setType(Material.CHEST);
                        Chest c = (Chest) gameplay.getLogoutPosition(UUID.fromString(u)).getWorld().getBlockAt(gameplay.getLogoutPosition(UUID.fromString(u))).getState();
                        gameplay.setLogoutPosition(UUID.fromString(u), gameplay.getLogoutPosition(UUID.fromString(u)).add(0, 1, 0));

                        c.getInventory().addItem(new ItemStack(Material.DIAMOND, 20));
                    }
                }
            }

            PointManager pm = PointManager.getInstance(e.getPlayer().getUniqueId(), e.getPlugin());
            int points = 10;
            pm.addPoints(points);
            gameplay.sendMessage(e.getPlayer(), GameplayMessageType.SYSTEM, "Du hast §a" + points + " " + (points == 1 ? "Punkt" : "Punkte") + " §rfür das Entdecken des Events §a" + e.getEvent().getEventName() + " §rerhalten.");
        }

        if (e.getEvent() instanceof PointEarningEvent) {
            PointEarningEvent pee = (PointEarningEvent) e.getEvent();
            PointManager pm = PointManager.getInstance(e.getPlayer().getUniqueId(), e.getPlugin());
            int points = pee.getPoints();

            switch (pee.getPointEventType()) {
                case ADD -> {
                    pm.addPoints(points);
                    gameplay.sendMessage(e.getPlayer(), GameplayMessageType.SYSTEM, "Du hast §a" + points + " " + (points == 1 ? "Punkt" : "Punkte") + " §rfür das Event §a" + e.getEvent().getEventName() + " §rerhalten.");
                }
                case SET -> {
                    pm.setPoints(points);
                    gameplay.sendMessage(e.getPlayer(), GameplayMessageType.SYSTEM, "Deine Punkte wurden durch das Event §a" + e.getEvent().getEventName() + " §rauf §a" + points + " §rgesetzt.");
                }
                case REMOVE -> {
                    pm.removePoints(points);
                    gameplay.sendMessage(e.getPlayer(), GameplayMessageType.SYSTEM, "Du hast §a" + points + " " + (points == 1 ? "Punkt" : "Punkte") + " §rdurch das Event §a" + e.getEvent().getEventName() + " §rverloren.");
                }
            }
        }
    }

}
