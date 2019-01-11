package com.scs.multiplayervoxelworld.abilities.spells;

import com.scs.multiplayervoxelworld.abilities.IAbility;
import com.scs.multiplayervoxelworld.entities.PlayersAvatar;
import com.scs.multiplayervoxelworld.modules.GameModule;

public abstract class AbstractSpell implements IAbility {

	protected GameModule module;
	protected String name;
	protected PlayersAvatar player;
	
	public AbstractSpell(GameModule _module, PlayersAvatar _player, String _name) {
		name = _name;
		module = _module;
		player = _player;
	}


	/*public boolean activate(float interpol) {
		return cast(interpol);
	}*/
	
	
	public boolean process(float interpol) {
		return false;
	}

	
	public String getHudText() {
		return name;
	}

	
}
