package me.vyxisprime.vbox.listeners;

import me.vyxisprime.vbox.Main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CurseWordListener implements Listener {
	Main plugin;
	@EventHandler(priority = EventPriority.HIGH)
    public final void onPlayerChat(AsyncPlayerChatEvent event) {
            for(String word : event.getMessage().split(" ")) {
                    if(plugin.getConfig().getStringList("censoredwords").contains(word)) {
                            if(!(event.getPlayer().hasPermission("curseprevention.bypass"))) { // If player has permission to bypass the censor.
                                    event.setMessage(event.getMessage().replace(word, "****"));
                            } else {
                                    event.setMessage(event.getMessage().replace(event.getMessage(), event.getMessage()));
                            }
                    }
            }
    }
}
