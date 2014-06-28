package com.tilisty.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This provides the basic observer type infrastructure for this 
 * application. The change function should be called with a namespace
 * in order to trigger updates to all IObserver instances that are listening
 * to a particular namespace.
 *  
 * @author Jason Simpson <jsnsimpson@gmail>
 * @version 1.0
 */
public abstract class AbstractModel implements IObservable {

	private HashMap<Integer, ArrayList<IObserver>> observers;
	
	/**
	 * What namespaces are being managed by this AbstractModel
	 * Pass them in as an array of integers.
	 * @param Integer[] namespaces
	 */
	public AbstractModel(Integer[] namespaces) {
		// TODO Auto-generated constructor stub
		this.observers = new HashMap<Integer, ArrayList<IObserver>>(namespaces.length);
		for(Integer s : namespaces) {
			observers.put(s, new ArrayList<IObserver>());
		}
	}
	
	public AbstractModel() {
		this.observers = new HashMap<Integer, ArrayList<IObserver>>();
	}
	
	/**
	 * Adds an IObserver instance to the list of observers.
	 * A namespace must be passed indicating what the observer
	 * wants to listen to updates of.
	 * @param int ns
	 * @param IObserver observer
	 */
	public void addObserver(int ns, IObserver observer) {
		if(!this.observers.containsKey(ns)) {
			this.observers.put(ns, new ArrayList<IObserver>());
		}
		this.observers.get(ns).add(observer);
	}
	
	public void removeObserver(int ns, IObserver observer) {
		this.observers.get(ns).remove(observer);
	}

	public void destroy() {
		this.observers = new HashMap<Integer, ArrayList<IObserver>>(0);
	}
	
	/**
	 * Change of an observable value, same as the change event only
	 * it calls the tostring method to pass as the message.
	 * 
	 * @param ns
	 */
	public void change(int ns) {
		this.change(ns, this.toString());
	}
	
	/**
	 * This method goes through all of the IObserver instances which are
	 * listnening to this namespace and calls the update method on it
	 * passing it a message which is passed in to this function.
	 * 
	 * @param int ns
	 * @param String msg
	 */
	public void change(int ns, String msg) {
		if(this.observers.containsKey(ns)) {
			for(int i = 0; i < this.observers.get(ns).size(); i++) {
				this.observers.get(ns).get(i).update(ns, msg);
			}
		}
	}
}
