package chupa.skills.chupaskils;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

class SkillGui implements Listener {
	private static final String invid = "§2§e§0§8§r";
    @EventHandler
    public void onClickGui(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        Inventory inv = event.getView().getTopInventory();
        if(inv == null || !event.getView().getTitle().startsWith(invid) || inv.getSize() != 27 || event.getClickedInventory()!=inv) {
        	return;
        }
        event.setCancelled(true);
    	if(!event.getClick().isShiftClick()) {
            return;
        }
        byte slot = (byte)event.getSlot();
        if(slot == 22) {
        	SkillData.set(player, SkillType.MESSAGES, SkillData.get(player, SkillType.MESSAGES)==0?(byte)1:(byte)0);
        	SkillData.save(player);
        	inv.setItem(22, getOption(player));
        	return;
        }
    	SkillType skilltype = SkillType.getByMenuplace(slot);
        if(skilltype==null) {
        	return;
        }
        byte lvl = SkillData.get(player, skilltype);
        if(lvl>=skilltype.maxlevel) {
        	player.sendMessage(LangOptions.maxlvl.getMsg(player));
            return;
        }
        int exp = Experience.getExp(player);
        int price_exp = Experience.getExpFromLevel(getPriceLevels(lvl));
        if(exp < price_exp){
        	player.sendMessage(LangOptions.noexp.getMsg(player));
            return;
        }
        exp-=price_exp;
        Experience.setExp(player, exp);
        
        SkillData.set(player, skilltype, ++lvl);
        SkillData.save(player);
        SkillData.update(player, skilltype);
        
        inv.setItem(4, getStatistics(player));
        inv.setItem(slot, getItem(inv.getItem(slot), player, skilltype));
        
    	String skillname = skilltype.name().toLowerCase();
    	try {
    		player.sendMessage(LangOptions.valueOf("upgrade_" + skillname).getMsg(player, new LangOptions.Placeholders("%lvl%", Byte.toString(lvl))));
    	} catch (IllegalArgumentException ex) {
    	}
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT,10, 1);
    }
    
    protected static void sendMenu(Player player) {
    	Inventory iskills = Bukkit.createInventory(player, 27, invid.concat(LangOptions.inventory_name.getMsg(player)));
    	setBoard(iskills);
        iskills.setItem(4, getStatistics(player));
    	for(SkillType skilltype : SkillType.values()) {
    		if(skilltype.menuplace==-1) {
    			continue;
    		}
    		iskills.setItem(skilltype.menuplace, getItem(new ItemStack(skilltype.item), player, skilltype));
    	}
        iskills.setItem(22, getOption(player));
        player.openInventory(iskills);
    }
    private static Integer getPriceLevels(byte lvl) {
        int level = ChupaSkils.start_price_xp;
        if (level<1){
        	level=1;
        }
        ++lvl;
        level*=lvl;
        return level;
    }
    private static void setBoard(Inventory inv) {
        ItemStack bord = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta bordmeta = bord.getItemMeta();
        bordmeta.setDisplayName(" ");
        bord.setItemMeta(bordmeta);
        for (byte i = 0; i < 27; i++) {
            inv.setItem(i, bord);
        }
    }
    private static ItemStack getItem(ItemStack item, Player player, SkillType skill) {
    	byte lvl = SkillData.get(player,skill);
    	String skillname = skill.name().toLowerCase();
    	try {
    		String invitem = "inventory_item_" + skillname;
    		ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.setDisplayName(LangOptions.valueOf(invitem + "_name").getMsg(player));
            meta.setLore(LangOptions.valueOf(invitem + "_lore").getMsgs(player, new LangOptions.Placeholders("%lvl%", Byte.toString(lvl)), new LangOptions.Placeholders("%price_lvl%", Integer.toString(getPriceLevels(lvl)))));
    		item.setItemMeta(meta);
    	} catch (IllegalArgumentException e) {
    	}
        return item;
    }
	private static ItemStack getStatistics(Player player) {
    	LangOptions.Placeholders[] placeholders = new LangOptions.Placeholders[SkillType.values().length];
        byte i=0;
        for(SkillType skilltype : SkillType.values()) {
        	if(skilltype.menuplace==-1) {
        		continue;
        	}
        	placeholders[i] = new LangOptions.Placeholders("%" + skilltype.name().toLowerCase() + "%", Byte.toString(SkillData.get(player,skilltype)));
        	++i;
        }
        placeholders = Arrays.copyOf(placeholders, i);

        ItemStack exp = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta meta = exp.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(LangOptions.inventory_item_exp_name.getMsg(player));
        meta.setLore(LangOptions.inventory_item_exp_lore.getMsgs(player, placeholders));
        exp.setItemMeta(meta);
        return exp;
    }
    private static ItemStack getOption(Player player) {
    	ItemStack item;
        if(SkillData.get(player, SkillType.MESSAGES) == 1){
            item = new ItemStack(Material.WATER_BUCKET);
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.setDisplayName(LangOptions.inventory_item_optionon_name.getMsg(player));
            meta.setLore(LangOptions.inventory_item_optionon_lore.getMsgs(player));
        	item.setItemMeta(meta);
        } else {
        	item = new ItemStack(Material.BUCKET);
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        	meta.setDisplayName(LangOptions.inventory_item_optionoff_name.getMsg(player));
        	meta.setLore(LangOptions.inventory_item_optionoff_lore.getMsgs(player));
        	item.setItemMeta(meta);
        }
        return item;
    }
}
