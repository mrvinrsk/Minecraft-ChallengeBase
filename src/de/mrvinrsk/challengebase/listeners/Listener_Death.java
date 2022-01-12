package de.mrvinrsk.challengebase.listeners;

import de.chatvergehen.spigotapi.util.random.RandomNumber;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;
import java.util.List;

public class Listener_Death implements Listener {

    List<String> messages = Arrays.asList(
            "##player## hat dem Tod ins Auge geblickt.",
            "##player## hat uns verlassen.",
            "##player## ist zu hart auf die Fresse geflogen. RIP.",
            "##player## scheint ein wenig lost zu sein...",
            "##player## ist gestorben.",
            "Das war's wohl mit ##player##.",
            "RIP, ##player##."
    );

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        String msg = messages.get(RandomNumber.rndinteger(0, messages.size() - 1));
        msg = "§c[§4†§c] §r§7" + msg.replace("##player##", "§6" + e.getEntity().getName() + "§7");

        e.setDeathMessage(msg);
    }

}
