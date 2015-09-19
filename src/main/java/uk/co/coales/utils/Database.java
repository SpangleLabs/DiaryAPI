package uk.co.coales.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
	private Connection mConn = null;

	public void connect() {
		String dbHost = "localhost";
		Integer dbPort = 5432;
		String dbName = "DiaryAPI";
		String dbUsername = "postgres";
		String dbPassword = "postgres";
		try {
			this.mConn = DriverManager.getConnection("jdbc:postgresql://"+dbHost+":"+dbPort.toString()+"/"+dbName,dbUsername,dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Connected to database.");
	}
}
