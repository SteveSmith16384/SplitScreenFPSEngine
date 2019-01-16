package com.scs.multiplayervoxelworld.weapons;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.abilities.IAbility;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.entities.KillerCrateBullet;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class KillerCrateGun extends AbstractGun implements IAbility {

	public KillerCrateGun(MultiplayerVoxelWorldMain _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "KrateGun", 1000, shooter);
	}
	

	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			new KillerCrateBullet(game, module, shooter);
			return true;
		}
		return false;
	}


	@Override
	public boolean onlyActivateOnClick() {
		return true;
	}


}
