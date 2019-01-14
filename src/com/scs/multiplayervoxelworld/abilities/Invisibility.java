package com.scs.multiplayervoxelworld.abilities;

import com.jme3.scene.Spatial.CullHint;
import com.scs.multiplayervoxelworld.entities.PlayersAvatar;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class Invisibility extends AbstractAbility {

	private static final float MAX_POWER = 10;
	
	private float power;
	private boolean isInvisible;
	
	public Invisibility(GameModule module, PlayersAvatar _player) {
		super(module, _player);
	}

	
	@Override
	public boolean process(float interpol) {
		this.player.getMainNode().setCullHint(CullHint.Inherit); // Default
		isInvisible = false;
		power += interpol;
		power = Math.min(power, MAX_POWER);
		return true;
	}

	
	@Override
	public boolean activate(float interpol) {
		power -= interpol*4;
		power = Math.max(power, 0);
		if (power > 0) {
			this.player.getMainNode().setCullHint(CullHint.Always);
			isInvisible = true;
			return true;
		}
		return false;
	}

	
	@Override
	public String getHudText() {
		return (isInvisible ? "INVISIBLE! " : "[not invisible] ") + ((int)(power*10));
	}

}
