/**
 * 
 */
package com.sec.app.model;

import java.util.HashMap;

import com.sec.app.model.EnumValues.Relation;
import com.sec.app.model.EnumValues.RingerMode;


/**
 * @author sukhdev
 *
 */
public class UserContext {
	
	
	int mUserID = 5043;
	
	RingerMode mUserExpectedMode;
	RingerMode mUserMode;
	
	HashMap<String, Relation> mSocialCircle = new HashMap<>();
	Place mPlace;
	CallerContext mCallerContext;
	
	/**
	 * 
	 * @param pID
	 * @param pEMode
	 * @param pMode
	 */
	public UserContext(int pID,RingerMode pEMode, RingerMode pMode) {
		mUserID = pID;
		mPlace = new Place();
		mCallerContext = new CallerContext();
		mUserMode = pMode;
		mUserExpectedMode = pEMode;
		addingSocialRelations(); 
	}
	
	/**
	 * 
	 */
	private void addingSocialRelations() {
		mSocialCircle.put("Arwen", Relation.FAMILY);
		mSocialCircle.put("Bilbo", Relation.FRIEND);
		mSocialCircle.put("Ceorl", Relation.COLLEAGUE);
		mSocialCircle.put("Denethor", Relation.STRANGER);
		mSocialCircle.put("Elrond", Relation.FAMILY);
		mSocialCircle.put("Faramir", Relation.FRIEND);
		mSocialCircle.put("Gandalf", Relation.COLLEAGUE);
		mSocialCircle.put("Hurin", Relation.STRANGER);
		mSocialCircle.put("Isildur", Relation.FAMILY);
		mSocialCircle.put("Legolas", Relation.FRIEND);
		mSocialCircle.put("Maggot", Relation.COLLEAGUE);
		mSocialCircle.put("Nazgul", Relation.STRANGER);
		mSocialCircle.put("Orophin", Relation.FAMILY);
		mSocialCircle.put("Radagast", Relation.FRIEND);
		mSocialCircle.put("Sauron", Relation.COLLEAGUE);
	}


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
	 * @return the mSocialCircle
	 */
	public HashMap<String, Relation> getmSocialCircle() {
		return mSocialCircle;
	}
	/**
	 * @param mSocialCircle the mSocialCircle to set
	 */
	public void setmSocialCircle(HashMap<String, Relation> mSocialCircle) {
		this.mSocialCircle = mSocialCircle;
	}
	/**
	 * @return the mPlace
	 */
	public Place getmPlace() {
		return mPlace;
	}
	/**
	 * @param mPlace the mPlace to set
	 */
	public void setmPlace(Place mPlace) {
		this.mPlace = mPlace;
	}
	/**
	 * @return the mCallerContext
	 */
	public CallerContext getmCallerContext() {
		return mCallerContext;
	}
	/**
	 * @param mCallerContext the mCallerContext to set
	 */
	public void setmCallerContext(CallerContext mCallerContext) {
		this.mCallerContext = mCallerContext;
	}
	/**
	 * @return the mUserExpectedMode
	 */
	public RingerMode getmUserExpectedMode() {
		return mUserExpectedMode;
	}
	/**
	 * @param mUserExpectedMode the mUserExpectedMode to set
	 */
	public void setmUserExpectedMode(RingerMode mUserExpectedMode) {
		this.mUserExpectedMode = mUserExpectedMode;
	}
	/**
	 * @return the mUserMode
	 */
	public RingerMode getmUserMode() {
		return mUserMode;
	}
	/**
	 * @param mUserMode the mUserMode to set
	 */
	public void setmUserMode(RingerMode mUserMode) {
		this.mUserMode = mUserMode;
	}
	
}
