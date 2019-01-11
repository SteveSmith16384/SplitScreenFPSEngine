package com.scs.multiplayervoxelworld.components;


/**
 * Implement this if you want to run special code when entity  collides.
 *
 */
public interface ICollideable {

	void collidedWith(ICollideable other);
		
}
