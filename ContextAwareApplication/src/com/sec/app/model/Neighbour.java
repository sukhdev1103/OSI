package com.sec.app.model;

import com.sec.app.model.EnumValues.FeedBack;
import com.sec.app.model.EnumValues.Relation;
import com.sec.app.model.EnumValues.RingerMode;

public class Neighbour {	
	
	int mUserID;
	Relation mRelation;
	RingerMode mMode;
	RingerMode mExpectedMode;
	FeedBack mFeedBack;
	FeedBack mUpdatedFeedBack;
	String mNeighbourName;
	/**
	 * @return the mUserID
	 */
	public int getmUserID() {
		return mUserID;
	}
	/**
	 * @param mUserID the mUserID to set
	 */
	public void setmUserID(int mUserID) {
		this.mUserID = mUserID;
	}
	/**
	 * @return the mRelation
	 */
	public Relation getmRelation() {
		return mRelation;
	}
	/**
	 * @param mRelation the mRelation to set
	 */
	public void setmRelation(Relation mRelation) {
		this.mRelation = mRelation;
	}
	/**
	 * @return the mMode
	 */
	public RingerMode getmMode() {
		return mMode;
	}
	/**
	 * @param mMode the mMode to set
	 */
	public void setmMode(RingerMode mMode) {
		this.mMode = mMode;
	}
	/**
	 * @return the mExpectedMode
	 */
	public RingerMode getmExpectedMode() {
		return mExpectedMode;
	}
	/**
	 * @param mExpectedMode the mExpectedMode to set
	 */
	public void setmExpectedMode(RingerMode mExpectedMode) {
		this.mExpectedMode = mExpectedMode;
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
	 * @return the mNeighbourName
	 */
	public String getmNeighbourName() {
		return mNeighbourName;
	}
	/**
	 * @param mNeighbourName the mNeighbourName to set
	 */
	public void setmNeighbourName(String mNeighbourName) {
		this.mNeighbourName = mNeighbourName;
	}
	/**
	 * @return the mUpdatedFeedback
	 */
	public FeedBack getmUpdatedFeedBack() {
		return mUpdatedFeedBack;
	}
	/**
	 * @param mUpdatedFeedback the mUpdatedFeedback to set
	 */
	public void setmUpdatedFeedBack(FeedBack mUpdatedFeedback) {
		this.mUpdatedFeedBack = mUpdatedFeedback;
	}
	
	
}
