package com.scs.splitscreentowerdefence.entities;

import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfpsengine.input.IInputDevice;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;
import com.scs.splitscreentowerdefence.abilities.CycleThroughAbilitiesAbility;
import com.scs.splitscreentowerdefence.abilities.PlaceTurretAbility;
import com.scs.splitscreentowerdefence.components.ITargetByAI;
import com.scs.splitscreentowerdefence.models.RobotModel;

public class TDPlayersAvatar extends AbstractPlayersAvatar implements ITargetByAI {

	public int resources = 10;

	public TDPlayersAvatar(SplitScreenFpsEngine _game, AbstractGameModule _module, int _playerID, Camera _cam, IInputDevice _input, int _side) {
		super(_game, _module, _playerID, _cam, _input, _side);

		ability[0] = new PlaceTurretAbility(game, _module, this); //LaserRifle(_game, _module, this);
		this.ability[1] = new CycleThroughAbilitiesAbility(game, _module, this);

}

	@Override
	protected Spatial getPlayersModel(SplitScreenFpsEngine game, int pid) {
		return new RobotModel(game.getAssetManager(), pid);
	}


}
