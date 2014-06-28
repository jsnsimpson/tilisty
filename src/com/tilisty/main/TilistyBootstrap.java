package com.tilisty.main;

import javafx.application.Application;

import com.tilisty.data.DataServices;
import com.tilisty.views.TilistyView;

/**
 * Kick starts the Tilisty UI and DataServices
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 */
public class TilistyBootstrap 
{
	
	private TilistyView application;
	private TiViewWatcher watcher;
	
	public TilistyBootstrap() 
	{
		
	}
	
	public void start() 
	{
		DataServices.getInstance().startServices();
		this.application = new TilistyView();
		String[] s = {};
		watcher = new TiViewWatcher();
		
		Application.launch(TilistyView.class, s);
	}
	
}
