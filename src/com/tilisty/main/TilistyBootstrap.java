package com.tilisty.main;

import com.tilisty.data.DataServices;

/**
 * Kick starts the Tilisty UI and DataServices
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 */
public class TilistyBootstrap 
{
	
	private TiViewWatcher watcher;
	
	public TilistyBootstrap() 
	{
		
	}
	
	public void start() 
	{
		DataServices.getInstance().startServices();
		String[] s = {};
		this.setWatcher(new TiViewWatcher());
		
	}

	public TiViewWatcher getWatcher() {
		return watcher;
	}

	public void setWatcher(TiViewWatcher watcher) {
		this.watcher = watcher;
	}
	
}
