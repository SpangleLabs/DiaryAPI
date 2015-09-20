package uk.co.coales.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
	private Connection mConn = null;
	
	public Database() {
		this.connect();
	}

	public void connect() {
		String dbHost = "localhost";
		Integer dbPort = 5432;
		String dbName = "DiaryAPI";
		String dbUsername = "postgres";
		String dbPassword = "postgres";
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to load PostgreSQL JDBC driver.");
			e.printStackTrace();
			return;
		}
		try {
			this.mConn = DriverManager.getConnection("jdbc:postgresql://"+dbHost+":"+dbPort.toString()+"/"+dbName,dbUsername,dbPassword);
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
	public ResultSet addUniqueSalt(String salt) {
		String query = "INSERT INTO unique_salts (salt) VALUES (?)";
		ResultSet results = null;
		try {
			PreparedStatement statement = this.mConn.prepareStatement(query);
			statement.setString(1,salt);
			results = statement.executeQuery();
		} catch (SQLException e) {
			System.out.println("DB ERROR: Add unique salt failed.");
			e.printStackTrace();
		}
		return results;
	}
}
