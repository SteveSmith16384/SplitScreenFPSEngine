package com.scs.splitscreentowerdefence.abilities;

import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.abilities.AbstractAbility;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class BoostFwd extends AbstractAbility {

	private static final float POWER = 10f;//.3f;
	private static final float MAX_FUEL = 10;

	private float fuel = 100;

	public BoostFwd(SplitScreenFpsEngine _game, AbstractGameModule module, AbstractPlayersAvatar _player) {
		super(_game, module, _player, "BoostFwd");

	}


	@Override
	public boolean process(float interpol) {
		fuel += interpol*3;
		fuel = Math.min(fuel, MAX_FUEL);
		return fuel < MAX_FUEL;
	}


	@Override
	public boolean activate(float interpol) {
		fuel -= (interpol*10);
		fuel = Math.max(fuel, 0);
		if (fuel > 0) {
			//Settings.p("Jetpac-ing!");
			//player.walkDirection.addLocal(FORCE);//, Vector3f.ZERO);

			avatar.camDir.set(avatar.cam.getDirection()).multLocal(POWER, 0.0f, POWER);
			avatar.walkDirection.addLocal(avatar.camDir);
			return true;
		}
		return false;
	}


	@Override
	public String getHudText() {
		return "Boost Fuel:" + ((int)(fuel*10));
	}

}
