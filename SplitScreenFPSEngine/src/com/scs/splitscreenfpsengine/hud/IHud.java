package com.scs.splitscreenfpsengine.hud;

import com.jme3.scene.Spatial;

public interface IHud {

	Spatial getSpatial();
	
	void refresh();
	
	void showDamageBox();
	
	void showCollectBox();
	
	void appendToLog(String s);
}
