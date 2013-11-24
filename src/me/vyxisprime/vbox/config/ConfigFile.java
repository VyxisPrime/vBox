package me.vyxisprime.vbox.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.vyxisprime.vbox.api.vConfig;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigFile implements vConfig {

	private FileConfiguration config;
	private final File configFile;

	public ConfigFile(File dataFolder) {
		this.configFile = new File(dataFolder, "config.yml");
	}

	@Override
	public boolean isGroupSpecific() {
		return this.config.getBoolean("motd.group-specific");
	}

	@Override
	public boolean isWorldSpecific() {
		return this.config.getBoolean("motd.world-specific");
	}
	
	public void load(Plugin plugin){
		if(this.configFile.exists()){
			this.config = YamlConfiguration.loadConfiguration(this.configFile);
		}else{
			this.config = YamlConfiguration.loadConfiguration(plugin.getResource("config.yml"));
			save();
		}
	}
	public void save(){
		if(!this.configFile.exists()){
			try{
				this.configFile.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		try{
			this.config.save(this.configFile);;
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	  public ChatColor[] getRainbowColors()
	  {
	    List<String> colors = this.config.getStringList("rainbow-colors");
	    if (colors == null) {
	      return new ChatColor[] { ChatColor.RED, ChatColor.GREEN, ChatColor.BLUE };
	    }
	    ChatColor[] result = new ChatColor[colors.size()];
	    int i = 0;
	    for (String s : colors)
	    {
	      ChatColor cc = ChatColor.valueOf(s.toUpperCase());
	      result[i] = cc;
	      i++;
	    }
	    return result;
	  }
	
}
