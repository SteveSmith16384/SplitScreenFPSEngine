package com.scs.splitscreentowerdefence.abilities;

import java.util.ArrayList;

import com.scs.splitscreenfpsengine.MultiplayerVoxelWorldMain;
import com.scs.splitscreenfpsengine.abilities.AbstractAbility;
import com.scs.splitscreenfpsengine.abilities.IAbility;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class CycleThroughAbilitiesAbility extends AbstractAbility {
	
	private ArrayList<IAbility> abilities = new ArrayList<>();
	private int index;

	public CycleThroughAbilitiesAbility(MultiplayerVoxelWorldMain _game, AbstractGameModule module, AbstractPlayersAvatar p) {
		super(_game, module, p);
		
		abilities.add(new AddBlockAbility(game, module, p));
		abilities.add(new RemoveBlockAbility(game, module, p));
		abilities.add(new PlaceTurretAbility(game, module, p));
	}


	@Override
	public boolean process(float interpol) {
		return false;
	}


	@Override
	public boolean activate(float interpol) {
		index++;
		if (index >= this.abilities.size()) {
			index = 0;
		}
		this.player.ability[0] = this.abilities.get(index);
		return true;
	}
	

	@Override
	public boolean onlyActivateOnClick() {
		return true;
	}

	
	@Override
	public String getHudText() {
		return "[CycleThru]";
	}

}
