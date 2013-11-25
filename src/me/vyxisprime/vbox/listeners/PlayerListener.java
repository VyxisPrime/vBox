package me.vyxisprime.vbox.listeners;

import me.vyxisprime.vbox.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {
	Main plugin;
	
	public PlayerListener(Main instance){
		this.plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoinEvent(PlayerLoginEvent e){
		Player p = e.getPlayer();
		if(!plugin.db.checkBanList(p.getName())){
			return;
		}
		EditBan playerInfo = this.plugin.db.getInfo(p.getName());
	}
}
