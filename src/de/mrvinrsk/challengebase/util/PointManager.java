package de.mrvinrsk.challengebase.util;

import de.chatvergehen.spigotapi.util.filemanaging.FileBuilder;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class PointManager {

    private static PointManager pm;
    private final ChallengeBase base = ChallengeBase.getInstance();

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

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }


    public UUID getUUID() {
        return uuid;
    }

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

    public int getPoints() {
        if (getFile().exists()) {
            return (int) getFile().getConfig().get("Points");
        } else {
            return -1;
        }
    }

    public void setPoints(int points) {
        setup();

        getFile().getConfig().set("Points", points);
    }

    public void addPoints(int points) {
        setup();

        setPoints(getPoints() + points);
    }

    public void removePoints(int points) {
        setup();

        if (getPoints() > points) {
            setPoints(getPoints() - points);
        } else {
            setPoints(0);
        }
    }

    public static HashMap<UUID, Integer> getAll(Plugin plugin) {
        HashMap<UUID, Integer> points = new HashMap<>();
        UUID rnd = UUID.randomUUID();
        File folder = new File(ChallengeBase.getUserFolder(plugin, rnd).getPath().replace("//" + rnd, ""));

        if(folder.list() != null) {
            if(folder.list().length >= 1) {
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
