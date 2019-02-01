package com.scs.splitscreenfpsengine.components;

public interface IEntity {

	void actuallyAdd(); // Any code to run when actually added
	
	//void markForRemoval();
	
	void actuallyRemove();  // Any code to run when actually removed.  Do not call directly, call game.removeEntity();

}
