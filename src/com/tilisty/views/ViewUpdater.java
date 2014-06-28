package com.tilisty.views;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class ViewUpdater implements Runnable 
{
	
	private ObservableList<Node> nodes;
	private Node node;
	private ViewTasks taskType;
	private ArrayList<Node> shapes;
	
	public ViewUpdater(ObservableList<Node> nodes, Node shape, ViewTasks taskType) {
		this.nodes = nodes;
		this.node = shape;
		this.taskType = taskType;
	}
	
	public ViewUpdater(ObservableList<Node> nodes, ArrayList<Node> shapes) {
		this.taskType = ViewTasks.TASK_ADD_ALL;
		this.nodes = nodes;
		this.shapes = shapes;
	}
	
	public void run() {
		switch(this.taskType) {
			case TASK_ADD:
				this.add();
				break;
			case TASK_REMOVE:
				this.remove();
				break;
			case TASK_ADD_ALL:
				this.addAll();
				break;
		}
	}
	
	public void addAll() {
		this.nodes.addAll(this.shapes);
	}
	
	public void add() {
		this.nodes.add(this.node);
	}
	
	public void remove() {
		this.nodes.remove(this.node);
	}
}