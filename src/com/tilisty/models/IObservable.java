package com.tilisty.models;

public interface IObservable {

	public void addObserver(int ns, IObserver observer);
	public void removeObserver(int ns, IObserver observer);
	public void destroy();
	
}
