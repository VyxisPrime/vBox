package me.vyxisprime.vbox;

import java.util.logging.Logger;

import me.vyxisprime.vbox.handlers.GeneralCommandHandler;
import me.vyxisprime.vbox.handlers.InteractionCommandHandler;

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
	public void onDisable() {
		this.logger.info(frMsg + " " + pFile.getVersion() + " has been succesfully disabled!");
	}

	public void onEnable() {
		this.logger.info(frMsg + " " + pFile.getVersion() + " has been succesfully enabled!");
	}
	//CommandHandler in seperate file.
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args){
		InteractionCommandHandler.registerCommands(sender, cmd, lbl, args);
		GeneralCommandHandler.registerCommands(sender,cmd,lbl,args);
		return false;
	}
}