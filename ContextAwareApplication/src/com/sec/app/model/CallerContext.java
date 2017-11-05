/**
 * 
 */
package com.sec.app.model;

import com.sec.app.model.EnumValues.CallUrgency;
import com.sec.app.model.EnumValues.FeedBack;
import com.sec.app.model.EnumValues.Relation;
import com.sec.app.model.EnumValues.RingerMode;

/**
 * @author sukhdev
 *
 */
public class CallerContext {
	
	
	int mCallerID;
	Relation mRealtionToUser;
	CallUrgency mCallUrgency;
	FeedBack mFeedBack;
	FeedBack mUpdatedFeedBack;
	String mCallerName;
	int mCallId;
	RingerMode mReceivedModeFromUser; 
	
	/**
	 * @return the mRealtionToUser
	 */
	public Relation getmRealtionToUser() {
		return mRealtionToUser;
	}
	/**
	 * @param mRealtionToUser the mRealtionToUser to set
	 */
	public void setmRealtionToUser(Relation mRealtionToUser) {
		this.mRealtionToUser = mRealtionToUser;
	}
	/**
	 * @return the mCallUrgency
	 */
	public CallUrgency getmCallUrgency() {
		return mCallUrgency;
	}
	/**
	 * @param mCallUrgency the mCallUrgency to set
	 */
	public void setmCallUrgency(CallUrgency mCallUrgency) {
		this.mCallUrgency = mCallUrgency;
	}
	/**
	 * @return the mFeedBack
	 */
	public FeedBack getmFeedBack() {
		return mFeedBack;
	}
	/**
	 * @param mFeedBack the mFeedBack to set
	 */
	public void setmFeedBack(FeedBack mFeedBack) {
		this.mFeedBack = mFeedBack;
	}
	/**
	 * @return the mCallerID
	 */
	public int getmCallerID() {
		return mCallerID;
	}
	/**
	 * @param mCallerID the mCallerID to set
	 */
	public void setmCallerID(int mCallerID) {
		this.mCallerID = mCallerID;
	}
	/**
	 * @return the mCallerName
	 */
	public String getmCallerName() {
		return mCallerName;
	}
	/**
	 * @param mCallerName the mCallerName to set
	 */
	public void setmCallerName(String mCallerName) {
		this.mCallerName = mCallerName;
	}
	/**
	 * @return the mCallId
	 */
	public int getmCallId() {
		return mCallId;
	}
	/**
	 * @param mCallId the mCallId to set
	 */
	public void setmCallId(int mCallId) {
		this.mCallId = mCallId;
	}
	/**
	 * @return the mReceivedModeFromUser
	 */
	public RingerMode getmReceivedModeFromUser() {
		return mReceivedModeFromUser;
	}
	/**
	 * @param mReceivedModeFromUser the mReceivedModeFromUser to set
	 */
	public void setmReceivedModeFromUser(RingerMode mReceivedModeFromUser) {
		this.mReceivedModeFromUser = mReceivedModeFromUser;
	}
	/**
	 * @return the mUpdatedFeedBack
	 */
	public FeedBack getmUpdatedFeedBack() {
		return mUpdatedFeedBack;
	}
	/**
	 * @param mUpdatedFeedBack the mUpdatedFeedBack to set
	 */
	public void setmUpdatedFeedBack(FeedBack mUpdatedFeedBack) {
		this.mUpdatedFeedBack = mUpdatedFeedBack;
	}

}
