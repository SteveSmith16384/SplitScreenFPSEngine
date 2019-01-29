package com.scs.splitscreenfpsengine.components;

import com.jme3.math.Vector3f;

/** 
 * Implemented by anything that can shoot
 * @author StephenCS
 *
 */
public interface ICanShoot {

	int getSide();
	
	Vector3f getBulletStartPosition();

	Vector3f getShootDir();
	
	void hasSuccessfullyHit(IEntity e);
	
	float getRadius(); // So the bullets don't start on top of us
	
}
