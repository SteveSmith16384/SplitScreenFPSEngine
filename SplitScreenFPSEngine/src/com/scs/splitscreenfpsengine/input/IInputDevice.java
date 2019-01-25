package com.scs.splitscreenfpsengine.input;

public interface IInputDevice {

	void process(float tpfSecs); // For devices that need polling
	
	float getFwdValue();

	float getBackValue();

	float getStrafeLeftValue();

	float getStrafeRightValue();

	boolean isJumpPressed();

	boolean isAbilityPressed(int num);
	
	boolean isCycleAbilityPressed(boolean fwd);

	void resetAbilitySwitch(int num); // Turn off ability, so not on constantly
}
