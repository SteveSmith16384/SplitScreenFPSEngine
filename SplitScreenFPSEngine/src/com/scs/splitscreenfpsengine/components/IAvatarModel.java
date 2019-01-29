package com.scs.splitscreenfpsengine.components;

import com.jme3.scene.Spatial;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;

public interface IAvatarModel {

	Spatial getModel();
	
	void setAvatarAnim(AbstractPlayersAvatar.Anim anim);


}
