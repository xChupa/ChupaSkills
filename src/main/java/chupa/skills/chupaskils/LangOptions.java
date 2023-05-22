package chupa.skills.chupaskils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

enum LangOptions {noperm,commandforplayers,upgrade_health,upgrade_damage,upgrade_speed,upgrade_krit,upgrade_kritchance,upgrade_takeover,maxlvl,noexp,damage,tdamage,krit,tkrit,takeover,ttakeover,inventory_name,inventory_item_exp_name,inventory_item_health_name,inventory_item_damage_name,inventory_item_speed_name,inventory_item_krit_name,inventory_item_kritchance_name,inventory_item_takeover_name,inventory_item_exp_lore,inventory_item_health_lore,inventory_item_damage_lore,inventory_item_speed_lore,inventory_item_krit_lore,inventory_item_kritchance_lore,inventory_item_takeover_lore,inventory_item_optionon_name,inventory_item_optionon_lore,inventory_item_optionoff_name,inventory_item_optionoff_lore;
	static {
		JavaPlugin plugin = JavaPlugin.getPlugin(ChupaSkils.class);
		YamlConfiguration alang = null; 
		File langfile = new File(plugin.getDataFolder() + File.separator + "lang.yml");
		if(!langfile.exists()) {
			try {
				byte[] buf = new byte[16192];
				InputStream in = plugin.getResource("lang.yml");
				if (in!=null) {
					buf = Arrays.copyOf(buf, in.read(buf));
					in.close();
					OutputStream out = new FileOutputStream(langfile);
					if(out!=null) {
						out.write(buf);
						out.close();
					}
				}
			} catch (IOException e) {
			}
		}
		alang = YamlConfiguration.loadConfiguration(langfile);
		ConfigurationSection deffaultconfigurationsection = alang.getConfigurationSection("");
		Set<String> aavilablelocales = null;

		if(deffaultconfigurationsection!=null) {
			aavilablelocales = deffaultconfigurationsection.getKeys(false);
			aavilablelocales.add("default");
			for(LangOptions lang : values()) {
				for(String locale : aavilablelocales) {
					String optionpath = locale.concat(".").concat(lang.name().replaceAll("_", "."));
					List<String> msgs = new ArrayList<String>();
					if(alang.isList(optionpath)) {
						for(String line : alang.getStringList(optionpath)) {
							if(line.isEmpty()) {
								msgs.add(line);
								continue;
							}
							char startchar = line.charAt(0),endchar = line.charAt(line.length()-1);
							if(startchar=='['&&endchar==']'||startchar=='{'&&endchar=='}') {
								msgs.add(TextComponent.toLegacyText(ComponentSerializer.parse(line)));
							} else {
								msgs.add(line);
							}
						}
					} else if(alang.isString(optionpath)) {
						String msg = alang.getString(optionpath);
						if(msg.isEmpty()) {
							msgs.add(msg);
						} else {
							char startchar = msg.charAt(0),endchar = msg.charAt(msg.length()-1);
							if(startchar=='['&&endchar==']'||startchar=='{'&&endchar=='}') {
								msgs.add(TextComponent.toLegacyText(ComponentSerializer.parse(msg)));
							} else {
								msgs.add(msg);
							}
						}
					}
					if(msgs.isEmpty()) {
						msgs.add(TextComponent.toLegacyText(ComponentSerializer.parse("[{\"text\":\"ERROR LANG OPTION \",\"bold\":true,\"color\":\"dark_red\"},{\"text\":\"".concat(lang.name()).concat("\",\"bold\":true,\"color\":\"red\"},{\"text\":\" FOR LOCALE \",\"bold\":true,\"color\":\"dark_red\"},{\"text\":\"").concat(locale).concat("\",\"bold\":true,\"color\":\"red\"},{\"text\":\" NOT EXSIST\",\"bold\":true,\"color\":\"dark_red\"}]"))));
					}
					lang.text.put(locale, msgs);
				}
			}
		}
		avilablelocales = aavilablelocales;
	}
	private static final Set<String> avilablelocales;
	private final Map<String,List<String>> text = new HashMap<String,List<String>>();
	
	protected String getMsg(CommandSender target,Placeholders...placeholders) {
		String msg = text.get("default").get(0);
		if(target instanceof Player) {
			String locale = ((Player)target).getLocale().toLowerCase();
			if(avilablelocales.contains(locale)) {
				msg = text.get(locale).get(0);
			}
		}
		if(msg.isEmpty()) {
			return msg;
		}
		for(Placeholders placeholder:placeholders) {
			msg = msg.replaceAll(placeholder.placeholder, placeholder.value);
		}
		return msg;
	}
	protected List<String> getMsgs(CommandSender target,Placeholders...placeholders) {
		List<String> msgs = new ArrayList<String>();
		msgs = text.get("default");
		if(target instanceof Player) {
			String locale = ((Player)target).getLocale().toLowerCase();
			if(avilablelocales.contains(locale)) {
				msgs = text.get(locale);
			}
		}
		ArrayList<String> nmsgs = new ArrayList<String>();
		for(byte i=0;i<msgs.size()&&i!=-128;++i) {
			String msg = msgs.get(i);
			if(msg.isEmpty()) {
				nmsgs.add(msg);
			}
			for(Placeholders placeholder:placeholders) {
				msg = msg.replaceAll(placeholder.placeholder, placeholder.value);
			}
			nmsgs.add(msg);
		}
		return nmsgs;
	}
	protected static class Placeholders {
		protected final String placeholder;
		protected final String value;
		protected Placeholders(String placeholder,String value) {
			this.placeholder = placeholder;
			this.value = value;
		}
	}
}