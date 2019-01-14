package com.scs.multiplayervoxelworld.components;

import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;

/**
 * Implement this if you want to run special code when entity collides.
 *
 */
public interface INotifiedOfCollision {

	void notifiedOfCollision(AbstractPhysicalEntity other);
		
}
