package com.scs.samescreentowerdefence.entities;

import com.jme3.renderer.Camera;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.entities.AbstractPlayersAvatar;
import com.scs.multiplayervoxelworld.input.IInputDevice;
import com.scs.multiplayervoxelworld.modules.GameModule;
import com.scs.samescreentowerdefence.abilities.CycleThroughAbilitiesAbility;
import com.scs.samescreentowerdefence.abilities.PlaceTurretAbility;

public class TDPlayersAvatar extends AbstractPlayersAvatar {

	public TDPlayersAvatar(MultiplayerVoxelWorldMain _game, GameModule _module, int _playerID, Camera _cam, IInputDevice _input, int _side) {
		super(_game, _module, _playerID, _cam, _input, _side);

		ability[0] = new PlaceTurretAbility(game, _module, this); //LaserRifle(_game, _module, this);
		//this.abilityOther = new RemoveBlockAbility(_module, this);
		this.ability[1] = new CycleThroughAbilitiesAbility(game, _module, this);

}

}