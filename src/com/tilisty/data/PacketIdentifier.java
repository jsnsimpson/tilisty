package com.tilisty.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper to identify what packet type the server is dealing with.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 */
public class PacketIdentifier {
	
	public static PacketTypes identifyPacket(JSONObject message) {
		try {
			if(message.getString("type").equals("register_device")) {
				return PacketTypes.REGISTER_DEVICE;
			} else if(message.getString("type").equals("register_view")) {
				return PacketTypes.REGISTER_VIEWS;
			} else {
				return PacketTypes.UNKNOWN;
			}
			
		} catch(JSONException e) {
			
		}
		return PacketTypes.UNKNOWN;
	}
}
