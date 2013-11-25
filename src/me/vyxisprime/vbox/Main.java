package me.vyxisprime.vbox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.vyxisprime.vbox.db.MySQLDatabase;
import me.vyxisprime.vbox.events.Events;
import me.vyxisprime.vbox.handlers.GeneralCommandHandler;
import me.vyxisprime.vbox.handlers.InteractionCommandHandler;
import me.vyxisprime.vbox.listeners.CapsLockListener;
import me.vyxisprime.vbox.listeners.CurseWordListener;
import me.vyxisprime.vbox.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public boolean useMySQL;
	public final static Logger logger = Logger.getLogger("Minecraft");
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
	static boolean dev = false;
	public static String maindir = "plugins/vBox/";
	public static final Logger log = Logger.getLogger("Minecraft");
	private final PlayerListener playerListener = new PlayerListener(this);
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<String> bannedNubs = new ArrayList();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<String> bannedIPs = new ArrayList();
	public String broadcastBan;
	public String broadcastKick;
	public String broadcastTempBan;
	public String broadcastUnban;
	public String broadcastWarn;
	public String userKick;
	public String userBan;
	public String userTempBan;
	public String userIPBan;
	public String userWarn;

	public int maxWarnings;
	public MySQLDatabase db;

	@SuppressWarnings("static-access")
	public void onDisable() {
		this.logger.info(frMsg + " " + pFile.getVersion() + " has been succesfully disabled!");
	}

	@SuppressWarnings("static-access")
	public void onEnable() {
		this.logger.info(frMsg + " loading Config File");
		loadConfig();
		this.logger.info(frMsg + " loading Events & Listeners");
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		Bukkit.getPluginManager().registerEvents(new CapsLockListener(this), this);
		Bukkit.getPluginManager().registerEvents(new CurseWordListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		this.logger.info(frMsg + " setting up MySQL Database");
		this.db = new MySQLDatabase(this);
		this.logger.info(frMsg + " " + pFile.getVersion() + " has been succesfully enabled!");
	}

	// CommandHandler in seperate file.
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		InteractionCommandHandler.registerCommands(sender, cmd, lbl, args);
		GeneralCommandHandler.registerCommands(sender, cmd, lbl, args);
		return false;
	}

	@SuppressWarnings("static-access")
	public boolean loadConfig() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			this.logger.info(frMsg + "creating Config File!");
			saveDefaultConfig();
		}
		reloadConfig();
		getStrings();
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

	public void getStrings() {
		this.broadcastBan = plugin.getConfig().getString("broadcast.Ban", "&6Player &e%victim%&6 was banned by &e%admin%&6! Reason: &e%reason%");
		this.broadcastKick = plugin.getConfig().getString("broadcast.Kick", "&6Player &e%victim%&6 was kicked by &e%admin%&6! Reason: &e%reason%");
		this.broadcastTempBan = plugin.getConfig().getString("broadcast.TempBan", "&6Player &e%victim%&6 was temp-banned by &e%admin%&6 for &e%time% &6Reason: &e%reason%");
		this.broadcastUnban = plugin.getConfig().getString("broadcast.Unban", "&e%victim%&6 was unbanned by &e%admin%&6!");
		this.broadcastWarn = plugin.getConfig().getString("broadcast.warn", "&e%victim%&6 was warned by &e%admin%&6. Reason: &e%reason%");

		this.userKick = plugin.getConfig().getString("user.Kick", "&6You were kicked by &e%admin%. &6Reason: &e%reason%");
		this.userBan = plugin.getConfig().getString("user.Ban", "&6You were banned by &e%admin%. &6Reason: &e%reason%");
		this.userTempBan = plugin.getConfig().getString("user.TempBan", "&6You were temp-banned by %admin% for %time% Reason: &e%reason%");
		this.userIPBan = plugin.getConfig().getString("user.IPBan", "&6Your IP is banned.");
		this.useMySQL = plugin.getConfig().getBoolean("use-mysql", true);
		this.userWarn = plugin.getConfig().getString("user.warn", "&6You were warned by &e%admin%&6 Reason &e%reason%");
		this.maxWarnings = plugin.getConfig().getInt("max-warnings", 3);

		String mysqlDatabase = plugin.getConfig().getString("mysql-database", "jdbc:mysql://localhost:3306/minecraft");
		String mysqlUser = plugin.getConfig().getString("mysql-user", "root");
		String mysqlPassword = plugin.getConfig().getString("mysql-password", "root");

		plugin.getConfig().set("use-mysql", Boolean.valueOf(this.useMySQL));
		plugin.getConfig().set("max-warnings", Integer.valueOf(this.maxWarnings));
		plugin.getConfig().set("mysql-database", mysqlDatabase);
		plugin.getConfig().set("mysql-user", mysqlUser);
		plugin.getConfig().set("mysql-password", mysqlPassword);
		plugin.getConfig().set("broadcast.ban", this.broadcastBan);
		plugin.getConfig().set("broadcast.Kick", this.broadcastKick);
		plugin.getConfig().set("broadcast.TempBan", this.broadcastTempBan);
		plugin.getConfig().set("broadcast.Unban", this.broadcastUnban);
		plugin.getConfig().set("broadcast.warn", this.broadcastWarn);
		plugin.getConfig().set("user.Kick", this.userKick);
		plugin.getConfig().set("user.ban", this.userBan);
		plugin.getConfig().set("user.TempBan", this.userTempBan);
		plugin.getConfig().set("user.IPban", this.userIPBan);
		plugin.getConfig().set("user.warn", this.userWarn);
		saveConfig();
	}
}
