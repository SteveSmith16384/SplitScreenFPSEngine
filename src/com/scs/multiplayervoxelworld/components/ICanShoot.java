package com.scs.multiplayervoxelworld.components;

import com.jme3.math.Vector3f;

/** 
 * Implemented by anything that can shoot
 * @author StephenCS
 *
 */
public interface ICanShoot {

	int getSide();
	
	Vector3f getLocation();

	Vector3f getShootDir();
	
	void hasSuccessfullyHit(IEntity e);
	
}
