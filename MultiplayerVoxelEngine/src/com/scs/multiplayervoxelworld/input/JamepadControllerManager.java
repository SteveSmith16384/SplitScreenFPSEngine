package com.scs.multiplayervoxelworld.input;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;

public class JamepadControllerManager {

	public static int NUM_CONTROLLERS = 4;

	private IControllerListener listener;
	private ControllerManager controllers;
	private boolean[] exists = new boolean[NUM_CONTROLLERS];
	private int numControllers;
	
	public JamepadControllerManager(IControllerListener _listener) {
		listener = _listener;
		
		controllers = new ControllerManager(NUM_CONTROLLERS);
		controllers.initSDLGamepad();

	}

	
	public ControllerIndex getController(int idx) {
		return controllers.getControllerIndex(idx);
	}

	
	public void process() {
		numControllers = 0;
		controllers.update();
		for (int i=0 ; i<NUM_CONTROLLERS ; i++) {
			ControllerIndex controllerAtIndex = controllers.getControllerIndex(i);
			if(controllerAtIndex.isConnected()) {
				numControllers++;
				if (!exists[i]) {
					exists[i] = true;
					// todo - store current values for each axis
					this.listener.newController(i); // todo - only send to listener once we know the total number of controllers!
				}
				/*try {
					updateController(controllerAtIndex);
				} catch (ControllerUnpluggedException e) {
					e.printStackTrace();
				}*/
			} else {
				if (exists[i]) {
					exists[i] = false;
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
		int num = 0;
		for (int i=0 ; i<NUM_CONTROLLERS ; i++) {
			ControllerIndex controllerAtIndex = controllers.getControllerIndex(i);
			if(controllerAtIndex.isConnected()) {
				num++;
			}
		}
		return num;
	}
}
