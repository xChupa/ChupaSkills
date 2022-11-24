package chupa.skills.chupaskils;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class skillgui implements Listener {

    private main plugin;
    public skillgui(main plg) {
        this.plugin = plg;
    }
    @EventHandler
    public void onClickGui(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        String inventory_name = ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("inventory"));
        if(e.getInventory().getTitle().equals(inventory_name) && (e.getInventory().getSize() == 27)){
            if(e.getClick().isRightClick()){
                e.setCancelled(true);
            }
            if(e.getClick().isLeftClick()){
                e.setCancelled(true);
            }
            if(e.getClick().isShiftClick()){
                if(e.getSlot() == 10){
                    upLvl(player, ".health", e.getInventory(),10);
                }
                if(e.getSlot() == 11){
                    upLvl(player, ".damage", e.getInventory(),11);
                }
                if(e.getSlot() == 12){
                    upLvl(player, ".speed", e.getInventory(),12);
                }
                if(e.getSlot() == 14){
                    upLvl(player, ".krit", e.getInventory(),14);
                }
                if(e.getSlot() == 15){
                    upLvl(player, ".kritchance", e.getInventory(),15);
                }
                if(e.getSlot() == 16){
                    upLvl(player, ".takeover", e.getInventory(),16);
                }
                if(e.getSlot() == 22){
                    e.getInventory().setItem(22, setOption(player));
                }
                e.setCancelled(true);
            }
        }
    }

    public List<String> replace(List<String> lore, String oldchar, String newchar){
        lore = lore.stream().map(s -> s.replace(oldchar, newchar)).collect(Collectors.toList());
        return lore;
    }
    public ItemStack getExp(Inventory inv, Player player){
        String name = player.getName();
        ItemStack exp = inv.getItem(4);
        ItemMeta expmeta = exp.getItemMeta();
        List<String> explore = (List<String>) this.plugin.getConfig().getList("exp.lore");
        explore = replace(explore, "&", "§");
        explore = replace(explore, "%health%", String.valueOf(main.getMap().getLevel().get(name + ".health")));
        explore = replace(explore, "%damage%", String.valueOf(main.getMap().getLevel().get(name + ".damage")));
        explore = replace(explore, "%speed%", String.valueOf(main.getMap().getLevel().get(name + ".speed")));
        explore = replace(explore, "%krit%", String.valueOf(main.getMap().getLevel().get(name + ".krit")));
        explore = replace(explore, "%kritchance%", String.valueOf(main.getMap().getLevel().get(name + ".kritchance")));
        explore = replace(explore, "%takeover%", String.valueOf(main.getMap().getLevel().get(name + ".takeover")));
        expmeta.setLore(explore);
        exp.setItemMeta(expmeta);
        return exp;
    }
    public ItemStack getItem(Inventory inv,Player player, Integer slot, String lvlname){
        ItemStack item = inv.getItem(slot);
        ItemMeta itemmeta = item.getItemMeta();
        int price_lvl = this.plugin.getConfig().getInt("start_price_exp")
                * (int) main.getMap().getLevel().get(player.getName() + lvlname);
        List<String> itemlore = (List<String>) this.plugin.getConfig().getList(lvlname.substring(1) + ".lore");
        itemlore = replace(itemlore,"&", "§");
        itemlore = replace(itemlore, "%lvl%", String.valueOf(main.getMap().getLevel()
                .get(player.getName() + lvlname)));
        itemlore = replace(itemlore, "%price_lvl%", String.valueOf(price_lvl));
        itemmeta.setLore(itemlore);
        item.setItemMeta(itemmeta);
        return item;
    }
    public void upLvl(Player player, String lvlname, Inventory inv, Integer slot){
        int explevel = player.getLevel();
        int start_price_exp = this.plugin.getConfig().getInt("start_price_exp");
        HashMap<String, Integer> levels = main.getMap().getLevel();
        int lvl = levels.get(player.getName() + lvlname);
        int price_exp = start_price_exp * lvl;
        if (price_exp == 0){
            price_exp = 2;
        }
        if(explevel >= price_exp && lvl != 10){
            player.setLevel(explevel - price_exp);
            levels.put(player.getName() + lvlname, lvl +1);
            main.getData().getPlayers().set(player.getName() + lvlname, lvl + 1);
            main.getData().save();
            float speed = (float) (0.2 + 0.2 * levels.get(player.getName() + ".speed") * 0.03);
            player.setMaxHealth(20 + levels.get(player.getName() + ".health"));
            player.setWalkSpeed(speed);
            inv.setItem(4, getExp(inv, player));
            inv.setItem(slot, getItem(inv, player, slot, lvlname));
            String upgrade = this.plugin.getConfig().getString("messages.upgrade");
            upgrade = upgrade.replace("%lvl%", String.valueOf(levels.get(player.getName() + lvlname)));
            upgrade = upgrade.replace("%skill%", this.plugin.getConfig().getString("messages.replace" + lvlname));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', upgrade));
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT,10, 1);
        } else {
            if(lvl >= 10){
                String maxlvl = this.plugin.getConfig().getString("messages.maxlvl");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', maxlvl));
            } else {
                String noexp = this.plugin.getConfig().getString("messages.noexp");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noexp));
            }
        }
    }
    public ItemStack setOption(Player player){
        String way; ItemStack option;
        int option_value = main.getData().getPlayers().getInt(player.getName() + ".messages");
        if(option_value == 0){
            way = "option_on";
            option = new ItemStack(Material.WATER_BUCKET);
            main.getMap().getLevel().put(player.getName() + ".messages", 1);
            main.getData().getPlayers().set(player.getName() + ".messages", 1);
            main.getData().save();
        } else{
            way = "option_off";
            option = new ItemStack(Material.BUCKET);
            main.getMap().getLevel().put(player.getName() + ".messages", 0);
            main.getData().getPlayers().set(player.getName() + ".messages", 0);
            main.getData().save();
        }
        String option_name =
                ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString(way + ".name"));
        List<String> option_lore = (List<String>) this.plugin.getConfig().getList(way +".lore");
        option_lore = replace(option_lore, "&", "§");
        ItemMeta option_meta = option.getItemMeta();
        option_meta.setLore(option_lore);
        option_meta.setDisplayName(option_name);
        option.setItemMeta(option_meta);
        return option;
    }
}
