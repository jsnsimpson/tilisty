package com.tilisty.views;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
		
		//set the default title for all properties panels.
		Text scenetitle = new Text("PROPERTIES");
		scenetitle.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		this.add(scenetitle, 0, 0, 2, 1);
		
	}

	/**
	 * Renders the properties for a particular view as part of the grid
	 * 
	 * @param tiView
	 */
	public void renderPropertiesForView(TiViewModel tiView) {
		
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
		
		this.propertyViews = new ArrayList<TiPropertyView>();
	}
	
}
