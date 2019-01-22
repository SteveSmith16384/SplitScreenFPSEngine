package com.scs.multiplayervoxelworld.jamepad;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;

public class JamepadTest {

	public static int NUM_CONTROLLERS = 4;

	public static void main(String[] args) {
		new JamepadTest();

	}


	public JamepadTest() {
		ControllerManager controllers = new ControllerManager(NUM_CONTROLLERS);
		controllers.initSDLGamepad();

		while (true) {
			controllers.update();
			ControllerIndex controllerAtIndex = controllers.getControllerIndex(i);
			if(controllerAtIndex.isConnected()) {
				updateController(controllerAtIndex);
			} else {
				//controllerTabs[i].setAsDisconnected();
			}
		}
	}
	
	
	private void updateController(ControllerIndex c) {
        for (ControllerAxis a : ControllerAxis.values()) {
            progressBar.setValue((int) (c.getAxisState(a) * 100));
        }
        for (ControllerButton b : ControllerButton.values()) {
            button.setEnabled(c.isButtonPressed(b));
        }

	}
}
