package com.scs.multiplayervoxelworld.jamepad;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

public class JamepadTest2 {

	public static int NUM_CONTROLLERS = 4;

	public static void main(String[] args) {
		new JamepadTest2();

	}


	private JamepadTest2() {
		ControllerManager controllers = new ControllerManager();
		controllers.initSDLGamepad();
		
		while(true) {
			ControllerState currState = controllers.getState(0);

			if(!currState.isConnected || currState.b) {
				break;
			}
			if(currState.a) {
				System.out.println("\"A\" on \"" + currState.controllerType + "\" is pressed");
			}
		}
	}

}
