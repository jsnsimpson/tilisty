package com.tilisty.main;

import java.util.ArrayList;

import com.tilisty.data.DataServices;
import com.tilisty.models.IObserver;
import com.tilisty.models.TiViewModel;
import com.tilisty.models.TilistyModel;

/**
 * This class simply observes all of the TiProperty instances
 * that are managed by each of the views. 
 * If a property changes, we send the message to the device
 * from here.
 * 
 * @author Jason Simpson <jsnsnimpson@gmail.com>
 * @version 1.0
 */
public class TiViewWatcher implements IObserver {

	
	public TiViewWatcher() {
		// TODO Auto-generated constructor stub
		this.setupObserving();
	}
	
	private void setupObserving() {
		TilistyModel.getInstance().addObserver(TilistyModel.UPDATE_VIEWS, this);
	}
	
	@Override
	public void update(int ns, String message) {
		switch(ns) {
			case TilistyModel.UPDATE_VIEWS:
				this.updateViews(TilistyModel.getInstance().getViews());
				break;
			case TilistyModel.UPDATE_PROPERTY:
				DataServices.getInstance().sendToAll(message);
				break;
		}
		
	}
	
	/**
	 * Add observer to all properties of all views.
	 * @param views
	 */
	public void updateViews(ArrayList<TiViewModel> views) {
		
		for(int i = 0; i < views.size(); i++) {
			views.get(i).addObserver(TilistyModel.UPDATE_PROPERTY, this);
			if(views.get(i).getChildren().size() > 0) {
				this.recursiveChildren(views.get(i));
			}
		}
	}
	
	private void recursiveChildren(TiViewModel tiView) {
		ArrayList<TiViewModel> children = tiView.getChildren();
		for(int i = 0; i < children.size(); i++) {
			children.get(i).addObserver(TilistyModel.UPDATE_PROPERTY, this);
			if(children.get(i).getChildren().size() > 0) {
				this.recursiveChildren(children.get(i));
			}
		}
	}
}
