package de.mrvinrsk.challengebase.listeners;

import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.FastBoard;
import de.mrvinrsk.challengebase.util.PointManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Listener_Join implements Listener {

    private static ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    public static HashMap<Player, FastBoard> boards = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        updateBoard(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        FastBoard board = Listener_Join.boards.remove(p.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }

    public static void updateBoard(Player p) {
        if (!boards.containsKey(p)) {
            FastBoard b = new FastBoard(p);
            b.updateTitle("§fChallenges");
            Listener_Join.boards.put(p, b);
        }

        FastBoard board = boards.get(p);
        int points = 0;
        for (Plugin plugin : eventManager.getChallengePlugins()) {
            PointManager pm = PointManager.getInstance(p.getUniqueId(), plugin);
            points += pm.getPoints();
        }

        board.updateLines(
                "",
                "§7Position: §a" + p.getLocation().getBlockX() + "§7/§a" + p.getLocation().getBlockY() + "§7/§a" + p.getLocation().getBlockZ(),
                "§7Punkte: §a" + points + " " + (points == 1 ? "Punkt":"Punkte"),
                "§7Events: §a" + eventManager.getAchieved().size() + "§7/§a" + eventManager.getEvents().size(),
                ""
        );
    }

}
