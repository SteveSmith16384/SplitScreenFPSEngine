package com.scs.splitscreenfpsengine;


import com.scs.splitscreenfpsengine.components.ICausesHarmOnContact;
import com.scs.splitscreenfpsengine.components.IDamagable;
import com.scs.splitscreenfpsengine.components.INotifiedOfCollision;
import com.scs.splitscreenfpsengine.entities.AbstractPhysicalEntity;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

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
