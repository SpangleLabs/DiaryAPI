package uk.co.coales.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Config {

	private String mDbHost = null;
	private Integer mDbPort = null;
	private String mDbName = null;
	private String mDbUsername = null;
	private String mDbPassword = null;
	private String mKeyDirectory = null;

	/**
	 * Constructor just loads all variables from config file.
	 */
	public Config() {
		this.loadFromYaml();
	}
	
	/**
	 * Loads configuration information from the yaml config file.
	 * Gets very upset if the yaml is formatted incorrectly.
	 */
	@SuppressWarnings("unchecked")
	public void loadFromYaml() {
		InputStream input;
		try {
			input = new FileInputStream(new File("src/main/resources/config.yml"));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: CONFIG FILE MISSING.");
			e.printStackTrace();
			return;
		}
		Yaml yaml = new Yaml();
		Map<String,Object> data = (Map<String,Object>) yaml.load(input);
		Map<String,String> databaseData = (Map<String,String>) data.get("database");
		this.mDbHost = (String) databaseData.get("host");
		this.mDbPort = Integer.parseInt(databaseData.get("port"));
		this.mDbName = (String) databaseData.get("name");
		this.mDbUsername = (String) databaseData.get("username");
		this.mDbPassword = (String) databaseData.get("password");
		this.mKeyDirectory = (String) data.get("key_directory");
	}

	/**
	 * @return the mDbHost
	 */
	public String getDbHost() {
		return mDbHost;
	}

	/**
	 * @return the mDbPort
	 */
	public Integer getDbPort() {
		return mDbPort;
	}

	/**
	 * @return the mDbName
	 */
	public String getDbName() {
		return mDbName;
	}

	/**
	 * @return the mDbUsername
	 */
	public String getDbUsername() {
		return mDbUsername;
	}

	/**
	 * @return the mDbPassword
	 */
	public String getDbPassword() {
		return mDbPassword;
	}

	/**
	 * @return the mKeyDirectory
	 */
	public String getKeyDirectory() {
		return mKeyDirectory;
	}

}
