package com.scs.multiplayervoxelworld.weapons;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.abilities.IAbility;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;

import ssmith.util.RealtimeInterval;

public abstract class AbstractGun implements IAbility {

	protected MultiplayerVoxelWorldMain game;
	protected AbstractGameModule module;
	protected ICanShoot shooter;
	protected String name;
	protected RealtimeInterval shotInterval;

	public AbstractGun(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, String _name, long shotIntervalMS, ICanShoot _shooter) {
		game = _game;
		module = _module;
		name = _name;
		shooter = _shooter;
		shotInterval = new RealtimeInterval(shotIntervalMS);
		
	}


	@Override
	public boolean process(float interpol) {
		// Do nothing
		return false;
	}


	@Override
	public String getHudText() {
		return name;
	}

}
