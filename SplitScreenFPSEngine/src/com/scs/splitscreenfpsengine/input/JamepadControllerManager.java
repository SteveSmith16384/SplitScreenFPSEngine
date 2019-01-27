package com.scs.splitscreenfpsengine.input;

import java.util.HashMap;

import com.scs.splitscreenfpsengine.jamepad.JamepadFullAxisState;
import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;

public class JamepadControllerManager {

	public static int MAX_CONTROLLERS = 4;

	private IControllerListener listener;
	private ControllerManager controllerManager;
	
	private HashMap<Integer, ControllerIndex> controllers = new HashMap<Integer, ControllerIndex>();
	private HashMap<Integer, JamepadFullAxisState> controllerStates = new HashMap<Integer, JamepadFullAxisState>();

	
	public JamepadControllerManager(IControllerListener _listener) {
		listener = _listener;
		
		controllerManager = new ControllerManager(MAX_CONTROLLERS);
		controllerManager.initSDLGamepad();

	}

	
	public ControllerIndex getController(int idx) {
		return controllers.get(idx);
	}

	
	public JamepadFullAxisState getInitialStates(int idx) {
		return controllerStates.get(idx);
	}

	
	public void checkControllers() {
		controllerManager.update();
		for (int i=0 ; i<MAX_CONTROLLERS ; i++) {
			ControllerIndex controllerAtIndex = controllerManager.getControllerIndex(i);
			if(controllerAtIndex.isConnected()) {
				if (!controllers.containsKey(i)) {
					controllers.put(i, controllerAtIndex);
					JamepadFullAxisState states = new JamepadFullAxisState(controllerAtIndex);
					controllerStates.put(i, states);
					this.listener.newController(i); // todo - only send to listener once we know the total number of controllers!
				}
			} else {
				if (controllers.containsKey(i)) {
					controllers.remove(i);
					controllerStates.remove(i);
					this.listener.controllerDisconnected(i); // todo - only send to listener once we know the total number of controllers!
				}
			}
		}
		
	}
	
/*
	private void updateController(ControllerIndex c) throws ControllerUnpluggedException {
		for (ControllerAxis a : ControllerAxis.values()) {
			Settings.p("Axis:" + a + "=" + c.getAxisState(a));
			//progressBar.setValue((int) (c.getAxisState(a) * 100));
		}
		for (ControllerButton b : ControllerButton.values()) {
			//button.setEnabled(c.isButtonPressed(b));
			if (c.isButtonPressed(b)) {
				Settings.p("Button " + b + " pressed");
			}

		}

	}
	*/
	
	public int getNumControllers() {
		this.checkControllers();
		return this.controllers.size();
	}
}
