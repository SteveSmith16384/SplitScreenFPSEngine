package com.scs.splitscreenfpsengine.entities;

import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public abstract class AbstractPhysicalEntity extends AbstractEntity {

	protected Node mainNode, leftNode, rightNode;;
	public RigidBodyControl rigidBodyControl;

	public AbstractPhysicalEntity(SplitScreenFpsEngine _game, AbstractGameModule _module, String _name) {
		super(_game, _module, _name);

		mainNode = new Node(name + "_MainNode");

		leftNode = new Node("left_node");
		mainNode.attachChild(leftNode);
		leftNode.setLocalTranslation(-3, 0, 0);

		rightNode = new Node("right_node");
		mainNode.attachChild(rightNode);
		rightNode.setLocalTranslation(3, 0, 0);

		mainNode.setUserData(Settings.ENTITY, this);

	}


	public boolean collides() {
		return true;
	}


	@Override
	public void actuallyAdd() {
		game.getRootNode().attachChild(getMainNode());
		module.bulletAppState.getPhysicsSpace().add(mainNode);
		if (this instanceof PhysicsTickListener) {
			module.bulletAppState.getPhysicsSpace().addTickListener((PhysicsTickListener)this);
		}

	}


	@Override
	public void actuallyRemove() {
		super.actuallyRemove();

		this.mainNode.removeFromParent();
		if (rigidBodyControl != null) {
			this.module.bulletAppState.getPhysicsSpace().remove(this.rigidBodyControl);
		}
	}


	public Node getMainNode() {
		return mainNode;
	}


	public float distance(AbstractPhysicalEntity o) {
		return distance(o.getLocation());
		//return distance(o.rigidBodyControl.getPhysicsLocation()); Sometimes rbc is null!
	}


	public float distance(Vector3f pos) {
		//float dist = this.getMainNode().getWorldTranslation().distance(pos);
		float dist = this.getLocation().distance(pos);
		return dist;
	}


	public Vector3f getLocation() {
		if (rigidBodyControl != null) {
			return this.rigidBodyControl.getPhysicsLocation();
		} else {
			return this.mainNode.getWorldTranslation();
		}
	}


	public void applyForce(Vector3f dir) {
		rigidBodyControl.applyImpulse(dir, Vector3f.ZERO);//.applyCentralForce(dir);
	}


	public void setLocation(Vector3f pos) {
		if (rigidBodyControl != null) {
			this.rigidBodyControl.setPhysicsLocation(pos);
		} else {
			//Settings.pe(this + " has no rigid body!");
			this.mainNode.setLocalTranslation(pos);
		}
	}


	public Vector3f getCenter() {
		return this.getMainNode().getWorldBound().getCenter();
	}


}
