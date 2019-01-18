package com.scs.samescreentowerdefence.weapons;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.abilities.IAbility;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.modules.GameModule;
import com.scs.multiplayervoxelworld.weapons.AbstractGun;
import com.scs.samescreentowerdefence.entities.Grenade;

public class GrenadeLauncher extends AbstractGun implements IAbility {

	public GrenadeLauncher(MultiplayerVoxelWorldMain _game, GameModule _module, ICanShoot shooter) {
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