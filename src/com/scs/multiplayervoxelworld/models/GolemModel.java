package com.scs.multiplayervoxelworld.models;


import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.scs.multiplayervoxelworld.jme.JMEModelFunctions;

/**
 * Anims are idle, smash, walk
 *
 */
public class GolemModel {

	public static final float MODEL_WIDTH = 0.4f;
	public static final float MODEL_HEIGHT = 0.7f;

	private AssetManager assetManager;
	private Spatial model;

	// Anim
	private AnimChannel channel;
	private int currAnimCode = -1;

	public GolemModel(AssetManager _assetManager) {
		assetManager = _assetManager;

		model = assetManager.loadModel("Models/golem/golem_clean.blend");
		JMEModelFunctions.setTextureOnSpatial(assetManager, model, "Textures/lavarock.jpg");
		model.setShadowMode(ShadowMode.CastAndReceive);
		JMEModelFunctions.scaleModelToHeight(model, MODEL_HEIGHT);
		JMEModelFunctions.moveYOriginTo(model, 0f);
		//JMEAngleFunctions.rotateToWorldDirection(model, new Vector3f(0, 0, 0)); // Point model fwds

		AnimControl control = JMEModelFunctions.getNodeWithControls(null, (Node)model);
		channel = control.createChannel();

		//return model;
	}

/*
	@Override
	public Vector3f getCollisionBoxSize() {
		return new Vector3f(MODEL_WIDTH, MODEL_HEIGHT, MODEL_WIDTH);
	}


	@Override
	public Spatial getModel() {
		return model;
	}


	@Override
	public void setAnim(int animCode) {
		if (currAnimCode == animCode) {
			return;			
		}

		switch (animCode) {
		case AbstractAvatar.ANIM_IDLE:
			channel.setLoopMode(LoopMode.Loop);
			channel.setAnim("idle");
			break;

		case AbstractAvatar.ANIM_WALKING:
			channel.setLoopMode(LoopMode.Loop);
			channel.setAnim("walk");
			break;

		case AbstractAvatar.ANIM_ATTACK:
			channel.setLoopMode(LoopMode.Loop);
			channel.setAnim("smash");
			break;

		default:
			Globals.pe(this.getClass().getSimpleName() + ": Unable to show anim " + animCode);
		}

		currAnimCode = animCode;

	}
*/
}

