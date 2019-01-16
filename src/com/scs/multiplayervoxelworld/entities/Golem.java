package com.scs.multiplayervoxelworld.entities;

import com.jme3.math.Vector3f;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.MyBetterCharacterControl;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.jme.JMEAngleFunctions;
import com.scs.multiplayervoxelworld.models.GolemModel;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class Golem extends AbstractPhysicalEntity implements IProcessable {

	private static final float TURN_SPEED = 1f;

	private static final float PLAYER_HEIGHT = 1.5f;
	private static final float PLAYER_RAD = 0.4f;
	private static final float WEIGHT = 1f;
	
	private enum AiMode {WalkToCrystal, AvoidOtherGolem}; 

	private GolemModel model;
	private MyBetterCharacterControl playerControl;
	private AiMode aiMode = AiMode.WalkToCrystal;
	private Vector3f targetPos;

	public Golem(MultiplayerVoxelWorldMain _game, GameModule _module, Vector3f startPos, Vector3f _targetPos) {
		super(_game, _module, "Golem");
		
		targetPos = _targetPos;
		
		model = new GolemModel(game.getAssetManager());
		this.getMainNode().attachChild(model.getModel());
		this.getMainNode().setLocalTranslation(startPos);
		
		playerControl = new MyBetterCharacterControl(PLAYER_RAD, PLAYER_HEIGHT, WEIGHT);
		playerControl.setJumpForce(new Vector3f(0, Settings.JUMP_FORCE, 0)); 
		this.getMainNode().addControl(playerControl);

		playerControl.getPhysicsRigidBody().setUserObject(this);

	}

	
	@Override
	public void process(float tpfSecs) {
		model.setAnim(GolemModel.ANIM_WALK);
		// TODO Auto-generated method stub
	}


	private void turnTowardsPlayer() {
		float leftDist = this.leftNode.getWorldTranslation().distance(targetPos); 
		float rightDist = this.rightNode.getWorldTranslation().distance(targetPos); 
		if (leftDist > rightDist) {
			JMEAngleFunctions.turnSpatialLeft(this.mainNode, TURN_SPEED);
		} else {
			JMEAngleFunctions.turnSpatialLeft(this.mainNode, -TURN_SPEED);
		}
		this.playerControl.setViewDirection(mainNode.getWorldRotation().getRotationColumn(2));
	}


	private void moveFwds() {
		Vector3f walkDirection = this.playerControl.getViewDirection();//.mainNode.getWorldRotation().getRotationColumn(2);
		walkDirection.y = 0;
		//Globals.p("Ant dir: " + walkDirection);
		playerControl.setWalkDirection(walkDirection.mult(2.4f));

	}


	@Override
	public void actuallyRemove() {
		super.actuallyRemove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.playerControl);

	}


}