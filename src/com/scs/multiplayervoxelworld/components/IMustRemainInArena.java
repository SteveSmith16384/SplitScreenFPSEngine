package com.scs.multiplayervoxelworld.components;

import com.jme3.math.Vector3f;

public interface IMustRemainInArena { // todo - remove

	Vector3f getLocation();
	
	void markForRemoval();
	
	void respawn();
}
