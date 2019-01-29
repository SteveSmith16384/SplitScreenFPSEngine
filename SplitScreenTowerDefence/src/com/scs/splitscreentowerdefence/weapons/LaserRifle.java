package com.scs.splitscreentowerdefence.weapons;

import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.abilities.IAbility;
import com.scs.splitscreenfpsengine.components.ICanShoot;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;
import com.scs.splitscreenfpsengine.weapons.AbstractMagazineGun;
import com.scs.splitscreentowerdefence.entities.LaserBullet;

public class LaserRifle extends AbstractMagazineGun implements IAbility {

	public LaserRifle(SplitScreenFpsEngine _game, AbstractGameModule _module, ICanShoot shooter) {
		super(_game, _module, "Laser Rifle", shooter, .2f, 2, 10);
	}

	
	@Override
	public void launchBullet(SplitScreenFpsEngine game, AbstractGameModule module, ICanShoot _shooter) {
		new LaserBullet(game, module, shooter);
		
	}
	

	@Override
	public boolean onlyActivateOnClick() {
		return false;
	}


}
