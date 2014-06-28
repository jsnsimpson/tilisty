package com.tilisty.models;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A Representation of a property of a Titanium View.
 * Can take one of two forms, PROPERTY_TYPE_NORMAL or 
 * PROPERTY_TYPE_OBJECT. The latter of which creates sub 
 * properties of this property.
 * 
 * If normal it will just have a key and a value which map
 * directly to the titanium names for these properties. 
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 *
 */
public class TiProperty extends AbstractModel implements IObserver {
	
	public static final int PROPERTY_TYPE_NORMAL = 0;
	public static final int PROPERTY_TYPE_OBJECT = 1;
	
	private int propertyType;
	private String key;
	private String value;
	private ArrayList<TiProperty> objectValues;
	
	public TiProperty() 
	{
		this.setPropertyType(PROPERTY_TYPE_NORMAL);
	}
	
	public TiProperty(String key, JSONObject obj) 
	{
		this.setPropertyType(PROPERTY_TYPE_OBJECT);
		this.setKey(key);
		this.setObjectValues(obj);
	}
	
	/**
	 * Add any sub properties if this property is of type PROPERTY_TYPE_OBJECT
	 * 
	 * @param JSONObject props
	 * @return ArrayList props
	 */
	private ArrayList processProperties(JSONObject props) 
	{
		ArrayList<TiProperty> nestedProps = new ArrayList<TiProperty>();
		try {
			Iterator<String> keys = props.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				if(props.get(key) instanceof JSONObject) {
					TiProperty prop = new TiProperty(key, props.getJSONObject(key));
					nestedProps.add(prop);
				} else {
					TiProperty prop = new TiProperty();
					prop.setKey(key);
					prop.setValue(props.getString(key));
					nestedProps.add(prop);
				}
			}
		}catch(JSONException e) {
			
		}
		return nestedProps;
	}

	public String getValue() {
		return value;
	}

	/**
	 * Set the value, if the value has changed at all call 
	 * the change method to notify all IObserver instances
	 * watching this object. 
	 * 
	 * @param String val
	 */
	public void setValue(String val) {
		boolean update = false;
		if(val != this.value) {
			update = true;
		}
		this.value = val;
		
		if(update) {
			this.change(TilistyModel.UPDATE_PROPERTY);
		}
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Sets the object values if this is PROPERTY_TYPE_OBJECT.
	 * Also adds listeners to all of these properties by invoking
	 * the addListeners() method.
	 * 
	 * @param JSONObject obj
	 */
	public void setObjectValues(JSONObject obj) {
		this.objectValues = this.processProperties(obj);
		this.addListeners();
	}
	
	/**
	 * Listen for changes on any subProperties.
	 */
	protected void addListeners() {
		for(int i = 0; i < this.objectValues.size(); i++) {
			this.objectValues.get(i).addObserver(TilistyModel.UPDATE_PROPERTY, this);
		}
	}
	
	public ArrayList<TiProperty> getObjectValues() {
		return this.objectValues;
	}

	public int getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(int propertyType) {
		this.propertyType = propertyType;
	}
	
	public String toString() {
		return this.getKey();
	}
	
	/**
	 * Convert this property to a JSONObject, if there are subproperties
	 * it will also list each of them.
	 * 
	 * @return JSONObject json - This property as a JSONObject
	 */
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			if(this.getPropertyType() == TiProperty.PROPERTY_TYPE_OBJECT) {
				//if type object, then add the sub properties.
				JSONObject subProps = new JSONObject();
				for(int i = 0; i < this.getObjectValues().size(); i++) {
					TiProperty prop = this.getObjectValues().get(i);
					subProps.put(prop.getKey(), prop.getValue());
				}
				json.put(this.getKey(), subProps);
			} else {
				json.put(this.getKey(), this.getValue());
			}
		} catch(JSONException e) {
			
		}
		return json;
	}

	@Override
	public void update(int ns, String message) {
		// TODO Auto-generated method stub
		this.change(ns);
	}
	
}
