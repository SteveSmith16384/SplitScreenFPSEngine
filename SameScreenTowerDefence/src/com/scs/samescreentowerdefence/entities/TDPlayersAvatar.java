package com.scs.samescreentowerdefence.entities;

import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.entities.AbstractPlayersAvatar;
import com.scs.multiplayervoxelworld.input.IInputDevice;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;
import com.scs.samescreentowerdefence.abilities.CycleThroughAbilitiesAbility;
import com.scs.samescreentowerdefence.abilities.PlaceTurretAbility;
import com.scs.samescreentowerdefence.models.RobotModel;

public class TDPlayersAvatar extends AbstractPlayersAvatar {

	public TDPlayersAvatar(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, int _playerID, Camera _cam, IInputDevice _input, int _side) {
		super(_game, _module, _playerID, _cam, _input, _side);

		ability[0] = new PlaceTurretAbility(game, _module, this); //LaserRifle(_game, _module, this);
		//this.abilityOther = new RemoveBlockAbility(_module, this);
		this.ability[1] = new CycleThroughAbilitiesAbility(game, _module, this);

}

	@Override
	protected Spatial getPlayersModel(MultiplayerVoxelWorldMain game, int pid) {
		return new RobotModel(game.getAssetManager(), pid);
	}

}
