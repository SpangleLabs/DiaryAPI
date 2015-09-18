package uk.co.coales.data;

import java.util.Date;

public class DiaryEntry {
	private Integer mId = null;
	private Date mDate = null;
	private String mEntry = null;
	
	/**
	 * Date getter
	 * @return
	 */
	public Date getDate() {
		return this.mDate;
	}
	
	/**
	 * Entry text getter
	 */
	public String getEntry() {
		return this.mEntry;
	}
}
