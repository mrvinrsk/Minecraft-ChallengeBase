package de.mrvinrsk.challengebase.commands;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import de.mrvinrsk.challengebase.util.ChallengeEvent;
import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.ChallengeEventType;
import de.mrvinrsk.challengebase.util.PointEarningEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
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

        icon.addLoreLine("§8» §7§oEvents in dieser Kategorie: §f" + eventManager.getByType(type).size());
        return icon.getItemStack();
    }

    private Item getEventIcon(ChallengeEvent event) {
        Item item = new Item(Material.LIGHT_GRAY_DYE);

        String type = "";

        switch (event.getType()) {
            case POSITIVE:
                type = "§aPositiv";
                break;

            case NEGATIVE:
                type = "§cNegativ";
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

            for (String desc : event.getDescription()) {
                String dsc = desc;

                if (event.getDescription().get(0).equalsIgnoreCase(desc)) {
                    dsc = "„" + dsc;
                }

                if (event.getDescription().get(event.getDescription().size() - 1).equalsIgnoreCase(desc)) {
                    dsc += "“";
                }

                item.addLoreLine("§7§o" + dsc);
            }
        }

        return item;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(cs instanceof Player)) return true;

            Player p = (Player) cs;
            Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, chooseInventoryTitle);

            for (ChallengeEventType type : ChallengeEventType.values()) {
                inv.addItem(getIconByType(type));
            }

            p.openInventory(inv);
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
                    if (e.getView().getTitle().equalsIgnoreCase(allInventoryTitle)) {
                        e.setCancelled(true);
                    } else {
                        for (ChallengeEventType type : ChallengeEventType.values()) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(type.getIconTitle())) {
                                e.setCancelled(true);

                                e.getWhoClicked().openInventory(getByType(type));
                            }
                        }
                    }
                }
            }
        }
    }

    private Inventory getAll() {
        Inventory inv = Bukkit.createInventory(null, 9 * 4, allInventoryTitle);

        for (ChallengeEvent event : eventManager.getEvents()) {
            inv.addItem(getEventIcon(event).getItemStack());
        }

        return inv;
    }

    public Inventory getByType(ChallengeEventType type) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, type.getIconTitle());

        for (ChallengeEvent event : eventManager.getByType(type)) {
            inv.addItem(getEventIcon(event).getItemStack());
        }

        return inv;
    }

}
