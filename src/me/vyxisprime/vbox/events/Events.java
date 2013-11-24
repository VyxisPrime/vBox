package me.vyxisprime.vbox.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {

	@EventHandler
	public void joinMsg(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		Bukkit.broadcastMessage(ChatColor.GREEN + "[" + ChatColor.RED + "+" + ChatColor.GREEN + "] " + e.getPlayer().getName());
	}

	@EventHandler
	public void leaveMsg(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Bukkit.broadcastMessage(ChatColor.GREEN + "[" + ChatColor.RED + "-" + ChatColor.GREEN + "] " + e.getPlayer().getName());
	}

}
