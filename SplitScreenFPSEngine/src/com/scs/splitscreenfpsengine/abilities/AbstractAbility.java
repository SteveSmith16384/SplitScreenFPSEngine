package com.scs.splitscreenfpsengine.abilities;

import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public abstract class AbstractAbility implements IAbility {
	
	protected AbstractPlayersAvatar avatar;
	protected SplitScreenFpsEngine game;
	protected AbstractGameModule module;

	public AbstractAbility(SplitScreenFpsEngine _game, AbstractGameModule _module, AbstractPlayersAvatar p) {
		super();
		
		game = _game;
		module = _module;
		avatar = p;
	}
	
	
	/**
	 * Override if required
	 */
	@Override
	public boolean onlyActivateOnClick() {
		return false;
	}

}
