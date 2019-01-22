package com.scs.multiplayervoxelworld.input;

public interface IControllerListener {

	void newController(int idx);

	void controllerDisconnected(int idx);
}
