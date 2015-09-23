package uk.co.coales.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import uk.co.coales.utils.Database;

public class Login {
	private Database mDB = null;
	private Integer mLoginId = null;
	private String mUsername = null;
	private String mEmail = null;
	private byte[] mPassHash = null;
	private byte[] mPassSalt = null;
	private Integer mFailedLogins = null;
	private Date mLockoutTime = null;
	
	/**
	 * Constructor, builds Login object from database class and the result of a query 
	 * @param db
	 * @param loginResult
	 * @throws SQLException 
	 */
	Login(Database db, ResultSet loginResult) throws SQLException {
		if(loginResult == null) {
			throw new NullPointerException();
		}
		this.mDB = db;
		this.mLoginId = loginResult.getInt("login_id");
		this.mUsername = loginResult.getString("username");
		this.mEmail = loginResult.getString("email");
		this.mPassHash = loginResult.getBytes("pass_hash");
		this.mPassSalt = loginResult.getBytes("pass_salt");
		this.mFailedLogins = loginResult.getInt("failed_logins");
		this.mLockoutTime = loginResult.getDate("time_lockout");
	}

	/**
	 * Creates a new login from a valid authentication token.
	 * @param authToken
	 * @return
	 */
	public static Login fromSessionToken(String authToken) {
		return null;
	}
	
	/**
	 * Creates a new login from a valid username.
	 * @param db
	 * @param username
	 * @return
	 */
	public static Login fromUsername(Database db, String username) {
		ResultSet loginData = db.getLoginByUsername(username);
		Login newLogin = null;
		try {
			newLogin = new Login(db,loginData);
		} catch (SQLException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
		return newLogin;
	}

	/**
	 * Lists diary entries for the current login.
	 * @return 
	 */
	public ArrayList<DiaryEntry> listDiaryEntries() {
		return null;
	}

	/**
	 * Gets a diary entry by entry id for current login.
	 * @param diaryEntryId
	 * @return
	 */
	public DiaryEntry getDiaryEntryById(Integer diaryEntryId) {
		return null;
	}
	
	/**
	 * Add a new diary entry
	 * @param entryDate
	 * @param entryText
	 * @return
	 */
	public DiaryEntry addDiaryEntry(Date entryDate, String entryText) {
		return null;
	}
}
