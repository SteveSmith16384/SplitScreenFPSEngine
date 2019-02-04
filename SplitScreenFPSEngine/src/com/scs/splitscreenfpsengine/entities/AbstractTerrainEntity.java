package com.scs.splitscreenfpsengine.entities;

import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public abstract class AbstractTerrainEntity extends AbstractPhysicalEntity {

	public AbstractTerrainEntity(SplitScreenFpsEngine _game, AbstractGameModule _module) {
		super(_game, _module, "Terrain");

	}

}
