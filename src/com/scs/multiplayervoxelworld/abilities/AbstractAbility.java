package com.scs.multiplayervoxelworld.abilities;

import com.scs.multiplayervoxelworld.entities.PlayersAvatar;

public abstract class AbstractAbility implements IAbility {
	
	protected PlayersAvatar player;

	public AbstractAbility(PlayersAvatar p) {
		player = p;
	}

}
