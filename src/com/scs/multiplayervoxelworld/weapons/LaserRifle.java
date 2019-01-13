package com.scs.multiplayervoxelworld.weapons;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.abilities.IAbility;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.entities.LaserBullet;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class LaserRifle extends AbstractMagazineGun implements IAbility {

	public LaserRifle(MultiplayerVoxelWorldMain _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "Laser Rifle", shooter, .2f, 2, 10);
	}

	
	@Override
	public void launchBullet(MultiplayerVoxelWorldMain game, GameModule module, ICanShoot _shooter) {
		new LaserBullet(game, module, shooter);
		
	}
	
}
