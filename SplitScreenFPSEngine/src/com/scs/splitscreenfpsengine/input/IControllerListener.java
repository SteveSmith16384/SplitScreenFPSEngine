package com.scs.splitscreenfpsengine.input;

public interface IControllerListener {

	void newController(int idx);

	void controllerDisconnected(int idx);
}
