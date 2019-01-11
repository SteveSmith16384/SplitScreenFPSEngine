package com.scs.multiplayervoxelworld.weapons;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.abilities.IAbility;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.entities.Rocket;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class RocketLauncher extends AbstractGun implements IAbility {

	public RocketLauncher(MultiplayerVoxelWorldMain _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "Rocket Launcher", 1200, shooter);
	}
	

	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			Rocket b = new Rocket(game, module, shooter);
			module.addEntity(b);
			return true;
		}
		return false;
	}


}
