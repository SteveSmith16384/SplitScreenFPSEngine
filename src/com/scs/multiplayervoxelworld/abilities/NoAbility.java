package com.scs.multiplayervoxelworld.abilities;

public class NoAbility implements IAbility {

	public NoAbility() {
	}

	@Override
	public boolean process(float interpol) {
		return false;
	}

	
	@Override
	public boolean activate(float interpol) {
		// Do nothing
		return false;
	}

	
	@Override
	public String getHudText() {
		return "[no ability]";
	}


	@Override
	public boolean onlyActivateOnClick() {
		return true;
	}


}
