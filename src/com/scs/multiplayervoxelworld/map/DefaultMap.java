package com.scs.multiplayervoxelworld.map;
/*
import java.awt.Point;

import com.jme3.math.Vector3f;
import com.scs.multiplayervoxelworld.BlockCodes;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.entities.Golem;
import com.scs.multiplayervoxelworld.entities.VoxelTerrainEntity;
import com.scs.multiplayervoxelworld.modules.GameModule;

import mygame.util.Vector3Int;

public class DefaultMap implements IPertinentMapData {

	private static final int WIDTH = 100;

	private MultiplayerVoxelWorldMain game;
	private GameModule module;

	public DefaultMap(MultiplayerVoxelWorldMain _game, GameModule _module) {
		game = _game;
		module = _module;

	}


	public void setup() {
		VoxelTerrainEntity vte = new VoxelTerrainEntity(game, module, 0, 0, 0, new Vector3Int(200, 20, 200), 16, 1, 1);
		vte.addRectRange_Blocks(BlockCodes.GRASS, new Vector3Int(0, 0, 0), new Vector3Int(100, 1, 100));
		vte.addRectRange_Blocks(BlockCodes.SAND, new Vector3Int(10, 1, 10), new Vector3Int(1, 1, 1));
		module.addEntity(vte);
		
		Golem golem = new Golem(game, module, new Vector3f(20, 5, 20));
		module.addEntity(golem);
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

}*/