package com.scs.multiplayervoxelworld.hud;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.components.IEntity;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;

public class AbstractHUDImage extends Picture implements IEntity, IProcessable {

	MultiplayerVoxelWorldMain game;
	private AbstractGameModule module;
	private float timeLeft;

	public AbstractHUDImage(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, Node guiNode, String tex, float w, float h, float dur) {
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
