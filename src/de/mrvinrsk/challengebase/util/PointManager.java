package de.mrvinrsk.challengebase.util;

import de.chatvergehen.spigotapi.util.filemanaging.FileBuilder;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class PointManager {

    private static PointManager pm;
    private final ChallengeBase base = ChallengeBase.getInstance();

    /**
     * Get an instance of this class.
     *
     * @param uuid   the uuid of the player you want to manage.
     * @param plugin the plugin whichs points you want to manipulate.
     * @return the instance.
     */
    public static PointManager getInstance(UUID uuid, Plugin plugin) {
        if (pm == null) {
            pm = new PointManager(uuid, plugin);
        }

        if (!pm.getUUID().equals(uuid)) {
            pm.changeUUID(uuid);
        }
        pm.setPlugin(plugin);

        return pm;
    }

    private UUID uuid;
    private Plugin plugin;

    private PointManager(UUID uuid, Plugin plugin) {
        this.uuid = uuid;
        this.plugin = plugin;
    }

    /**
     * Change the plugin used in the instance.
     *
     * @param plugin the new plugin.
     */
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the plugin which is currently in use by the instance.
     *
     * @return the plugin.
     */
    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Get the UUID of the player whom is currently in use by the instance.
     *
     * @return the uuid.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Change the UUID used in the instance.
     *
     * @param uuid the new UUID.
     */
    public void changeUUID(UUID uuid) {
        this.uuid = uuid;
    }

    private FileBuilder getFile() {
        String path = base.getUserFolder(getPlugin(), getUUID()).getPath();
        return new FileBuilder(path, "points.yml");
    }

    private void setup() {
        if (!getFile().exists()) {
            getFile().create();

            getFile().getConfig().set("Points", 0);
        }
    }

    /**
     * Get the points that the player has in the current plugin.
     *
     * @return the points
     */
    public int getPoints() {
        if (getFile().exists() || Bukkit.getPlayer(getUUID()) != null) {
            if (!getFile().exists()) {
                setup();
            }

            return (int) getFile().getConfig().get("Points");
        } else {
            return -1;
        }
    }

    /**
     * Set the points that the player should have.
     *
     * @param points the amount of points.
     */
    public void setPoints(int points) {
        setup();

        getFile().getConfig().set("Points", points);
    }

    /**
     * Add points to the players account.
     *
     * @param points the amount of points to add.
     */
    public void addPoints(int points) {
        setup();

        setPoints(getPoints() + points);
    }

    /**
     * Remove points from the players account.
     *
     * @param points the amount of points to remove.
     */
    public void removePoints(int points) {
        setup();

        if (getPoints() > points) {
            setPoints(getPoints() - points);
        } else {
            setPoints(0);
        }
    }

    /**
     * Get all point accounts from all players who participate in the given plugin.
     *
     * @param plugin the plugin.
     * @return a hash of all UUIDs and Integers.
     */
    public static HashMap<UUID, Integer> getAll(Plugin plugin) {
        HashMap<UUID, Integer> points = new HashMap<>();
        UUID rnd = UUID.randomUUID();
        File folder = new File(ChallengeBase.getUserFolder(plugin, rnd).getPath().replace("//" + rnd, ""));

        if (folder.list() != null) {
            if (folder.list().length >= 1) {
                for (String u : folder.list()) {
                    PointManager pm = PointManager.getInstance(UUID.fromString(u), plugin);

                    if (pm.getPoints() != -1) {
                        points.put(UUID.fromString(u), pm.getPoints());
                    }
                }
            }
        }

        return points;
    }

}
