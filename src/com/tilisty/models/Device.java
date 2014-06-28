package com.tilisty.models;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * A representation of a Device. Simply stores the device id to 
 * keep a record of the what devices are connected.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 *
 */
public class Device {

	private String deviceId;
	public Device(JSONObject info) {
		// TODO Auto-generated constructor stub
		this.initializeFromJSON(info);
	}
	
	public Device() {
		this.deviceId = "";
	}
	
	public void initializeFromJSON(JSONObject info) {
	
		try {
			if(info.has("deviceId")) {
				this.deviceId = info.getString("deviceId");
			}
		} catch(JSONException e) {
			
		}
	}

}
