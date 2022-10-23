package de.mrvinrsk.challengebase.util;

import de.chatvergehen.spigotapi.util.filemanaging.ConfigEditor;
import de.chatvergehen.spigotapi.util.filemanaging.FileBuilder;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * This is a utility class which simplifies some of the most used actions used in challenge plugins
 * such as sending messages with a consistent formatting.
 */
public class Gameplay implements Listener {

    private static Gameplay INSTANCE;
    private ChallengeBase plugin = ChallengeBase.getInstance();

    public static Gameplay getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Gameplay();
        }
        return INSTANCE;
    }

    /**
     * Send a message in a pre-made format to the given player.
     *
     * @param player      the player.
     * @param messageType the format.
     * @param message     the message.
     */
    public void sendMessage(Player player, GameplayMessageType messageType, String message) {
        TextComponent tc = new TextComponent(messageType.getFormattedMessage(message));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Nachricht von " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " Uhr")));

        player.spigot().sendMessage(tc);
    }

    /**
     * Update the logout position of a player.
     *
     * @param uuid     the UUID of the player.
     * @param location the location.
     */
    public void setLogoutPosition(UUID uuid, Location location) {
        FileBuilder file = new FileBuilder(plugin.getUserFolder(plugin, uuid), "logoutPosition.yml");
        ConfigEditor config = file.getConfig();

        config.set("Location", location);
    }

    @EventHandler
    public void login(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        FileBuilder file = new FileBuilder(plugin.getUserFolder(plugin, p.getUniqueId()), "logoutPosition.yml");
        ConfigEditor config = file.getConfig();

        if (config.contains("Location")) {
            p.teleport(getLogoutPosition(p.getUniqueId()));
            config.set("Location", null);
        }
    }

    @EventHandler
    public void logout(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        setLogoutPosition(p.getUniqueId(), p.getLocation());
    }

    /**
     * Get the last location from a player.
     *
     * @param uuid the UUID of the player.
     * @return the location.
     */
    public Location getLogoutPosition(UUID uuid) {
        FileBuilder file = new FileBuilder(plugin.getUserFolder(plugin, uuid), "logoutPosition.yml");
        ConfigEditor config = file.getConfig();

        if (config.contains("Location")) {
            return (Location) config.get("Location");
        }
        return null;
    }

    /**
     * Send a message to the actionbar of a player.
     *
     * @param player  the player.
     * @param message the message.
     */
    public void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

}
