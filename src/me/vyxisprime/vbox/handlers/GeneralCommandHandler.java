package me.vyxisprime.vbox.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.vyxisprime.vbox.Main;
import me.vyxisprime.vbox.listeners.KitListener;
import me.vyxisprime.vbox.util.Vars;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;

public class GeneralCommandHandler {
	static ChatColor darkRed = ChatColor.DARK_RED;
	static ChatColor darkBlue = ChatColor.DARK_BLUE;
	static ChatColor darkGray = ChatColor.DARK_GRAY;
	static ChatColor darkAqua = ChatColor.DARK_AQUA;
	static ChatColor darkGreen = ChatColor.DARK_GREEN;
	static ChatColor purple = ChatColor.DARK_PURPLE;
	static ChatColor red = ChatColor.RED;
	static ChatColor blue = ChatColor.BLUE;
	static ChatColor black = ChatColor.BLACK;
	static ChatColor gray = ChatColor.GRAY;
	static ChatColor gold = ChatColor.GOLD;
	static ChatColor green = ChatColor.GREEN;
	static ChatColor aqua = ChatColor.AQUA;
	static ChatColor pink = ChatColor.LIGHT_PURPLE;
	static ChatColor yellow = ChatColor.YELLOW;
	static ChatColor reset = ChatColor.RESET;
	static ChatColor bold = ChatColor.BOLD;
	static ChatColor white = ChatColor.WHITE;
	static public String frMsg = white + "[" + green + "vBox" + white + "]" + reset;

	public static Permission perm = null;
	static Player p;
	static Player tp;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static HashMap<Player, Player> Tpa = new HashMap();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<Player> godModdedPlayers = new ArrayList();
	static Main plugin;

	public static void registerCommands(CommandSender sender, Command cmd, String lbl, String[] args) {
		ccCommand(sender, cmd, lbl, args);
		kilallCommand(sender, cmd, lbl, args);
		afkCommand(sender, cmd, lbl, args);
		gamemodeCommand(sender, cmd, lbl, args);
		flyCommand(sender, cmd, lbl, args);
		seenCommand(sender, cmd, lbl, args);
		tpCommand(sender, cmd, lbl, args);
		tphereCommand(sender, cmd, lbl, args);
		tpaCommand(sender, cmd, lbl, args);
		setspawnCommand(sender, cmd, lbl, args);
		spawnCommand(sender, cmd, lbl, args);
		ciCommand(sender, cmd, lbl, args);
		repairCommand(sender, cmd, lbl, args);
		worldCommand(sender, cmd, lbl, args);
		speedCommand(sender, cmd, lbl, args);
		reloadCommand(sender, cmd, lbl, args);
		bansystemCommands(sender, cmd, lbl, args);
		addcursewordCommand(sender, cmd, lbl, args);
		kitsCommand(sender, cmd, lbl, args);
		kitCommand(sender, cmd, lbl, args);
		vanishCommand(sender, cmd, lbl, args);
		godCommand(sender, cmd, lbl, args);
		weatherCommand(sender, cmd, lbl, args);
		timeCommand(sender, cmd, lbl, args);
	}

	public static void weatherCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("weather")) {
			if (a.length == 0) {
				sM(p, frMsg + red + "Usage: /weather <Storm/Sun/Thunder>");
			} else if (a.length == 1) {
				if (a[0].equalsIgnoreCase("storm")) {
					p.getWorld().setStorm(true);
					p.getWorld().setThundering(true);
					sM(p, frMsg + gold + "You've made it storm!");
				} else if (a[0].equalsIgnoreCase("sun")) {
					p.getWorld().setStorm(false);
					p.getWorld().setThundering(false);
					sM(p, frMsg + gold + "You've made it sunny!");
				} else if (a[0].equalsIgnoreCase("thunder")) {
					p.getWorld().setThundering(true);
				} else {
					sM(p, frMsg + red + "Arguments not recognised");
				}
			} else {
				sM(p, frMsg + red + "Usage: /weather <Storm/Sun/Thunder>");
			}
		}
	}

	public static void timeCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("time")) {
			if (a.length == 0) {
				sM(p, frMsg + red + "Usage: /time <day/night>");
			} else if (a.length == 1) {
				if (a[0].equalsIgnoreCase("day")) {
					p.getWorld().setTime(0l);
					sM(p, frMsg + blue + "you've  set the time to day!");
					bC(p, frMsg + p.getName() + blue + " has set the time to day!");
				} else if (a[0].equalsIgnoreCase("Night")) {
					p.getWorld().setTime(1800l);
					sM(p, frMsg + blue + "you've  set the time to night!");
					bC(p, frMsg + p.getName() + blue + " has set the time to night!");
				}
			} else {
				sM(p, frMsg + red + "Usage: /time <day/night>");
			}
		}
	}

	public static void godCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("god")) {
			if (p.hasPermission("vbox.general.god.other")) {
				if (a.length > 1) {
					sM(p, frMsg + red + "Correct usage: /god or /god <name>");

				} else if (a.length == 1) {
					try {
						toggleGodMode(Bukkit.getServer().getPlayer(a[0]));
					} catch (Exception e) {
						sM(p, frMsg + red + "Can't find " + a[0] + ".");
					}

				}
			} else {
				sM(p, frMsg + red + "Permission denied!");
			}
			if (s.hasPermission("vbox.general.god")) {
				if ((s instanceof Player)) {
					p = (Player) s;
					toggleGodMode(p);
				} else {
					sM(p, frMsg + red + "Insufficient permissions");
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	public static void toggleGodMode(Player player) {
		setMetadata(player, "Godmode", Boolean.valueOf(!isGod(player)));
		plugin.logger.fine("[" + player.getDisplayName() + "] Toggled godmode " + isGod(player));
		if (isGod(player)) {
			player.sendMessage(frMsg + gold + "Godmode activated!");
		} else {
			sM(p, frMsg + gold + "Godmode Deactivated");
		}
	}

	public static boolean isGod(Player player) {
		return getMetadata(player, "Godmode").booleanValue();
	}

	public static void setMetadata(Player player, String key, Object value) {
		player.setMetadata(key, new FixedMetadataValue(plugin, value));
	}

	@SuppressWarnings("static-access")
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDamage(EntityDamageEvent evt) {
		if ((evt.getEntity() instanceof Player)) {
			Player player = (Player) evt.getEntity();
			if (isGod(player)) {
				evt.setCancelled(true);
				player.setFireTicks(0);
				player.removePotionEffect(PotionEffectType.BLINDNESS);
				player.removePotionEffect(PotionEffectType.CONFUSION);
				player.removePotionEffect(PotionEffectType.POISON);
				player.removePotionEffect(PotionEffectType.WITHER);
				player.removePotionEffect(PotionEffectType.WEAKNESS);
				player.removePotionEffect(PotionEffectType.SLOW);
				plugin.logger.finest("[" + player.getDisplayName() + "] Damage by " + evt.getCause().name() + " cancelled.");
			}
		}
	}

	public static Boolean getMetadata(Player player, String key) {
		List<MetadataValue> values = player.getMetadata(key);
		for (MetadataValue value : values) {
			if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
				if (value.value() != null) {
					return Boolean.valueOf(value.asBoolean());
				}
				return Boolean.valueOf(false);
			}
		}
		return Boolean.valueOf(false);
	}

	@SuppressWarnings("static-access")
	public static boolean kitsCommand(CommandSender s, Command c, String l, String[] a) {
		if ((s instanceof ConsoleCommandSender)) {
			plugin.logger.warning(plugin.frMsg + "You may not use this command from the console!");
			return false;
		} else if ((s instanceof Player)) {
			if (l.equalsIgnoreCase("kits")) {
				if ((p.hasPermission("vBox.kits.view")) || (p.isOp())) {
					if (a.length == 0) {
						return KitListener.getKitList(p);
					}
					if (a.length == 1) {
						sM(p, frMsg + ChatColor.RED + "Use /kit <KitName> to get a Kit.");
					} else if (a.length >= 2) {
						sM(p, frMsg + ChatColor.RED + "Incorrect Usage!");
						sM(p, frMsg + ChatColor.RED + "Usage1: /kits");
						sM(p, frMsg + ChatColor.RED + "Usage2: /kit <KitName>");
					}
				} else {
					sM(p, frMsg + ChatColor.RED + "Sorry, You don't have permissions to view the Kit List.");
				}
			}
		}
		return true;
	}

	public static boolean kitCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("kit")) {
			if (a.length < 1) {
				sM(p, frMsg + red + "Incorrect Usage");
				sM(p, frMsg + red + "Usage 1: /kits");
				sM(p, frMsg + red + "Usage 2: /kit <KitName>");
			} else if (a.length == 1) {
				String kit = a[0].toLowerCase();
				if (plugin.getConfig().contains("kits." + kit)) {
					boolean groupon = plugin.getConfig().getBoolean("GroupOn");
					if (groupon) {
						List<String> groupList = plugin.getConfig().getStringList("kits." + kit.toLowerCase() + ".groups");
						String playergroups = perm.getPrimaryGroup(p);
						Iterator<String> iterator = groupList.iterator();
						if (iterator.hasNext()) {
							String group = (String) iterator.next();
							if (playergroups.contains(group)) {
								if (KitListener.onCooldown(p, kit)) {
									return KitListener.difference(p, kit);
								}
								return KitListener.giveKit(p, kit);
							}
							if ((p.hasPermission("vbox.kits." + kit)) || (p.isOp())) {
								if (KitListener.onCooldown(p, kit)) {
									return KitListener.difference(p, kit);
								}
								return KitListener.giveKit(p, kit);
							}
							if ((p.hasPermission("vbox.kits.*")) || (p.isOp())) {
								if (KitListener.onCooldown(p, kit)) {
									return KitListener.difference(p, kit);
								}
								return KitListener.giveKit(p, kit);
							}
							sM(p, frMsg + ChatColor.RED + "Sorry, You do not have permissions to use kit: " + kit + ".");
							return true;
						}
					} else if (!groupon) {
						if ((p.hasPermission("vbox.kits." + kit)) || (p.isOp())) {
							if (KitListener.onCooldown(p, kit)) {
								return KitListener.difference(p, kit);
							}
							return KitListener.giveKit(p, kit);
						}
						if ((p.hasPermission("vbox.kits.*")) || (p.isOp())) {
							if (KitListener.onCooldown(p, kit)) {
								return KitListener.difference(p, kit);
							}
							return KitListener.giveKit(p, kit);
						}
						sM(p, frMsg + ChatColor.RED + "Sorry, You do not have permissions to use kit: " + kit + ".");
						return true;
					}
				} else {
					sM(p, frMsg + ChatColor.RED + "Sorry! The kit '" + kit + "' doesn't exist!");
					return true;
				}
			} else if (a.length >= 2) {
				sM(p, frMsg + ChatColor.RED + "Incorrect Usage!");
				sM(p, frMsg + ChatColor.RED + "Usage1: /kits");
				sM(p, frMsg + ChatColor.RED + "Usage2: /kit <KitName>");
			}
		}
		return true;
	}

	public static void speedCommand(CommandSender s, Command c, String l, String[] a) {
		p = (Player) s;
		if (l.equalsIgnoreCase("speed")) {
			if (a.length == 0) {
				p.sendMessage(ChatColor.RED + "Not enough args! /Speed <Speed>");
			}
			if (a.length == 1) {
				p.sendMessage(ChatColor.GOLD + "Changed your fly speed!");
				if (p.isFlying()) {
					p.setFlySpeed(4.0F);
				}
				if (!p.isFlying()) {
					p.setWalkSpeed(4.0F);
				}
			}
			if (a.length > 1) {
				p.sendMessage(ChatColor.RED + "/speed <speed>");
			}
		}
	}

	public static void worldCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("world")) {
			if (a.length == 0) {
				p.sendMessage(ChatColor.GOLD + "You are currently in world " + ChatColor.RED + p.getWorld().toString().toLowerCase());

			}
			if (a.length == 1) {
				Player target = p.getServer().getPlayer(a[0]);
				if (target == null) {
					p.sendMessage(ChatColor.RED + "That player is currently not online.");

				}
				if (target != null) {
					p.sendMessage(ChatColor.GOLD + "'" + ChatColor.RED + target.getName() + ChatColor.GOLD + "' is currently in " + ChatColor.RED + target.getWorld().toString().toLowerCase());

				}
			}
			if (a.length > 1) {
				p.sendMessage(ChatColor.RED + "Usage: /world [playerName]");

			}
		}
	}

	public static void ccCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("clearchat") || l.equalsIgnoreCase("cc")) {
			for (Player all : Bukkit.getOnlinePlayers()) {
				for (int x = 0; x < 120; x++) {
					all.sendMessage(" ");
					if (x == 119) {
						sM(all, frMsg + "Cleared the chat!");
					}
				}
			}
		}
	}

	public static void kilallCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("clearchat")) {
			for (Entity all : p.getWorld().getEntities()) {
				all.remove();
				sM(p, frMsg + s.getName() + " enities removed");
			}
		}
	}

	// necessary to have for the /afk moment
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static ArrayList<String> AFK = new ArrayList();

	@SuppressWarnings("static-access")
	@EventHandler
	public void AFKMove(PlayerMoveEvent event) {
		if (this.AFK.contains(event.getPlayer().getName())) {
			this.AFK.remove(event.getPlayer().getName());
		}
	}

	public static void afkCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("akf")) {
			if (AFK.contains(p.getName())) {
				Bukkit.broadcastMessage(frMsg + gold + p.getName() + bold + " is no longer AFK!");
				AFK.remove(p.getName());

			}
			if (!AFK.contains(p.getName())) {
				Bukkit.broadcastMessage(frMsg + gold + p.getName() + bold + " is now AFK!");
				AFK.remove(p.getName());

			}
		}
	}

	public static void gamemodeCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("gamemode")) {
			Player p = (Player) s;
			if (((HumanEntity) s).getGameMode() == GameMode.CREATIVE) {
				((HumanEntity) s).setGameMode(GameMode.SURVIVAL);
				p.sendMessage(frMsg + ChatColor.GRAY + "You are now in SURVIVAL mode");
			}
			if (((HumanEntity) s).getGameMode() == GameMode.SURVIVAL) {
				((HumanEntity) s).setGameMode(GameMode.CREATIVE);
				p.sendMessage(frMsg + ChatColor.GRAY + "You are now in CREATIVE mode");
			}
		}
	}

	public static void flyCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("fly")) {
			if (p.getAllowFlight()) {

				p.setFlying(false);

				p.setAllowFlight(false);
			} else {
				p.sendMessage(frMsg + gold + "You have Enabled fly mode!");

				p.setAllowFlight(true);

				p.setFlying(true);

			}
		}
	}

	public static void tpCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("tp")) {
			if (a.length == 0) {
				sM(p, frMsg + gray + "Usage: /Tp [PlayerName]");

			}
			Player target = Bukkit.getServer().getPlayer(a[0]);
			if (target == null) {
				sM(p, frMsg + red + "Couldn't find " + a[0] + "!");

			}
			p.teleport(target.getLocation());
			sM(p, frMsg + "You have tped to " + red + target.getName());
			sM(target, frMsg + p.getName() + gold + " has tped to you!");

		}
	}

	public static void tphereCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("tphere")) {
			try {
				Player player1 = (Player) s;
				if (a.length == 0) {
					player1.sendMessage(frMsg + red + "Missing the name of the player!");
				} else if (a.length == 1) {
					{
						Player destination = (Player) s;
						Player person = Bukkit.getPlayer(a[0].toString());
						person.teleport(destination);
						person.sendMessage(frMsg + red + "Teleported to " + reset + destination.getName().toString());
					}
				}
			} catch (Exception localException3) {
				localException3.printStackTrace();
			}
		}
	}

	public static void seenCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("seen")) {
			Player p = (Player) s;
			if (a.length == 0) {
				sM(p, gray + "Usage: /seen [playerName]");

			}
			Player target = Bukkit.getServer().getPlayer(a[0]);
			if (target == null) {
				sM(p, gray + "Could not find the player " + a[0] + " D:");

			}
			p.sendMessage(gold + "- " + target.getName() + " -");
			p.sendMessage(red + "Player has not been seen till " + target.getLastPlayed());
		}
	}

	public static void tpaCommand(CommandSender s, Command c, String l, String[] a) {

		// TP Ask
		if (l.equalsIgnoreCase("tpa")) {
			Player mittente = (Player) s;
			try {
				if (a.length == 0) {
					mittente.sendMessage(frMsg + red + "Missing the name of the player");
				} else if (a.length == 1) {
					final Player targetPlayer = mittente.getServer().getPlayer(a[0]);
					mittente.sendMessage(frMsg + green + "Request sent...");
					targetPlayer.sendMessage(frMsg + green + mittente.getName().toString() + " asked to come to you, you have 30 seconds to accept with " + gold + " /tpaccept ");
					Tpa.put(targetPlayer, mittente);
					plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
						@SuppressWarnings("static-access")
						public void run() {
							GeneralCommandHandler.this.Tpa.remove(targetPlayer);
						}
					}, 600L);
				}
			} catch (Exception exc) {
				mittente.sendMessage(frMsg + red + "The player is not ON-LINE");
			}
		}
		// TP accept
		if (l.equalsIgnoreCase("tpaccept")) {
			Player player1 = (Player) s;
			try {
				if (Tpa.containsKey(player1)) {

				}
				((Player) Tpa.get(player1)).teleport(player1);
				Tpa.remove(player1);
				((Player) Tpa.get(player1)).sendMessage(frMsg + green + "Teleported to " + gold + player1.getName().toString());
			} catch (Exception localException2) {
			}
		}
	}

	public static void setspawnCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("setspawn")) {
			if (a.length > 0) {
				sM(p, frMsg + red + "Please only use this command as /setspawn");
			}
			Player p = (Player) s;
			plugin.getConfig().set("spawn.world", p.getLocation().getWorld().getName());
			plugin.getConfig().set("spawn.x", Double.valueOf(p.getLocation().getX()));
			plugin.getConfig().set("spawn.y", Double.valueOf(p.getLocation().getY()));
			plugin.getConfig().set("spawn.z", Double.valueOf(p.getLocation().getZ()));
			plugin.saveConfig();
			p.sendMessage(frMsg + green + "You have set Spawn location!");
		}
	}

	public static void spawnCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("spawn")) {
			Player p = (Player) s;

			World w = Bukkit.getServer().getWorld(plugin.getConfig().getString("spawn.world"));
			double x = plugin.getConfig().getDouble("spawn.x");
			double y = plugin.getConfig().getDouble("spawn.y");
			double z = plugin.getConfig().getDouble("spawn.z");
			p.teleport(new Location(w, x, y, z));
			p.sendMessage(frMsg + green + "You been have teleported to Spawn!");
		}
	}

	public static void ciCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("clearinventory") || l.equalsIgnoreCase("ci")) {
			Player p = (Player) s;
			p.getInventory().clear();
			p.sendMessage(frMsg + gold + "You have cleared your Inventory.");
		}
	}

	public static void repairCommand(CommandSender s, Command c, String l, String[] a) {
		p = (Player) s;
		if (l.equalsIgnoreCase("repair")) {
			p.getItemInHand().setDurability((short) 0);
			sM(p, ChatColor.GOLD + "Your " + ChatColor.RED + p.getItemInHand().toString().toLowerCase() + ChatColor.GOLD + " has been repaired");
		}
	}

	public static void reloadCommand(CommandSender s, Command c, String l, String[] a) {
		p = (Player) s;
		if ((l.equalsIgnoreCase("vBox")) && (a.length == 0)) {
			sM(p, frMsg + gold + "Current commands: " + darkRed + "/vBox (reload");

		}
		if ((l.equalsIgnoreCase("vBox")) && (a[0].equalsIgnoreCase("reload"))) {
			if (plugin.loadConfig()) {
				sM(p, frMsg + gold + "Config reloaded!");
			} else {
				sM(p, frMsg + gold + "Config failed to reload!");
			}
		}
	}

	public static void addcursewordCommand(CommandSender s, Command c, String l, String[] a) {
		if ((l.equalsIgnoreCase("curseword") && a[0].equalsIgnoreCase("add")) || (l.equalsIgnoreCase("cw") && a[0].equalsIgnoreCase("add"))) {
			if (a.length > 0) {
				s.sendMessage(ChatColor.GOLD + "You need to provide a word to filter.");
				s.sendMessage(ChatColor.DARK_RED + "Correct Usage: /curseprevention add <word>");
			} else {

				if (!Vars.censoredwords.contains(a[1])) {
					Vars.censoredwords.add(a[1]);
					plugin.getConfig().set("censoredwords", a[1]);
					s.sendMessage(ChatColor.GOLD + "Word has been filtered!");
					plugin.saveConfig();
				} else {
					s.sendMessage(ChatColor.GOLD + "Word was already filtered!");
				}
			}
		} else {
			sM(p, frMsg + red + "Error: please add arguments to the command /curseword add <word>");
		}
	}

	@SuppressWarnings("static-access")
	public static void vanishCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("vanish")) {
			if (a.length < 1) {
				sM(p, frMsg + gold + " ===== Vanish Help ====");
				sM(p, frMsg + gold + " |    /Vanish on      |");
				sM(p, frMsg + gold + " |    /Vanish off     |");
				sM(p, frMsg + gold + " ======================");
			} else {
				p = (Player) s;
				if (a[0].equalsIgnoreCase("on")) {
					p.hidePlayer(p);
					sM(p, frMsg + gold + "You've been hidden!");
					plugin.logger.info(frMsg + p.getName() + " vanished");
				} else if (a[0].equalsIgnoreCase("off")) {
					sM(p, frMsg + gold + " you're visible once more!");
				}
			}
		}
	}

	public static boolean bansystemCommands(CommandSender s, Command c, String l, String[] a) {
		String[] trimmedArgs = a;
		if (l.equals("baninfo")) {
			return plugin.banInfo(s, trimmedArgs);
		}
		if (l.equals("addinfo")) {
			return plugin.addInfo(s, trimmedArgs);
		}
		if (l.equals("removeinfo")) {
			return plugin.removeInfo(s, trimmedArgs);
		}
		if (l.equals("bantp")) {
			return plugin.banTp(s, trimmedArgs).booleanValue();
		}
		if (l.equals("ban")) {
			return plugin.banPlayer(s, trimmedArgs).booleanValue();
		}
		if (l.equals("unban")) {
			return plugin.unbanPlayer(s, trimmedArgs).booleanValue();
		}
		if (l.equals("kick")) {
			return plugin.kickPlayer(s, trimmedArgs).booleanValue();
		}
		if (l.equals("banip")) {
			return plugin.banIP(s, trimmedArgs).booleanValue();
		}
		if (l.equals("tempban")) {
			return plugin.tempBan(s, trimmedArgs).booleanValue();
		}
		if (l.equals("banexport")) {
			if (a.length < 1) {
				return plugin.exportBans(s);
			}
		}
		if (l.equals("banimport")) {
			return plugin.importBans(s);
		}
		if (l.equals("warn")) {
			return plugin.warnPlayer(s, trimmedArgs);
		}
		if (l.equals("warnings")) {
			return plugin.viewWarnings(s, trimmedArgs);
		}
		return false;

	}

	public static void bC(Player p, String s) {
		p.getServer().broadcastMessage(s);
	}

	public static void sM(Player p, String s) {
		p.sendMessage(s);
	}
}
