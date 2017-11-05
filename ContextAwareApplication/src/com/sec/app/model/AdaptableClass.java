/**
 * 
 */
package com.sec.app.model;

import com.sec.app.model.EnumValues.BrightNessLevel;
import com.sec.app.model.EnumValues.CallUrgency;
import com.sec.app.model.EnumValues.NoiseLevel;
import com.sec.app.model.EnumValues.Relation;
import com.sec.app.model.EnumValues.RingerMode;

/**
 * @author sukhdev
 *
 */
public class AdaptableClass {
	
	
	public AdaptableClass() {
		// TODO Auto-generated constructor stub
	}
	
	public AdaptableClass(String getmPlaceName, NoiseLevel getmNoiseLevel,
			BrightNessLevel getmBrightnessLevel, CallUrgency getmCallUrgency,
			Relation getmRealtionToUser, RingerMode getmReceivedModeFromUser, String getmRationale) {
		mPlaceName = getmPlaceName;
		mRelation = getmRealtionToUser;
		mCallUrgency = getmCallUrgency;
		mNoiseLevel = getmNoiseLevel;
		mBrightNessLevel = getmBrightnessLevel;
		mRingerMode = getmReceivedModeFromUser;
		mRationale = getmRationale;
	}

	public String mPlaceName;
	public Relation mRelation;
	public CallUrgency mCallUrgency;
	public NoiseLevel mNoiseLevel;
	public BrightNessLevel mBrightNessLevel;
	public RingerMode mRingerMode;
	public String mRationale;

}
