package me.vyxisprime.vbox.listeners;

import me.vyxisprime.vbox.Main;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CapsLockListener implements Listener {

	private Main plugin;

	public CapsLockListener(Main capslock) {
		this.plugin = capslock;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		Player p = event.getPlayer();
		if (p.hasPermission("vbox.caps.ignore")) {
			return;
		}
		if (this.plugin.enableNoCaps) {
			int[] newMessage = checkCaps(message);
			if ((percentageCaps(newMessage) >= this.plugin.totalCapsPercentage) || (checkCapsInRow(newMessage) >= this.plugin.capsInRow)) {
				if ((!this.plugin.enableForceLowercase) && (!this.plugin.enableMessageOverride)) {
					event.setCancelled(true);
				}
				if (this.plugin.enableMessageOverride) {
					event.setMessage(this.plugin.overrideMessage);
				} else if (this.plugin.enableForceLowercase) {
					String[] parts = message.split(" ");
					boolean allowedCaps = false;
					for (int i = 0; i < parts.length; i++) {
						boolean whitelisted = false;
						for (String whiteWord : this.plugin.whitelist) {
							if (whiteWord.equalsIgnoreCase(parts[i])) {
								whitelisted = true;
								allowedCaps = true;
								break;
							}
						}
						if (!whitelisted) {
							if (!allowedCaps) {
								char firstChar = parts[i].charAt(0);
								parts[i] = (firstChar + parts[i].toLowerCase().substring(1));
							} else {
								parts[i] = parts[i].toLowerCase();
							}
							allowedCaps = (!parts[i].endsWith(".")) && (!parts[i].endsWith("!"));
						}
					}
					event.setMessage(StringUtils.join(parts, " "));
				} else {
					event.setCancelled(true);
				}
				if (this.plugin.enablePlayerMessage) {
					p.sendMessage(ChatColor.RED + this.plugin.playerMessage);
				}
			}
		}
	}

	public int[] checkCaps(String message) {
		int[] newMessage = new int[message.length()];
		String[] parts = message.split(" ");
		for (int i = 0; i < parts.length; i++) {
			for (String whiteWord : this.plugin.whitelist) {
				if (whiteWord.equalsIgnoreCase(parts[i])) {
					parts[i] = parts[i].toLowerCase();
				}
			}
		}
		String msgLow = StringUtils.join(parts, " ");
		for (int j = 0; j < msgLow.length(); j++) {
			if ((Character.isUpperCase(msgLow.charAt(j))) && (Character.isLetter(msgLow.charAt(j)))) {
				newMessage[j] = 1;
			} else {
				newMessage[j] = 0;
			}
		}
		return newMessage;
	}

	public int percentageCaps(int[] caps) {
		int sum = 0;
		for (int i = 0; i < caps.length; i++) {
			sum += caps[i];
		}
		double ratioCaps = sum / caps.length;
		int percentCaps = (int) (100.0D * ratioCaps);
		return percentCaps;
	}

	public int checkCapsInRow(int[] caps) {
		int sum = 0;
		int sumTemp = 0;
		for (int i : caps) {
			if (i == 1) {
				sumTemp++;
				sum = Math.max(sum, sumTemp);
			} else {
				sumTemp = 0;
			}
		}
		return sum;
	}
}
