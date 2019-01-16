package com.scs.multiplayervoxelworld.abilities;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.entities.PlayersAvatar;
import com.scs.multiplayervoxelworld.modules.GameModule;

public abstract class AbstractAbility implements IAbility {
	
	protected PlayersAvatar player;
	protected MultiplayerVoxelWorldMain game;
	protected GameModule module;

	public AbstractAbility(MultiplayerVoxelWorldMain _game, GameModule _module, PlayersAvatar p) {
		super();
		
		game = _game;
		module = _module;
		player = p;
	}
	
	
	/**
	 * Override if required
	 */
	@Override
	public boolean onlyActivateOnClick() {
		return false;
	}

}
