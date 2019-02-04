package com.scs.splitscreenfpsengine.components;

/**
 * Implemented by anything that causes harm on contact.
 * @author StephenCS
 *
 */
public interface ICausesHarmOnContact {

	int getSide();
	
	float getDamageCaused();
	
	ICanShoot getShooter();
	
	String getName();
	
}
