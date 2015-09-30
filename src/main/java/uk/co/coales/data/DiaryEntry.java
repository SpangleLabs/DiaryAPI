package uk.co.coales.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
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
	 * Entry ID getter
	 * @return
	 */
	public Integer getEntryId() {
		return this.mEntryId;
	}

	/**
	 * Date getter
	 * @return
	 */
	public Date getDate() {
		return this.mDate;
	}
	
	/**
	 * Returns the date in ISO8601 format.
	 * @return
	 */
	public String getDateString() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getDate());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
	}
	
	/**
	 * Entry text getter
	 */
	public String getText() {
		return this.mText;
	}
	
	/**
	 * Output JSON object for this diary entry
	 * @return
	 * @throws JSONException
	 */
	public JSONObject toJson() throws JSONException {
		JSONObject jsonOutput = new JSONObject();
		jsonOutput.put("id",this.getEntryId());
		jsonOutput.put("date",this.getDateString());
		jsonOutput.put("text",this.getText());
		return jsonOutput;
	}
}
