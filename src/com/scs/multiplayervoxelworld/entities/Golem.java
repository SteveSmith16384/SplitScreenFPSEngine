package com.scs.multiplayervoxelworld.entities;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class Golem extends AbstractPhysicalEntity implements IProcessable {

	public Golem(MultiplayerVoxelWorldMain _game, GameModule _module) {
		super(_game, _module, "Golem");
	}

	@Override
	public void process(float tpfSecs) {
		// TODO Auto-generated method stub
		
	}

}
