package com.tilisty.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tilisty.models.Device;
import com.tilisty.models.TiViewModel;
import com.tilisty.models.TilistyModel;

/**
 * This object is responsible for handling the messages received from the
 * device for registering itself and its views.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 *
 */
public class DataModelDelegate {

	public DataModelDelegate() {}
	
	/**
	 * Process the packet received, either register the device or start the population
	 * of the Views on TilistyModel. 
	 * 
	 * @param JSONObject message
	 * @return JSONObject response - to send back in response to the message received.
	 */
	public JSONObject processPacket(JSONObject message) {
		PacketTypes type = PacketIdentifier.identifyPacket(message);
		JSONObject response = new JSONObject();
		try {
			switch(type) {
			case REGISTER_DEVICE:
				System.out.println("Registering Device");
				Device device = new Device(message);
				TilistyModel.getInstance().registerDevice(device);
				break;
			case REGISTER_VIEWS:
				//do we have multiple views or just one?
				if(message.has("views")) {
					try {
						JSONArray arr = message.getJSONArray("views");
						for(int i = 0; i < arr.length(); i++) {
							TiViewModel view = new TiViewModel(arr.getJSONObject(i));
							TilistyModel.getInstance().addView(view);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					} 
					
				} else if(message.has("view")) {
					TiViewModel view = new TiViewModel(message.getJSONObject("view"));
					TilistyModel.getInstance().addView(view);
				}
				break;
				case UNKNOWN:
					break;
				default:
					break;
			}
			
			if(message.has("msgId")) {
				response.put("msgId", message.getInt("msgId"));
				response.put("type", "rsp");
			}
			
		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		return response;
		
	}

}
