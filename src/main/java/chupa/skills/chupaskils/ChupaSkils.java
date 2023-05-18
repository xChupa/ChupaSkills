package chupa.skills.chupaskils;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public final class ChupaSkils extends JavaPlugin {

	private static ChupaSkils instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getCommand("skill").setExecutor(new SkillCommand());
        getServer().getPluginManager().registerEvents(new SkillGui(), this);
        getServer().getPluginManager().registerEvents(new SkillData(new File(getDataFolder(),"players.yml")), this);
        getServer().getPluginManager().registerEvents(new DamageHandler(), this);
        
        LangOptions.values();
    }
    protected static FileConfiguration config() {
    	return instance.getConfig();
    }
}
