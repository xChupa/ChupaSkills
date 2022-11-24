package chupa.skills.chupaskils;

import org.bukkit.plugin.java.JavaPlugin;


public final class main extends JavaPlugin {

    private static main instance;
    private dbskills data;
    private joinevent level;

    public static main getInstance() {
        return instance;
    }

    public static joinevent getMap(){
        return instance.level;
    }
    public static dbskills getData(){
        return instance.data;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        data = new dbskills("players.yml");
        level = new joinevent(this);

        getCommand("skill").setExecutor(new skill(this));
        getServer().getPluginManager().registerEvents(new skillgui(this), this);
        getServer().getPluginManager().registerEvents(new joinevent(this), this);
        getServer().getPluginManager().registerEvents(new damageevent(this), this);

        System.out.println("Chupa: Plugin ChupaSkills on enabled");
    }
    @Override
    public void onDisable() {
        System.out.println("Chupa: Plugin ChupaSkills on disabled");
    }
}
