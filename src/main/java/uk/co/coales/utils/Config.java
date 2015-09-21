package uk.co.coales.utils;

public class Config {
	private String mDbHost = null;
	private Integer mDbPort = null;
	private String mDbName = null;
	private String mDbUsername = null;
	private String mDbPassword = null;
	private String mKeyDirectory = null;
	
	public void loadFromXml() {
		
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
