package com.scs.multiplayervoxelworld.components;

import com.jme3.math.Vector3f;

public interface IMustRemainInArena {

	Vector3f getLocation();
	
	void markForRemoval();
	
	void respawn();
}
