package com.scs.multiplayervoxelworld;

import com.scs.multiplayervoxelworld.blocks.GrassBlock;
import com.scs.multiplayervoxelworld.blocks.SandBlock;

import mygame.blocks.IBlock;

public class BlockCodes {

	public static final int GRASS = 1;
	public static final int SAND = 2;

	
	public static Class<? extends IBlock> getClassFromCode(int code) {
		switch (code) {
		case GRASS: return GrassBlock.class;
		case SAND: return SandBlock.class;

		default: throw new RuntimeException("code: " + code);
		}
	}
}
