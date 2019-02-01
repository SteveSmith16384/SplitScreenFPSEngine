package com.scs.splitscreenfpsengine.systems;

public interface ISystem<T> {

	boolean add(T o);
	
	void process(float tpfSecs);
	
}
