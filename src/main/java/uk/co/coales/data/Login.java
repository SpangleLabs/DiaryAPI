package uk.co.coales.data;

import java.util.ArrayList;
import java.util.Date;

public class Login {
	private String mUsername = null;
	private String mEmail = null;
	
	/**
	 * Creates a new login from a valid authentication token.
	 */
	public static Login fromSessionToken(String authToken) {
		return null;
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
