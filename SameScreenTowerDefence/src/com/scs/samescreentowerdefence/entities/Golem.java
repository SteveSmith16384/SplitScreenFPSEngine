package com.scs.samescreentowerdefence.entities;

import com.jme3.math.Vector3f;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.MyBetterCharacterControl;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.components.IDamagable;
import com.scs.multiplayervoxelworld.components.INotifiedOfCollision;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;
import com.scs.multiplayervoxelworld.jme.JMEAngleFunctions;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;
import com.scs.samescreentowerdefence.models.GolemModel;

import ssmith.util.RealtimeInterval;

public class Golem extends AbstractPhysicalEntity implements IProcessable, IDamagable, INotifiedOfCollision {

	private static final float TURN_SPEED = 1f;

	private static final float PLAYER_HEIGHT = 3f;
	private static final float WEIGHT = 1f;

	private enum AIMode {WalkToCrystal, AvoidBlockage}; 

	private GolemModel model;
	private MyBetterCharacterControl playerControl;

	private float health = 1;
	private boolean dead = false;
	private long removeAt;

	// AI
	private AIMode aiMode = AIMode.WalkToCrystal;
	private Vector3f targetPos;
	private float avoidUntil = 0;
	private RealtimeInterval checkPosInterval = new RealtimeInterval(2000);
	private Vector3f prevPos = new Vector3f();

	public Golem(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, Vector3f startPos, Vector3f _targetPos) {
		super(_game, _module, "Golem");

		targetPos = _targetPos;

		model = new GolemModel(game.getAssetManager());
		this.getMainNode().attachChild(model.getModel());
		this.getMainNode().setLocalTranslation(startPos);

		playerControl = new MyBetterCharacterControl(PLAYER_HEIGHT/3, PLAYER_HEIGHT, WEIGHT); // todo - calc from model size
		playerControl.setJumpForce(new Vector3f(0, Settings.JUMP_FORCE, 0)); 
		this.getMainNode().addControl(playerControl);

		playerControl.getPhysicsRigidBody().setUserObject(this);

	}


	@Override
	public void process(float tpfSecs) {
		if (module.isGameOver()) {
			return;
		}
		if (!dead) {
			model.setAnim(GolemModel.ANIM_WALK);

			if (checkPosInterval.hitInterval()) {
				if (this.mainNode.getWorldTranslation().distance(this.prevPos) < .5f) {
					Settings.p(this + " stuck, changing dir");
					this.aiMode = AIMode.AvoidBlockage;
				}
				prevPos.set(this.mainNode.getWorldTranslation());
			}

			if (this.aiMode == AIMode.WalkToCrystal) {
				this.turnTowardsDestination();
			} else {
				turnAwayFromDestination();
				this.avoidUntil -= tpfSecs;
				if (avoidUntil < 0) {
					aiMode = AIMode.WalkToCrystal;
				}
			}
			this.moveFwds();
		} else {
			if (this.removeAt < System.currentTimeMillis()) {
				this.markForRemoval();
			}
		}
	}


	private void turnTowardsDestination() {
		float leftDist = this.leftNode.getWorldTranslation().distance(targetPos); 
		float rightDist = this.rightNode.getWorldTranslation().distance(targetPos); 
		if (leftDist > rightDist) {
			JMEAngleFunctions.turnSpatialLeft(this.mainNode, TURN_SPEED);
		} else {
			JMEAngleFunctions.turnSpatialLeft(this.mainNode, -TURN_SPEED);
		}
		this.playerControl.setViewDirection(mainNode.getWorldRotation().getRotationColumn(2));
	}


	private void turnAwayFromDestination() {
		float leftDist = this.leftNode.getWorldTranslation().distance(targetPos); 
		float rightDist = this.rightNode.getWorldTranslation().distance(targetPos); 
		if (leftDist < rightDist) {
			JMEAngleFunctions.turnSpatialLeft(this.mainNode, TURN_SPEED);
		} else {
			JMEAngleFunctions.turnSpatialLeft(this.mainNode, -TURN_SPEED);
		}
		this.playerControl.setViewDirection(mainNode.getWorldRotation().getRotationColumn(2));
	}


	private void moveFwds() {
		Vector3f walkDirection = this.playerControl.getViewDirection();
		walkDirection.y = 0;
		//Globals.p("Ant dir: " + walkDirection);
		playerControl.setWalkDirection(walkDirection.mult(.3f));//1));

	}


	@Override
	public void actuallyRemove() {
		super.actuallyRemove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.playerControl);

	}


	@Override
	public int getSide() {
		return 1;
	}


	@Override
	public void damaged(float amt, String reason) {
		if (dead) {
			return;
		}
		this.health -= amt;
		if (this.health <= 0) {
			Settings.p(this + " killed");
			dead = true;
			model.setAnim(GolemModel.ANIM_IDLE);
			removeAt = System.currentTimeMillis() + 2000;
		}

	}


	@Override
	public void notifiedOfCollision(AbstractPhysicalEntity other) {
		if (other instanceof Crystal) {
			Settings.p(this + " collided with " + other);
			module.gameOver();
		}

	}

}
