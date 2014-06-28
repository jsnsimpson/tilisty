package com.tilisty.data;

import java.util.ArrayList;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

public class TilistyServerHandler extends IoHandlerAdapter {

	private DataModelDelegate delegate;
	private ArrayList<IoSession> sessions;
	
	public TilistyServerHandler() {
		this.delegate = new DataModelDelegate();
		this.sessions = new ArrayList<IoSession>();
	}
	
	public void sessionOpened(IoSession session) {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        this.sessions.add(session);
        System.out.println("New connection established...");
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		 try {
			 JSONObject json = new JSONObject(message.toString());
			 
			 System.out.println("Received Message " + message.toString());
			 if(json.has("type")) {
				 JSONObject response = this.delegate.processPacket(json);
				 session.write(response.toString());
				 System.out.println("Response sent");
			 }
			 
		 } catch(Exception e) {
			e.printStackTrace(); 
		 }
	 }
	 
	public void submitMessage(Object msg) {
		 for(int i = 0; i < this.sessions.size(); i++) {
			 this.sessions.get(i).write(msg.toString());
		 }
	}
	
	@Override
	
	public void sessionClosed(IoSession session) {
		for(int i = 0; i < this.sessions.size(); i++) {
			if(this.sessions.get(i) == session) {
				this.sessions.remove(session);
			}
		}
	}

	 
    public void sessionIdle(IoSession session, IdleStatus status) {
    	session.write("PROBE");
    }
}
