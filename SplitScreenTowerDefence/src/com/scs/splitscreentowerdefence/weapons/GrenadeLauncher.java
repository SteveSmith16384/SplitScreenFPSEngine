package com.scs.splitscreentowerdefence.weapons;

import com.scs.splitscreenfpsengine.MultiplayerVoxelWorldMain;
import com.scs.splitscreenfpsengine.abilities.IAbility;
import com.scs.splitscreenfpsengine.components.ICanShoot;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;
import com.scs.splitscreenfpsengine.weapons.AbstractGun;
import com.scs.splitscreentowerdefence.entities.Grenade;

public class GrenadeLauncher extends AbstractGun implements IAbility {

	public GrenadeLauncher(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, ICanShoot shooter) {
		super(_game, _module, "GrenadeLauncher", 1500, shooter);
	}
	

	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			new Grenade(game, module, shooter);
			return true;
		}
		return false;
	}


	@Override
	public boolean onlyActivateOnClick() {
		return true;
	}


}
