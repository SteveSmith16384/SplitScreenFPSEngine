package com.scs.multiplayervoxelworld.map;

import java.awt.Point;

public interface IPertinentMapData {
	
	void setup();

	int getWidth();
	
	int getDepth();
	
	Point getPlayerStartPos(int id);
	
	Point getRandomCollectablePos(); // todo - rename
	
	float getRespawnHeight();
	
}
