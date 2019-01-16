package com.scs.multiplayervoxelworld.games;

import com.jme3.math.Vector3f;
import com.scs.multiplayervoxelworld.BlockCodes;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.components.IEntity;
import com.scs.multiplayervoxelworld.entities.Crystal;
import com.scs.multiplayervoxelworld.entities.Golem;
import com.scs.multiplayervoxelworld.entities.VoxelTerrainEntity;
import com.scs.multiplayervoxelworld.modules.GameModule;

import mygame.util.Vector3Int;

public class TowerDefence extends AbstractGame implements IEntity {

	private static final int MAP_SIZE = 100;

	private enum Phase {Waiting, Attack};
	
	private Vector3f CRYSTAL_POS = new Vector3f(MAP_SIZE/2, 2, MAP_SIZE/2);

	private long nextPhaseInterval;
	
	public void setup(MultiplayerVoxelWorldMain game, GameModule module) {
		VoxelTerrainEntity vte = new VoxelTerrainEntity(game, module, 0, 0, 0, new Vector3Int(200, 20, 200), 16, 1, 1);
		vte.addRectRange_Blocks(BlockCodes.GRASS, new Vector3Int(0, 0, 0), new Vector3Int(100, 1, 100));
		vte.addRectRange_Blocks(BlockCodes.SAND, new Vector3Int(10, 1, 10), new Vector3Int(1, 1, 1));
		module.addEntity(vte);
		
		Crystal c = new Crystal(game, module, CRYSTAL_POS);
		module.addEntity(c);
		
		Golem golem = new Golem(game, module, new Vector3f(20, 5, 20), CRYSTAL_POS);
		module.addEntity(golem);
	}


	@Override
	public Vector3f getPlayerStartPos(int id) {
		return new Vector3f(MAP_SIZE/2-3, 2, MAP_SIZE/2-3);
	}


	@Override
	public void actuallyAdd() {
		// Do nothing
		
	}


	@Override
	public void markForRemoval() {
		module.markEntityForRemoval(this);		
	}


	@Override
	public void actuallyRemove() {
		// Do nothing
		
	}


}
