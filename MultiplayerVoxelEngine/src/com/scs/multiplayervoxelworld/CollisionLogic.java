package com.scs.multiplayervoxelworld;


import com.scs.multiplayervoxelworld.components.ICausesHarmOnContact;
import com.scs.multiplayervoxelworld.components.IDamagable;
import com.scs.multiplayervoxelworld.components.INotifiedOfCollision;
import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;

public class CollisionLogic {

	public static void collision(AbstractGameModule game, AbstractPhysicalEntity a, AbstractPhysicalEntity b) {
		if (a.getMainNode().getParent() == null) { // Prevent collisions after changing level
			return;
		}
		if (b.getMainNode().getParent() == null) { // Prevent collisions after changing level
			return;
		}
		if (a instanceof INotifiedOfCollision) {
			INotifiedOfCollision anoc = (INotifiedOfCollision)a;
			anoc.notifiedOfCollision(b);
		}
		if (b instanceof INotifiedOfCollision) {
			INotifiedOfCollision anoc = (INotifiedOfCollision)b;
			anoc.notifiedOfCollision(a);
		}

		if (a instanceof IDamagable && b instanceof ICausesHarmOnContact) {
			harmed(game, (IDamagable)a, (ICausesHarmOnContact)b);
		}
		if (a instanceof ICausesHarmOnContact && b instanceof IDamagable) {
			harmed(game, (IDamagable)b, (ICausesHarmOnContact)a);
		}
	}
	
	
	private static void harmed(AbstractGameModule game, IDamagable damagable, ICausesHarmOnContact col) {
		if (col.getSide() != damagable.getSide()) {
			//todo
		}
	}
	
	
}
