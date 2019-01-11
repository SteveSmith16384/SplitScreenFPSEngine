package com.scs.multiplayervoxelworld.entities;

import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.modules.GameModule;

public abstract class AbstractPhysicalEntity extends AbstractEntity implements IProcessable {

	protected Node mainNode;
	public RigidBodyControl rigidBodyControl;

	public AbstractPhysicalEntity(MultiplayerVoxelWorldMain _game, GameModule _module, String _name) {
		super(_game, _module, _name);

		mainNode = new Node(name + "_MainNode");
	}


	@Override
	public void actuallyAdd() {
		//AbstractPhysicalEntity ape = (AbstractPhysicalEntity)e;
		game.getRootNode().attachChild(getMainNode());
		module.bulletAppState.getPhysicsSpace().add(mainNode);
		if (this instanceof PhysicsTickListener) {
			module.bulletAppState.getPhysicsSpace().addTickListener((PhysicsTickListener)this);
		}
		
	}


	@Override
	public void actuallyRemove() {
		if (!this.isMarkedForRemoval()) {
			throw new RuntimeException("You must mark an item for removal!");
		}
		
		this.mainNode.removeFromParent();
		if (rigidBodyControl != null) {
			this.module.bulletAppState.getPhysicsSpace().remove(this.rigidBodyControl);
		}
	}


	public Node getMainNode() {
		return mainNode;
	}


	public float distance(AbstractPhysicalEntity o) {
		//return distance(o.getMainNode().getWorldTranslation());
		return distance(o.rigidBodyControl.getPhysicsLocation());
	}


	public float distance(Vector3f pos) {
		//float dist = this.getMainNode().getWorldTranslation().distance(pos);
		float dist = this.rigidBodyControl.getPhysicsLocation().distance(pos);
		return dist;
	}


	/*public boolean canSee(PhysicalEntity cansee) {
		Ray r = new Ray(this.getMainNode().getWorldTranslation(), cansee.getMainNode().getWorldTranslation().subtract(this.getMainNode().getWorldTranslation()).normalizeLocal());
		//synchronized (module.objects) {
		//if (go.collides) {
		CollisionResults results = new CollisionResults();
		Iterator<IEntity> it = module.entities.iterator();
		while (it.hasNext()) {
			IEntity o = it.next();
			if (o instanceof PhysicalEntity && o != this) {
				PhysicalEntity go = (PhysicalEntity)o;
				// if (go.collides) {
				if (go.getMainNode().getWorldBound() != null) {
					results.clear();
					try {
						go.getMainNode().collideWith(r, results);
					} catch (UnsupportedCollisionException ex) {
						System.out.println("Spatial: " + go.getMainNode());
						ex.printStackTrace();
					}
					if (results.size() > 0) {
						float go_dist = this.distance(cansee)-1;
						CollisionResult cr = results.getClosestCollision();
						if (cr.getDistance() < go_dist) {
							return false;
						}
					}
				}
				//}
			}
		}
		return true;
	}*/


	public Vector3f getLocation() {
		//return this.main_node.getWorldTranslation(); 000?
		return this.rigidBodyControl.getPhysicsLocation();
		
	}


	public void applyForce(Vector3f dir) {
		rigidBodyControl.applyImpulse(dir, Vector3f.ZERO);//.applyCentralForce(dir);
		//Settings.p("Bang!");
	}


}
