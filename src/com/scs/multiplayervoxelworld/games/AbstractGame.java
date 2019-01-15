package com.scs.multiplayervoxelworld.games;

import com.jme3.math.Vector3f;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.modules.GameModule;

public abstract class AbstractGame {

	public AbstractGame() {
		// TODO Auto-generated constructor stub
	}

	
	public abstract void setup(MultiplayerVoxelWorldMain game, GameModule module);
	
	public abstract Vector3f getPlayerStartPos(int id);
}
