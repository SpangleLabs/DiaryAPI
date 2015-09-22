package uk.co.coales.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	 * Adds a new unique salt to the database.
	 * @param salt
	 * @return
	 */
	public void addUniqueSalt(String salt) {
		String query = "INSERT INTO unique_salts (salt) VALUES (?)";
		ResultSet results = null;
		try {
			PreparedStatement statement = this.mConn.prepareStatement(query);
			statement.setString(1,salt);
			statement.executeQuery();
		} catch (SQLException e) {
			System.out.println("DB ERROR: Add unique salt failed.");
			e.printStackTrace();
		}
	}
}
