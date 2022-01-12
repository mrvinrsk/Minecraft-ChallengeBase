package de.mrvinrsk.challengebase.util;

import de.chatvergehen.spigotapi.util.filemanaging.ConfigEditor;
import de.chatvergehen.spigotapi.util.filemanaging.FileBuilder;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ChallengeEventManager {

    private static ChallengeEventManager INSTANCE;
    private HashMap<Plugin, List<ChallengeEvent>> events = new HashMap<>();


    /**
     * Get an instance of this class.
     * @return the instance.
     */
    public static ChallengeEventManager getManager() {
        if (INSTANCE == null) {
            INSTANCE = new ChallengeEventManager();
        }

        return INSTANCE;
    }

    /**
     * Get a list of all plugins which contain events.
     *
     * @return the list.
     */
    public List<Plugin> getChallengePlugins() {
        List<Plugin> plugins = new ArrayList<>();

        for (Map.Entry<Plugin, List<ChallengeEvent>> entry : events.entrySet()) {
            plugins.add(entry.getKey());
        }

        return plugins;
    }

    public HashMap<Plugin, List<ChallengeEvent>> getEventHash() {
        return events;
    }

    /**
     * Get a list of all registered events.
     *
     * @return the list.
     */
    public List<ChallengeEvent> getEvents() {
        List<ChallengeEvent> es = new ArrayList<>();

        if (!events.isEmpty()) {
            for (Map.Entry<Plugin, List<ChallengeEvent>> entry : events.entrySet()) {
                for (ChallengeEvent evt : entry.getValue()) {
                    es.add(evt);
                }
            }
        }

        return es;
    }

    /**
     * Get a list of all events of one specific type.
     *
     * @param type the type.
     * @return the list.
     */
    public List<ChallengeEvent> getByType(ChallengeEventType type) {
        List<ChallengeEvent> es = new ArrayList<>();

        for (ChallengeEvent event : getEvents()) {
            if (event.getType().equals(type)) {
                es.add(event);
            }
        }

        return es;
    }

    /**
     * Add a event.
     *
     * @param event  the event.
     * @param plugin the plugin this event is a part of.
     */
    public void registerEvent(ChallengeEvent event, Plugin plugin) {
        if (!getEvents().contains(event)) {
            List<ChallengeEvent> plEv = new ArrayList<>();

            if (!events.isEmpty()) {
                if (events.containsKey(plugin)) {
                    plEv = events.get(plugin);
                }
            }

            plEv.add(event);

            events.put(plugin, plEv);
            Bukkit.getPluginManager().registerEvents(event, ChallengeBase.getInstance());

            if (!getConfig(event).contains("Achieved")) {
                getConfig(event).set("Achieved", new ArrayList<String>());
            }
        }
    }

    /**
     * Get a list of all {@link PercentageChallengeEvent}s.
     *
     * @return the list.
     */
    public List<PercentageChallengeEvent> getPercentageEvents() {
        List<PercentageChallengeEvent> pces = new ArrayList<>();

        for (ChallengeEvent event : getEvents()) {
            if (event instanceof PercentageChallengeEvent) {
                pces.add((PercentageChallengeEvent) event);
            }
        }

        return pces;
    }

    /**
     * Get the file in which the individual possibilities of a specific player are stored.
     *
     * @param p      the player
     * @param plugin the plugin which registered the needed events.
     * @return the file.
     */
    private FileBuilder getPercentageFile(Player p, Plugin plugin) {
        return new FileBuilder(ChallengeBase.getUserFolder(plugin, p.getUniqueId()), "eventPercentages.yml");
    }

    /**
     * Get the possibility to trigger an event for a specific player.
     * Returns the base percentage if the player has no specific value.
     *
     * @param player              the player.
     * @param percentageChallenge the event.
     * @return the possibility.
     */
    public double getPercentage(Player player, PercentageChallengeEvent percentageChallenge) {
        if (ChallengeBase.getUserFolder(getRegisterer(percentageChallenge), player.getUniqueId()).exists()) {
            if (getPercentageFile(player, getRegisterer(percentageChallenge)).exists()) {
                ConfigEditor ce = getPercentageFile(player, getRegisterer(percentageChallenge)).getConfig();

                if (ce.contains(percentageChallenge.getConfigName().toUpperCase())) {
                    return (double) ce.get(percentageChallenge.getConfigName().toUpperCase());
                }
            }
        }
        return percentageChallenge.getBasePercentage();
    }

    /**
     * Update the possibility to trigger an event for a specific player.
     *
     * @param player              the player.
     * @param percentageChallenge the event.
     * @param percentage          the new percentage.
     */
    public void setPercentage(Player player, PercentageChallengeEvent percentageChallenge, double percentage) {
        FileBuilder fb = getPercentageFile(player, getRegisterer(percentageChallenge));

        if (!fb.exists()) {
            fb.create();
        }

        ConfigEditor ce = fb.getConfig();

        ce.set(percentageChallenge.getConfigName().toUpperCase(), percentage);
    }

    /**
     * Get an event by its name.
     *
     * @param name the name (eventname or configname).
     * @return the event.
     */
    public ChallengeEvent getEvent(String name) {
        for (ChallengeEvent event : getEvents()) {
            if (event.getEventName().equalsIgnoreCase(name) || event.getConfigName().equalsIgnoreCase(name)) {
                return event;
            }
        }
        return null;
    }

    /**
     * Get the plugin which registered a specific event.
     *
     * @param event the event.
     * @return the plugin.
     */
    public Plugin getRegisterer(ChallengeEvent event) {
        for (Map.Entry<Plugin, List<ChallengeEvent>> entry : events.entrySet()) {
            List<ChallengeEvent> eventsByRegisterer = entry.getValue();

            for (ChallengeEvent evt : eventsByRegisterer) {
                if (evt.equals(event)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * Get the file in which the event is stored if discovered.
     *
     * @param event the event.
     * @return the file.
     */
    private FileBuilder getFile(ChallengeEvent event) {
        Plugin registerer = getRegisterer(event);

        if (registerer != null) {
            FileBuilder fb = new FileBuilder(ChallengeBase.getInstance().getPluginFolder(registerer), "achieved_events.yml");

            if (!fb.exists()) {
                fb.create();
            }

            return fb;
        }
        return null;
    }

    /**
     * Get the config of a event-file.
     *
     * @param event the event.
     * @return the configeditor.
     */
    private ConfigEditor getConfig(ChallengeEvent event) {
        return getFile(event).getConfig();
    }

    /**
     * Check if a specific event is already discovered.
     *
     * @param event the event.
     * @return true if discovered.
     */
    public boolean achieved(ChallengeEvent event) {
        return ((List<String>) getConfig(event).get("Achieved")).contains(event.getConfigName().toUpperCase());
    }

    /**
     * Change the discovery-status of an event
     *
     * @param event the event whoms status is about to be changed.
     * @param b     if the event is already discovered.
     */
    public void setAchieved(ChallengeEvent event, boolean b) {
        if (b) {
            if (!achieved(event)) {
                List<String> a = (List<String>) getConfig(event).get("Achieved");
                a.add(event.getConfigName().toUpperCase());
                getConfig(event).set("Achieved", a);
            }
        } else {
            if (achieved(event)) {
                List<String> a = (List<String>) getConfig(event).get("Achieved");
                a.remove(event.getConfigName().toUpperCase());
                getConfig(event).set("Achieved", a);
            }
        }
    }

    /**
     * Get a list of all discovered events.
     *
     * @return list of discovered events.
     */
    public List<ChallengeEvent> getAchieved() {
        List<ChallengeEvent> achieved = new ArrayList<>();

        for (ChallengeEvent event : getEvents()) {
            if (achieved(event)) achieved.add(event);
        }

        return achieved;
    }

    public List<ChallengeEvent> getAchievedByType(ChallengeEventType type) {
        List<ChallengeEvent> achieved = new ArrayList<>();
        List<ChallengeEvent> allAchieved = getAchieved();

        for(ChallengeEvent event : allAchieved) {
            if(event.getType() == type) {
                achieved.add(event);
            }
        }

        return achieved;
    }

    /**
     * Trigger the {@link ChallengeEventTriggerEvent}.
     *
     * @param player the player who triggered this event.
     * @param event  the event which was triggered.
     * @param plugin the plugin that triggered this event.
     * @deprecated use {@link #triggerEvent(Player, ChallengeEvent)} instead.
     */
    @Deprecated
    public void triggerEvent(Player player, ChallengeEvent event, Plugin plugin) {
        Bukkit.getPluginManager().callEvent(new ChallengeEventTriggerEvent(player, event, plugin));
    }

    /**
     * Trigger the {@link ChallengeEventTriggerEvent}.
     *
     * @param player the player who triggered this event.
     * @param event  the event which was triggered.
     */
    public void triggerEvent(Player player, ChallengeEvent event) {
        Bukkit.getPluginManager().callEvent(new ChallengeEventTriggerEvent(player, event, getRegisterer(event)));
    }

}
