package com.scs.splitscreenfpsengine.systems;

import java.util.ArrayList;

import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public abstract class AbstractSystem<T> extends ArrayList<T> implements ISystem<T> {

	protected AbstractGameModule module;
	
	public AbstractSystem(AbstractGameModule _module) {
		module = _module;
	}
	
	
	@Override
	public void process(float tpfSecs) {
		for(T o : this) {
			this.processItem(o, tpfSecs);
		}
	}
	
	
	protected abstract void processItem(T o, float tpfSecs);



}
