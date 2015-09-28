package uk.co.coales.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jettison.json.JSONObject;

import uk.co.coales.utils.Database;

public class DiaryEntry {
	private Database mDB = null;
	private Integer mEntryId = null;
	private Login mLogin = null;
	private Date mDate = null;
	private String mText = null;
	
	DiaryEntry(Database db, ResultSet entryResult, Login login) throws SQLException {
		if(entryResult == null) {
			throw new NullPointerException();
		}
		this.mDB = db;
		this.mLogin = login;
		this.mEntryId = entryResult.getInt("entry_id");
		this.mDate = entryResult.getDate("entry_date");
		this.mText = entryResult.getString("entry_text");
	}

	/**
	 * Date getter
	 * @return
	 */
	public Date getDate() {
		return this.mDate;
	}
	
	public String getDateString() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getDate());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String output = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
		return output;
	}
	
	/**
	 * Entry text getter
	 */
	public String getText() {
		return this.mText;
	}
	
	public JSONObject toJson() {
		JSONObject jsonOutput = new JSONObject();
		jsonOutput.put("date",value);
		return jsonOutput;
	}
}
