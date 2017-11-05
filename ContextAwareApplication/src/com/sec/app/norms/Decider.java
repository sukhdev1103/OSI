package com.sec.app.norms;

import java.util.List;
import java.util.Scanner;

import com.sec.app.model.AdaptableClass;
import com.sec.app.model.EnumValues.BrightNessLevel;
import com.sec.app.model.EnumValues.CallUrgency;
import com.sec.app.model.EnumValues.FeedBack;
import com.sec.app.model.EnumValues.NoiseLevel;
import com.sec.app.model.EnumValues.Relation;
import com.sec.app.model.Neighbour;
import com.sec.app.model.UserContext;
import com.sec.app.model.EnumValues.RingerMode;

public class Decider {
	
	boolean isRequiredToAdd = true;
	public String pRationale ="";
	
	/**
	 * 
	 * @param mUserContext
	 * @param mCallerContext
	 * @param mRingerMode
	 */
	public RingerMode socialBenefitFunction(UserContext mUserContext){
		RingerMode mR = checkForExistingRecord(mUserContext);  
		if(mR != null){ 
			isRequiredToAdd = false;
			return mR;
		}else{
			isRequiredToAdd = true;
		}
		
		RingerMode mMajorityExpected = null;
		mMajorityExpected = getMajority(mUserContext);
		Relation mRelationWithCaller = mUserContext.getmCallerContext().getmRealtionToUser();
		NoiseLevel mNoiseLevel = mUserContext.getmPlace().getmNoiseLevel();
		BrightNessLevel mBrightNessLevel = mUserContext.getmPlace().getmBrightnessLevel();
		
		switch(mUserContext.getmCallerContext().getmCallUrgency()){
		case URGENT:
			if(mRelationWithCaller == Relation.FAMILY){
				pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+caller_relationship-IS-1+AND+call_reason-IS-URGENT)";
			}else if(mRelationWithCaller == Relation.FRIEND){
				pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+caller_relationship-IS-2+AND+call_reason-IS-URGENT)";
			}
			
			if(mRelationWithCaller == Relation.FAMILY ||
					mRelationWithCaller == Relation.FRIEND){
				return RingerMode.LOUD;
			}
			if(mMajorityExpected == RingerMode.LOUD || mNoiseLevel == NoiseLevel.HIGH || 
					(mBrightNessLevel == BrightNessLevel.HIGH && mNoiseLevel == NoiseLevel.MEDIUM)){
				if(mMajorityExpected == RingerMode.LOUD){
					pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+Majority(expected_mode)-IS-LOUD+AND+call_reason-IS-URGENT)";
				}else if(mNoiseLevel == NoiseLevel.HIGH){
					pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+noise-IS-"+mUserContext.getmPlace().getmNoiseValue()
							+"AND+call_reason-IS-URGENT)";
				}else if(mNoiseLevel == NoiseLevel.MEDIUM && mBrightNessLevel == BrightNessLevel.HIGH){
					pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+noise-IS-"+mUserContext.getmPlace().getmNoiseValue()
							+"AND+brightness-IS-"+mUserContext.getmPlace().getmBrightnessValue()+"AND+call_reason-IS-URGENT)";
				}
				return RingerMode.LOUD;
			}else{
				pRationale = "ArgInFav(ringermode-IS-VIBRATE+WHEN+call_reason-IS-URGENT)";
				return RingerMode.VIBRATE;
			}
		
		case CASUAL:  
			switch(mUserContext.getmCallerContext().getmRealtionToUser()){
			
			case FAMILY:
			case FRIEND:
				if(mMajorityExpected == RingerMode.VIBRATE){
					if(mNoiseLevel == NoiseLevel.HIGH || 
							(mBrightNessLevel == BrightNessLevel.HIGH && mNoiseLevel == NoiseLevel.MEDIUM)){
						if(mNoiseLevel == NoiseLevel.HIGH){
							pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+noise-IS-"+mUserContext.getmPlace().getmNoiseValue()+")";
						}else if(mBrightNessLevel == BrightNessLevel.HIGH && mNoiseLevel == NoiseLevel.MEDIUM){
							pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+noise-IS-"+mUserContext.getmPlace().getmNoiseValue()
									+"AND+brightness-IS-"+mUserContext.getmPlace().getmBrightnessValue()+")";
						}
						return RingerMode.LOUD;
					}else{
						pRationale = "ArgInFav(ringermode-IS-VIBRATE+WHEN+Majority(expected_mode)-IS-VIBRATE)";
						return RingerMode.VIBRATE;
					}
				
				}else if(mMajorityExpected == RingerMode.SILENT){
					if(mNoiseLevel == NoiseLevel.HIGH || 
							(mBrightNessLevel == BrightNessLevel.HIGH && mNoiseLevel == NoiseLevel.MEDIUM)){
						if(mNoiseLevel == NoiseLevel.HIGH){
							pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+noise-IS-"+mUserContext.getmPlace().getmNoiseValue()+")";
						}else if(mBrightNessLevel == BrightNessLevel.HIGH && mNoiseLevel == NoiseLevel.MEDIUM){
							pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+noise-IS-"+mUserContext.getmPlace().getmNoiseValue()
									+"AND+brightness-IS-"+mUserContext.getmPlace().getmBrightnessValue()+")";
						}
						return RingerMode.LOUD;
					}else if(mNoiseLevel == NoiseLevel.MEDIUM || 
							(mBrightNessLevel == BrightNessLevel.MEDIUM)){
						pRationale = "ArgInFav(ringermode-IS-VIBRATE+WHEN+noise-IS-"+mUserContext.getmPlace().getmNoiseValue()
								+"AND+brightness-IS-"+mUserContext.getmPlace().getmBrightnessValue()+")";
						return RingerMode.VIBRATE;
					}else{
						pRationale = "ArgInFav(ringermode-IS-SILENT+WHEN+Majority(expected_mode)-IS-SILENT)";
						return RingerMode.SILENT;
					}
				}
				else{
					pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+Majority(expected_mode)-IS-LOUD)";
					return RingerMode.LOUD;
				}
			case STRANGER:
			case COLLEAGUE:
				if(mMajorityExpected == RingerMode.VIBRATE){
					pRationale = "ArgInFav(ringermode-IS-VIBRATE+WHEN+Majority(expected_mode)-IS-VIBRATE)";	
					return RingerMode.VIBRATE;
				}else if(mMajorityExpected == RingerMode.SILENT){
						pRationale = "ArgInFav(ringermode-IS-SILENT+WHEN+Majority(expected_mode)-IS-SILENT)";
						return RingerMode.SILENT;
				}else{
					pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+Majority(expected_mode)-IS-LOUD)";
					return RingerMode.LOUD;
				}
			}
		case NONE:	
			if(mMajorityExpected == RingerMode.LOUD && (mNoiseLevel == NoiseLevel.HIGH ||
					(mBrightNessLevel == BrightNessLevel.HIGH && mNoiseLevel == NoiseLevel.MEDIUM))){
				if(mMajorityExpected == RingerMode.LOUD && mNoiseLevel == NoiseLevel.HIGH){
					pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+Majority(expected_mode)-IS-LOUD+AND+noise-IS-"
				+mUserContext.getmPlace().getmBrightnessValue()+")";
				}else if(mMajorityExpected == RingerMode.LOUD && 
						(mNoiseLevel == NoiseLevel.MEDIUM && mBrightNessLevel == BrightNessLevel.HIGH)){
					pRationale = "ArgInFav(ringermode-IS-LOUD+WHEN+Majority(expected_mode)-IS-LOUD+AND+noise-IS-"
							+mUserContext.getmPlace().getmBrightnessValue()+
							"AND+brightness-IS-"+mUserContext.getmPlace().getmBrightnessValue()+")";
				}
				return RingerMode.LOUD;
			}else{
				if(mMajorityExpected == RingerMode.VIBRATE){
					pRationale = "ArgInFav(ringermode-IS-VIBRATE+WHEN+Majority(expected_mode)-IS-VIBRATE)";
					return RingerMode.VIBRATE;	
				}else{
					pRationale = "ArgInFav(ringermode-IS-SILENT+WHEN+Majority(expected_mode)-IS-SILENT)";
					return RingerMode.SILENT;
				}
				
			}
		default:
				return RingerMode.LOUD;
		}
	}
	
	/**
	 * 
	 * @param mUserContext
	 * @return
	 */
	private RingerMode checkForExistingRecord(UserContext mUserContext) { 
		if(MainClass.mAdaptableList.size() > 0){
			AdaptableClass mTemp;
			for(int i = 1; i <= MainClass.mAdaptableList.size();i++){
				mTemp = MainClass.mAdaptableList.get(i);
				if(mTemp.mPlaceName.equals(mUserContext.getmPlace().getmPlaceName())
						&& mTemp.mBrightNessLevel == mUserContext.getmPlace().getmBrightnessLevel()
						&& mTemp.mNoiseLevel == mUserContext.getmPlace().getmNoiseLevel()
						&& mTemp.mCallUrgency == mUserContext.getmCallerContext().getmCallUrgency()
						&& mTemp.mRelation == mUserContext.getmCallerContext().getmRealtionToUser()){
					pRationale = mTemp.mRationale;
					return mTemp.mRingerMode;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param mUserContext
	 * @return
	 */
	private RingerMode getMajority(UserContext mUserContext) {
		
		int mCountForLoud = 0;
		int mCountForVibreat = 0;
		int mCountForSilent = 0;
		List<Neighbour> mList = mUserContext.getmPlace().getmNeighbours();
		for(Neighbour mN: mList){
			switch (mN.getmExpectedMode()){ 
			case LOUD:
				mCountForLoud++;
				break;
			case SILENT:
				mCountForSilent++;
				break;
			case VIBRATE:
				mCountForVibreat++;
				break;
			default:
				break;
			}
		}
		
		if(mCountForLoud >= mCountForSilent && mCountForLoud >= mCountForVibreat){
			return RingerMode.LOUD;
		}else if(mCountForVibreat >= mCountForSilent && mCountForVibreat >= mCountForLoud){
			return RingerMode.VIBRATE;
		}else{
			return RingerMode.SILENT;
		}
	}

	/**
	 * 
	 * @param mSendRingerMode
	 * @param mUserContext
	 */
	public void addToAdaptableList(RingerMode mSendRingerMode, UserContext mUserContext) { 
		if(isRequiredToAdd){
			AdaptableClass m = new AdaptableClass(mUserContext.getmPlace().getmPlaceName(),
					mUserContext.getmPlace().getmNoiseLevel(),
					mUserContext.getmPlace().getmBrightnessLevel(),
					mUserContext.getmCallerContext().getmCallUrgency(),
					mUserContext.getmCallerContext().getmRealtionToUser(),
					mUserContext.getmCallerContext().getmReceivedModeFromUser(),pRationale);
			MainClass.mAdaptableList.put(MainClass.mAdaptableList.size() + 1,m);
		}
		
	}
	
	/**
	 * 
	 * @param mUserContext
	 */
	public void updatingExistingRecords(UserContext mUserContext){
		int mNeutral = 0;
		int mPositive = 0;
		int mNegative = 0;
		for(int i = 0; i < mUserContext.getmPlace().getmNeighbours().size();i++){
			Neighbour m  = mUserContext.getmPlace().getmNeighbours().get(i);
			if(m != null){
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
		if(mUserContext.getmCallerContext().getmCallUrgency() == CallUrgency.URGENT){
			if(mUserContext.getmCallerContext().getmFeedBack() ==  FeedBack.NEGATIVE){
				System.out.println("Updating for Urgent Mode beacuse Caller feedback was negative."); 
				if(MainClass.mAdaptableList.size() > 0){
					AdaptableClass mTemp;
					for(int i = 1; i <= MainClass.mAdaptableList.size();i++){
						mTemp = MainClass.mAdaptableList.get(i);
						if(mTemp.mPlaceName.equals(mUserContext.getmPlace().getmPlaceName())
								&& mTemp.mBrightNessLevel == mUserContext.getmPlace().getmBrightnessLevel()
								&& mTemp.mNoiseLevel == mUserContext.getmPlace().getmNoiseLevel()
								&& mTemp.mCallUrgency == mUserContext.getmCallerContext().getmCallUrgency()
								&& mTemp.mRelation == mUserContext.getmCallerContext().getmRealtionToUser()){
							if(mTemp.mRingerMode == RingerMode.VIBRATE){
								mTemp.mRingerMode = RingerMode.LOUD;
							}else if(mTemp.mRingerMode == RingerMode.SILENT){
								mTemp.mRingerMode = RingerMode.VIBRATE;
							}
							MainClass.mAdaptableList.put(i, mTemp);
						}
					}
				}
			}
		}else if(mUserContext.getmCallerContext().getmCallUrgency() == CallUrgency.CASUAL){
			if(mNegative > (mNeutral + mPositive)){
				System.out.println("Updating for Casusal Mode beacuse Neighbour's feedbacks were negative.");
				if(MainClass.mAdaptableList.size() > 0){
					AdaptableClass mTemp;
					for(int i = 1; i <= MainClass.mAdaptableList.size();i++){
						mTemp = MainClass.mAdaptableList.get(i);
						if(mTemp.mPlaceName.equals(mUserContext.getmPlace().getmPlaceName())
								&& mTemp.mBrightNessLevel == mUserContext.getmPlace().getmBrightnessLevel()
								&& mTemp.mNoiseLevel == mUserContext.getmPlace().getmNoiseLevel()
								&& mTemp.mCallUrgency == mUserContext.getmCallerContext().getmCallUrgency()
								&& mTemp.mRelation == mUserContext.getmCallerContext().getmRealtionToUser()){
							if(mTemp.mRingerMode == RingerMode.VIBRATE){
								mTemp.mRingerMode = RingerMode.SILENT;
							}else if(mTemp.mRingerMode == RingerMode.LOUD){
								mTemp.mRingerMode = RingerMode.VIBRATE;
							}
							MainClass.mAdaptableList.put(i, mTemp);
						}
					}
				}
			}else if((mUserContext.getmCallerContext().getmRealtionToUser() == Relation.FAMILY ||
					mUserContext.getmCallerContext().getmRealtionToUser() == Relation.FRIEND) 
					&& mUserContext.getmCallerContext().getmFeedBack() == FeedBack.NEGATIVE){
				System.out.println("Updating for Casual Mode beacuse Caller(Family/Friend) feedback was negative.");
				if(MainClass.mAdaptableList.size() > 0){
					AdaptableClass mTemp;
					for(int i = 1; i <= MainClass.mAdaptableList.size();i++){
						mTemp = MainClass.mAdaptableList.get(i);
						if(mTemp.mPlaceName.equals(mUserContext.getmPlace().getmPlaceName())
								&& mTemp.mBrightNessLevel == mUserContext.getmPlace().getmBrightnessLevel()
								&& mTemp.mNoiseLevel == mUserContext.getmPlace().getmNoiseLevel()
								&& mTemp.mCallUrgency == mUserContext.getmCallerContext().getmCallUrgency()
								&& mTemp.mRelation == mUserContext.getmCallerContext().getmRealtionToUser()){
							if(mTemp.mRingerMode == RingerMode.VIBRATE){
								mTemp.mRingerMode = RingerMode.LOUD;
							}else if(mTemp.mRingerMode == RingerMode.SILENT){
								mTemp.mRingerMode = RingerMode.VIBRATE;
							}
							MainClass.mAdaptableList.put(i, mTemp);
						}
					}
				}
			}

		}else{
			if(mNegative > (mNeutral + mPositive)){
				System.out.println("Updating for None Mode beacuse Neighbour's feedbacks were negative.");
				if(MainClass.mAdaptableList.size() > 0){
					AdaptableClass mTemp;
					for(int i = 1; i <= MainClass.mAdaptableList.size();i++){
						mTemp = MainClass.mAdaptableList.get(i);
						if(mTemp.mPlaceName.equals(mUserContext.getmPlace().getmPlaceName())
								&& mTemp.mBrightNessLevel == mUserContext.getmPlace().getmBrightnessLevel()
								&& mTemp.mNoiseLevel == mUserContext.getmPlace().getmNoiseLevel()
								&& mTemp.mCallUrgency == mUserContext.getmCallerContext().getmCallUrgency()
								&& mTemp.mRelation == mUserContext.getmCallerContext().getmRealtionToUser()){
							if(mTemp.mRingerMode == RingerMode.VIBRATE){
								mTemp.mRingerMode = RingerMode.SILENT;
							}else if(mTemp.mRingerMode == RingerMode.LOUD){
								mTemp.mRingerMode = RingerMode.VIBRATE;
							}
							MainClass.mAdaptableList.put(i, mTemp);
						}
					}
				}
			}
		}
		
		System.out.println("Select any char to continue:");
		Scanner mScanner = new Scanner(System.in);
		mScanner.next();
		
	}

}
