package com.scs.splitscreenfpsengine.modules;

public interface IModule {

	void init();
	
	void update(float tpf);
	
	void destroy();
	
	void newController(int idx);
	
	void controllerDisconnected(int id);
}
