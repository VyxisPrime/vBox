package me.vyxisprime.vbox.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;

import me.vyxisprime.vbox.Main;
import me.vyxisprime.vbox.bansystem.BanInfo;
import me.vyxisprime.vbox.bansystem.EditBan;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
@SuppressWarnings("static-access")
public class MySQLDatabase {
	Main plugin;
	String mysqlTable = "banlist";

	public MySQLDatabase(Main instance) {
		this.plugin = instance;
	}

	public Connection getSQLConnection() {
		if (this.plugin.useMySQL) {
			String mysqlDatabase = plugin.config.getString("mysql-database", "jdbc:mysql://localhost:3306/minecraft");
			String mysqlUser = plugin.config.getString("mysql-user", "root");
			String mysqlPassword = plugin.config.getString("mysql-password", "root");
			this.mysqlTable = plugin.config.getString("mysql-table", "banlist");
			try {
				return DriverManager.getConnection(mysqlDatabase + "?autoReconnect=true&user=" + mysqlUser + "&password=" + mysqlPassword);
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, "Unable to retreive connection", ex);
			}
		} else {
			try {
				try {
					Class.forName("org.sqlite.JDBC");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				return DriverManager.getConnection("jdbc:sqlite:plugins/BanReport/banlist.db");
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, "Unable to retreive connection", ex);
			}
		}
		return null;
	}

	public void initialise() {
		Connection conn = getSQLConnection();

		this.plugin.bannedNubs.clear();
		this.plugin.bannedIPs.clear();
		if (!this.plugin.useMySQL) {
			makeSQLiteTables();
		}
		if (conn == null) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Could not establish SQL connection. Disabling BanReport");
			this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
			return;
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM " + this.mysqlTable);
			rs = ps.executeQuery();
			while (rs.next()) {
				this.plugin.bannedNubs.add(rs.getString("name").toLowerCase());
			}
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute MySQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close MySQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close MySQL connection: ", ex);
			}
		}
		try {
			conn.close();
			initialiseIPs();
			this.plugin.logger.log(Level.INFO, plugin.frMsg + " SQL connection initialised.");
		} catch (SQLException e) {
			e.printStackTrace();
			this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
		}
	}

	public void initialiseIPs() {
		Connection conn = getSQLConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM " + this.mysqlTable + " WHERE IP != ''");
			rs = ps.executeQuery();
			while (rs.next()) {
				this.plugin.bannedIPs.add(rs.getString("IP"));
			}
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}

	public int getWarnings(String player) {
		Connection conn = getSQLConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int warnings = 0;
		try {
			ps = conn.prepareStatement("SELECT * FROM warnings WHERE name = ?");
			ps.setString(1, player);
			rs = ps.executeQuery();
			while (rs.next()) {
				warnings++;
			}
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
		return warnings;
	}

	public void viewWarnings(CommandSender sender, String player) {
		Connection conn = getSQLConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM warnings WHERE name = ?");
			ps.setString(1, player);
			rs = ps.executeQuery();
			sender.sendMessage(ChatColor.GREEN + "Warnings for " + player + ":");
			while (rs.next()) {
				sender.sendMessage(ChatColor.GREEN + rs.getString("warning") + " issued by " + ChatColor.YELLOW + rs.getString("admin"));
			}
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}

	public void warnPlayer(String player, String admin, String warning) {
		String mysqlTable = "warnings";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("INSERT INTO " + mysqlTable + " (name,warning,admin) VALUES(?,?,?)");
			ps.setString(1, player);
			ps.setString(2, warning);
			ps.setString(3, admin);
			ps.executeUpdate();
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}

	public boolean checkBanList(String player) {
		if (this.plugin.bannedNubs.contains(player.toLowerCase())) {
			return true;
		}
		return false;
	}

	public EditBan getInfo(String player) {
		EditBan editban = null;
		Connection conn = getSQLConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM " + this.mysqlTable + " WHERE name = ?");
			ps.setString(1, player);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getLong("temptime") != 0L) {
					editban = new EditBan(rs.getString("name").toLowerCase(), rs.getString("reason"), rs.getString("admin"), rs.getTimestamp("temptime"));
				} else {
					editban = new EditBan(rs.getString("name"), rs.getString("reason"), rs.getString("admin"), new Timestamp(0L));
				}
			}
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
		return editban;
	}

	public void addPermList(String victim, String reason, String admin, String IP) {
		PreparedStatement ps = null;
		try {
			ps = getSQLConnection().prepareStatement("INSERT INTO banhistory (name, reason, admin, date, IP) values (?,?,?,?,?)");
			ps.setString(1, victim);
			ps.setString(2, reason);
			ps.setString(3, admin);
			ps.setTimestamp(4, new Timestamp(new Date().getTime()));
			ps.setString(5, IP);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addPlayer(String player, String reason, String admin, long tempTime) {
		this.plugin.bannedNubs.add(player.toLowerCase());
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("INSERT INTO " + this.mysqlTable + " (name,reason,admin,time,temptime) VALUES(?,?,?,?,?)");
			Timestamp temptime = new Timestamp(tempTime);
			if (tempTime != 0L) {
				ps.setTimestamp(5, temptime);
			} else {
				ps.setLong(5, tempTime);
			}
			ps.setString(1, player);
			ps.setString(2, reason);
			ps.setString(3, admin);
			Timestamp time = new Timestamp(new Date().getTime());
			ps.setTimestamp(4, time);
			ps.executeUpdate();
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}

	public void addIP(String IP, String admin) {
		if (IP.length() > 15) {
			IP = IP.substring(0, 14);
		}
		this.plugin.bannedIPs.add(IP);
		this.plugin.bannedNubs.add(IP);
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("INSERT INTO " + this.mysqlTable + " (name,reason,admin,time,temptime,IP) VALUES(?,?,?,?,?,?)");
			ps.setLong(5, 0L);
			ps.setString(1, IP);
			ps.setString(2, "IP BAN");
			ps.setString(3, admin);
			Timestamp time = new Timestamp(new Date().getTime());
			ps.setTimestamp(4, time);
			ps.setString(6, IP);
			ps.executeUpdate();
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}

	public boolean removePlayer(String player) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("DELETE FROM " + this.mysqlTable + " WHERE name = ?");
			ps.setString(1, player);
			ps.executeUpdate();
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			return false;
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
		this.plugin.bannedNubs.remove(player.toLowerCase());
		if (this.plugin.bannedIPs.contains(player.toLowerCase())) {
			this.plugin.bannedIPs.remove(player.toLowerCase());
		}
		return true;
	}

	public boolean checkIP(String IP) {
		if (this.plugin.bannedIPs.contains(IP)) {
			return true;
		}
		return false;
	}

	public void addInfo(CommandSender sender, String victim, String information) {
		int x = 0;
		int y = 0;
		int z = 0;
		if ((sender instanceof Player)) {
			Player player = (Player) sender;
			x = player.getLocation().getBlockX();
			y = player.getLocation().getBlockY();
			z = player.getLocation().getBlockZ();
		}
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("INSERT INTO baninfo (player,information,admin,x,y,z) VALUES(?,?,?,?,?,?)");
			ps.setString(1, victim);
			ps.setString(2, information);
			ps.setString(3, sender.getName());
			ps.setInt(4, x);
			ps.setInt(5, y);
			ps.setInt(6, z);
			ps.executeUpdate();
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}

	public HashMap<Integer, BanInfo> getBanInfo(String player) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HashMap<Integer, BanInfo> hashMap = new HashMap();
		int i = 0;
		Connection conn = getSQLConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM baninfo WHERE player = ?");
			ps.setString(1, player);
			rs = ps.executeQuery();
			while (rs.next()) {
				hashMap.put(Integer.valueOf(i), new BanInfo(player, rs.getString("admin"), rs.getString("information"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getInt("id")));
				i++;
			}
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
		return hashMap;
	}

	public void removeInfo(int id) {
		String mysqlTable = "baninfo";

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("DELETE FROM " + mysqlTable + " WHERE id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}

	@SuppressWarnings("resource")
	public void updateSQLite() {
		String query = "CREATE TABLE `warnings` (`id` integer primary key autoincrement,`name` varchar(30) NOT NULL,`warning` text NOT NULL,`admin` varchar(30) NOT NULL)";

		String query2 = "CREATE TABLE `baninfo` (`id` integer primary key autoincrement,`player` varchar(32) NOT NULL,`information` text NOT NULL,`admin` varchar(32) NOT NULL,`x` int(4) NOT NULL,`y` int(4) NOT NULL,`z` int(4) NOT NULL)";

		String query3 = "CREATE TABLE `banhistory` ( `id` integer primary key autoincrement,`name` varchar(32) NOT NULL,`reason` text NOT NULL,`admin` varchar(32) NOT NULL,`date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,`ip` varchar(15) NOT NULL)";

		PreparedStatement ps = null;
		Connection conn = getSQLConnection();
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps = conn.prepareStatement(query2);
			ps.executeUpdate();
			ps = conn.prepareStatement(query3);
			ps.executeUpdate();
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}

	public void makeSQLiteTables() {
		String query = "CREATE TABLE `banlist` (  `name` varchar(32) NOT NULL,  `reason` text NOT NULL, `admin` varchar(32) NOT NULL,`time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,`temptime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',`IP` varchar(15) DEFAULT NULL, PRIMARY KEY (`name`)); ";
		if (!tableExists()) {
			PreparedStatement ps = null;
			Connection conn = getSQLConnection();
			try {
				conn = getSQLConnection();
				ps = conn.prepareStatement(query);
				ps.executeUpdate();
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
				try {
					if (ps != null) {
						ps.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", e);
				}
			} finally {
				try {
					if (ps != null) {
						ps.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException ex) {
					this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
				}
			}
		}
		if (!otherTablesExist()) {
			updateSQLite();
		}
	}

	private boolean tableExists() {
		ResultSet rs = null;
		try {
			Connection conn = getSQLConnection();
			DatabaseMetaData dbm = conn.getMetaData();
			rs = dbm.getTables(null, null, "banlist", null);
			if (!rs.next()) {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
				return false;
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
			return true;
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}

	private boolean otherTablesExist() {
		ResultSet rs = null;
		try {
			Connection conn = getSQLConnection();
			DatabaseMetaData dbm = conn.getMetaData();
			rs = dbm.getTables(null, null, "banhistory", null);
			if (!rs.next()) {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
				return false;
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
			return true;
		} catch (SQLException ex) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Couldn't execute SQL statement: ", ex);
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Failed to close SQL connection: ", ex);
			}
		}
	}
}
