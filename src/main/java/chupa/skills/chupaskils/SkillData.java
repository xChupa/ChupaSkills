package chupa.skills.chupaskils;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.UUID;

class SkillData implements Listener {
	private static SkillData instance;
	private final File datafile;
	private final FileConfiguration config;
    private final HashMap<UUID,EnumMap<SkillType,Byte>> levels = new HashMap<UUID,EnumMap<SkillType,Byte>>();
    protected SkillData(File datafile) {
    	instance = this;
    	this.datafile = datafile;
        if(!datafile.exists()) {
            try {
            	datafile.createNewFile();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(datafile);
        for(Player player : Bukkit.getOnlinePlayers()) {
        	UUID uuid = player.getUniqueId();
            EnumMap<SkillType,Byte> playerlevels = new EnumMap<SkillType,Byte>(SkillType.class);
            for(SkillType skilltype : SkillType.values()) {
            	playerlevels.put(skilltype, (byte)config.getInt(uuid + "." + skilltype.name().toLowerCase(),skilltype.defaultvalue));
            }
            levels.put(uuid, playerlevels);
            update(player, SkillType.HEALTH);
            update(player, SkillType.DAMAGE);
            update(player, SkillType.SPEED);
        }
    }
    
    @EventHandler
    public void Join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        EnumMap<SkillType,Byte> playerlevels = new EnumMap<SkillType,Byte>(SkillType.class);
        for(SkillType skilltype : SkillType.values()) {
        	playerlevels.put(skilltype, (byte)config.getInt(uuid + "." + skilltype.name().toLowerCase(),skilltype.defaultvalue));
        }
        levels.put(uuid, playerlevels);
        update(player, SkillType.HEALTH);
        update(player, SkillType.DAMAGE);
        update(player, SkillType.SPEED);
    }
    @EventHandler
    public void Quit(PlayerQuitEvent e){
    	levels.remove(e.getPlayer().getUniqueId());
    }
    protected static byte get(Player player,SkillType skilltype) {
    	return player!=null&&skilltype!=null&&instance.levels.containsKey(player.getUniqueId())?instance.levels.get(player.getUniqueId()).get(skilltype):skilltype.defaultvalue;
    }
    protected static void set(Player player,SkillType skilltype,byte level) {
    	if(player==null&&skilltype==null) {
    		return;
    	}
    	UUID uuid = player.getUniqueId();
    	if(level<0) level=0;
    	if(level>skilltype.maxlevel) level=skilltype.maxlevel;
    	EnumMap<SkillType,Byte> playerlevels = instance.levels.containsKey(uuid)?instance.levels.get(uuid):new EnumMap<SkillType,Byte>(SkillType.class);
    	playerlevels.put(skilltype, level);
    	instance.levels.put(uuid, playerlevels);
    }
    protected static void update(Player player,SkillType skilltype) {
		byte lvl = SkillData.get(player, skilltype);
    	switch(skilltype) {
    	case HEALTH: {
    		ArrayList<AttributeModifier> toremovemodifiers = new ArrayList<AttributeModifier>();
        	AttributeInstance healthattrib = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            for(AttributeModifier modifier : healthattrib.getModifiers()) {
            	if(modifier.getName().equals("chupaskill_health")) {
            		toremovemodifiers.add(modifier);
            	}
            }
            if(lvl>0) {
            	healthattrib.addModifier(new AttributeModifier("chupaskill_health", SkillData.get(player, SkillType.HEALTH), Operation.ADD_NUMBER));
            }
            for(AttributeModifier modifier : toremovemodifiers) {
            	healthattrib.removeModifier(modifier);
            }
            return;
    	}
    	case DAMAGE: {
    		ArrayList<AttributeModifier> toremovemodifiers = new ArrayList<AttributeModifier>();
            AttributeInstance damageattrib = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            for(AttributeModifier modifier : damageattrib.getModifiers()) {
            	if(modifier.getName().equals("chupaskill_damage")) {
            		toremovemodifiers.add(modifier);
            	}
            }
            if(lvl>0) {
            	damageattrib.addModifier(new AttributeModifier("chupaskill_damage", lvl*0.25, Operation.ADD_NUMBER));
            }
            for(AttributeModifier modifier : toremovemodifiers) {
            	damageattrib.removeModifier(modifier);
            }
            return;
    	}
    	case SPEED: {
    		player.setWalkSpeed(0.2f+0.2f*lvl*0.03f);
            return;
    	}
    	default: return;
    	}
    }
    protected static void save(Player player){
        UUID uuid = player.getUniqueId();
        EnumMap<SkillType,Byte> levels = instance.levels.containsKey(uuid)?instance.levels.get(uuid):new EnumMap<SkillType,Byte>(SkillType.class);
        FileConfiguration config = instance.config;
        for(SkillType skilltype : SkillType.values()) {
        	byte level = levels.containsKey(skilltype)?levels.get(skilltype):skilltype.defaultvalue;
        	config.set(uuid + "." + skilltype.name().toLowerCase(), level);
        }
        try{
        	instance.config.save(instance.datafile);
        } catch (IOException e){
        	e.printStackTrace();
        }
    }
}
