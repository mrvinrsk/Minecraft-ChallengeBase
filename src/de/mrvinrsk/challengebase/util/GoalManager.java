package de.mrvinrsk.challengebase.util;

import de.chatvergehen.spigotapi.util.filemanaging.ConfigEditor;
import de.chatvergehen.spigotapi.util.filemanaging.FileBuilder;
import de.chatvergehen.spigotapi.util.instances.Item;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is being used as a singleton for managing custom crafting recipes.
 * If you need to access a function from this class please use the provided instance by {@link #getInstance()}.
 */
public class GoalManager {

    private static ChallengeBase base = ChallengeBase.getInstance();
    private static GoalManager INSTANCE;
    private static HashMap<Plugin, List<Goal>> goals = new HashMap<>();

    /**
     * Get the singleton instance of this class.
     *
     * @return the instance.
     */
    public static GoalManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoalManager();
        }

        return INSTANCE;
    }

    /**
     * Get all registered goals from all extensions.
     *
     * @return all registered goals.
     */
    public static List<Goal> getAllGoals() {
        List<Goal> gs = new ArrayList<>();

        for (List<Goal> g : goals.values()) {
            gs.addAll(g);
        }

        return gs;
    }

    /**
     * Get all registered goals from a specific extension.
     *
     * @param plugin the plugin you want to get the goals from.
     * @return all registered goals from the given plugin.
     */
    public List<Goal> getPluginGoals(Plugin plugin) {
        return goals.get(plugin);
    }

    /**
     * Get the save-file.
     *
     * @return the save-file.
     */
    private static FileBuilder getFile() {
        return new FileBuilder(ChallengeBase.getInstance().getDataFolder().getPath(), "goals.yml");
    }

    /**
     * Get the config for the save-file.
     *
     * @return the config for the save-file.
     */
    private static ConfigEditor getConfig() {
        getFile().create();
        return getFile().getConfig();
    }

    /**
     * Register a new goal.
     *
     * @param plugin the plugin which registers the goal.
     * @param goal   the goal you want to register.
     */
    public static void registerGoal(Plugin plugin, Goal goal) {
        if (!goals.containsKey(plugin)) {
            goals.put(plugin, new ArrayList<>());
        }

        System.out.println(getFile().getFile().getPath());

        if (!getConfig().contains(getCompleteConfigGoalName(goal))) {
            getConfig().set(getCompleteConfigGoalName(goal), false);
        }

        List<Goal> g = goals.get(plugin);
        g.add(goal);
        goals.put(plugin, g);

        ChallengeBase.getInstance().getLogger().info("Registered goal " + goal.getName() + " from " + plugin.getName());
    }

    private static String getCompleteConfigGoalName(Goal goal) {
        return goal.getPlugin().getName() + ".Goals." + goal.getName().toLowerCase()
                .replace(" ", "__")
                .replace("&", "")
                .replace(".", "_")
                .replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("ß", "ss");
    }

    public static List<Plugin> getPlugins() {
        return new ArrayList<>(goals.keySet());
    }

    private static String getConfigGoalName(Goal goal) {
        String path = getCompleteConfigGoalName(goal);
        return path.substring(path.lastIndexOf(".") + 1);
    }


    public static boolean isGoalReached(Goal goal) {
        String path = getCompleteConfigGoalName(goal);
        return (getConfig().get(path) != null && ((Boolean) getConfig().get(path)));
    }

    public static void setReached(Plugin plugin, Goal goal, boolean reached) {
        String path = getCompleteConfigGoalName(goal);
        getConfig().set(path, reached);
    }

    public static Item getGoalItem(Goal goal) {
        Item i = new Item(goal.getMaterial()).setName("§a" + goal.getName());

        for (String s : goal.getDescription()) {
            i.addLoreLine("§f" + s);
        }

        i.addLoreLine("§7");
        i.addLoreLine("§7-------------------");
        i.addLoreLine("§7Erreicht: §a" + (isGoalReached(goal) ? "§a✔" : "§c✘"));

        return i;
    }

    public static List<Goal> getAchieved() {
        List<Goal> g = new ArrayList<>();

        for(Goal goal : getAllGoals()) {
            if(isGoalReached(goal)) {
                g.add(goal);
            }
        }

        return g;
    }

}
