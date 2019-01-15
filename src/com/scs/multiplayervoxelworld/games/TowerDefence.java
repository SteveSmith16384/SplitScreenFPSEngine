package com.scs.multiplayervoxelworld.games;

import com.jme3.math.Vector3f;
import com.scs.multiplayervoxelworld.BlockCodes;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.entities.Golem;
import com.scs.multiplayervoxelworld.entities.VoxelTerrainEntity;
import com.scs.multiplayervoxelworld.modules.GameModule;

import mygame.util.Vector3Int;

public class TowerDefence extends AbstractGame {

	private static final int MAP_SIZE = 100;
	
	private Vector3f CRYSTAL_POS = new Vector3f();
	
	public TowerDefence() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void setup(MultiplayerVoxelWorldMain game, GameModule module) {
		VoxelTerrainEntity vte = new VoxelTerrainEntity(game, module, 0, 0, 0, new Vector3Int(200, 20, 200), 16, 1, 1);
		vte.addRectRange_Blocks(BlockCodes.GRASS, new Vector3Int(0, 0, 0), new Vector3Int(100, 1, 100));
		vte.addRectRange_Blocks(BlockCodes.SAND, new Vector3Int(10, 1, 10), new Vector3Int(1, 1, 1));
		module.addEntity(vte);
		
		Golem golem = new Golem(game, module, new Vector3f(20, 5, 20));
		module.addEntity(golem);
	}


}
