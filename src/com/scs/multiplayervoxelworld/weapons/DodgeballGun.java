package com.scs.multiplayervoxelworld.weapons;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.abilities.IAbility;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.entities.DodgeballBall;
import com.scs.multiplayervoxelworld.entities.PlayersAvatar;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class DodgeballGun extends AbstractGun implements IAbility {

	public DodgeballGun(MultiplayerVoxelWorldMain _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "DodgeballGun", 1000, shooter);
	}


	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			PlayersAvatar av = (PlayersAvatar) shooter;
			if (av.getHasBall()) {
				new DodgeballBall(game, module, shooter);
				av.setHasBall(false);
				return true;
			}
		}
		return false;
	}

}
