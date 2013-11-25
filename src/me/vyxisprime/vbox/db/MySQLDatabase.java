package me.vyxisprime.vbox.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import me.vyxisprime.vbox.Main;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class MySQLDatabase {

	Main plugin;
	String mysqlTable;

	public MySQLDatabase(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("static-access")
	public Connection getSQLConnection() {
		if (this.plugin.useMySQL) {
			String mysqlDatabase = plugin.config.getString("mysql-database", "jdbc:mysql://localhost:3306/minecraft");
			String mysqlUser = plugin.config.getString("mysql-user", "root");
			String mysqlPassword = plugin.config.getString("mysql-password", "root");
			this.mysqlTable = plugin.config.getString("mysql-table", "banlist");
			try {
				return (Connection) DriverManager.getConnection(mysqlDatabase + "?autoReconnect=true&user=" + mysqlUser + "&password=" + mysqlPassword);
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
				return (Connection) DriverManager.getConnection("jdbc:sqlite:plugins/vBox/banlist.db");
			} catch (SQLException ex) {
				this.plugin.logger.log(Level.SEVERE, "Unable to retreive connection", ex);
			}
		}
		return null;
	}

	@SuppressWarnings("static-access")
	public void init() {
		Connection conn = getSQLConnection();

		this.plugin.bannedNubs.clear();
		this.plugin.bannedIPs.clear();
		if (!this.plugin.useMySQL) {
			makeSQLiteTables();
		}
		if (conn == null) {
			this.plugin.logger.log(Level.SEVERE, plugin.frMsg + " Could not establish SQL connection. Bans wont work now.");
			return;
		}
		java.sql.PreparedStatement ps = null;
	    ResultSet rs = null;
	    
	    try{
	    	ps = conn.prepareStatement("SELECT * FROM " + this.mysqlTable);
	    	rs = ps.executeQuery();
	    	while(rs.next()){
	    		this.plugin.bannedNubs.add(rs.getString("name").toLowerCase());
	    	}
	    }
	}

}
