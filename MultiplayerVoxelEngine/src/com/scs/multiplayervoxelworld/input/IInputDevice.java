package com.scs.multiplayervoxelworld.input;

public interface IInputDevice {

	void process(float tpfSecs); // For devices that need polling
	
	float getFwdValue();

	float getBackValue();

	float getStrafeLeftValue();

	float getStrafeRightValue();

	boolean isJumpPressed();

	//boolean isShootPressed();

	boolean isAbilityPressed(int num);
	
	//boolean isSelectNextAbilityPressed();

	void resetAbilitySwitch(int num); // Turn off ability, so not on constantly
}
