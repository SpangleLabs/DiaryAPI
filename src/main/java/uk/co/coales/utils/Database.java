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
	
	public ResultSet getSessionTokenByTokenAndIpAddr(String token,String ipAddr) {
		String query = "SELECT token, login_id, logins.username, logins.email, logins.pass_hash,"+
					   "  logins.pass_salt, logins.failed_logins, logins.lockout_time "+
					   " FROM session_tokens "+
					   " LEFT JOIN logins ON session_tokens.login_id = logins.login_id "+
					   " WHERE session_tokens.token = ? "+
					   "  AND session_tokens.ip_addr = ? "+
					   "  AND session_tokens.time_used > ? ";
		ResultSet results = null;
		try {
			PreparedStatement statement = this.mConn.prepareStatement(query);
			statement.setString(1,token);
			statement.setString(2,ipAddr);
			results = statement.executeQuery();
		} catch (SQLException e) {
			System.out.println("DB ERROR: Get session token by token failed.");
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * List diary entries for a given login.
	 * @param loginId
	 * @return
	 */
	public ResultSet listEntriesByLogin(Integer loginId) {
		String query = "SELECT entry_id, entry_date, entry_text, login_id "+
					   " FROM entries "+
					   " WHERE login_id = ?";
		ResultSet results = null;
		try {
			PreparedStatement statement = this.mConn.prepareStatement(query);
			statement.setInt(1,loginId);
			results = statement.executeQuery();
		} catch (SQLException e) {
			System.out.println("DB ERROR: List entries by login failed.");
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * Adds 1 to the failed login count for an account, then locks out any accounts with too many failed logins.
	 * @param accountId
	 */
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
