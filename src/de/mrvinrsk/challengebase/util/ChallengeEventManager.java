package de.mrvinrsk.challengebase.util;

import de.chatvergehen.spigotapi.util.filemanaging.ConfigEditor;
import de.chatvergehen.spigotapi.util.filemanaging.FileBuilder;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ChallengeEventManager {

    private static ChallengeEventManager INSTANCE;
    private HashMap<Plugin, List<ChallengeEvent>> events = new HashMap<>();

    public static ChallengeEventManager getManager() {
        if (INSTANCE == null) {
            INSTANCE = new ChallengeEventManager();
        }

        return INSTANCE;
    }

    public HashMap<Plugin, List<ChallengeEvent>> getEventHash() {
        return events;
    }

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

    public List<ChallengeEvent> getByType(ChallengeEventType type) {
        List<ChallengeEvent> es = new ArrayList<>();

        for (ChallengeEvent event : getEvents()) {
            if (event.getType().equals(type)) {
                es.add(event);
            }
        }

        return es;
    }

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

    public void unregisterEvent(ChallengeEvent event, Plugin plugin) {
        List<ChallengeEvent> ev = events.get(plugin);

        if (ev != null && ev.size() >= 1) {
            ev.removeIf(evn -> evn.equals(event));
            HandlerList.unregisterAll(event);
        }

        events.put(plugin, ev);
    }

    public ChallengeEvent getEvent(String name) {
        for (ChallengeEvent event : getEvents()) {
            if (event.getEventName().equalsIgnoreCase(name) || event.getConfigName().equalsIgnoreCase(name)) {
                return event;
            }
        }
        return null;
    }

    private Plugin getRegisterer(ChallengeEvent event) {
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

    private ConfigEditor getConfig(ChallengeEvent event) {
        return getFile(event).getConfig();
    }

    public boolean achieved(ChallengeEvent event) {
        return ((List<String>) getConfig(event).get("Achieved")).contains(event.getConfigName().toUpperCase());
    }

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

    public List<ChallengeEvent> getAchieved() {
        List<ChallengeEvent> achieved = new ArrayList<>();

        for (ChallengeEvent event : getEvents()) {
            if (achieved(event)) achieved.add(event);
        }

        return achieved;
    }

    public void triggerEvent(Player player, ChallengeEvent event, Plugin plugin) {
        Bukkit.getPluginManager().callEvent(new ChallengeEventTriggerEvent(player, event, plugin));
    }

}
