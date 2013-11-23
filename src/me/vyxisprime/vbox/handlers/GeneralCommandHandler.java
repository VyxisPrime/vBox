package me.vyxisprime.vbox.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import me.vyxisprime.vbox.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

@SuppressWarnings("unused")
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

	static Player p;
	static Player tp;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static HashMap<Player, Player> Tpa = new HashMap();
	static Main plugin;

	public static void registerCommands(CommandSender sender, Command cmd, String lbl, String[] args) {
		ccCommand(sender, cmd, lbl, args);
		kilallCommand(sender, cmd, lbl, args);
		afkCommand(sender, cmd, lbl, args);
		tpCommand(sender, cmd, lbl, args);
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
		worldCommand(sender,cmd,lbl,args);
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
		if (l.equalsIgnoreCase("clearchat")) {
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
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.setHealth(0);
				sM(all, frMsg + s.getName() + " killed everyone!");
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
			p.sendMessage(frMsg + green + "You have teleported to Spawn!");
		}
	}

	public static void ciCommand(CommandSender s, Command c, String l, String[] a) {
		Player p = (Player) s;

		p.getInventory().clear();

		p.sendMessage(frMsg + gold + "You have cleared your Inventory.");

	}

	public static void repairCommand(CommandSender s, Command c, String l, String[] a) {
		p = (Player) s;
		if (l.equalsIgnoreCase("repair")) {
			p.getItemInHand().setDurability((short) 0);
			sM(p, ChatColor.GOLD + "Your " + ChatColor.RED + p.getItemInHand().toString().toLowerCase() + ChatColor.GOLD + " has been repaired");
		}
	}

	public static void bC(Player p, String s) {
		p.getServer().broadcastMessage(s);
	}

	public static void sM(Player p, String s) {
		p.sendMessage(s);
	}
}
