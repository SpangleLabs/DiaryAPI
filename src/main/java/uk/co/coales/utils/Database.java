package uk.co.coales.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Database {
	private Connection mConn = null;
	private Config mConf = null;
	
	public Database() {
		this.mConf = new Config();
		this.connect();
	}

	public void connect() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to load PostgreSQL JDBC driver.");
			e.printStackTrace();
			return;
		}
		try {
			this.mConn = DriverManager.getConnection("jdbc:postgresql://"+this.mConf.getDbHost()+":"+this.mConf.getDbPort().toString()+"/"+this.mConf.getDbName(),this.mConf.getDbUsername(),this.mConf.getDbPassword());
		} catch (Exception e) {
			System.out.println("Failed to connect to database.");
			e.printStackTrace();
			return;
		}
		System.out.println("Connected to database.");
	}
	
	public void disconnect() {
		try {
			this.mConn.close();
		} catch (SQLException e) {
			System.out.println("Failed to disconnect from database.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a new session token to the database.
	 * @param token
	 * @param loginId
	 * @param ipAddr
	 */
	public void addSessionToken(String token, Integer loginId, String ipAddr) {
		String query = "INSERT INTO session_tokens (token,login_id,ip_addr) "+
					   " VALUES (?,?,?) ";
		try {
			PreparedStatement statement = this.mConn.prepareStatement(query);
			statement.setString(1,token);
			statement.setInt(2,loginId);
			statement.setString(3,ipAddr);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DB ERROR: Add session token failed.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets data on a login by a given username.
	 * @param username
	 * @return
	 */
	public ResultSet getLoginByUsername(String username) {
		String query = "SELECT login_id, username, email, pass_hash, pass_salt, "+
					   "  failed_logins, time_lockout "+
					   " FROM logins "+
					   " WHERE username = ?";
		ResultSet results = null;
		try {
			PreparedStatement statement = this.mConn.prepareStatement(query);
			statement.setString(1,username);
			results = statement.executeQuery();
		} catch (SQLException e) {
			System.out.println("DB ERROR: Get login by username failed.");
			e.printStackTrace();
		}
		return results;
	}
	
	public void updateLoginFailedLogins(Integer accountId) {
		String query = "UPDATE logins "+
					   " SET failed_logins = failed_logins + 1"+
					   " WHERE login_id = ?";
		try {
			PreparedStatement statement = this.mConn.prepareStatement(query);
			statement.setInt(1,accountId);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DB ERROR: Add failed login failed.");
			e.printStackTrace();
		}
		String queryLock = "UPDATE logins "+
					       " SET time_lockout = ?, failed_logins = 0"+
					       " WHERE failed_logins >= 3";
		try {
			PreparedStatement statement = this.mConn.prepareStatement(queryLock);
			//TODO: SET DATE
			long t = new java.util.Date().getTime();
			long m = 60*60*1000;
			Timestamp lockedUntil = new Timestamp(t+m);
			statement.setTimestamp(1,lockedUntil);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DB ERROR: Locking account failed.");
			e.printStackTrace();
		}
	}
}
