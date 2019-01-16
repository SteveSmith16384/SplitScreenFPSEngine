package com.scs.multiplayervoxelworld.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.components.IEntity;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.components.IShowOnHUD;
import com.scs.multiplayervoxelworld.jme.JMEModelFunctions;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class Turret extends AbstractPhysicalEntity implements IShowOnHUD, IProcessable, ICanShoot {

	private static final float SHOT_INTERVAL = 3;

	private float timeUntilNextShot = 0;
	private int side;
	private AbstractPhysicalEntity currentTarget;
	private Spatial rotatingTurret;

	public Turret(MultiplayerVoxelWorldMain _game, GameModule _module, Vector3f pos, int _side) {
		super(_game, _module, "Trap");

		side = _side;

		Spatial model = game.getAssetManager().loadModel("Models/Turret_0/Base1.blend");
		rotatingTurret = ((Node)((Node)model).getChild(0)).getChild(0);
		JMEModelFunctions.setTextureOnSpatial(game.getAssetManager(), model, "Models/Turret_0/Turret1_Albedo.png");
		model.setLocalScale(.3f);
		model.setShadowMode(ShadowMode.CastAndReceive);

		this.mainNode.attachChild(model);
		mainNode.setLocalTranslation(pos);

		rigidBodyControl = new RigidBodyControl(0f);
		mainNode.addControl(rigidBodyControl);

		model.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

		module.addEntity(this);
	}


	@Override
	public void process(float tpfSecs) {
		timeUntilNextShot -= tpfSecs;
		if (timeUntilNextShot <= 0) {
			shoot();
		}

	}


	private AbstractPhysicalEntity findTarget() {
		for (IEntity e : module.entities) {
			if (e instanceof Golem) {
				return (Golem)e;
			}
		}
		return null;
	}


	private void shoot() {
		Settings.p("Turret shooting");
		timeUntilNextShot = SHOT_INTERVAL;
		currentTarget = findTarget();
		rotatingTurret.lookAt(currentTarget.getMainNode().getLocalTranslation(), Vector3f.UNIT_Y);
		if (currentTarget != null) {
			new LaserBullet(game, module, this);
		}
	}


	@Override
	public int getSide() {
		return side;
	}


	@Override
	public Vector3f getShootDir() {
		return currentTarget.getMainNode().getLocalTranslation().subtract(this.getMainNode().getLocalTranslation()).normalizeLocal();
	}


	@Override
	public void hasSuccessfullyHit(IEntity e) {
		// ?
	}


}
