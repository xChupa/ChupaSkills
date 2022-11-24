package chupa.skills.chupaskils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class dbskills {
    private File file;
    private FileConfiguration config;

    public dbskills(String name){
        file = new File(main.getInstance().getDataFolder(), name);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public FileConfiguration getPlayers(){
        return config;
    }
    public void save(){
        try{
            config.save(file);
        } catch (IOException e){
            throw new RuntimeException("File saved players.yml", e);
        }
    }
}
