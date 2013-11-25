package me.vyxisprime.vbox;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import me.vyxisprime.vbox.events.Events;
import me.vyxisprime.vbox.handlers.GeneralCommandHandler;
import me.vyxisprime.vbox.handlers.InteractionCommandHandler;
import me.vyxisprime.vbox.listeners.CapsLockListener;
import me.vyxisprime.vbox.listeners.CurseWordListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");
	public static Main plugin;
	ChatColor darkRed = ChatColor.DARK_RED;
	ChatColor darkBlue = ChatColor.DARK_BLUE;
	ChatColor darkGray = ChatColor.DARK_GRAY;
	ChatColor darkAqua = ChatColor.DARK_AQUA;
	ChatColor darkGreen = ChatColor.DARK_GREEN;
	ChatColor darkPurple = ChatColor.DARK_PURPLE;
	ChatColor red = ChatColor.RED;
	ChatColor blue = ChatColor.BLUE;
	ChatColor black = ChatColor.BLACK;
	ChatColor gray = ChatColor.GRAY;
	ChatColor gold = ChatColor.GOLD;
	ChatColor green = ChatColor.GREEN;
	ChatColor aqua = ChatColor.AQUA;
	ChatColor pink = ChatColor.LIGHT_PURPLE;
	ChatColor yellow = ChatColor.YELLOW;
	ChatColor reset = ChatColor.RESET;
	ChatColor bold = ChatColor.BOLD;
	ChatColor white = ChatColor.WHITE;
	public String frMsg = this.white + "[" + this.green + "vBox" + this.white + "]";
	PluginDescriptionFile pFile = getDescription();

	public Configuration config;
	public boolean enableNoCaps;
	public boolean enableMessageOverride;
	public boolean enablePlayerMessage;
	public boolean enableForceLowercase;
	public String overrideMessage;
	public String playerMessage;
	public List<String> whitelist;
	public int capsInRow;
	public int totalCapsPercentage;

	public void onDisable() {
		this.logger.info(frMsg + " " + pFile.getVersion() + " has been succesfully disabled!");
	}

	public void onEnable() {
		this.logger.info(frMsg + " loading Config File");
		loadConfig();
		this.logger.info(frMsg + " loading Events");
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		Bukkit.getPluginManager().registerEvents(new CapsLockListener(this), this);
		Bukkit.getPluginManager().registerEvents(new CurseWordListener(), this);
		this.logger.info(frMsg + " " + pFile.getVersion() + " has been succesfully enabled!");
	}

	// CommandHandler in seperate file.
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		InteractionCommandHandler.registerCommands(sender, cmd, lbl, args);
		GeneralCommandHandler.registerCommands(sender, cmd, lbl, args);
		return false;
	}

	public boolean loadConfig() {
		 if (!new File(getDataFolder(), "config.yml").exists()) {
			 this.logger.info(frMsg + "creating Config File!");
		      saveDefaultConfig();
		    }
		reloadConfig();
		if (this.config.isBoolean("enableNoCaps")) {
			this.enableNoCaps = this.config.getBoolean("enableNoCaps");
		} else {
			return false;
		}
		if (this.config.isBoolean("enableMessageOverride")) {
			this.enableMessageOverride = this.config.getBoolean("enableMessageOverride");
		} else {
			return false;
		}
		if (this.config.isBoolean("enablePlayerMessage")) {
			this.enablePlayerMessage = this.config.getBoolean("enablePlayerMessage");
		} else {
			return false;
		}
		if (this.config.isBoolean("enableForceLowercase")) {
			this.enableForceLowercase = this.config.getBoolean("enableForceLowercase");
		} else {
			return false;
		}
		if (this.config.isString("overrideMessage")) {
			this.overrideMessage = this.config.getString("overrideMessage");
		} else {
			return false;
		}
		if (this.config.isString("playerMessage")) {
			this.playerMessage = this.config.getString("playerMessage");
		} else {
			return false;
		}
		if (this.config.isInt("capsInRow")) {
			this.capsInRow = this.config.getInt("capsInRow");
		} else {
			return false;
		}
		if ((this.config.isInt("totalCapsPercentage")) && (this.config.getInt("totalCapsPercentage") >= 0)) {
			this.totalCapsPercentage = this.config.getInt("totalCapsPercentage");
		} else {
			return false;
		}
		if (this.config.isList("whitelist")) {
			this.whitelist = this.config.getStringList("whitelist");
		} else {
			return false;
		}
		return true;
	}
}
