package com.scs.splitscreentowerdefence.abilities;

import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.abilities.AbstractAbility;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class RunFast extends AbstractAbility {

	private static final float MAX_POWER = 10;
	
	private float power;
	private boolean isRunningFast;
	
	public RunFast(SplitScreenFpsEngine _game, AbstractGameModule module, AbstractPlayersAvatar _player) {
		super(_game, module, _player);
	}

	
	@Override
	public boolean process(float interpol) {
		isRunningFast = false;
		power += interpol;
		power = Math.min(power, MAX_POWER);
		this.avatar.moveSpeed = Settings.PLAYER_MOVE_SPEED;
		return true;
	}

	
	@Override
	public boolean activate(float interpol) {
		power -= interpol;
		power = Math.max(power, 0);
		if (power > 0) {
			this.avatar.moveSpeed = Settings.PLAYER_MOVE_SPEED * 1.5f;
			isRunningFast = true;
			return true;
		}
		return false;
	}

	
	@Override
	public String getHudText() {
		return isRunningFast ? "RUNNING FAST!" : "[running normally]";
	}

}
