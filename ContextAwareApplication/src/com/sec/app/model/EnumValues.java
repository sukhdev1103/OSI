/**
 * 
 */
package com.sec.app.model;

/**
 * @author sukhdev
 *
 */
public class EnumValues {
	

	public static enum Relation{
		FAMILY,
		FRIEND,
		STRANGER,
		COLLEAGUE
	};
	
	public static enum NoiseLevel{
		HIGH,
		MEDIUM,
		LOW
	};
	
	public static enum BrightNessLevel{
		HIGH,
		MEDIUM,
		LOW
	};
	
	

	public static enum RingerMode{
		LOUD,
		VIBRATE,
		SILENT
	};
	
	public static enum CallUrgency{
		URGENT,
		CASUAL,
		NONE
	};
	
	public static enum FeedBack{
		POSITIVE,
		NEGATIVE,
		NEUTRAL,
		NULL
	};
	
}
