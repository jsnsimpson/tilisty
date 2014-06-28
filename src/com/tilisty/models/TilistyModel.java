package com.tilisty.models;

import java.util.ArrayList;

/**
 * Holds all of the models and delegates to each of them.
 * This also holds each of the constants for the frequently
 * used namespaces throughout the application.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 *
 */
public class TilistyModel extends AbstractModel {

	public static final int REGISTER_DEVICE = 1;
	public static final int UPDATE_PROPERTIES = 2;
	public static final int UPDATE_VIEWS = 3;
	public static final int UPDATE_PROPERTY = 4;
	
	private static TilistyModel _instance;
	
	private Device device;
	private ArrayList<TiViewModel> views;
	
	
	public static TilistyModel getInstance() 
	{
		if(_instance == null) {
			_instance = new TilistyModel();
		}
		return _instance;
	}
	
	private TilistyModel() 
	{
		this.views = new ArrayList<TiViewModel>();
	}
	
	public void registerDevice(Device device) 
	{
		this.device = device;
		this.change(REGISTER_DEVICE);
	}
	
	public void addView(TiViewModel view) 
	{
		this.views.add(view);
		this.change(UPDATE_VIEWS);
	}
	
	public ArrayList<TiViewModel> getViews() 
	{
		return this.views;
	}
	
	public void removeAllViews() 
	{
		this.views = new ArrayList<TiViewModel>();
	}

	public void removeView(TiViewModel view) 
	{
		for(int i = 0; i < this.views.size(); i++) {
			if(view == this.views.get(i)) {
				this.views.remove(i);
			}
		}
	}
}
