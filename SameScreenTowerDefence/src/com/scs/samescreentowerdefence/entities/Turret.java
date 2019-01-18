package com.scs.samescreentowerdefence.entities;

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
import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;
import com.scs.multiplayervoxelworld.jme.JMEAngleFunctions;
import com.scs.multiplayervoxelworld.jme.JMEModelFunctions;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class Turret extends AbstractPhysicalEntity implements IShowOnHUD, IProcessable, ICanShoot {

	private static final float SHOT_RANGE = 30;
	private static final float SHOT_INTERVAL = 3;

	private float timeUntilNextShot = 0;
	private int side;
	private AbstractPhysicalEntity currentTarget;
	private Node rotatingTurret;
	private Node launchPos = new Node();

	public Turret(MultiplayerVoxelWorldMain _game, GameModule _module, Vector3f pos, int _side) {
		super(_game, _module, "Turret");

		side = _side;

		Spatial model = JMEModelFunctions.loadModel(game.getAssetManager(), "Models/Turret_0/Base1.blend"); //game.getAssetManager().loadModel("Models/Turret_0/Base1.blend");
		JMEModelFunctions.setTextureOnSpatial(game.getAssetManager(), model, "Models/Turret_0/Turret1_Albedo.png");
		Spatial barrel = ((Node)((Node)model).getChild(0)).getChild(0);
		barrel.removeFromParent();
		JMEAngleFunctions.rotateToWorldDirection(barrel, new Vector3f(-1, 0, 0)); // Point model fwds.  Must be before we attach it to a parent, otherwise the parent will be rotated

		rotatingTurret = new Node("TurretNode");
		rotatingTurret.attachChild(barrel);
		rotatingTurret.attachChild(launchPos);
		launchPos.setLocalTranslation(2, 2.5f, 0);
		this.getMainNode().attachChild(rotatingTurret);

		this.getMainNode().setLocalScale(.25f);
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
		if (module.isGameOver()) {
			return;
		}

		timeUntilNextShot -= tpfSecs;
		if (currentTarget != null) {
			Vector3f targetPos = currentTarget.getMainNode().getWorldBound().getCenter();
			rotatingTurret.lookAt(targetPos, Vector3f.UNIT_Y); // currentTarget.getMainNode().getLocalTranslation()
		}
		if (timeUntilNextShot <= 0) {
			shoot();
		}

	}


	private AbstractPhysicalEntity findTarget() {
		AbstractPhysicalEntity closest = null;
		float closestDist = SHOT_RANGE;
		for (IEntity e : module.entities) {
			if (e instanceof Golem) {
				Golem golem = (Golem)e;
				float dist = this.distance(golem);
				if (dist <= closestDist) {
					closestDist = dist;
					closest = golem;
				}
				return (Golem)e;
			}
		}
		return closest;
	}


	private void shoot() {
		Settings.p("Turret shooting");
		timeUntilNextShot = SHOT_INTERVAL;
		currentTarget = findTarget();
		if (currentTarget != null) {
			new LaserBullet(game, module, this);
		}
	}


	@Override
	public Vector3f getBulletStartPosition() {
		return launchPos.getWorldTranslation();//this.getMainNode().getWorldBound().getCenter();
	}


	@Override
	public int getSide() {
		return side;
	}


	@Override
	public Vector3f getShootDir() {
		Vector3f targetPos = currentTarget.getMainNode().getWorldBound().getCenter();
		return targetPos.subtract(this.getMainNode().getLocalTranslation()).normalizeLocal();
	}


	@Override
	public void hasSuccessfullyHit(IEntity e) {
		// ?
	}


}
