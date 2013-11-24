package me.vyxisprime.vbox;

import java.io.File;
import java.util.logging.Logger;

import me.vyxisprime.vbox.config.ConfigFile;
import me.vyxisprime.vbox.events.Events;
import me.vyxisprime.vbox.handlers.GeneralCommandHandler;
import me.vyxisprime.vbox.handlers.InteractionCommandHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

	private ConfigFile cfg;

	public void onDisable() {
		this.logger.info(frMsg + " " + pFile.getVersion() + " has been succesfully disabled!");
	}

	public void onEnable() {
		this.logger.info(frMsg + "  creating vBox Directory");
		this.cfg = new ConfigFile(getDataFolder());
		try {
			if (!getDataFolder().mkdir()) {
				Runtime.getRuntime().exec("mkdir " + getDataFolder().getAbsolutePath());
			}
		} catch (Exception e) {
			getLogger().severe("Directory for vBox could not be created:");
			e.printStackTrace();
		}
		this.logger.info(frMsg + " creating vBox Config File!");
		File file = new File(getDataFolder(), "messages");
		if (!file.exists()) {
			file.mkdir();
		}
		this.logger.info(frMsg + " loading Config File");
		this.cfg.load(this);
		this.logger.info(frMsg + " loading Events");
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		this.logger.info(frMsg + " " + pFile.getVersion() + " has been succesfully enabled!");
	}

	// CommandHandler in seperate file.
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		InteractionCommandHandler.registerCommands(sender, cmd, lbl, args);
		GeneralCommandHandler.registerCommands(sender, cmd, lbl, args);
		return false;
	}
}
