package chupa.skills.chupaskils;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;


public final class ChupaSkils extends JavaPlugin {

	protected static int start_price_xp = 0;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        start_price_xp = getConfig().getInt("start_price_exp",0);
        getCommand("skill").setExecutor(new SkillCommand());
        getServer().getPluginManager().registerEvents(new SkillGui(), this);
        getServer().getPluginManager().registerEvents(new SkillData(new File(getDataFolder(),"players.yml")), this);
        getServer().getPluginManager().registerEvents(new DamageHandler(), this);
        
        LangOptions.values();
    }
}
