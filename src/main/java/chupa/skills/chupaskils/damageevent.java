package chupa.skills.chupaskils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class damageevent implements Listener {

    private main plugin;
    public damageevent(main plg){
        this.plugin = plg;
    }
    @EventHandler
    public void Damage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player player = (Player) e.getEntity();
            double start_damage = e.getDamage();
            double damage = start_damage + ((int) main.getMap().getLevel().get(damager.getName() + ".damage")) * 0.25;
            int krit_chance = (int) main.getMap().getLevel().get(damager.getName() + ".kritchance") * 3;
            int takeover_chance = (int) main.getMap().getLevel().get(player.getName() + ".takeover") * 3;
            Random random_krit = new Random();
            Random random_takeover = new Random();
            if (random_takeover.nextInt(100) <= takeover_chance) {
                damage = 0;
                sendMessageDamager(damager,player,"messages.takeover", damage);
                sendMessagePlayer(damager, player, "messages.ttakeover", damage);
            } else if (random_krit.nextInt(100) <= krit_chance){
                double krit_damage = (int) main.getMap().getLevel().get(damager.getName() + ".krit") * 0.1 * damage + damage;
                damage = krit_damage;
                sendMessageDamager(damager,player,"messages.krit", damage);
                sendMessagePlayer(damager, player, "messages.tkrit", damage);
            } else {
                sendMessageDamager(damager,player,"messages.damage", damage);
                sendMessagePlayer(damager, player, "messages.tdamage", damage);
            }
            e.setDamage(damage);
        }
    }
    public void sendMessageDamager(Player damager, Player player, String way, double damage){
        int option_value = main.getData().getPlayers().getInt(damager.getName() + ".messages");
        if(option_value == 1){
            String message = ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString(way));
            message = message.replace("%player%", player.getName());
            message = message.replace("%damage%", String.valueOf(damage));
            damager.sendMessage(message);
        }
    }
    public void sendMessagePlayer(Player damager, Player player, String way, double damage){
        int option_value = main.getData().getPlayers().getInt(player.getName() + ".messages");
        if(option_value == 1){
            String message = ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString(way));
            message = message.replace("%player%", damager.getName());
            message = message.replace("%damage%", String.valueOf(damage));
            player.sendMessage(message);
        }
    }
}