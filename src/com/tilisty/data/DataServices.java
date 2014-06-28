package com.tilisty.data;

/**
 * DataServices object just sets up the MINA socket server. 
 * to listen for incoming connections from devices. 
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 */
public class DataServices 
{

	private static DataServices _instance;
	private TilistyServer server;
	private TilistyServerHandler serverHandler;
	
	public static DataServices getInstance() 
	{
		if(_instance == null) {
			_instance = new DataServices();
		}
		return _instance;
	}
	
	private DataServices() 
	{
		this.serverHandler = new TilistyServerHandler();
	}
	
	public void sendToAll(String msg) {
		this.serverHandler.submitMessage(msg);
	}
	
	public void startServices() 
	{
		new TilistyServer(this.serverHandler).start();
	}

}
