package com.tilisty.models;

/**
 * Classes which wish to listen to updates from a particular object
 * should implement the IObserver interface.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 */
public interface IObserver {

	public void update(int ns, String message);
	
}
