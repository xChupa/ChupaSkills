package chupa.skills.chupaskils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class joinevent implements Listener {

    private main plugin;
    static HashMap<String, Integer> lvl = new HashMap<>();
    public joinevent(main plg) {
        this.plugin = plg;
    }
    @EventHandler
    public void Join(PlayerJoinEvent e){
        Player player = e.getPlayer();
        String name = player.getDisplayName();
        if(!(main.getData().getPlayers().isSet(name))){
            main.getData().getPlayers().set(name + ".health", 0);
            main.getData().getPlayers().set(name + ".damage", 0);
            main.getData().getPlayers().set(name + ".speed", 0);
            main.getData().getPlayers().set(name + ".krit", 0);
            main.getData().getPlayers().set(name + ".kritchance", 0);
            main.getData().getPlayers().set(name + ".takeover", 0);
            main.getData().getPlayers().set(name + ".messages", 1);
            main.getData().save();
            player.setMaxHealth(20);
        }
        lvl.put(name + ".health", main.getData().getPlayers().getInt(name + ".health"));
        lvl.put(name + ".damage", main.getData().getPlayers().getInt(name + ".damage"));
        lvl.put(name + ".speed", main.getData().getPlayers().getInt(name + ".speed"));
        lvl.put(name + ".krit", main.getData().getPlayers().getInt(name + ".krit"));
        lvl.put(name + ".kritchance", main.getData().getPlayers().getInt(name + ".kritchance"));
        lvl.put(name + ".takeover", main.getData().getPlayers().getInt(name + ".takeover"));
        lvl.put(name + ".messages", main.getData().getPlayers().getInt(name + ".messages"));
        float speed = (float) (0.2 + 0.2 * lvl.get(name + ".speed") * 0.03);
        player.setWalkSpeed(speed);
        player.setMaxHealth(20 + lvl.get(name + ".health"));
    }

    @EventHandler
    public void Quit(PlayerQuitEvent e){
        lvl.remove(e.getPlayer().getName() + ".health");
        lvl.remove(e.getPlayer().getName() + ".damage");
        lvl.remove(e.getPlayer().getName() + ".speed");
        lvl.remove(e.getPlayer().getName() + ".krit");
        lvl.remove(e.getPlayer().getName() + ".kritchance");
        lvl.remove(e.getPlayer().getName() + ".takeover");
        lvl.remove(e.getPlayer().getName() + ".messages");
    }

    public HashMap getLevel(){
        return lvl;
    }
}
