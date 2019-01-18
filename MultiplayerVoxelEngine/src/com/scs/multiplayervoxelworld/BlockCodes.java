package com.scs.multiplayervoxelworld;

import com.scs.multiplayervoxelworld.blocks.GrassBlock;
import com.scs.multiplayervoxelworld.blocks.LavaBlock;
import com.scs.multiplayervoxelworld.blocks.SandBlock;
import com.scs.multiplayervoxelworld.blocks.StoneBlock;

import mygame.blocks.IBlock;

public class BlockCodes {

	public static final int GRASS = 1;
	public static final int SAND = 2;
	public static final int STONE = 3;
	public static final int LAVA = 4;

	/*
	public static Class<? extends IBlock> getClassFromCode(int code) {
		switch (code) {
		case GRASS: return GrassBlock.class;
		case SAND: return SandBlock.class;
		case STONE: return StoneBlock.class;
		case LAVA: return LavaBlock.class;

		default: throw new RuntimeException("code: " + code);
		}
	}*/
}
