package de.mrvinrsk.challengebase.commands;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.chatvergehen.spigotapi.util.inventories.ChestSlot;
import de.chatvergehen.spigotapi.util.math.PercentageCalculator;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import de.mrvinrsk.challengebase.util.*;
import org.bukkit.Bukkit;
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

public class Command_Event implements CommandExecutor, Listener {

    ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    Plugin plugin = ChallengeBase.getInstance();

    private String allInventoryTitle = "§eAlle Events";
    private String chooseInventoryTitle = "§7Welche Events anzeigen?";

    private ItemStack getIconByType(ChallengeEventType type) {
        Item icon = new Item(type.getMaterial());
        icon.setName(type.getIconTitle());

        if(eventManager.getByType(type).size() >= 1) {
            icon.addLoreLine("§8» §7§oEntdeckt in dieser Kategorie: §e" + eventManager.getAchievedByType(type).size() + "§7/§6" + eventManager.getByType(type).size());
        }else {
            icon.addLoreLine("§8» §c§oDiese Kategorie beinhält keine Events.");
        }
        return icon.getItemStack();
    }

    private Item getEventIcon(ChallengeEvent event, Player player, Plugin plugin) {
        Item item = new Item(Material.LIGHT_GRAY_DYE);

        String type = "";

        switch (event.getType()) {
            case POSITIVE:
            case NEGATIVE:
            case UNKNOWN:
                type = event.getType().getIconTitle();
                break;

            case POINTS:
                if (event instanceof PointEarningEvent) {
                    type = switch (((PointEarningEvent) event).getPointEventType()) {
                        case ADD -> "§aPunkte erhalten";
                        case REMOVE -> "§cPunkte verlieren";
                        case SET -> "§ePunkte setzen";
                    };
                } else {
                    type = "§fUndefiniert";
                }
                break;

            default:
                type = "§fUndefiniert";
                break;
        }

        if (event instanceof PercentageChallengeEvent) {
            PercentageChallengeEvent pce = (PercentageChallengeEvent) event;

            type += " §7& §3Prozentual";

            if (pce.getBasePercentage() == eventManager.getPercentage(player, pce)) {
                type += " §8(§f" + eventManager.getPercentage(player, pce) + "%§8)";
            } else {
                type += " §8(§7§m§o" + pce.getBasePercentage() + "%§r §f" + eventManager.getPercentage(player, pce) + "%§8)";
            }
        }

        if (!eventManager.achieved(event)) {
            StringBuilder obfName = new StringBuilder();
            for (int i = 0; i < event.getEventName().length(); i++) {
                obfName.append("0");
            }

            item.setName("§7§k§n" + obfName);
            item.addLoreLine("§7§oEvent-Typ: " + type);
            item.addLoreLine("§8§oDieses Event wurde noch nicht entdeckt.");
            item.addLoreLine("§0");
            item.addLoreLine("§7§oSobald das Event ein mal ausgelöst wurde,");
            item.addLoreLine("§7§okann hier die Beschreibung eingesehen werden.");
        } else {
            item = new Item(event.getIcon());
            item.setName("§a§n" + event.getEventName());
            item.addLoreLine("§7§oEvent-Typ: " + type);
            item.addLoreLine("");
            item.addLoreLine("§f§nBeschreibung");

            for (String desc : event.getDescription(player)) {
                String dsc = desc;

                if (event.getDescription(player).get(0).equalsIgnoreCase(desc)) {
                    dsc = "„" + dsc;
                }

                if (event.getDescription(player).get(event.getDescription(player).size() - 1).equalsIgnoreCase(desc)) {
                    dsc += "“";
                }

                item.addLoreLine("§7§o" + dsc);
            }
        }

        item.addLoreLine("§1");
        item.addLoreLine("§7§oChallenge von §f§o" + eventManager.getRegisterer(event).getName());

        return item;
    }

    private ItemStack getInfoIcon() {
        PercentageCalculator pc = new PercentageCalculator();

        Item item = new Item(Material.PAPER)
                .setName("§aFortschritt")
                .addLoreLine("§7Es gibt aktuell §e" + eventManager.getEvents().size() + " " + (eventManager.getEvents().size() == 1 ? "Event" : "Events") + "§7,")
                .addLoreLine("§7davon " + (eventManager.getEvents().size() == 1 ? "wurde" : "wurden") + " bisher §6" + (eventManager.getAchieved().size() >= 1 ? eventManager.getAchieved().size() : "keine") + " §7entdeckt.")
                .addLoreLine("§7Das sind §a" + Math.round(pc.getPercentage(eventManager.getAchieved().size(), eventManager.getEvents().size())) + "%§7.");

        return item.getItemStack();
    }

    private void openMain(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9 * 1, chooseInventoryTitle);

        for (ChallengeEventType type : ChallengeEventType.values()) {
            inv.addItem(getIconByType(type));
        }
        inv.setItem(ChestSlot.getSlot(1, 9), getInfoIcon());

        p.openInventory(inv);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(cs instanceof Player)) return true;

            Player p = (Player) cs;
            openMain(p);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                cs.sendMessage("/" + label);
                cs.sendMessage("/" + label + " help");
            } else if (args[0].equalsIgnoreCase("list")) {
                if (cs.isOp()) {
                    if (!eventManager.getEventHash().isEmpty()) {
                        cs.sendMessage("§fEine Liste aller registrierten Events:");
                        for (Map.Entry entry : eventManager.getEventHash().entrySet()) {
                            Plugin registeredBy = ((Plugin) entry.getKey());
                            List<ChallengeEvent> events = (List<ChallengeEvent>) entry.getValue();

                            if (events != null) {
                                for (ChallengeEvent event : events) {
                                    cs.sendMessage("§7- §e" + event.getEventName() + " §e§o<" + event.getConfigName().toUpperCase() + "§e> §7- §" + (registeredBy.isEnabled() ? "a" : "c") + registeredBy.getDescription().getName());
                                }
                            } else {
                                cs.sendMessage("§cEs scheinen keine Events gefunden worden zu sein...");
                            }
                        }
                    } else {
                        cs.sendMessage("§cEs sind noch keine Events registriert worden!");
                    }
                }
            }
        }
        return true;
    }

    @EventHandler
    public void cancelClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        if (e.getClickedInventory() != null) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getView().getTitle().equalsIgnoreCase(chooseInventoryTitle)) {
                        e.setCancelled(true);

                        for (ChallengeEventType type : ChallengeEventType.values()) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(type.getIconTitle())) {
                                e.getWhoClicked().openInventory(getByType(type, ((Player) e.getWhoClicked()).getPlayer(), plugin));
                            } else if (e.getView().getTitle().equalsIgnoreCase(type.getIconTitle())) {
                                e.setCancelled(true);
                            }
                        }
                    } else {
                        for (ChallengeEventType type : ChallengeEventType.values()) {
                            if (e.getView().getTitle().equalsIgnoreCase(type.getIconTitle())) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }

        if (e.getCurrentItem().isSimilar(getBack())) {
            openMain((Player) e.getWhoClicked());
        }
    }

    private Inventory getAll(Player player, Plugin plugin) {
        Inventory inv = Bukkit.createInventory(null, 9 * 4, allInventoryTitle);

        for (ChallengeEvent event : eventManager.getEvents()) {
            inv.addItem(getEventIcon(event, player, plugin).getItemStack());
        }

        return inv;
    }

    private ItemStack getBack() {
        return new Item(Material.OAK_DOOR)
                .setName("§f« §cZurück zu allen Event-Typen")
                .getItemStack();
    }

    public Inventory getByType(ChallengeEventType type, Player player, Plugin plugin) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, type.getIconTitle());

        if(eventManager.getByType(type).size() >= 1) {
            int i = 0;
            for (ChallengeEvent event : eventManager.getByType(type)) {
                inv.setItem(i++, getEventIcon(event, player, plugin).getItemStack());
            }
        }else {
            inv.setItem(ChestSlot.getSlot(2, 5), new Item(Material.BARRIER).setName("§cKeine Events").addLoreLine("§7In dieser Kategorie gibt es keine Events.").getItemStack());
        }

        inv.setItem(ChestSlot.getSlot(3, 9), getBack());

        return inv;
    }

}
