package com.scs.multiplayervoxelworld.abilities;

import com.scs.multiplayervoxelworld.entities.PlayersAvatar;
import com.scs.multiplayervoxelworld.modules.GameModule;

public abstract class AbstractAbility implements IAbility {
	
	protected PlayersAvatar player;
	protected GameModule module;

	public AbstractAbility(GameModule _module, PlayersAvatar p) {
		super();
		
		module = _module;
		player = p;
	}

}
