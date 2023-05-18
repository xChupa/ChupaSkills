package chupa.skills.chupaskils;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

class DamageHandler implements Listener {

    @EventHandler
    public void Damage(EntityDamageByEntityEvent e) {
    	byte random = (byte) new Random().nextInt(100);
    	double damage = e.getDamage();
    	if(e.getEntity().getType() == EntityType.PLAYER) {
    		Player player = (Player) e.getEntity();
    		if (random < SkillData.get(player,SkillType.TAKEOVER) * 3) {
    			e.setCancelled(true);
    			if(e.getDamager().getType().equals(EntityType.PLAYER)) {
    				Player damager = (Player) e.getDamager();
    				if(SkillData.get(damager, SkillType.MESSAGES) == 1) {
    					damager.sendMessage(LangOptions.takeover.getMsg(damager, new LangOptions.Placeholders("%player%", player.getName()),new LangOptions.Placeholders("%damage%", Double.toString(damage))));
    	            }
    	            if(SkillData.get(player, SkillType.MESSAGES) == 1) {
    	            	player.sendMessage(LangOptions.ttakeover.getMsg(player, new LangOptions.Placeholders("%player%", damager.getName()),new LangOptions.Placeholders("%damage%", Double.toString(damage))));
    	            }
    			}
                return;
    		}
    	}
    	if(e.getDamager().getType() == EntityType.PLAYER) {
    		Player damager = (Player) e.getDamager();
        	if (random <= SkillData.get(damager,SkillType.KRITCHANCE) * 3){
                damage += SkillData.get(damager,SkillType.KRIT) * 0.1 * damage;
                if(e.getEntity().getType().equals(EntityType.PLAYER)) {
            		Player player = (Player) e.getEntity();
                	if(SkillData.get(damager, SkillType.MESSAGES) == 1) {
        				damager.sendMessage(LangOptions.krit.getMsg(damager, new LangOptions.Placeholders("%player%", player.getName()),new LangOptions.Placeholders("%damage%", Double.toString(damage))));
                    }
                    if(SkillData.get(player, SkillType.MESSAGES) == 1) {
                    	player.sendMessage(LangOptions.tkrit.getMsg(player, new LangOptions.Placeholders("%player%", damager.getName()),new LangOptions.Placeholders("%damage%", Double.toString(damage))));
                    }
                }
                return;
        	}
    	}

    	if(e.getDamager().getType() != EntityType.PLAYER || e.getEntity().getType() != EntityType.PLAYER) {
    		return;
    	}
    	Player damager = (Player) e.getDamager(),player = (Player) e.getEntity();
        
        if(SkillData.get(damager, SkillType.MESSAGES) == 1) {
			damager.sendMessage(LangOptions.damage.getMsg(damager, new LangOptions.Placeholders("%player%", player.getName()),new LangOptions.Placeholders("%damage%", Double.toString(damage))));
        }
        if(SkillData.get(player, SkillType.MESSAGES) == 1) {
        	player.sendMessage(LangOptions.tdamage.getMsg(player, new LangOptions.Placeholders("%player%", damager.getName()),new LangOptions.Placeholders("%damage%", Double.toString(damage))));
        }
        e.setDamage(damage);
    }
}