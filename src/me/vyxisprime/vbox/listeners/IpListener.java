package me.vyxisprime.vbox.listeners;

import me.vyxisprime.vbox.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class IpListener implements Listener {

	public Main plugin;

	public IpListener(Main plugin) {
		this.plugin = plugin;
		
	}


	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		String[] wordsInMessage = msg.split(" ");
		for (int i = 0; i < wordsInMessage.length; i++) {
			try{
				p.sendMessage(this.plugin.getConfig().getString("IPHidder.Messages.NotPermitted").replaceAll("(?i)&([a-f0-9])", "§$1"));
		        if (Boolean.valueOf(this.plugin.getConfig().getBoolean("IPHidder.Options.Action.Kick")).booleanValue()) {
		          p.kickPlayer(this.plugin.getConfig().getString("IPHidder.Messages.KickMessage").replaceAll("(?i)&([a-f0-9])", "§$1"));
		        }
		        if (Boolean.valueOf(this.plugin.getConfig().getBoolean("IPHidder.Options.Action.Ban")).booleanValue())
		        {
		          p.kickPlayer(this.plugin.getConfig().getString("IPHidder.Messages.BanMessage").replaceAll("(?i)&([a-f0-9])", "§$1"));
		          p.setBanned(true);
		        }
		        e.setCancelled(true);
		      }
		      catch (IllegalArgumentException localIllegalArgumentException) {}
		    }
	}
}
