package com.scs.multiplayervoxelworld.models;


import com.jme3.animation.AnimChannel;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

/**
 * This class, and classes like this (i.e. a class for a model), are designed to keep all the model-specific settings in one place.
 * 
 * Anims: [Walk, Die, Hit, Idle, Attack]
 */
public class WizardModel {

	public static final float MODEL_WIDTH = 0.4f;
	private static final float MODEL_DEPTH = 0.3f;
	public static final float MODEL_HEIGHT = 0.7f;

	private AssetManager assetManager;
	private Spatial model;

	// Anim
	private AnimChannel channel;
	private int currAnimCode = -1;

	public WizardModel(AssetManager _assetManager) {
		assetManager = _assetManager;

	}

/*
	@Override
	public Spatial createAndGetModel() {
		model = assetManager.loadModel("Models/mage/mage.blend");
		JMEModelFunctions.setTextureOnSpatial(assetManager, model, "Models/mage/mage.png");
		model.setShadowMode(ShadowMode.CastAndReceive);
		JMEModelFunctions.scaleModelToHeight(model, MODEL_HEIGHT);
		JMEModelFunctions.moveYOriginTo(model, 0f);
		JMEAngleFunctions.rotateToWorldDirection(model, new Vector3f(-1, 0, 1)); // Point model fwds

		AnimControl control = JMEModelFunctions.getNodeWithControls(null, (Node)model);
		channel = control.createChannel();
		setAnim(AbstractAvatar.ANIM_IDLE); // Default

		return model;
	}


	@Override
	public float getCameraHeight() {
		return MODEL_HEIGHT - 0.2f;
	}


	@Override
	public float getBulletStartHeight() {
		return MODEL_HEIGHT - 0.3f;
	}


	@Override
	public Vector3f getCollisionBoxSize() {
		return new Vector3f(MODEL_WIDTH, MODEL_HEIGHT, MODEL_DEPTH);
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
			channel.setAnim("Idle");
			break;
			
		case AbstractAvatar.ANIM_WALKING:
		case AbstractAvatar.ANIM_RUNNING: // Wizard only has one "move" anim, so use the same for walking and running
			channel.setLoopMode(LoopMode.Loop);
			channel.setAnim("Walk");
			break;

		case AbstractAvatar.ANIM_ATTACK:
			channel.setLoopMode(LoopMode.Loop);
			channel.setAnim("Attack");
			break;

		case AbstractAvatar.ANIM_DIED:
			channel.setLoopMode(LoopMode.Loop);
			channel.setAnim("Die");
			break;

		default:
			Globals.pe(this.getClass().getSimpleName() + ": Unable to show anim " + animCode);
		}

		currAnimCode = animCode;


	}
*/
}
