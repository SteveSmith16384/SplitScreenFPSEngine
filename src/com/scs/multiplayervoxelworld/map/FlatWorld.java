package com.scs.multiplayervoxelworld.map;

import java.awt.Point;

import com.scs.multiplayervoxelworld.BlockCodes;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.entities.VoxelTerrainEntity;
import com.scs.multiplayervoxelworld.modules.GameModule;

import mygame.util.Vector3Int;

public class FlatWorld implements IPertinentMapData {

	private static final int WIDTH = 100;

	private MultiplayerVoxelWorldMain game;
	private GameModule module;

	public FlatWorld(MultiplayerVoxelWorldMain _game, GameModule _module) {
		game = _game;
		module = _module;

	}


	public void setup() {
		VoxelTerrainEntity vte = new VoxelTerrainEntity(game, module, 0, 0, 0, new Vector3Int(200, 20, 200), 16, 1, 1);
		vte.addRectRange_Blocks(BlockCodes.SAND, new Vector3Int(0, 0, 0), new Vector3Int(100, 1, 100));
		module.addEntity(vte);
	}

	
	@Override
	public int getWidth() {
		return WIDTH;
	}


	@Override
	public int getDepth() {
		return WIDTH;
	}


	@Override
	public Point getPlayerStartPos(int id) {
		return new Point(3, 3);
	}


	@Override
	public float getRespawnHeight() {
		return 5f;
	}


	@Override
	public Point getRandomCollectablePos() {
		return new Point(6, 6);
	}

}