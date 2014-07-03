package com.tilisty.views;

import org.apache.commons.lang.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.tilisty.models.TiProperty;
import com.tilisty.models.TiViewModel;

/**
 * The Property views, Maps the key and value 
 * of a TiProperty to JavaFX views. 
 * 
 * The property key maps to a Label
 * The value maps to an TextField to allow for editing.
 * 
 * If the value changes, it updates the TiProperty instance
 * which should trigger the change event and cause a message
 * to be sent to the devices.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 */
public class TiPropertyView {
	
	private TiViewModel view;
	private TiProperty prop;
	private Label propKey;
	private TextField field;
	
	public TiPropertyView(TiProperty prop, TiViewModel view) {
		this.prop = prop;
		this.setupPropertyView();
	}
	/**
	 * Sets up the property views and the listeners on the property.
	 * In this listener, we update the TiProperty instance.
	 */
	private void setupPropertyView() {
		this.propKey = new Label(this.prop.getKey());
		//set the font, if it is an object type (has sub properties) then make it bold like a header.
		if(this.prop.getPropertyType() == TiProperty.PROPERTY_TYPE_OBJECT) {
			this.propKey.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		} else {
			this.propKey.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
		}
		
		this.field = new TextField(this.prop.getValue());
		field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
				
				TiPropertyView.this.prop.setValue(newValue);
			}
		});
		
		field.setOnKeyPressed( new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				TiProperty prop = TiPropertyView.this.prop;
				
				if(prop.getPropertyType() == TiProperty.PROPERTY_TYPE_NORMAL) {
					if(event.getCode() == KeyCode.UP) {
						try {
							int newVal = Integer.parseInt(prop.getValue())+1;
							prop.setValue(String.valueOf(newVal));
							field.setText(prop.getValue());								
						} catch(NumberFormatException e) {
//							System.out.println("Can't use arrows on non-numeric fields");
						}
					} else if(event.getCode() == KeyCode.DOWN) {
						try {
							int newVal = Integer.parseInt(prop.getValue())-1;
							prop.setValue(String.valueOf(newVal));
							field.setText(prop.getValue());							
						} catch(NumberFormatException e) {
//							System.out.println("Can't use arrows on non-numeric fields");
						} 
					}
					
					
					
				}
			}
		});
	}
	
	public Label getLabel() {
		return this.propKey;
	}
	
	public TextField getTextField() {
		return this.field;
	}

}
