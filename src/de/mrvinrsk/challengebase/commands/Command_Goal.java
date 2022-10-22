package de.mrvinrsk.challengebase.commands;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.chatvergehen.spigotapi.util.inventories.ChestSlot;
import de.chatvergehen.spigotapi.util.math.PercentageCalculator;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import de.mrvinrsk.challengebase.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class Command_Goal implements CommandExecutor, Listener {

    ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    GoalManager goalManager = GoalManager.getInstance();
    Plugin plugin = ChallengeBase.getInstance();

    private String chooseInventoryTitle = "§7Welche Ziele anzeigen?";

    private ItemStack getIconByType(ChallengeEventType type) {
        Item icon = new Item(type.getMaterial());
        icon.setName(type.getIconTitle());

        if (eventManager.getByType(type).size() >= 1) {
            icon.addLoreLine("§8» §7§oEntdeckt in dieser Kategorie: §e" + eventManager.getAchievedByType(type).size() + "§7/§6" + eventManager.getByType(type).size());
        } else {
            icon.addLoreLine("§8» §c§oDiese Kategorie beinhält keine Events.");
        }
        return icon.getItemStack();
    }

    private Material pluginIconMaterial = Material.BOOK;

    private Item getPluginIcon(Plugin plugin) {
        Item i = new Item(pluginIconMaterial);
        i.setName("§a" + plugin.getName());
        i.addLoreLine("§8» §7Klicke um §f" + goalManager.getPluginGoals(plugin).size() + " Ziel" + (goalManager.getPluginGoals(plugin).size() != 1 ? "e" : "") + " §7anzuzeigen.");
        return i;
    }

    private void openMain(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, chooseInventoryTitle);

        for (Plugin pl : goalManager.getPlugins()) {
            inv.addItem(getPluginIcon(pl).getItemStack());
        }

        p.openInventory(inv);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;
        Player p = (Player) cs;
        openMain(p);
        return true;
    }

    private ItemStack getBack() {
        return new Item(Material.OAK_DOOR)
                .setName("§f« §cZurück zu allen Zielen")
                .getItemStack();
    }

    @EventHandler
    public void cancelClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        if (e.getClickedInventory() != null) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getView().getTitle().equalsIgnoreCase(chooseInventoryTitle)) {
                        e.setCancelled(true);

                        if(e.getCurrentItem().getType() == pluginIconMaterial) {
                            Plugin pl = Bukkit.getPluginManager().getPlugin(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

                            if(pl != null) {
                                openPlugin((Player) e.getWhoClicked(), pl);
                            }
                        }
                    }else if(e.getView().getTitle().contains("Ziele von ")) {
                        e.setCancelled(true);
                    }
                }
            }
        }

        if (e.getCurrentItem().isSimilar(getBack())) {
            openMain((Player) e.getWhoClicked());
        }
    }

    private void openPlugin(Player p, Plugin plugin) {
        List<Goal> goals = goalManager.getPluginGoals(plugin);
        Inventory inv = Bukkit.createInventory(null, 9 * 6, "§7Ziele von §a" + plugin.getName());

        if(goals.size() >= 1) {
            for (Goal goal : goals) {
                inv.addItem(GoalManager.getGoalItem(goal).getItemStack());
            }
        }else {
            inv.setItem(13, new Item(Material.BARRIER)
                    .setName("§cKeine Ziele gefunden")
                    .getItemStack());
        }

        inv.setItem(53, getBack());
        p.openInventory(inv);
    }

}
