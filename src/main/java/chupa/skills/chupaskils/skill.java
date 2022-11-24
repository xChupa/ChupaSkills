package chupa.skills.chupaskils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class skill implements CommandExecutor {

    private main plugin;
    static ItemStack health = new ItemStack(Material.TOTEM);
    static ItemStack damage = new ItemStack(Material.IRON_SWORD);
    static ItemStack krit = new ItemStack(Material.REDSTONE);
    static ItemStack kritchance = new ItemStack(Material.BLAZE_POWDER);
    static ItemStack speed = new ItemStack(Material.IRON_BOOTS);
    static ItemStack takeover = new ItemStack(Material.IRON_CHESTPLATE);
    static ItemStack exp = new ItemStack(Material.EXP_BOTTLE);
    public skill(main plg) {
        this.plugin = plg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (player.hasPermission("ChupaSkill.skill")) {
            String invenory_name =
                    ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("inventory"));
            Inventory iskills = Bukkit.createInventory(player, 27, invenory_name);
            setBoard(iskills);
            iskills.setItem(4, getStatistics(exp, player));
            iskills.setItem(10, getItem(health, player, "health"));
            iskills.setItem(11, getItem(damage, player, "damage"));
            iskills.setItem(12, getItem(speed, player, "speed"));
            iskills.setItem(14, getItem(krit, player, "krit"));
            iskills.setItem(15, getItem(kritchance, player, "kritchance"));
            iskills.setItem(16, getItem(takeover, player, "takeover"));
            iskills.setItem(22, getOption(player));
            player.openInventory(iskills);
            return true;
        }
        return true;
    }
    public List<String> replace(List<String> lore, String oldchar, String newchar){
        lore = lore.stream().map(s -> s.replace(oldchar, newchar)).collect(Collectors.toList());
        return lore;
    }
    public Integer getPriceLvl(Player player, String lvlname){
        int price_lvl = this.plugin.getConfig().getInt("start_price_exp")
                * (int) main.getMap().getLevel().get(player.getName() + lvlname);
        if (price_lvl == 0){
            price_lvl = 2;
        }
        return  price_lvl;
    }
    public void setBoard(Inventory inv){
        ItemStack bord = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta bordmeta = bord.getItemMeta();
        bordmeta.setDisplayName(" ");
        bord.setItemMeta(bordmeta);
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, bord);
        }
    }
    public ItemStack getItem(ItemStack item, Player player, String skill){
        int lvl = (int) main.getMap().getLevel().get(player.getName() + "." + skill);
        String itemname =
                ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString(skill + ".name"));
        List<String> itemlore = (List<String>) this.plugin.getConfig().getList(skill + ".lore");
        itemlore = replace(itemlore, "&", "§");
        itemlore = replace(itemlore, "%lvl%", String.valueOf(lvl));
        itemlore = replace(itemlore, "%price_lvl%", String.valueOf(getPriceLvl(player,"." + skill)));
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemmeta.setDisplayName(itemname);
        itemmeta.setLore(itemlore);
        item.setItemMeta(itemmeta);
        return item;
    }
    public ItemStack getStatistics(ItemStack item, Player player){
        HashMap<String, Integer> lvl = main.getMap().getLevel();
        String expname = this.plugin.getConfig().getString("exp.name");
        List<String> explore = (List<String>) this.plugin.getConfig().getList("exp.lore");
        expname = expname.replace("&", "§");
        explore = replace(explore, "&", "§");
        explore = replace(explore, "%health%", String.valueOf(lvl.get(player.getName() + ".health")));
        explore = replace(explore, "%damage%", String.valueOf(lvl.get(player.getName() + ".damage")));
        explore = replace(explore, "%speed%", String.valueOf(lvl.get(player.getName() + ".speed")));
        explore = replace(explore, "%krit%", String.valueOf(lvl.get(player.getName() + ".krit")));
        explore = replace(explore, "%kritchance%", String.valueOf(lvl.get(player.getName() + ".kritchance")));
        explore = replace(explore, "%takeover%", String.valueOf(lvl.get(player.getName() + ".takeover")));
        ItemMeta expmeta = exp.getItemMeta();
        expmeta.setDisplayName(expname);
        expmeta.setLore(explore);
        exp.setItemMeta(expmeta);
        return item;
    }
    public ItemStack getOption(Player player){
        String way; ItemStack option;
        int option_value = main.getData().getPlayers().getInt(player.getName() + ".messages");
        if(option_value == 1){
            way = "option_on";
            option = new ItemStack(Material.WATER_BUCKET);
        } else {
            way = "option_off";
            option = new ItemStack(Material.BUCKET);
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
