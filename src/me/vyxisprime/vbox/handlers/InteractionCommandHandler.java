package me.vyxisprime.vbox.handlers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class InteractionCommandHandler {
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
	static PotionEffect pe;

	public static void hugCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		p = (Player) sender;
		if (lbl.equalsIgnoreCase("hug")) {
			if (args.length == 0) {
				bC(p, frMsg + p.getDisplayName() + pink + " wants a hug!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					Player targetplayer = p.getServer().getPlayer(args[0]);
					p.getServer().broadcastMessage(frMsg + p.getDisplayName() + pink + " gave " + reset + targetplayer.getDisplayName() + pink + " a hug!");
				} else {
					p.sendMessage(frMsg + red + "Error: Player not found!");
				}
			}
		}
	}

	public static void cuddleCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		p = (Player) sender;
		if (lbl.equalsIgnoreCase("cuddle")) {
			if (args.length == 0) {
				bC(p, frMsg + p.getDisplayName() + purple + " wants to cuddle!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					Player targetplayer = p.getServer().getPlayer(args[0]);

					p.getServer().broadcastMessage(frMsg + p.getDisplayName() + purple + " cuddled with " + reset + targetplayer.getDisplayName() + reset);
				} else {
					p.sendMessage(frMsg + red + "Error: Player not found!");
				}
			}
		}
	}

	public static void kissCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		p = (Player) sender;
		if (lbl.equalsIgnoreCase("kiss")) {
			if (args.length == 0) {
				bC(p, p.getDisplayName() + pink + " wants a kiss!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					Player tp = p.getServer().getPlayer(args[0]);
					if (p.getServer().getPlayer(args[0]) == p) {
						p.sendMessage(frMsg + red + "you cannot give yourself a kiss!");
					} else {
						bC(p, p.getDisplayName() + pink + " kissed " + reset + tp.getDisplayName());
					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline");
				}
			}
		}
	}

	public static void trollCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		p = (Player) sender;

		if (lbl.equalsIgnoreCase("troll")) {
			if (args.length == 0) {
				sM(p, frMsg + red + "you can only troll a player!");
			}
		} else if (args.length == 1) {
			if (p.getServer().getPlayer(args[0]) != null) {
				sM(p, frMsg + red + "it's pretty hard to troll yourself, just don't try it ;) ");
			} else {
				tp = p.getServer().getPlayer(args[0]);
				if (p.getServer().getPlayer(args[0]) != null) {
					Player tp = p.getServer().getPlayer(args[0]);
					bC(p, frMsg + p.getDisplayName() + green + " trolled " + reset + tp.getDisplayName());
				} else {
					p.sendMessage(frMsg + red + "Error: This player is offline!");
				}
			}
		}
	}

	public static void runoverCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		p = (Player) sender;
		Player tp = p.getServer().getPlayer(args[0]);
		if (lbl.equalsIgnoreCase("runover")) {
			if (args.length == 0) {
				p.sendMessage(frMsg + red + "you can only run over a player!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					sM(p, frMsg + red + "it's pretty hard to run yourself over, just don't try it ;) ");
				} else {
					tp = p.getServer().getPlayer(args[0]);
					if (p.getServer().getPlayer(args[0]) != null) {
						bC(p, frMsg + p.getDisplayName() + darkRed + " ran over " + reset + tp.getDisplayName());
					} else {
						p.sendMessage(frMsg + red + "Error: Player not found!");
						tp.damage(3, null);
					}
				}
			} else {
				sM(p, frMsg + red + "Error: This player is offline!");
			}
		}
	}

	public static void yoloCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("yolo")) {
			if (args.length == 0) {
				bC(p, frMsg + p.getDisplayName() + red + " is about to yolobatt someone!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					Player tp = p.getServer().getPlayer(args[0]);
					if (p.getServer().getPlayer(args[0]) == p) {
						p.sendMessage(frMsg + red + "you can not hit yourself with a yolobatt!");
					} else {
						tp.damage(4, null);
						bC(p, frMsg + p.getDisplayName() + reset + " Hit the swag out of " + tp.getDisplayName() + reset + " with a " + gold + " YOLOBATT");

					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline!");
				}
			}
		}
	}

	public static void singCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		p = (Player) sender;
		if (lbl.equalsIgnoreCase("sing")) {
			if (args.length == 0) {
				bC(p, frMsg + p.getDisplayName() + blue + " started singing!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					Player tp = p.getServer().getPlayer(args[0]);
					if (p.getServer().getPlayer(args[0]) == p) {
						p.sendMessage(frMsg + red + "you can not sing towards yourself!");
					} else {
						bC(p, frMsg + p.getDisplayName() + blue + " sings towards " + reset + tp.getDisplayName());
					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline!");
				}
			}
		}
	}

	public static void buttspankCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		p = (Player) sender;
		if (lbl.equalsIgnoreCase("Buttspank")) {
			if (args.length == 0) {
				p.sendMessage(frMsg + red + "you can only buttspank a player!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					Player tp = p.getServer().getPlayer(args[0]);
					if (p.getServer().getPlayer(args[0]) == p) {
						p.sendMessage(frMsg + red + "you can not use this expression on your self!");
					} else {
						bC(p, frMsg + p.getDisplayName() + darkRed + " buttspanked " + reset + tp.getDisplayName());
					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline!");
				}
			}
		}
	}

	public static void cookieCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		p = (Player) sender;
		if (lbl.equalsIgnoreCase("Cookie")) {
			if (args.length == 0) {
				p.sendMessage(frMsg + red + "you can only give a cookie to another player!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					tp = p.getServer().getPlayer(args[0]);
					if (p.getServer().getPlayer(args[0]) == p) {
						p.sendMessage(frMsg + red + "you cannot give yourself a cookie!");
						bC(p, frMsg + p.getDisplayName() + red + " gave " + reset + tp.getDisplayName() + red + " a cookie");
						ItemStack c = new ItemStack(Material.COOKIE, 1, (byte) 13);
						p.getInventory().addItem(new ItemStack(c));
					} else {
						sM(p, frMsg + red + "Error: This player is offline!");
					}
				}
			}
		}
	}

	public static void loveCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("love")) {
			if (args.length == 0) {
				bC(p, p.getDisplayName() + red + " wants some love!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					tp = p.getServer().getPlayer(args[0]);
					if (p.getServer().getPlayer(args[0]) == p) {
						sM(p, frMsg + red + "you cannot give love to yourself! ofcourse you already love youself!");
					} else {
						bC(p, frMsg + p.getDisplayName() + red + " loves " + reset + tp.getDisplayName());
						tp.setHealth(10);
					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline!");
				}
			}
		}
	}

	public static void snapatCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("snap")) {
			if (args.length == 0) {
				sM(p, frMsg + red + "You cannot snap at your self!");
			} else if (args.length == 1) {
				if (p.getServer().getPlayer(args[0]) != null) {
					tp = p.getServer().getPlayer(args[0]);
					if (p.getServer().getPlayer(args[0]) == p) {
						sM(p, frMsg + red + "Come on now! You dont want to snap at yourself!");
					} else {
						bC(p, frMsg + p.getDisplayName() + blue + " snaps at " + reset + tp.getDisplayName());
						tp.damage(1);
					}
				} else {
					sM(p, frMsg + "Error: This player is offline!");
				}
			}
		}
	}

	public static void slapCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("slap")) {
			if (args.length == 0) {
				bC(p, frMsg + p.getDisplayName() + red + " is about to slap someone!");
			} else if (args.length == 1) {
				tp = p.getServer().getPlayer(args[0]);
				if (tp != null) {
					if (p.getServer().getPlayer(args[0]) == p) {
						sM(p, frMsg + red + "Come on now! You dont want to slap yourself!");
					} else {
						bC(p, frMsg + p.getDisplayName() + darkBlue + " slapped " + reset + tp.getDisplayName());
						tp.damage(2);
					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline!");
				}
			}
		}
	}

	public static void welcomeCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("welcome")) {
			if (args.length == 0) {
				sM(p, frMsg + red + "you have to select someone to welcome to the server!");
			} else if (args.length == 1) {
				tp = p.getServer().getPlayer(args[0]);
				if (tp != null) {
					if (p.getServer().getPlayer(args[0]) == p) {
						sM(p, frMsg + red + "you can not welcome yourself to the server!");
					} else {
						bC(p, frMsg + p.getDisplayName() + aqua + " Welcomes " + reset + tp.getDisplayName() + " to the server!");
						p.setHealth(10);
						sM(p, frMsg + red + "As a token of social kindness you have been" + gold + " healed" + reset + "!");
					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline!");
				}
			}
		}
	}

	public static void healCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("heal")) {
			if (args.length == 0) {
				bC(p, frMsg + p.getDisplayName() + gold + " healed " + reset + "him/herself!");
				p.setHealth(10);
			}
			if (args.length == 1) {
				tp = p.getServer().getPlayer(args[0]);
				if (tp != null) {
					if (p.getServer().getPlayer(args[0]) == p) {
						bC(p, frMsg + p.getDisplayName() + gold + " healed " + reset + "him/herself!");
						p.setHealth(10);
					} else {
						bC(p, frMsg + p.getDisplayName() + gold + " healed " + tp.getDisplayName());
					}
				}
			} else {
				sM(p, frMsg + red + "Error: This player is offline!");
			}
		}
	}

	public static void reviveCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("revive")) {
			if (args.length == 0) {
				sM(p, frMsg + red + "please use it like \"\revive <playerName>\"");
			} else if (args.length == 1) {
				tp = p.getServer().getPlayer(args[0]);
				if (tp != null) {
					if (!tp.isDead()) {
						sM(p, frMsg + red + "Error: this player isn't dead!");
						// Location w = tp.getWorld().getLocation()
					} else {
						Location l = p.getLocation();
						p.setHealth(20);
						p.teleport(l);
						bC(p, frMsg + p.getDisplayName() + gold + " revived " + reset + tp.getDisplayName());
					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline!");
				}
			}

		}

	}

	public static void danceCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("dance")) {
			if (args.length == 0) {
				bC(p, frMsg + blue + " bursts into dancing!");
			} else if (args.length == 1) {
				tp = p.getServer().getPlayer(args[0]);
				if (tp != null) {
					if (p.getServer().getPlayer(args[0]) == p) {
						bC(p, frMsg + blue + " bursts into dancing!");
					} else {
						bC(p, frMsg + p.getDisplayName() + blue + " dances with " + reset + tp.getDisplayName());
					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline!");
				}
			}
		}
	}

	public static void clapCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("clap")) {
			if (args.length == 0) {
				bC(p, frMsg + p.getDisplayName() + aqua + " claps!");
			} else if (args.length == 1) {
				tp = p.getServer().getPlayer(args[0]);
				if (tp != null) {
					if (p.getServer().getPlayer(args[0]) == p) {
						sM(p, frMsg + red + "Clapping at your self? nah!");
					} else {
						bC(p, frMsg + p.getDisplayName() + aqua + " claps at" + tp.getDisplayName());
					}
				} else {
					sM(p, frMsg + red + "Error: This player is offline!");
				}
			}
		}
	}

	public static void fireCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("fire")) {
			if (args.length == 0) {
				sM(p, frMsg + red + "You can not set your self on fire dummy!");
			} else if (args.length == 1) {
				tp = p.getServer().getPlayer(args[0]);
				if (tp == p) {
					sM(p, frMsg + red + "You can not set your self on fire dummy!");
				} else {
					bC(p, frMsg + p.getDisplayName() + darkRed + " set " + reset + tp.getDisplayName() + darkRed + " in flames!");
					tp.setFireTicks(1000);
				}
			}
		}
	}

	private static void devilCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("devil")) {
			if (args.length == 0) {
				sM(p, frMsg + red + "Please use the command like this: /devil <msg>");
			} else {
				String defaultString = gray + "[" + red + "Devil" + gray + "]" + darkRed;
				for (int n = 0; n < args.length; n++) {
					if (args[n].length() > 0) {
						defaultString = defaultString + " " + args[n];
					}
				}
				bC(p, defaultString);
			}
		}
	}

	private static void godCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("devil")) {
			if (args.length == 0) {
				sM(p, frMsg + red + "Please use the command like this: /god <msg>");
			} else {
				String defaultString = gray + "[" + gold + "God" + gray + "]" + darkRed;
				for (int n = 0; n < args.length; n++) {
					if (args[n].length() > 0) {
						defaultString = defaultString + " " + args[n];
					}
				}
				bC(p, defaultString);
			}
		}
	}

	public static void registerCommands(CommandSender sender, Command cmd, String lbl, String[] args) {
		// vBox.express.low
		hugCommand(sender, cmd, lbl, args);
		cuddleCommand(sender, cmd, lbl, args);
		cookieCommand(sender, cmd, lbl, args);
		singCommand(sender, cmd, lbl, args);
		kissCommand(sender, cmd, lbl, args);
		danceCommand(sender, cmd, lbl, args);
		clapCommand(sender, cmd, lbl, args);
		welcomeCommand(sender, cmd, lbl, args);
		// vBox.express.high
		slapCommand(sender, cmd, lbl, args);
		snapatCommand(sender, cmd, lbl, args);
		loveCommand(sender, cmd, lbl, args);
		runoverCommand(sender, cmd, lbl, args);
		buttspankCommand(sender, cmd, lbl, args);
		// vBox.express.staff
		fireCommand(sender, cmd, lbl, args);
		reviveCommand(sender, cmd, lbl, args);
		healCommand(sender, cmd, lbl, args);
		yoloCommand(sender, cmd, lbl, args);
		// vBox.ai.rapture
		godCommand(sender, cmd, lbl, args);
		devilCommand(sender, cmd, lbl, args);

	}

	public static void bC(Player p, String s) {
		p.getServer().broadcastMessage(s);
	}

	public static void sM(Player p, String s) {
		p.sendMessage(s);
	}
	// public static OfflinePlayer gP(Player p, String args) {
	// return p.getServer().getOfflinePlayer(args);
	// }
}
