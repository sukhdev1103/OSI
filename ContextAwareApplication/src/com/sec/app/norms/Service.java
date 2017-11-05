package com.sec.app.norms;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.print.Printer.MarginType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sec.app.model.EnumValues.FeedBack;
import com.sec.app.model.EnumValues.RingerMode;

public class Service {
    private final ScheduledExecutorService scheduler = Executors
        .newScheduledThreadPool(1);

	public void startScheduleTask() {
    final ScheduledFuture<?> taskHandle = scheduler.scheduleAtFixedRate(
        new Runnable() {
            public void run() {
                try {
                	if(MainClass.mUserContext != null){	
                		JSONObject mJ = requestingCall();
                		sendFeedback(mJ);
                	}
                }catch(Exception ex) {
                    ex.printStackTrace(); //or loggger would be better
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * 
     * @param mJ
     */
    protected void sendFeedback(JSONObject mJ) { 
    	try {
			JSONArray mCalls = (JSONArray)mJ.get("calls");
			String mReason;
			String mRationale;
			int mCallId;
			String mRingerMode;
			int mCalleeId;
			for(int i=0; i< mCalls.length(); i++){
				mCallId = Integer.valueOf(((JSONObject)mCalls.get(i)).getString("callId"));
				mRationale = ((JSONObject)mCalls.get(i)).getString("rationale");
				mReason = ((JSONObject)mCalls.get(i)).getString("reason");
				mRingerMode = ((JSONObject)mCalls.get(i)).getString("ringermode");
				mCalleeId = Integer.valueOf(((JSONObject)mCalls.get(i)).getString("calleeId"));
				if(mCalleeId != 5043)
				calcucalteF1AndF2(mCallId,mRationale,mReason,mRingerMode); 
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

    /**
     * 
     * @param mCallId
     * @param mRationale
     * @param mReason
     * @param mRingerMode
     */
	private void calcucalteF1AndF2(int mCallId, String mRationale,
			String mReason, String mRingerMode) { 
		FeedBack F1;
		FeedBack F2;
		RingerMode mG = getRingerMode(mRingerMode);
		switch (mG) {
		case LOUD: 
			if(MainClass.mUserContext.getmUserExpectedMode() == RingerMode.SILENT 
				|| MainClass.mUserContext.getmUserExpectedMode() == RingerMode.VIBRATE){
				F1 = FeedBack.NEGATIVE;
			}else{
				F1 = FeedBack.NEUTRAL;
			}
			break;
		case VIBRATE: 
			if(MainClass.mUserContext.getmUserExpectedMode() == RingerMode.SILENT){
				F1 = FeedBack.NEGATIVE;
			}else if(MainClass.mUserContext.getmUserExpectedMode() == RingerMode.VIBRATE){
				F1 = FeedBack.NEUTRAL;
			}else{
				F1 = FeedBack.POSITIVE;
			}
			break;
		case SILENT:
			if(MainClass.mUserContext.getmUserExpectedMode() == RingerMode.SILENT){
				F1 = FeedBack.NEUTRAL;
			}else {
				F1 = FeedBack.POSITIVE;
			}
	
			break;

		default:
			F1 = FeedBack.NEUTRAL;
			break;
		}
		if(F1 == FeedBack.NEGATIVE){
			F2 = calculateF2(mRationale,mG,mRingerMode); 
		}else{
			F2 = F1;
		}
		makeFeedbackCall(F1,F2,mCallId); 
	}

	/**
	 * 
	 * @param f1
	 * @param f2
	 * @param mCallId
	 */
	private void makeFeedbackCall(FeedBack f1, FeedBack f2, int mCallId) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			HttpGet getNRequest = new HttpGet("http://yangtze.csc.ncsu.edu:9090/csc555sd/services.jsp"
					+ "?action=giveFeedback&callId="+mCallId+"&userId=5043&feedback="+f1.name()+"&feedbackUpdated="+f2.name());
			getNRequest.addHeader("content-type", "application/json");
			HttpResponse getNResult = httpClient.execute(getNRequest); 
			
			String jsonData = EntityUtils.toString(getNResult.getEntity());
			System.out.println("Notifications :");
			System.out.println("Feedback sent results for call:"+mCallId+ ", and result is:"+jsonData.trim()); 
		} catch (Exception ex) {
			//System.out.println(ex.getMessage()); 
		}
	}

	/**
	 * 
	 * @param mRationale
	 * @param mG
	 * @param mRingerMode
	 * @return
	 */
	private FeedBack calculateF2(String mRationale, RingerMode mG,
			String mRingerMode) {
		switch (mG) {
		case LOUD:
			if((mRationale.contains("caller_relationship-IS-1") || (mRationale.contains("caller_relationship-IS-2"))
					&& mRationale.toLowerCase().contains("call_reason-IS-URGENT".toLowerCase()))){ 
				return FeedBack.POSITIVE;
			}
			if(mRationale.toLowerCase().contains("call_reason-IS-URGENT".toLowerCase())){ 
				return FeedBack.POSITIVE;
			}
			if(mRationale.toLowerCase().contains("Majority(expected_mode)-IS-LOUD".toLowerCase())){ 
				return FeedBack.NEUTRAL;
			}
			
			if(mRationale.toLowerCase().contains("noise-IS-7".toLowerCase()) 
					|| mRationale.toLowerCase().contains("noise-IS-8".toLowerCase())
					|| mRationale.toLowerCase().contains("noise-IS-9".toLowerCase())
					|| mRationale.toLowerCase().contains("noise-IS-10".toLowerCase())){ 
				return FeedBack.NEUTRAL;
			}
			if(mRationale.toLowerCase().contains("brightness-IS-7".toLowerCase()) 
					|| mRationale.toLowerCase().contains("brightness-IS-8".toLowerCase())
					|| mRationale.toLowerCase().contains("brightness-IS-9".toLowerCase())
					|| mRationale.toLowerCase().contains("brightness-IS-10".toLowerCase())){ 
				return FeedBack.NEUTRAL;
			}
			
			return FeedBack.NEGATIVE;
		case SILENT:
			return FeedBack.NEUTRAL;
		case VIBRATE:
			if(mRationale.toLowerCase().contains("Majority(expected_mode)-IS-VIBRATE".toLowerCase())){ 
				return FeedBack.POSITIVE;
			}
			
			if(mRationale.toLowerCase().contains("noise-IS-5".toLowerCase()) 
					|| mRationale.toLowerCase().contains("noise-IS-6".toLowerCase())
					|| mRationale.toLowerCase().contains("noise-IS-7".toLowerCase())
					|| mRationale.toLowerCase().contains("noise-IS-4".toLowerCase())){ 
				return FeedBack.POSITIVE;
			}
			if(mRationale.toLowerCase().contains("brightness-IS-4".toLowerCase()) 
					|| mRationale.toLowerCase().contains("brightness-IS-5".toLowerCase())
					|| mRationale.toLowerCase().contains("brightness-IS-6".toLowerCase())
					|| mRationale.toLowerCase().contains("brightness-IS-7".toLowerCase())){ 
				return FeedBack.POSITIVE;
			}
			return FeedBack.NEUTRAL;
		default:
			
			break;
		}
		return null;
	}

	/**
	 * 
	 * @param mRingerMode
	 * @return
	 */
	private RingerMode getRingerMode(String mRingerMode) {
		if(mRingerMode.toLowerCase().equals("loud")){
			return RingerMode.LOUD;
		}else if(mRingerMode.toLowerCase().equals("vibrate")){
			return RingerMode.VIBRATE;
		}else if(mRingerMode.toLowerCase().equals("silent")){
			return RingerMode.SILENT;
		}
		return RingerMode.SILENT;
	}

	/**
     * 
     * @return JSONObject
     */
    private JSONObject requestingCall() {
    	JSONObject mJSon = null;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			HttpGet getNRequest = new HttpGet("http://yangtze.csc.ncsu.edu:9090/csc555sd/services.jsp"
					+ "?action=getCallsInCurrentPlace&userId=5043");
			getNRequest.addHeader("content-type", "application/json");
			HttpResponse getNResult = httpClient.execute(getNRequest); 
			
			String jsonData = EntityUtils.toString(getNResult.getEntity());
			System.out.println();
			System.out.println("Notification : Listening to neighbours calls:"+jsonData.trim()); 
			System.out.println();
			mJSon = new JSONObject(new JSONTokener(jsonData));
		} catch (Exception ex) {
		}
		return mJSon;
    }

    /**
     * 
     */
    public void shutdown() {
        if(scheduler != null) {
            scheduler.shutdown();
        }
    }
    
}