package com.scs.splitscreenfpsengine.systems;

import com.scs.splitscreenfpsengine.components.IExpiringEffect;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class ExpiringEffectSystem extends AbstractSystem<IExpiringEffect> implements ISystem<IExpiringEffect> {

	public ExpiringEffectSystem(AbstractGameModule module) {
		super(module);
	}

	@Override
	protected void processItem(IExpiringEffect o, float tpfSecs) {
		o.setTimeRemaining(o.getTimeRemaining() - tpfSecs);
		if (o.getTimeRemaining() <= 0) {
			o.effectFinished();
		}
		
	}


}
