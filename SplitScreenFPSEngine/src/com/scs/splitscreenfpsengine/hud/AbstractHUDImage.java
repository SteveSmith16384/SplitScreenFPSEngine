package com.scs.splitscreenfpsengine.hud;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.components.IEntity;
import com.scs.splitscreenfpsengine.components.IProcessable;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class AbstractHUDImage extends Picture implements IEntity, IProcessable {

	SplitScreenFpsEngine game;
	private AbstractGameModule module;
	private float timeLeft;

	public AbstractHUDImage(SplitScreenFpsEngine _game, AbstractGameModule _module, Node guiNode, String tex, float w, float h, float dur) {
		super("AbstractHUDImage");

		game = _game;
		module = _module;
		this.timeLeft = dur;

		setImage(game.getAssetManager(), tex, true);
		setWidth(w);
		setHeight(h);
		//this.setPosition(w/2, h/2);

		guiNode.attachChild(this);
		module.addEntity(this);

	}


	@Override
	public void process(float tpf) {
		if (timeLeft > 0) {
			this.timeLeft -= tpf;
			if (this.timeLeft <= 0) {
				this.removeFromParent();
				module.markEntityForRemoval(this);
			}
		}
	}


	@Override
	public void actuallyAdd() {
		// TODO Auto-generated method stub
		
	}

/*
	@Override
	public void markForRemoval() {
		// TODO Auto-generated method stub
		
	}
*/

	@Override
	public void actuallyRemove() {
		this.removeFromParent();
		
	}

}
