package com.scs.splitscreenfpsengine.abilities;

import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public abstract class AbstractAbility implements IAbility {
	
	protected AbstractPlayersAvatar avatar;
	protected SplitScreenFpsEngine game;
	protected AbstractGameModule module;
	protected String name;

	public AbstractAbility(SplitScreenFpsEngine _game, AbstractGameModule _module, AbstractPlayersAvatar p, String _name) {
		super();
		
		game = _game;
		module = _module;
		avatar = p;
		name = _name;

	}
	
	
	@Override
	public String getName() {
		return name;
	}


	/**
	 * Override if required
	 */
	@Override
	public boolean onlyActivateOnClick() {
		return false;
	}

}
