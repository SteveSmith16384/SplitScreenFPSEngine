package com.scs.splitscreenfpsengine.components;

import com.scs.splitscreenfpsengine.entities.AbstractPhysicalEntity;

/**
 * Implement this if you want to run special code when entity collides.
 *
 */
public interface INotifiedOfCollision {

	void notifiedOfCollision(AbstractPhysicalEntity other);
		
}
