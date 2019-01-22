package com.scs.multiplayervoxelworld.jamepad;

import com.scs.multiplayervoxelworld.Settings;
import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerUnpluggedException;

public class JamepadTest {

	public static int NUM_CONTROLLERS = 4;

	public static void main(String[] args) {
		new JamepadTest();

	}


	private JamepadTest() {
		ControllerManager controllers = new ControllerManager(NUM_CONTROLLERS);
		controllers.initSDLGamepad();

		while (true) {
			controllers.update();
			for (int i=0 ; i<NUM_CONTROLLERS ; i++) {
				ControllerIndex controllerAtIndex = controllers.getControllerIndex(i);
				if(controllerAtIndex.isConnected()) {
					try {
						updateController(controllerAtIndex);
					} catch (ControllerUnpluggedException e) {
						e.printStackTrace();
					}
				} else {
					//controllerTabs[i].setAsDisconnected();
				}
			}
		}
	}


	private void updateController(ControllerIndex c) throws ControllerUnpluggedException {
		for (ControllerAxis a : ControllerAxis.values()) {
			if (c.getAxisState(a) != 0) {
				Settings.p("Axis:" + a + "=" + c.getAxisState(a));
			}
		}
		for (ControllerButton b : ControllerButton.values()) {
			if (c.isButtonPressed(b)) {
				Settings.p("Button " + b + " pressed");
			}

		}

	}
}
