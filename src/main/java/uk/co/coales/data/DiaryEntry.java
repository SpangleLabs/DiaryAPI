package uk.co.coales.data;

import java.util.Date;

public class DiaryEntry {
	private Integer mEntryId = null;
	private Login mLogin = null;
	private Date mDate = null;
	private String mText = null;
	
	/**
	 * @return the mEntryId
	 */
	public Integer getmEntryId() {
		return mEntryId;
	}
	
	/**
	 * @param mEntryId the mEntryId to set
	 */
	public void setmEntryId(Integer mEntryId) {
		this.mEntryId = mEntryId;
	}
	
	/**
	 * @return the mLogin
	 */
	public Login getmLogin() {
		return mLogin;
	}
	
	/**
	 * @param mLogin the mLogin to set
	 */
	public void setmLogin(Login mLogin) {
		this.mLogin = mLogin;
	}
	
	/**
	 * @return the mDate
	 */
	public Date getmDate() {
		return mDate;
	}
	
	/**
	 * @param mDate the mDate to set
	 */
	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}
	
	/**
	 * @return the mText
	 */
	public String getmText() {
		return mText;
	}
	
	/**
	 * @param mText the mText to set
	 */
	public void setmText(String mText) {
		this.mText = mText;
	}
}
