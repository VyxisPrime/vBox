package me.vyxisprime.vbox.listeners;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import me.vyxisprime.vbox.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class KitListener implements Listener {
	static Main plugin;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static
	HashMap<String, Long> timer = new HashMap();

	@SuppressWarnings("static-access")
	public KitListener(Main main) {
		this.plugin = main;
	}

	@EventHandler
	public boolean onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String kit = plugin.getConfig().getString("firstJoinKitName");
		String firstWorld = plugin.getConfig().getString("firstJoinWorld");
		File file = new File(firstWorld + "/players/" + p.getName() + ".dat");
		boolean exists = file.exists();
		if (!exists) {
			return firstJoinKit(p, kit);
		}
		return exists;
	}

	@SuppressWarnings("static-access")
	public static boolean giveKit(Player p, String kit) {

		List<String> itemList = plugin.getConfig().getStringList("kits." + kit.toLowerCase() + ".items");
		ListIterator<String> it = itemList.listIterator();
		if (it.hasNext()) {
			plugin.logger.info(plugin.frMsg + "Giving Kit:" + kit + " to " + p.getName());
			while (it.hasNext()) {
				String[] line = ((String) it.next()).split(" ");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give" + p.getName() + " " + line[0] + " " + line[2] + " " + line[1]);
			}
			long start = System.currentTimeMillis();
			String key = p.getName() + " " + kit.toLowerCase();
			timer.put(key, Long.valueOf(start));
			plugin.logger.info(plugin.frMsg + "Finished giving Kit:" + kit + " to " + p.getName());
		}
		return true;
	}

	@SuppressWarnings("static-access")
	public boolean firstJoinKit(Player p, String kit) {
		List<String> itemList = plugin.getConfig().getStringList("kits." + kit.toLowerCase() + ".items");
		ListIterator<String> it = itemList.listIterator();
		if (it.hasNext()) {
			plugin.logger.info(plugin.frMsg + p.getName() + " joined for the first time, Giving Kit: " + kit + " to " + p.getName());
			while (it.hasNext()) {
				String[] line = ((String) it.next()).split(" ");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + p.getName() + " " + line[0] + " " + line[2] + " " + line[1]);
			}
			long start = System.currentTimeMillis();
			String key = p.getName() + " " + kit.toLowerCase();
			this.timer.put(key, Long.valueOf(start));
			plugin.logger.info("Finished giving kit: " + kit + " to " + p.getName());
		}
		return true;
	}

	
	public static boolean onCooldown(Player p, String kitName) {
		String key = p.getName() + " " + kitName.toLowerCase();
		if (timer.containsKey(key)) {
			long last = ((Long) timer.get(key).longValue());
			long difference = (System.currentTimeMillis() - last) / 1000L;
			if (difference < plugin.getConfig().getLong("kits." + kitName + ".cooldown"))
				return true;
		}
		return false;
	}

	public static boolean difference(Player p, String kit) {
		String key = p.getName() + " " + kit.toLowerCase();
		long last = ((Long) timer.get(key)).longValue();
		long difference = (System.currentTimeMillis() - last) / 1000L;
		long cooldown = plugin.getConfig().getLong("kits." + kit + ".cooldown");
		long timeLeft = cooldown - difference;
		p.sendMessage(plugin.frMsg + plugin.gray + "[" + plugin.green + "Kits" + plugin.gray + "]" + plugin.red + " You still have to wait " + plugin.white + timeLeft + plugin.red + " seconds before you can use kit: " + plugin.white + kit + plugin.red + " again!");
		return true;
	}

	public static boolean getKitList(Player player) {
		Set<String> kitList = plugin.getConfig().getConfigurationSection("kits").getKeys(false);
		Iterator<String> it = kitList.iterator();
		if (it.hasNext()) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Kits" + ChatColor.GRAY + "]" + " List of available kits: ");
			while (it.hasNext()) {
				player.sendMessage(ChatColor.GRAY + (String) it.next());
			}
		} else {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Kits" + ChatColor.GRAY + "]" + ChatColor.RED + " Sorry! There are no kits available at this time!");
		}
		return true;
	}

}
