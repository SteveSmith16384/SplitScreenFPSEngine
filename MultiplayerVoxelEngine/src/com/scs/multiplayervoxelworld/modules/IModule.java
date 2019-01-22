package com.scs.multiplayervoxelworld.modules;

public interface IModule {

	void init();
	
	void update(float tpf);
	
	void destroy();
	
	void newController();
	
	void controllerDisconnected();
}
