package com.scs.multiplayervoxelworld.components;

public interface IEntity {

	void actuallyAdd(); // Any code to run when actually added
	
	//void markForRemoval();
	
	void actuallyRemove();  // Any code to run when actually removed

}
