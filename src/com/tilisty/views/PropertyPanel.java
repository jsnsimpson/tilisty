package com.tilisty.views;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import com.tilisty.models.TiProperty;
import com.tilisty.models.TiViewModel;

/**
 * The Properties Panel View as a Grid.
 * Column 0 is the property name, while
 * column 1 is the property text field for
 * the user to change.
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 * @see TiPropertyView
 */
public class PropertyPanel extends GridPane {

	private ArrayList<TiPropertyView> propertyViews;
	private Button switchButton;
	private TiViewModel currentView;
	private TextArea jsonView;
	public PropertyPanel() {
		super();
		this.propertyViews = new ArrayList<TiPropertyView>();
		this.setupLayout();
	}
	
	/**
	 * Sets up the default properties required by the GridPane
	 * 
	 */
	private void setupLayout() {
		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(25, 25, 25, 25));
		this.setAlignment(Pos.CENTER);
		
		jsonView = new TextArea();
		jsonView.setEditable(false);
		jsonView.setPrefHeight(TilistyView.APP_HEIGHT);
		jsonView.setPrefWidth(TilistyView.APP_WIDTH);	
		//jsonView.applyCss();
		//set the default title for all properties panels.
		Text scenetitle = new Text("PROPERTIES");
		scenetitle.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		this.add(scenetitle, 0, 0, 1, 1);
		
		switchButton = new Button("VIEW JSON");
		switchButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				if(currentView != null) {
					if(switchButton.getText().equals("VIEW JSON")) {
						switchButton.setText("VIEW PROPERTIES");
						displayJSON();						
						
					} else {
						switchButton.setText("VIEW JSON");
						renderPropertiesForView(currentView);
					}
				}
				
			}	
		});
		this.add(switchButton, 1, 0);
	}
	
	private void displayJSON() {
		this.removeAllProps();
		
		String json = this.currentView.toJSON().toString();
		
		json = json.replace(",", ",\n\t");
		json = json.replace("{\"", "{\n\t\"");
		json = json.replace("\"}", "\"\n}");
				
		jsonView.setText(json);
		
		this.add(jsonView, 0, 1, 2, 1);
	}

	/**
	 * Renders the properties for a particular view as part of the grid
	 * 
	 * @param tiView
	 */
	public void renderPropertiesForView(TiViewModel tiView) {
		this.currentView = tiView;
		this.switchButton.setText("VIEW JSON");
		this.removeAllProps();
		ArrayList<TiProperty> props = tiView.getProperties();
		int rowNum = 1;
		for(int i = 0; i < props.size(); i++) {
			TiProperty prop = props.get(i);
			TiPropertyView view = new TiPropertyView(prop, tiView);
			//if its a normal type just display in normal mode.
			if(prop.getPropertyType() == TiProperty.PROPERTY_TYPE_NORMAL) {
				this.add(view.getLabel(), 0, rowNum);
				this.add(view.getTextField(), 1, rowNum);
			} else {
				// if the property is of type object then we need to display the sub properties with the
				// base property as the header.
				this.add(view.getLabel(), 0, rowNum);
				
				for(int n = 0; n < prop.getObjectValues().size(); n++) {
					rowNum++;
					TiPropertyView subView = new TiPropertyView((TiProperty)prop.getObjectValues().get(n), tiView);
					this.add(subView.getLabel(), 0, rowNum);
					this.add(subView.getTextField(), 1, rowNum);
					this.propertyViews.add(subView);
				}
			}
			
			this.propertyViews.add(view);
			rowNum += 1;
		}
	}
	
	/**
	 * Removes all the properties from the Properties Panel - generally called before 
	 * rendering a new TiView.
	 */
	public void removeAllProps() {
		for(int i = 0; i < this.propertyViews.size(); i++) {
			this.getChildren().remove(this.propertyViews.get(i).getLabel());
			this.getChildren().remove(this.propertyViews.get(i).getTextField());
		}
		
		this.getChildren().remove(this.jsonView);
		this.propertyViews = new ArrayList<TiPropertyView>();
	}
	
}
