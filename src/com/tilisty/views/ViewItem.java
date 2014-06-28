package com.tilisty.views;

import com.tilisty.models.TiViewModel;

import javafx.scene.control.TreeItem;

/**
 * Allows us to have a TiViewModel associated with a treeitem so
 * the click listener can pick up which TiView is associated with it.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 */
public class ViewItem extends TreeItem<String> {

	private TiViewModel ti;
	
	public ViewItem(TiViewModel tiView) {
		super(tiView.getType() != null ? tiView.getType() : tiView.getId());
		this.setTiViewModel(tiView);
	}
	
	public ViewItem() {
		super();
	}
	
	public void setTiViewModel(TiViewModel view) {
		this.ti = view;
	}
	
	public TiViewModel getView() {
		return this.ti;
	}
	
	
}
