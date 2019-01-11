package com.scs.multiplayervoxelworld.map;

public interface ISimpleMapData {

	int getWidth();
	
	int getDepth();
	
	int getCodeForSquare(int x, int z);
	
	void addMisc();

}
