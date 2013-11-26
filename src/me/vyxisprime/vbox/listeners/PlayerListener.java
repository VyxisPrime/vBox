package me.vyxisprime.vbox.listeners;

import java.util.Date;
import java.util.logging.Level;

import me.vyxisprime.vbox.Main;
import me.vyxisprime.vbox.bansystem.EditBan;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
				Main.logger.log(Level.INFO, plugin.frMsg + p.getName() + " has been released from temp ban!");
				return;
			}
			String time = this.plugin.getTimeDifference(difference + new Date().getTime());

			kickMsg = this.plugin.userTempBan;
			kickMsg = kickMsg.replace("%admin%", playerInfo.admin);
			kickMsg = kickMsg.replace("%reason%", playerInfo.reason);
			kickMsg = kickMsg.replace("time", time);
		}
		e.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.plugin.formatMessage(kickMsg));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String IP = player.getAddress().getAddress().getHostAddress().toString();
		if (((this.plugin.db.checkIP(IP) ? 0 : 1) & (this.plugin.db.checkBanList(player.getName()) ? 0 : 1)) != 0) {
			return;
		}
		if (this.plugin.db.checkIP(IP)) {
			event.setJoinMessage(null);
			if (!this.plugin.db.checkBanList(player.getName())) {
				Main.logger.log(Level.INFO, plugin.frMsg + " " + player.getName() + " was autobanned for their IP.");
				this.plugin.db.addPlayer(player.getName().toLowerCase(), "IP Ban.", "autoban", 0L);
			}
		}
		if (this.plugin.db.checkBanList(player.getName())) {
			event.setJoinMessage(null);
			EditBan playerInfo = this.plugin.db.getInfo(player.getName());

			String kickMsg = this.plugin.userBan;
			kickMsg = kickMsg.replace("%admin%", playerInfo.admin);
			kickMsg = kickMsg.replace("%reason%", playerInfo.reason);

			player.kickPlayer(this.plugin.formatMessage(kickMsg));
		}
	}
}
