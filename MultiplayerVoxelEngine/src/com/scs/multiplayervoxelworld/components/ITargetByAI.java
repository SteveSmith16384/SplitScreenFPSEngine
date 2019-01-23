package com.scs.multiplayervoxelworld.components;

import com.jme3.math.Vector3f;

public interface ITargetByAI {

	int getSide();
	
	boolean isAlive();
	
	Vector3f getLocation();

}
