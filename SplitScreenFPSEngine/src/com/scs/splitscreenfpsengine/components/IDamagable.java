package com.scs.splitscreenfpsengine.components;

/**
 * Implemented by anything that can be damaged.
 * @author StephenCS
 *
 */
public interface IDamagable {

	int getSide(); // Entities are not harmed by other entities on the same side
	
	void damaged(float amt, String reason);
	
}
