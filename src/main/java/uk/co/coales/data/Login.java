package uk.co.coales.data;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import uk.co.coales.utils.Database;

public class Login {
	private Database mDB = null;
	private Integer mLoginId = null;
	private String mUsername = null;
	private String mEmail = null;
	private byte[] mPassHash = null;
	private byte[] mPassSalt = null;
	private Integer mFailedLogins = null;
	private Timestamp mLockoutTime = null;
	
	/**
	 * Constructor, builds Login object from database class and the result of a query 
	 * @param db Database object
	 * @param loginResult Result of a query pulling from logins table of database
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
		this.mLockoutTime = loginResult.getTimestamp("time_lockout");
	}

	/**
	 * Creates a new login from a valid authentication token.
	 * @param db Database object
	 * @param authToken Authorization code provided by a user
	 * @param ipAddr IP address of user
	 * @return Returns new Login object for this auth token, or null.
	 */
	public static Login fromSessionToken(Database db, String authToken, String ipAddr) {
		//Clear out old session tokens
		db.deleteSessionTokenByAge();
		//Get login data from database
		ResultSet loginData = db.getLoginByTokenAndIpAddr(authToken,ipAddr);
		Login newLogin;
		//Build new login object
		try {
			newLogin = new Login(db,loginData);
		} catch (SQLException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
		//Update time used on session token
		db.updateSessionTokenTimeUsed(authToken,ipAddr);
		return newLogin;
	}
	
	/**
	 * Creates a new login from a valid username.
	 * @param db Database object
	 * @param username Username of Login object
	 * @return Returns the Login object with corresponding username
	 */
	public static Login fromUsername(Database db, String username) {
		ResultSet loginData = db.getLoginByUsername(username);
		Login newLogin;
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
	 * Returns database ID for login.
	 * @return the mLoginId
	 */
	public Integer getLoginId() {
		return mLoginId;
	}

	/**
	 * Returns username for login.
	 * @return the mUsername
	 */
	public String getUsername() {
		return mUsername;
	}

	/**
	 * Returns email address for login.
	 * @return the mEmail
	 */
	public String getEmail() {
		return mEmail;
	}
	
	/**
	 * Returns boolean representing whether this login is locked out.
	 * @return Returns boolean representing whether login is locked out.
	 */
	public Boolean isLockedOut() {
		Timestamp currentTime = new Timestamp(new Date().getTime());
		return !currentTime.after(this.mLockoutTime);
	}
	
	/**
	 * Checks if a given password matches this Login's salt and hash pair.
	 * @param password Password which user has supplied for this account
	 * @return Boolean, whether or not this password is correct
	 */
	public Boolean checkPassword(String password) {
		//Salt password
		String saltedPass = new String(this.mPassSalt) + password;
		//Hash password
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(saltedPass.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("ERROR: CANNOT USE SHA-256 TO HASH PASSWORD.");
			return false;
		} catch (UnsupportedEncodingException e) {
			System.out.println("ERROR: CANNOT ENCODE PASSWORD AS UTF-8.");
			return false;
		}
		byte[] digest = md.digest();
		//Check against existing hash
		if(MessageDigest.isEqual(digest,this.mPassHash)) {
			return true;
		}
		//Increment failed login
		this.mDB.updateLoginFailedLogins(this.mLoginId);
		return false;
	}
	
	/**
	 * Generates a new random base64 session token.
	 * @return New random session token
	 */
	private String generateToken() {
		//Generate salt
		Random r = new SecureRandom();
		byte[] binaryToken = new byte[24];
		r.nextBytes(binaryToken);
		//Convert to hex
		byte[] token = Base64.encodeBase64(binaryToken);
		return new String(token);
	}
	
	/**
	 * Creates and stores a new session token for this login.
	 * @param ipAddr IP address of user requesting a new token
	 * @return New random session token which has been saved to database
	 */
	public String getNewToken(String ipAddr) {
		String newToken = this.generateToken();
		this.mDB.addSessionToken(newToken,this.mLoginId,ipAddr);
		return newToken;
	}

	/**
	 * Lists diary entries for the current login.
	 * @return List of DiaryEntry objects created by this Login
	 */
	public ArrayList<DiaryEntry> listDiaryEntries() {
		//Start output list
		ArrayList<DiaryEntry> outputList = new ArrayList<>();
		//Get database results
		ResultSet results = this.mDB.listEntriesByLogin(this.getLoginId());
		//Loop results, creating entries
		try {
			while(results.next()) {
				DiaryEntry newEntry = new DiaryEntry(this.mDB,results,this);
				outputList.add(newEntry);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return outputList;
	}

	/**
	 * Gets a diary entry by entry id for current login.
	 * @param diaryEntryId ID of diary entry which user is requesting
	 * @return DiaryEntry with ID equal to user input
	 */
	public DiaryEntry getDiaryEntryById(Integer diaryEntryId) {
		DiaryEntry output = null;
		//Get database results
		ResultSet results = this.mDB.getEntriesByIdAndLoginId(diaryEntryId,this.getLoginId());
		//Create object
		try {
			if(results.next()) {
                output = new DiaryEntry(this.mDB,results,this);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}

    /**
     * Gets a diary entry for specified date and current login
     * @param diaryEntryDate Date of diary entry which user is requesting
     * @return DiaryEntry with date equal to user input
     */
    public DiaryEntry getDiaryEntryByDate(Date diaryEntryDate) {
        DiaryEntry output = null;
        //Get database results
        ResultSet results = this.mDB.getEntriesByDateAndLoginId(diaryEntryDate,this.getLoginId());
        //Create object
        try {
            if(results.next()) {
                output = new DiaryEntry(this.mDB,results,this);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }
	
	/**
	 * Add a new diary entry
	 * @param entryDate Date of diary entry to add
	 * @param entryText Text of diary entry to add
	 * @return The new diary entry which was created, or null.
	 */
	public DiaryEntry addDiaryEntry(Date entryDate, String entryText) {
		//Add entry in database
		ResultSet results = this.mDB.addEntry(this.getLoginId(),entryDate,entryText);
		//Check results
		if(results == null) {
			return null;
		}
		try {
			if(results.next()) {
                //Create new diaryEntry
				Integer entryId = results.getInt("entry_id");
				return this.getDiaryEntryById(entryId);
            }
		} catch (SQLException e) {
			return null;
		}
		return null;
	}
}
