package com.tilisty.views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.tilisty.main.TilistyBootstrap;
import com.tilisty.data.TilistyTreeView;
import com.tilisty.models.IObserver;
import com.tilisty.models.TiViewModel;
import com.tilisty.models.TilistyModel;

/**
 * The base JavaFX application for the Tilisty app.
 * Manages the parent layout for the tree view and 
 * the properties view. 
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 */
public class TilistyView extends Application implements IObserver {
	
	public static final int APP_WIDTH = 720;
	public static final int APP_HEIGHT = 280;
	private HBox mainArea;
	private Stage stage;
	private TilistyTreeView viewList;
	private PropertyPanel propView;
	private Text statusText;
	
	public TilistyView() {
		this.setupObserving();
		mainArea = new HBox();
	}

	/**
	 * Render the view. This method gets invoked automatically by JavaFX. 
	 * It sets up the TilistyTreeView and the PropertyPanel (the two
	 * major components of the UI).
	 * It also delegates from the tree view to the property panel when a new
	 * view is selected.
	 * @see TilistyTreeView
	 * @see PropertyPanel
	 */
	public void start(Stage stage) {
		
		new TilistyBootstrap().start();
		
		this.setStage(stage);
		Scene scene = new Scene(mainArea, APP_WIDTH, APP_HEIGHT);
		stage.setTitle("Tilisty");

		this.viewList = new TilistyTreeView();
		this.propView = new PropertyPanel();

		//add a listener to the tree view. If a new item is selected we need to update the properties panel!
        this.viewList.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<TreeItem <String>>() {
                public void changed(ObservableValue<?extends TreeItem<String>> observableValue, TreeItem<String> oldItem, TreeItem<String> newItem) {
                	if(newItem instanceof ViewItem) {
                		TilistyView.this.showPropertiesForView(((ViewItem) newItem).getView());
                	}
                	if(newItem != null) {
                		System.out.println("Item Selected " + newItem.getValue());                		
                	}
                }
        });
        
		//add the main views to scrollable panes
		ScrollPane listHolder = new ScrollPane();
		listHolder.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		listHolder.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		listHolder.setMaxWidth(APP_WIDTH * 0.33);
		listHolder.setPrefWidth(APP_WIDTH * 0.33);
		listHolder.setPrefHeight(APP_HEIGHT);
		listHolder.setContent(this.viewList);
		
		ScrollPane propHolder = new ScrollPane();
		propHolder.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		propHolder.setHbarPolicy(ScrollBarPolicy.NEVER);
		propHolder.setPrefWidth(APP_WIDTH * 0.66);
		propHolder.setContent(this.propView);

		HBox.setHgrow(propHolder, Priority.ALWAYS);
		VBox.setVgrow(listHolder, Priority.ALWAYS);
		VBox.setVgrow(this.viewList, Priority.ALWAYS);
		
		this.mainArea.getChildren().add(listHolder);
		this.mainArea.getChildren().add(propHolder);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Exiting Tilisty...");
				System.exit(1);
			}
		});
		
		stage.setScene(scene);
		stage.show();
	}
	
	
	/**
	 * Setup the namespaces to observe.
	 */
	private void setupObserving() {
		TilistyModel.getInstance().addObserver(TilistyModel.REGISTER_DEVICE, this);
		TilistyModel.getInstance().addObserver(TilistyModel.UPDATE_VIEWS, this);
	}

	@Override
	public void update(int ns, String message) {
		switch(ns) {
			case TilistyModel.REGISTER_DEVICE:
				this.deviceRegistered();
				break;
			case TilistyModel.UPDATE_VIEWS:
				this.removeView(this.statusText);
				break;
		}
	}
	
	
	public void showPropertiesForView(TiViewModel tiView) {
		this.propView.renderPropertiesForView(tiView);
	}
		
	public void deviceRegistered() {
		if(this.viewList != null) {
			this.viewList.removeAllChildren();
		}
		this.statusText = new Text("Found a device, initializing properties");
		this.addView(this.statusText);
	}

	private void addView(Node shape) {
		Platform.runLater(new ViewUpdater(this.mainArea.getChildren(), shape, ViewTasks.TASK_ADD));
	}
	
	private void removeView(Node shape) {
		Platform.runLater(new ViewUpdater(this.mainArea.getChildren(), shape, ViewTasks.TASK_REMOVE));
	}
	
	@SuppressWarnings("unused")
	private void removeAll() {
		int size = this.mainArea.getChildren().size();
		for(int i = 0; i < size; i++) {
			this.removeView(this.mainArea.getChildren().get(0));
		}
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
