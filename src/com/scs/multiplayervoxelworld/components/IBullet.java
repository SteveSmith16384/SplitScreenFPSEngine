package com.scs.multiplayervoxelworld.components;


public interface IBullet extends ICollideable {

	float getDamageCaused();
	
	ICanShoot getShooter();
	
	void markForRemoval();
}
