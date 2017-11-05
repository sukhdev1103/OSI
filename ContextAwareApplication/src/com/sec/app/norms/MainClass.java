/**
 * 
 */
package com.sec.app.norms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sec.app.model.EnumValues.BrightNessLevel;
import com.sec.app.model.EnumValues.CallUrgency;
import com.sec.app.model.EnumValues.FeedBack;
import com.sec.app.model.EnumValues.NoiseLevel;
import com.sec.app.model.EnumValues.Relation;
import com.sec.app.model.Neighbour;
import com.sec.app.model.EnumValues.RingerMode;
import com.sec.app.model.AdaptableClass;
import com.sec.app.model.Place;
import com.sec.app.model.UserContext;

/**
 * @author sukhdev
 *
 */
public class MainClass {
	
	public static Controller mController;
	public static List<String> mPlaces = new ArrayList<>();
	public static UserContext mUserContext;
	public static String mCurrentUserLocation;
	private static RingerMode mMode;
	private static RingerMode mExpectedMode;
	public static HashMap<Integer,AdaptableClass> mAdaptableList = new HashMap();
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		addingPlaces();
		runService(); 
		mainMethod(); 
	}

	private static void runService() {
		Service mBackgroundService = new Service();
		mBackgroundService.startScheduleTask();
	}

	/**
	 * 
	 */
	private static void mainMethod() {
		try{
			showsCommitment(); 
			selectLocation();  
			enterLocation();
			JSONObject mJsonObject = mController.requestCall();
			initializaNeighboursList(); 
			initializeCallerContext(mJsonObject); 
			mJsonObject = mController.sendResponse(mUserContext);
			mJsonObject = mController.getAllFeedbacks(mUserContext);
			checkForFeedback(mJsonObject);
			updateBasedOnFeedback();
			mController.mDecider.updatingExistingRecords(mUserContext); 
			//mController.exitLocation();
			mainMethod();
		}catch(Exception e){
			mainMethod();
		}
	}
	
	/**
	 * 
	 */
	private static void showsCommitment() {
		System.out.println(); 
		System.out.println("Commitments are:"); 
		if(MainClass.mAdaptableList.size() > 0){
			AdaptableClass mTemp;
			for(int i = 1; i <= MainClass.mAdaptableList.size();i++){
				mTemp = MainClass.mAdaptableList.get(i);
				System.out.println(mTemp.mPlaceName+","+mTemp.mCallUrgency+","+mTemp.mRelation+","
				+mTemp.mNoiseLevel+","+mTemp.mBrightNessLevel+","+mTemp.mRingerMode); 
			}
		}
		System.out.println(); 
	}

	/**
	 * 
	 */
	private static void enterLocation() {
		mUserContext = new UserContext(5043, mMode, mExpectedMode); 
		mController = new Controller();
		mController.enterLocation(mCurrentUserLocation, mMode, mExpectedMode); 
	}

	/**
	 * 
	 * @param mJsonObject
	 */
	private static void initializeCallerContext(JSONObject mJsonObject) {
		try {
			mUserContext.getmCallerContext().setmCallerID(Integer.valueOf(mJsonObject.getString("callerId")));
			mUserContext.getmCallerContext().setmCallerName(mJsonObject.getString("callerName")); 
			mUserContext.getmCallerContext().setmCallId(Integer.valueOf(mJsonObject.getString("callId")));
			mUserContext.getmCallerContext().setmCallUrgency(getUrgencyLevel(mJsonObject.getString("reason")));
			mUserContext.getmCallerContext().setmRealtionToUser(getRelationShip(Integer.valueOf(mJsonObject.getString("relationship")))); 
		} catch (JSONException e) {
			e.printStackTrace(); 
		} 
	}

	private static Relation getRelationShip(Integer valueOf) {
		switch (valueOf) {
		case 1: 
			return Relation.FAMILY;
		case 2: 
			return Relation.FRIEND;
		case 3: 
			return Relation.COLLEAGUE;
		case 4: 
			return Relation.STRANGER;
		default:
			return Relation.FAMILY;
		}
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private static CallUrgency getUrgencyLevel(String string) {
		if(string.equals("urgent")){
			return CallUrgency.URGENT;
		}else if(string.equals("casual")){
			return CallUrgency.CASUAL;
		}else {
			return CallUrgency.NONE;
		}
	}

	/**
	 * 
	 */
	private static void initializaNeighboursList() {
		JSONObject mJsonObject = mController.gettingNeighbourList();  
		parseJSONObjectToNeighbours(mJsonObject); 
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param mJsonObject
	 */
	private static void parseJSONObjectToNeighbours(JSONObject mJsonObject) { 
		
		JSONArray mNeighbours = null;
		try {
			mUserContext.getmPlace().setmNoiseLevel(getNoiseLevel(mJsonObject.getInt("noiselevel")));
			mUserContext.getmPlace().setmNoiseValue(mJsonObject.getInt("noiselevel")); 
			mUserContext.getmPlace().setmPlaceName(mJsonObject.getString("place"));
			mUserContext.getmPlace().setmBrightnessLevel(getBrightNessLevel(mJsonObject.getInt("brightnesslevel")));
			mUserContext.getmPlace().setmBrightnessValue(mJsonObject.getInt("brightnesslevel")); 
			mNeighbours = (JSONArray) mJsonObject.get("user"); 
		} catch (final JSONException e) {
			e.printStackTrace();
		}  
		populate(mNeighbours);  
	}
	
	/**
	 * 
	 * @param int1
	 * @return
	 */
	private static BrightNessLevel getBrightNessLevel(int int1) {
		if(int1 > 7){
			return BrightNessLevel.HIGH;
		}else if(int1 > 5){
			return BrightNessLevel.MEDIUM;
		}else {
			return BrightNessLevel.LOW;
		}
	}

	/**
	 * 
	 * @param int1
	 * @return
	 */
	private static NoiseLevel getNoiseLevel(int int1) { 
		if(int1 > 7){
			return NoiseLevel.HIGH;
		}else if(int1 > 4){
			return NoiseLevel.MEDIUM;
		}else {
			return NoiseLevel.LOW;
		}
	}

	/**
	 * 
	 * @param pJsonArray
	 * @return
	 */
	static void populate(JSONArray pJsonArray){
		String mString = "";
		for(int i = 0; i < pJsonArray.length(); i++){
			Neighbour mTemp = new Neighbour();
			try {
				mTemp.setmUserID(((JSONObject)pJsonArray.get(i)).getInt("id")); 
				mString = ((JSONObject)pJsonArray.get(i)).getString("expected");
				mTemp.setmExpectedMode(getRingerMode(mString)); 
				mString = ((JSONObject)pJsonArray.get(i)).getString("ringer-mode");
				mTemp.setmMode(getRingerMode(mString)); 
				mTemp.setmNeighbourName(((JSONObject)pJsonArray.get(i)).getString("name")); 
				mTemp.setmRelation(getRelationShip(((JSONObject)pJsonArray.get(i)).getInt("relationship")));   
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			mUserContext.getmPlace().getmNeighbours().add(mTemp);
		}
	}
	
	/**
	 * 
	 * @param mString
	 * @return
	 */
	private static RingerMode getRingerMode(String mString) {
		if(mString.equals("Silent")){
			return RingerMode.SILENT;	 
		}else if(mString.equals("Loud")){
			return RingerMode.LOUD;
		}else{
			return RingerMode.VIBRATE;
		}
	}

	/**
	 * 
	 */
	private static void addingPlaces() { 
		mPlaces.add("hunt");
		mPlaces.add("eb2");
		mPlaces.add("carmichael");
		mPlaces.add("oval");
		mPlaces.add("seminar");
		mPlaces.add("lab");
		mPlaces.add("meeting");
		mPlaces.add("party");
	}
	
	/**
	 * 
	 */
	private static void selectLocation() {
		System.out.println("Select Your Location from following list:");
		for(int i = 0; i < mPlaces.size() ; i++){
			System.out.println(i+": "+ mPlaces.get(i)); 
		}
		System.out.println(mPlaces.size()+": Any Other Location"); 
		Scanner mScanner = new Scanner(System.in);
		int mChoice = mScanner.nextInt();
		String mNewPlace = "";
		if(mChoice == mPlaces.size()){
			System.out.println("Enter Place Name:");
			mNewPlace = mScanner.next();
			mCurrentUserLocation = mNewPlace;
			mPlaces.add(mNewPlace);
		}else if(mChoice < 0 || mChoice > mPlaces.size()){
			System.out.println("Invalid Choice"); 
			System.exit(0); 
		}else{
			mCurrentUserLocation = mPlaces.get(mChoice);
		}	
		
		System.out.println("Enter Your Mode:");
		System.out.println("1 For Loud");
		System.out.println("2 For Vibrate");
		System.out.println("3 For Silent");
		mChoice = mScanner.nextInt();
		
		switch (mChoice) {
		case 1:
			mMode = RingerMode.LOUD;
			break;
		case 2:
			mMode = RingerMode.VIBRATE;
			break;	
		case 3:
			mMode = RingerMode.SILENT;
			break;
		default:
			System.out.println("Invalid Choice"); 
			System.exit(0);
			break;
		}
		
		System.out.println("Enter Your Expected Mode:");
		System.out.println("1 For Loud");
		System.out.println("2 For Vibrate");
		System.out.println("3 For Silent");
		mChoice = mScanner.nextInt();
		
		switch (mChoice) {
		case 1:
			mExpectedMode = RingerMode.LOUD;
			break;
		case 2:
			mExpectedMode = RingerMode.VIBRATE;
			break;	
		case 3:
			mExpectedMode = RingerMode.SILENT;
			break;
		default:
			System.out.println("Invalid Choice"); 
			System.exit(0);
			break;
		}
	}
	
	/**
	 * @param mJsonObject 
	 * 
	 */
	private static void checkForFeedback(JSONObject mJsonObject) {
		JSONArray mJsonArray = null;
		try {
			mJsonArray = (JSONArray) mJsonObject.get("feedbacks");  
			
			for(int i = 0; i < mJsonArray.length(); i++){
				int mId = ((JSONObject)(mJsonArray.get(i))).getInt("id");
				FeedBack mFeedBack = getFeedback(((JSONObject)(mJsonArray.get(i))).getString("feedback"));
				FeedBack mFeedBack1 = getFeedback(((JSONObject)(mJsonArray.get(i))).getString("feedbackUpdated"));
				if(mId ==  mUserContext.getmCallerContext().getmCallerID()){
					mUserContext.getmCallerContext().setmFeedBack(mFeedBack); 
					mUserContext.getmCallerContext().setmUpdatedFeedBack(mFeedBack1);
				}else{
					for(int j = 0; j < mUserContext.getmPlace().getmNeighbours().size(); j++){
						if(mUserContext.getmPlace().getmNeighbours().get(j).getmUserID() == mId){
							mUserContext.getmPlace().getmNeighbours().get(j).setmFeedBack(mFeedBack);
							mUserContext.getmPlace().getmNeighbours().get(j).setmUpdatedFeedBack(mFeedBack1); 
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param string
	 * @return
	 */
	private static FeedBack getFeedback(String string) {
		// TODO Auto-generated method stub
		if(string.equals("positive")){
			return FeedBack.POSITIVE;
		}else if(string.equals("negative")){
			return FeedBack.NEGATIVE;
		}else if(string.equals("neutral")){
			return FeedBack.NEUTRAL;
		}else {
			return FeedBack.NULL;
		}
	}
	
	private static void updateBasedOnFeedback() {
		int mNeutral = 0;
		int mPositive = 0;
		int mNegative = 0;
		System.out.println("Caller Detailes:");
		System.out.println();
		System.out.println("Caller Name : "+ mUserContext.getmCallerContext().getmCallerName());
		System.out.println("Caller Relationship with User : "+ mUserContext.getmCallerContext().getmRealtionToUser());
		System.out.println("Call Urgency : "+ mUserContext.getmCallerContext().getmCallUrgency());
		System.out.println("User Response as Ringer Mode : "+ mUserContext.getmCallerContext().getmReceivedModeFromUser());
		System.out.println("Caller Feedback : "+ mUserContext.getmCallerContext().getmFeedBack());
		System.out.println("Caller Feedback : "+ mUserContext.getmCallerContext().getmFeedBack());
		System.out.println("Neighbours Feedback :");
		if(mUserContext.getmPlace().getmNeighbours().size() > 0){
			
			for(int i = 0; i < mUserContext.getmPlace().getmNeighbours().size();i++){
				Neighbour m  = mUserContext.getmPlace().getmNeighbours().get(i);
				if(m != null && m.getmUpdatedFeedBack()!= null){
					switch(m.getmUpdatedFeedBack()) {
					case NEGATIVE:
						mNegative++;
						break;
					case NEUTRAL:
						mNeutral++;
						break;
					case POSITIVE:
						mPositive++;
						break;
					case NULL:
						switch(m.getmFeedBack()) {
						case NEGATIVE:
							mNegative++;
							break;
						case NEUTRAL:
							mNeutral++;
							break;
						case POSITIVE:
							mPositive++;
							break;
						default:
							break;
						}
						break;
					default:
						break;
					}
				}
			}

			System.out.println("Total Postive Sanctions: "+mPositive);
			System.out.println("Total Negative Sanctions: "+mNegative); 
			System.out.println("Total Neutral Sanctions: "+mNeutral);
			System.out.println(); 
		}
	}
	
}
