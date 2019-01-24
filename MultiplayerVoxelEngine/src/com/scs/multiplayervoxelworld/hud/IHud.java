package com.scs.multiplayervoxelworld.hud;

import com.jme3.scene.Spatial;

public interface IHud {

	Spatial getSpatial();
	
	void refresh();
	
	void showDamageBox();
	
	void showCollectBox();
}
