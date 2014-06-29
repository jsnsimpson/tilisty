package com.tilisty.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * TiViewModel is responsible for managing it's own properties and other things to do with
 * TiView's which are passed in to the class.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1
 */
public class TiViewModel extends AbstractModel implements IObserver {

	private HashMap<String, TiProperty> propertiesHash;
	private ArrayList<TiProperty> properties;
	private ArrayList<TiViewModel> children;
	private String id;
	private String type;
	
	public TiViewModel(JSONObject info) {
		
		super();
		this.propertiesHash = new HashMap<String, TiProperty>();
		this.properties = new ArrayList<TiProperty>();
		this.children = new ArrayList<TiViewModel>();
		this.fromJSON(info);
	}
	
	/**
	 * Constructs the view model based on a valid JSONObject
	 * passed to it
	 * @param JSONObject info
	 */
	public void fromJSON(JSONObject info) {
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> keys = info.keys();
			
			while(keys.hasNext()) {
				String key = keys.next();
				if(key.equals("children") && info.get(key) instanceof JSONArray) {
					JSONArray children = info.getJSONArray(key);
					this.addChildren(children);
				} else if (info.get(key) instanceof JSONObject) {
					TiProperty prop = new TiProperty(key, info.getJSONObject(key));
					this.addProperty(prop);
				} else {
					if(key.equals("id")) {
						this.setId(info.getString(key));
					} else if(key.equals("type")) {
						this.setType(info.getString(key));
					} else if(info.get(key) != null && info.getString(key) != "null") {
						this.addProperty(key, info.getString(key));
						
					}
				}
			}
		} catch(JSONException e) {
			System.out.println("Error processing properties");
		}
		
		System.out.println("Registered View");
	}
	
	/**
	 * This effectively adds children recursively. 
	 * @param children
	 */
	public void addChildren(JSONArray children) {
		try {
			for(int i = 0; i < children.length(); i++) {
				
				if(children.get(i) instanceof JSONObject) {
					TiViewModel child = new TiViewModel(children.getJSONObject(i));
					this.children.add(child);
				}
				
			}
		} catch(JSONException e) {
			
		}
	}
	
	public void addProperty(String key, String value) {
		TiProperty prop = new TiProperty();
		prop.setKey(key);
		prop.setValue(value);
		this.addProperty(prop);
		System.out.println("Set property: " + prop.getKey() + " - " + prop.getValue());
	}
	

	public ArrayList<TiProperty> getProperties() {
		return this.properties;
	}
	
	public void addProperty(TiProperty prop) {
		this.propertiesHash.put(prop.getKey(), prop);
		this.properties.add(prop);
		prop.addObserver(TilistyModel.UPDATE_PROPERTY, this);
		this.change(TilistyModel.UPDATE_PROPERTIES);
	}

	@Override
	public void update(int ns, String message) {
		JSONObject json = new JSONObject();
		try {
			json.put("type", "update_property");
			json.put("view", this.getId());
			json.put("properties", this.propertiesHash.get(message).toJSON());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.change(ns, json.toString());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public ArrayList<TiViewModel> getChildren() {
		return this.children;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
