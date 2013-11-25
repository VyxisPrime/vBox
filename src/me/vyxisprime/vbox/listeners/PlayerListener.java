package me.vyxisprime.vbox.listeners;

import java.util.Date;

import me.vyxisprime.vbox.Main;
import me.vyxisprime.vbox.bansystem.EditBan;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {
	Main plugin;

	public PlayerListener(Main instance) {
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoinEvent(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (!plugin.db.checkBanList(p.getName())) {
			return;
		}
		EditBan playerInfo = this.plugin.db.getInfo(p.getName());
		String kickMsg = this.plugin.userBan;
		kickMsg = kickMsg.replace("%admin%", playerInfo.admin);
		kickMsg = kickMsg.replace("%reason%", playerInfo.reason);
		if (playerInfo.temptime != 0L) {
			long difference = playerInfo.temptime - new Date().getTime();
			if (difference <= 0L) {
				this.plugin.db.removePlayer(p.getName());
			}
		}
	}
}