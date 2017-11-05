package com.sec.app.norms;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sec.app.model.EnumValues.RingerMode;
import com.sec.app.model.UserContext;

public class Controller {
	public Decider mDecider;
	/**
	 * 
	 */
	public Controller(){
		mDecider = new Decider();
	}

	/**
	 * 
	 * @return JSONObject
	 */
	public JSONObject gettingNeighbourList(){ 
		JSONObject mJSon = null;

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			HttpGet getNRequest = new HttpGet("http://yangtze.csc.ncsu.edu:9090/csc555sd/services.jsp?action=getNeighbors&userId=5043");
			getNRequest.addHeader("content-type", "application/json");
			HttpResponse getNResult = httpClient.execute(getNRequest); 


			String jsonData = EntityUtils.toString(getNResult.getEntity());
			//System.out.println(jsonData); 

			mJSon = new JSONObject(new JSONTokener(jsonData)); 
		} catch (Exception ex) {
			//System.out.println(ex.getMessage()); 
		}
		return mJSon;
	} 
	
	
	
	/**
	 * 
	 * @return JSONObject
	 */
	public JSONObject requestCall(){ 
		JSONObject mJSon = null;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			HttpGet getNRequest = new HttpGet("http://yangtze.csc.ncsu.edu:9090/csc555sd/services.jsp?action=requestCall&userId=5043");
			getNRequest.addHeader("content-type", "application/json");
			HttpResponse getNResult = httpClient.execute(getNRequest); 


			String jsonData = EntityUtils.toString(getNResult.getEntity());

			mJSon = new JSONObject(new JSONTokener(jsonData));
		} catch (Exception ex) {
			//System.out.println(ex.getMessage()); 
		}
		return mJSon;
	}

	/**
	 * 
	 * @param mUserContext
	 * @return JSONObject
	 */
	public JSONObject sendResponse(UserContext mUserContext) { 
		JSONObject mJSon = null;
		RingerMode mSendRingerMode = mDecider.socialBenefitFunction(mUserContext);
		mUserContext.getmCallerContext().setmReceivedModeFromUser(mSendRingerMode);
		mDecider.addToAdaptableList(mSendRingerMode,mUserContext);
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			HttpGet getNRequest = new HttpGet("http://yangtze.csc.ncsu.edu:9090/csc555sd/services.jsp?"
					+ "action=responseCall&callId="
					+ mUserContext.getmCallerContext().getmCallId()+"&ringerMode="
					+ mSendRingerMode.name()+"&rationale="+mDecider.pRationale);
			getNRequest.addHeader("content-type", "application/json");
			HttpResponse getNResult = httpClient.execute(getNRequest); 
			
			String jsonData = EntityUtils.toString(getNResult.getEntity());

			mJSon = new JSONObject(new JSONTokener(jsonData));
		} catch (Exception ex) {
			//System.out.println(ex.getMessage()); 
		}
		return mJSon; 
	}

	/**
	 * 
	 * @param mCurrentUserLocation
	 * @param mMode
	 * @param mExpectedMode
	 */
	public void enterLocation(String mCurrentUserLocation, RingerMode mMode, RingerMode mExpectedMode) {  
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			String URL = "http://yangtze.csc.ncsu.edu:9090/csc555sd/services.jsp?"+
					"action=enterPlace&place="+mCurrentUserLocation+"&userId=5043&"
					+ "myMode="+mMode.name()+"&expectedMode="+mExpectedMode.name();

			HttpPost enterRequest = new HttpPost(URL);
			HttpResponse enterResult = httpClient.execute(enterRequest);
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 */
	public void exitLocation() { 
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			String URL = "http://yangtze.csc.ncsu.edu:9090/csc555sd/services.jsp?"+
					"?action=exitPlace&userId=5043";

			HttpPost exitRequest = new HttpPost(URL);
			HttpResponse exitResult = httpClient.execute(exitRequest);
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * @param mUserContext
	 * @return
	 */
	public JSONObject getAllFeedbacks(UserContext mUserContext) { 
		JSONObject mJSon = null;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			HttpGet getNRequest = new HttpGet("http://yangtze.csc.ncsu.edu:9090/csc555sd/services.jsp?action=listFeedbacks&callId="+
					mUserContext.getmCallerContext().getmCallId());
			getNRequest.addHeader("content-type", "application/json");
			HttpResponse getNResult = httpClient.execute(getNRequest); 
			

			String jsonData = EntityUtils.toString(getNResult.getEntity());
			//System.out.println(jsonData); 

			mJSon = new JSONObject(new JSONTokener(jsonData));
		} catch (Exception ex) {
			//System.out.println(ex.getMessage()); 
		}
		return mJSon;
	}

}