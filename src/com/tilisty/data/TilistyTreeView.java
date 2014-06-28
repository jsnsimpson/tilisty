package com.tilisty.data;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import com.tilisty.models.IObserver;
import com.tilisty.models.TiViewModel;
import com.tilisty.models.TilistyModel;
import com.tilisty.views.TilistyView;
import com.tilisty.views.ViewItem;


/**
 * The Base class for the Tree View showing the views which
 * are available for editing.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 *
 */
public class TilistyTreeView extends TreeView<String> implements IObserver {

	private TreeItem<String> treeRoot;
	private ArrayList<TiViewModel> currentViews;
	
	public TilistyTreeView() {
		super();
		this.treeRoot = new TreeItem<String>("VIEWS");
		this.currentViews = new ArrayList<TiViewModel>();
		this.setupTree();
		this.setupObserving();
	}
	
	/**
	 * Setup the observing
	 * @see TilistyTreeView.update
	 */
	private void setupObserving() {
		TilistyModel.getInstance().addObserver(TilistyModel.UPDATE_VIEWS, this);
	}
	
	/**
	 * Sets up some basic treeview properties 
	 */
	public void setupTree() {
		this.setPrefWidth(TilistyView.APP_WIDTH*0.32);
		this.setPrefHeight(TilistyView.APP_HEIGHT-3);
		this.setMinHeight(TilistyView.APP_HEIGHT-3);
		this.setRoot(this.treeRoot);
	}
	
	/**
	 * Update the tree view with the new lsit of TiViewModels 
	 * as the list has been updated.
	 * 
	 * @param views
	 */
	public void updateViews(ArrayList<TiViewModel> views) {
		
		ArrayList<ViewItem> parents = new ArrayList<ViewItem>();
		for(int i = 0; i < views.size(); i++) {
			ViewItem tree = new ViewItem(views.get(i));
			if(views.get(i).getChildren().size() > 0) {
				ArrayList<ViewItem> trees = this.recursiveChildren(views.get(i));
				for(int p = 0; p < trees.size(); p++) {
					tree.getChildren().add(trees.get(p));
				}
			}
			parents.add(tree);
		}
		this.treeRoot.getChildren().addAll(parents);
	}
	
	/**
	 * If the TiViewModel has associated children
	 * we need to add them recursively to the tree view (until there are no more
	 * children of the children!).
	 * 
	 * @param tiView
	 * @return ArrayList\<ViewItem\>
	 */
	private ArrayList<ViewItem> recursiveChildren(TiViewModel tiView) {
		ArrayList<ViewItem> trees = new ArrayList<ViewItem>();
		ArrayList<TiViewModel> children = tiView.getChildren();
		for(int i = 0; i < children.size(); i++) {
			ViewItem item = new ViewItem(children.get(i));
			
			if(children.get(i).getChildren().size() > 0) {
				ArrayList<ViewItem> trs = this.recursiveChildren(children.get(i));
				for(int p = 0; p < trs.size(); p++) {
					item.getChildren().add(trs.get(p));
				}
			}
			trees.add(item);
		}
		return trees;
	}

	/**
	 * This is the listener to the change of TiViewModel list from the 
	 * TilistyModel. We are only listening to one namespace here
	 * so there is no need to test what type of update it is.
	 * 
	 * @param int ns - the namespace of the update
	 */
	@Override
	public void update(int ns, String message) {
		ArrayList<TiViewModel> views = TilistyModel.getInstance().getViews();
		ArrayList<TiViewModel> newViews = new ArrayList<TiViewModel>();
		
		for(int i = 0; i < views.size(); i++) {
			int index = this.currentViews.indexOf(views.get(i));
			if(index == -1) {
				this.currentViews.add(views.get(i));
				newViews.add(views.get(i));
			} 
		}
		
		this.updateViews(newViews);	
	}
	
	public void removeAllChildren() {
		ObservableList<TreeItem<String>> children = this.treeRoot.getChildren();
		this.treeRoot.getChildren().removeAll(children);
	}
	
}
