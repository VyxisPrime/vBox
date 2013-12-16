package me.vyxisprime.vbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.vyxisprime.vbox.bansystem.BanInfo;
import me.vyxisprime.vbox.db.MySQLDatabase;
import me.vyxisprime.vbox.events.Events;
import me.vyxisprime.vbox.handlers.GeneralCommandHandler;
import me.vyxisprime.vbox.handlers.InteractionCommandHandler;
import me.vyxisprime.vbox.listeners.CapsLockListener;
import me.vyxisprime.vbox.listeners.CurseWordListener;
import me.vyxisprime.vbox.listeners.IpListener;
import me.vyxisprime.vbox.listeners.KitListener;
import me.vyxisprime.vbox.listeners.PlayerListener;
import me.vyxisprime.vbox.util.Metrics;
import me.vyxisprime.vbox.util.VersionCheck;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public boolean useMySQL;
	public final static Logger logger = Logger.getLogger("Minecraft");
	public static Main plugin;
	public ChatColor darkRed = ChatColor.DARK_RED;
	public ChatColor darkBlue = ChatColor.DARK_BLUE;
	public ChatColor darkGray = ChatColor.DARK_GRAY;
	public ChatColor darkAqua = ChatColor.DARK_AQUA;
	public ChatColor darkGreen = ChatColor.DARK_GREEN;
	public ChatColor darkPurple = ChatColor.DARK_PURPLE;
	public ChatColor red = ChatColor.RED;
	public ChatColor blue = ChatColor.BLUE;
	public ChatColor black = ChatColor.BLACK;
	public ChatColor gray = ChatColor.GRAY;
	public ChatColor gold = ChatColor.GOLD;
	public ChatColor green = ChatColor.GREEN;
	public ChatColor aqua = ChatColor.AQUA;
	public ChatColor pink = ChatColor.LIGHT_PURPLE;
	public ChatColor yellow = ChatColor.YELLOW;
	public ChatColor reset = ChatColor.RESET;
	public ChatColor bold = ChatColor.BOLD;
	public ChatColor white = ChatColor.WHITE;
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
	@SuppressWarnings("unused")
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

	public VersionCheck vm = new VersionCheck();
	public static Permission perm = null;
	boolean vault = true;
	boolean perms = true;
	public String latestversion;

	@SuppressWarnings("static-access")
	public void onDisable() {
		this.logger.info(frMsg + " " + pFile.getVersion() + " has been succesfully disabled!");
	}

	@SuppressWarnings("static-access")
	public void onEnable() {
		// Version Check
		this.logger.info(frMsg + " checking version");
		boolean vcOn = getConfig().getBoolean("CheckForLatestVersion");
		if (vcOn) {
			VCThread check = new VCThread(this);
			this.logger.info(frMsg + "Checking for newer version!");
			check.start();
		}
		this.logger.info(frMsg + " loading Config File");
		loadConfig();
		this.logger.info(frMsg + " loading Metrics");
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			// Failed to submit the stats :-(
		}
		this.logger.info(frMsg + " loading Events & Listeners");
		Bukkit.getPluginManager().registerEvents(new Events(this), this);
		Bukkit.getPluginManager().registerEvents(new CapsLockListener(this), this);
		Bukkit.getPluginManager().registerEvents(new CurseWordListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new IpListener(this), this);
		Bukkit.getPluginManager().registerEvents(new KitListener(this), this);
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

	private class VC implements Runnable {
		private String message;

		private VC(String message) {
			this.message = message;
		}

		@SuppressWarnings("static-access")
		public void run() {
			Main.this.logger.info(plugin.frMsg + this.message);
		}
	}

	private class VCThread extends Thread {
		private Main skit;

		private VCThread(Main i) {
			this.skit = i;
		}

		@SuppressWarnings({ "static-access", "deprecation" })
		public void run() {
			try {
				Main.this.latestversion = Main.this.vm.getLatestVersion();
				if (Main.this.latestversion == null) {
					Main.this.logger.info("[Main] Could not find a new Version than " + Main.this.getDescription().getVersion() + ".");
				} else if (Main.this.vm.compareVersion(Main.this.latestversion, this.skit.getDescription().getVersion())) {
					this.skit.getServer().getScheduler().scheduleSyncDelayedTask(this.skit, new VC("New version of vBox available: " + Main.this.latestversion + ". You have version " + Main.this.getDescription().getVersion()), 0L);
				}
			} catch (MalformedURLException mue) {
				Main.this.logger.info("[Main] Main could not find a newer Version!");
			}
			stop();
		}
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
		plugin.getConfig().set("user.Ban", this.userBan);
		plugin.getConfig().set("user.TempBan", this.userTempBan);
		plugin.getConfig().set("user.IPban", this.userIPBan);
		plugin.getConfig().set("user.warn", this.userWarn);

		plugin.getConfig().addDefault("IP.Message.NotPermitted", "&cYou're not permitted to spam with IP'S");
		plugin.getConfig().addDefault("IP.Message.KickMessage", "&cYou got kicked for: Spamming with IP's");
		plugin.getConfig().addDefault("IP.Message.BanMessage", "&cYou got banned for: Spamming with IP's");
		plugin.getConfig().addDefault("IP.Action.Kick", Boolean.valueOf(false));
		plugin.getConfig().addDefault("IP.Action.Ban", Boolean.valueOf(false));

		saveConfig();
	}

	public boolean viewWarnings(CommandSender sender, String[] args) {
		String player;
		if (args.length < 1) {
			if (!getPermission(sender, "banreport.warnings.own")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission.");
				return true;
			}
			player = sender.getName();
		} else {
			if (!getPermission(sender, "banreport.warnings.viewall")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission.");
				return true;
			}
			player = expandName(args[0]);
		}
		this.db.viewWarnings(sender, player);
		return true;
	}

	public boolean warnPlayer(CommandSender sender, String[] args) {
		boolean silent = false;
		if (!getPermission(sender, "banreport.warn")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return true;
		}
		if (args.length < 2) {
			return false;
		}
		if (args[1].equals("-s")) {
			silent = true;
		}
		String victim = expandName(args[0]);
		String admin = sender.getName();
		String reason;
		if (silent) {
			if (args.length < 3) {
				return false;
			}
			reason = combineSplit(2, args, " ");
		} else {
			reason = combineSplit(1, args, " ");
		}
		this.db.warnPlayer(victim, admin, reason);

		Player onlineVictim = null;
		onlineVictim = getServer().getPlayer(victim);
		if (onlineVictim != null) {
			String warnMsg = this.userWarn;
			warnMsg = warnMsg.replace("%reason%", reason);
			warnMsg = warnMsg.replace("%admin%", admin);

			onlineVictim.sendMessage(formatMessage(warnMsg));
		}
		if (!silent) {
			String broadcastMsg = this.broadcastWarn;
			broadcastMsg = broadcastMsg.replace("%reason%", reason);
			broadcastMsg = broadcastMsg.replace("%admin%", admin);
			broadcastMsg = broadcastMsg.replace("%victim%", victim);

			getServer().broadcastMessage(formatMessage(broadcastMsg));
		}
		if (this.db.getWarnings(victim) >= this.maxWarnings) {
			if (!this.db.checkBanList(victim)) {
				this.db.addPlayer(victim, "Exceeded Maximum Warnings.", admin, 0L);
				if (onlineVictim != null) {
					onlineVictim.kickPlayer("Exceeded Maximum Warnings.");
				}
			}
		}
		return true;
	}

	public boolean banInfo(CommandSender sender, String[] args) {
		if (!getPermission(sender, "banreport.baninfo")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return true;
		}
		if (args.length < 1) {
			return false;
		}
		String victim = args[0];
		if (!this.db.checkBanList(args[0])) {
			sender.sendMessage(ChatColor.RED + victim + " is not banned!");
			return true;
		}
		String banReason = this.db.getInfo(victim).reason;
		String banAdmin = this.db.getInfo(victim).admin;

		sender.sendMessage(ChatColor.GREEN + "Player " + ChatColor.YELLOW + victim + ChatColor.GREEN + " banned by " + ChatColor.YELLOW + banAdmin);
		sender.sendMessage(ChatColor.RED + banReason);
		HashMap<Integer, BanInfo> banInfo = this.db.getBanInfo(victim);
		if (banInfo.size() < 1) {
			sender.sendMessage(ChatColor.RED + "No additional information found.");
			return true;
		}
		for (int i = 0; i < banInfo.keySet().size(); i++) {
			BanInfo info = (BanInfo) banInfo.get(Integer.valueOf(i));
			sender.sendMessage(ChatColor.GRAY + "[" + (i + 1) + "] " + info.getAdmin() + " @ (" + info.getX() + ", " + info.getY() + ", " + info.getZ() + ") -> " + ChatColor.GOLD + info.getInfo());
		}
		return true;
	}

	public boolean addInfo(CommandSender sender, String[] args) {
		if (!getPermission(sender, "banreport.baninfo")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return true;
		}
		if (args.length < 1) {
			return false;
		}
		String victim = args[0];
		String reason;
		if (args.length < 2) {
			reason = "<Coords>";
		} else {
			reason = combineSplit(1, args, " ");
		}
		this.db.addInfo(sender, victim, reason);
		sender.sendMessage(ChatColor.GREEN + "Info sent!");
		return true;
	}

	public boolean removeInfo(CommandSender sender, String[] args) {
		if (!getPermission(sender, "banreport.baninfo")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return true;
		}
		if (args.length < 2) {
			return false;
		}
		int id = Integer.parseInt(args[1]) - 1;
		String victim = args[0];
		if (((id < 0 ? 1 : 0) | (id > this.db.getBanInfo(victim).size() ? 1 : 0)) != 0) {
			sender.sendMessage(ChatColor.RED + "Invalid ID.");
			return true;
		}
		this.db.removeInfo(((BanInfo) this.db.getBanInfo(victim).get(Integer.valueOf(id))).getID());

		sender.sendMessage(ChatColor.GREEN + "Info removed.");
		return true;
	}

	public Boolean banTp(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("no console tp.");
			return Boolean.valueOf(true);
		}
		if (!getPermission(sender, "banreport.bantp")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return Boolean.valueOf(true);
		}
		if (args.length < 2) {
			return Boolean.valueOf(false);
		}
		Player player = (Player) sender;
		int id = Integer.parseInt(args[1]) - 1;
		String victim = args[0];
		if (((id < 0 ? 1 : 0) | (id > this.db.getBanInfo(victim).size() ? 1 : 0)) != 0) {
			sender.sendMessage(ChatColor.RED + "Invalid ID.");
			return Boolean.valueOf(true);
		}
		BanInfo banInfo = (BanInfo) this.db.getBanInfo(victim).get(Integer.valueOf(id));

		player.teleport(new Location(player.getWorld(), banInfo.getX(), banInfo.getY(), banInfo.getZ()));
		sender.sendMessage(ChatColor.GREEN + "Teleport.");
		return Boolean.valueOf(true);
	}

	public Boolean banPlayer(CommandSender sender, String[] args) {
		if (!getPermission(sender, "banreport.ban")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return Boolean.valueOf(true);
		}
		if (args.length < 1) {
			return Boolean.valueOf(false);
		}
		String victim = expandName(args[0]);
		if (this.db.checkBanList(victim)) {
			sender.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + victim + ChatColor.RED + " is already banned.");
			sender.sendMessage(ChatColor.RED + "Sending baninfo...");
			addInfo(sender, args);
			return Boolean.valueOf(true);
		}
		Boolean silent = Boolean.valueOf(false);
		if ((args.length > 1) && (args[1].equals("-s"))) {
			silent = Boolean.valueOf(true);
		}
		String reason;

		if (!silent.booleanValue()) {
			if (args.length < 2) {
				reason = "undefined.";
			} else {
				reason = combineSplit(1, args, " ");
			}
		} else {
			if (args.length < 3) {
				reason = "undefined.";
			} else {
				reason = combineSplit(2, args, " ");
			}
		}
		this.db.addPlayer(victim, reason, sender.getName(), 0L);

		sender.sendMessage(ChatColor.GREEN + "Successfully banned " + victim + "!");
		Player actualVictim = getServer().getPlayer(victim);
		if (actualVictim != null) {
			String message = this.userBan;
			message = message.replace("%victim%", victim);
			message = message.replace("%admin%", sender.getName());
			message = message.replace("%reason%", reason);
			String IP = actualVictim.getAddress().getAddress().getHostAddress();
			this.db.addPermList(victim, reason, sender.getName(), IP);
			actualVictim.kickPlayer(formatMessage(message));
		} else {
			this.db.addPermList(victim, reason, sender.getName(), "0.0.0.0");
		}
		String message = this.broadcastBan;
		message = message.replace("%victim%", victim);
		message = message.replace("%admin%", sender.getName());
		message = message.replace("%reason%", reason);
		if (!silent.booleanValue()) {
			getServer().broadcastMessage(formatMessage(message));
		}
		log.log(Level.INFO, "[BanReport] " + victim + " was banned by " + sender.getName() + " Reason: " + reason);

		return Boolean.valueOf(true);
	}

	public Boolean tempBan(CommandSender sender, String[] args) {
		if (!getPermission(sender, "banreport.ban")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return Boolean.valueOf(true);
		}
		if (args.length < 2) {
			return Boolean.valueOf(false);
		}
		String victim = expandName(args[0]);
		if (this.db.checkBanList(victim)) {
			sender.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + victim + ChatColor.RED + " is already banned.");
			return Boolean.valueOf(true);
		}
		Boolean silent = Boolean.valueOf(false);
		if (args[1].equals("-s")) {
			silent = Boolean.valueOf(true);
		}
		char unit;
		String reason;
		long time;
		if (!silent.booleanValue()) {

			if (args.length < 3) {
				reason = "undefined.";
			} else {
				reason = combineSplit(2, args, " ");
			}
			String unparsedTime = args[1];
			time = Long.parseLong(unparsedTime.replaceAll("[\\D+]", ""));
			unit = unparsedTime.charAt(unparsedTime.length() - 1);
		} else {
			if (args.length < 4) {
				reason = "undefined.";
			} else {
				reason = combineSplit(3, args, " ");
			}
			String unparsedTime = args[2];
			time = Long.parseLong(unparsedTime.replaceAll("[\\D]", ""));
			unit = unparsedTime.charAt(unparsedTime.length() - 1);
		}
		long temptime = new Date().getTime();
		if (unit == 's') {
			temptime += time * 1000L;
		}
		if (unit == 'm') {
			temptime += time * 1000L * 60L;
		}
		if (unit == 'h') {
			temptime += time * 1000L * 60L * 60L;
		}
		if (unit == 'd') {
			temptime += time * 1000L * 60L * 60L * 24L;
		}
		if (temptime <= new Date().getTime()) {
			sender.sendMessage(ChatColor.RED + "invalid time.");
			return Boolean.valueOf(true);
		}
		this.db.addPlayer(victim, reason, sender.getName(), temptime);

		sender.sendMessage(ChatColor.GREEN + "Successfully tempbanned " + victim + "!");
		Player actualVictim = getServer().getPlayer(victim);
		if (actualVictim != null) {
			String message = this.userTempBan;
			message = message.replace("%victim%", victim);
			message = message.replace("%admin%", sender.getName());
			message = message.replace("%reason%", reason);
			message = message.replace("%time%", getTimeDifference(temptime));
			actualVictim.kickPlayer(formatMessage(message));
		}
		String message = this.broadcastTempBan;
		message = message.replace("%victim%", victim);
		message = message.replace("%admin%", sender.getName());
		message = message.replace("%reason%", reason);
		message = message.replace("%time%", getTimeDifference(temptime));
		if (!silent.booleanValue()) {
			getServer().broadcastMessage(formatMessage(message));
		}
		log.log(Level.INFO, "[BanReport] " + victim + " was temp banned by " + sender.getName() + " for " + time + " reason: " + reason);

		return Boolean.valueOf(true);
	}

	public Boolean unbanPlayer(CommandSender sender, String[] args) {
		if (!getPermission(sender, "banreport.unban")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return Boolean.valueOf(true);
		}
		if (args.length < 1) {
			return Boolean.valueOf(false);
		}
		String victim = expandName(args[0]);
		if (!this.db.checkBanList(victim)) {
			sender.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + victim + ChatColor.RED + " is not banned.");
			return Boolean.valueOf(true);
		}
		Boolean silent = Boolean.valueOf(false);
		if ((args.length > 1) && (args[1].equals("-s"))) {
			silent = Boolean.valueOf(true);
		}
		this.db.removePlayer(victim);

		sender.sendMessage(ChatColor.GREEN + "Successfully unbanned " + victim + "!");

		String message = this.broadcastUnban;
		message = message.replace("%victim%", victim);
		message = message.replace("%admin%", sender.getName());
		if (!silent.booleanValue()) {
			getServer().broadcastMessage(formatMessage(message));
		}
		log.log(Level.INFO, "[BanReport] " + victim + " was unbanned by " + sender.getName());

		return Boolean.valueOf(true);
	}

	public Boolean kickPlayer(CommandSender sender, String[] args) {
		if (!getPermission(sender, "banreport.kick")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return Boolean.valueOf(true);
		}
		if (args.length < 1) {
			return Boolean.valueOf(false);
		}
		String victim = expandName(args[0]);

		Boolean silent = Boolean.valueOf(false);
		if ((args.length > 1) && (args[1].equals("-s"))) {
			silent = Boolean.valueOf(true);
		}
		String reason;

		if (!silent.booleanValue()) {

			if (args.length < 2) {
				reason = "undefined.";
			} else {
				reason = combineSplit(1, args, " ");
			}
		} else {

			if (args.length < 3) {
				reason = "undefined.";
			} else {
				reason = combineSplit(2, args, " ");
			}
		}
		Player actualVictim = getServer().getPlayer(victim);
		if (actualVictim == null) {
			sender.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + victim + ChatColor.RED + " is not online.");
			return Boolean.valueOf(true);
		}
		String message = this.userKick;
		message = message.replace("%victim%", victim);
		message = message.replace("%admin%", sender.getName());
		message = message.replace("%reason%", reason);
		actualVictim.kickPlayer(formatMessage(message));

		message = this.broadcastKick;
		message = message.replace("%victim%", victim);
		message = message.replace("%admin%", sender.getName());
		message = message.replace("%reason%", reason);
		if (!silent.booleanValue()) {
			getServer().broadcastMessage(formatMessage(message));
		}
		log.log(Level.INFO, "[BanReport] " + victim + " was kicked by " + sender.getName() + " Reason: " + reason);

		return Boolean.valueOf(true);
	}

	public Boolean banIP(CommandSender sender, String[] args) {
		if (!getPermission(sender, "banreport.banip")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return Boolean.valueOf(true);
		}
		if (args.length < 1) {
			return Boolean.valueOf(false);
		}
		String victim = args[0];
		Player actualvictim = getServer().getPlayer(expandName(victim));
		if (actualvictim != null) {
			victim = actualvictim.getAddress().getAddress().getHostAddress().toString();
		}
		if (this.db.checkIP(victim)) {
			sender.sendMessage(ChatColor.RED + "This IP is already blocked.");
			return Boolean.valueOf(true);
		}
		this.db.addIP(victim, sender.getName());

		sender.sendMessage(ChatColor.GREEN + "Successfully blocked " + victim + "!");
		log.log(Level.INFO, "[BanReport] " + victim + " blocked by " + sender.getName());
		kickIPs(victim);
		return Boolean.valueOf(true);
	}

	public String combineSplit(int startIndex, String[] string, String seperator) {
		StringBuilder builder = new StringBuilder();
		for (int i = startIndex; i < string.length; i++) {
			builder.append(string[i]);
			builder.append(seperator);
		}
		builder.deleteCharAt(builder.length() - seperator.length());
		return builder.toString();
	}

	public String expandName(String Name) {
		int m = 0;
		String Result = "";
		for (int n = 0; n < getServer().getOnlinePlayers().length; n++) {
			String str = getServer().getOnlinePlayers()[n].getName();
			if (str.matches("(?i).*" + Name + ".*")) {
				m++;
				Result = str;
				if (m == 2) {
					return null;
				}
			}
			if (str.equalsIgnoreCase(Name)) {
				return str;
			}
		}
		if (m == 1) {
			return Result;
		}
		if (m > 1) {
			return null;
		}
		if (m < 1) {
			return Name;
		}
		return Name;
	}

	public void kickIPs(String IP) {
		Player player = null;
		for (int n = 0; n < getServer().getOnlinePlayers().length; n++) {
			player = getServer().getOnlinePlayers()[n];
			if (player.getAddress().getAddress().getHostAddress().toString().equals(IP)) {
				this.db.addPlayer(player.getName().toLowerCase(), "IP Ban.", "autoban", 0L);
				log.log(Level.INFO, "[BanReport] " + player.getName() + " was autobanned by IP.");

				String message = this.userIPBan;
				player.kickPlayer(formatMessage(message));
			}
		}
	}

	public boolean exportBans(CommandSender sender) {
		if (!getPermission(sender, "banreport.banio")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return true;
		}
		try {
			BufferedWriter banlist = new BufferedWriter(new FileWriter("banned-players.txt", true));
			this.db.initialise();
			for (String i : this.bannedNubs) {
				banlist.newLine();
				banlist.write(i);
			}
			banlist.close();

			BufferedWriter banlistIP = new BufferedWriter(new FileWriter("banned-ips.txt", true));
			for (String i : this.bannedIPs) {
				banlistIP.newLine();
				banlistIP.write(i);
			}
			banlistIP.close();
			sender.sendMessage(ChatColor.GREEN + "Banlist exported.");
			log.log(Level.INFO, sender.getName() + " exported the banlist.");
			return true;
		} catch (IOException e) {
			log.log(Level.SEVERE, "[BanReport] could not export ban list.");
			sender.sendMessage(ChatColor.RED + "Could not export ban list.");
		}
		return true;
	}

	public boolean importBans(CommandSender sender) {
		if (!getPermission(sender, "banreport.banio")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission.");
			return true;
		}
		try {
			this.db.initialise();
			String strLine;
			BufferedReader banlist = new BufferedReader(new FileReader("banned-players.txt"));
			while ((strLine = banlist.readLine()) != null) {

				if (!this.bannedNubs.contains(strLine)) {
					this.db.addPlayer(strLine, "undefined", "bl: " + sender.getName(), 0L);
				}
			}
			@SuppressWarnings("resource")
			BufferedReader banlistIP = new BufferedReader(new FileReader("banned-ips.txt"));
			strLine = "";
			while ((strLine = banlistIP.readLine()) != null) {
				if (!this.bannedIPs.contains(strLine)) {
					this.db.removePlayer(strLine);
				}
				this.db.addIP(strLine, "bl: " + sender.getName());
			}
			banlist.close();

			sender.sendMessage(ChatColor.GREEN + "Banlist imported.");
			log.log(Level.INFO, sender.getName() + " imported the banlist to the database.");
			return true;
		} catch (IOException e) {
			log.log(Level.SEVERE, "[BanReport] could not import ban list.");
			sender.sendMessage(ChatColor.RED + "Could not import ban list.");
		}
		return true;
	}

	public String getTimeDifference(long tempTime) {
		long difference = tempTime - new Date().getTime();

		String timespaceUnit = " day(s).";
		long timespacedays = difference / 86400000L;
		long timespacehours = difference / 3600000L;
		long timespaceminutes = difference / 60000L;
		long timespaceseconds = difference / 1000L;

		long timespace = timespacedays;
		if (timespace < 1L) {
			timespace = timespacehours;
			timespaceUnit = " hour(s).";
		}
		if (timespace < 1L) {
			timespace = timespaceminutes;
			timespaceUnit = " minute(s).";
		}
		if (timespace < 1L) {
			timespace = timespaceseconds;
			timespaceUnit = " second(s).";
		}
		return timespace + timespaceUnit;
	}

	public String formatMessage(String str) {
		str = str.replace("&", "§");
		return str;
	}

	public boolean getPermission(CommandSender sender, String node) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player) sender;
		if (player.hasPermission(node)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private boolean setupPermissions() {
		if ((this.vault) && (this.perms)) {
			if (getServer().getPluginManager().getPlugin("Vault") == null) {
				return false;
			}
		}
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		if (rsp == null) {
			return false;
		}
		perm = (Permission) rsp.getProvider();
		return perm != null;
	}

	public Boolean empBan(String[] trimmedArgs) {
		// TODO Auto-generated method stub
		return null;
	}
}
