package com.scs.splitscreenfpsengine.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.components.ICanShoot;
import com.scs.splitscreenfpsengine.components.ICausesHarmOnContact;
import com.scs.splitscreenfpsengine.components.INotifiedOfCollision;
import com.scs.splitscreenfpsengine.components.IProcessable;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public abstract class AbstractBullet extends AbstractPhysicalEntity implements ICausesHarmOnContact, IProcessable, PhysicsTickListener, INotifiedOfCollision {

	public ICanShoot shooter;
	private float timeLeft = 6;
	private boolean forceApplied = false;
	private float damage;
	
	public AbstractBullet(SplitScreenFpsEngine _game, AbstractGameModule _module, String name, ICanShoot _shooter, float _damage) {
		super(_game, _module, name);

		this.shooter = _shooter;
		damage = _damage;
		
		Vector3f origin = shooter.getBulletStartPosition().clone();

		Spatial bullet = createBulletModel();

		this.mainNode.attachChild(bullet);
		
		// Add physical sphere
		Mesh sphere = new Sphere(8, 8, .2f, true, false);
		Geometry ball_geo = new Geometry("BulletSphere", sphere);
		ball_geo.setCullHint(CullHint.Always);
		this.mainNode.attachChild(ball_geo);
		
		mainNode.setLocalTranslation(origin.add(shooter.getShootDir().multLocal(shooter.getRadius()*2)));
		mainNode.getLocalTranslation().y -= 0.1f; // Drop bullets slightly

		rigidBodyControl = new RigidBodyControl(.1f);
		mainNode.addControl(rigidBodyControl);

		bullet.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);
		rigidBodyControl.setGravity(Vector3f.ZERO);

		module.addEntity(this);

		/*
		AudioNode audio_gun = new AudioNode(game.getAssetManager(), "Sound/laser3.wav", false);
		audio_gun.setPositional(false);
		audio_gun.setLooping(false);
		audio_gun.setVolume(2);
		this.getMainNode().attachChild(audio_gun);
		audio_gun.play();
*/
	}

	protected abstract Spatial createBulletModel();


	@Override
	public void process(float tpf) {
		if (Settings.DEBUG_FIREBALL_POS) { //this.mainNode
			new DebuggingSphere(game, module, this.getLocation());
		}
		this.timeLeft -= tpf;
		if (this.timeLeft < 0) {
			this.markForRemoval();
		}
	}


	@Override
	public ICanShoot getShooter() {
		return shooter;
	}


	@Override
	public void notifiedOfCollision(AbstractPhysicalEntity other) {
		if (other != this.shooter) {
			Settings.p(this + " collided with " + other + ", creating explosion at " + this.getLocation());
			//CubeExplosionShard.Factory(game, module, this.getLocation(), 3);
			//module.audioSmallExplode.play();
			new ParticleExplosion(game, module, this.getLocation());
			this.markForRemoval(); // Don't bounce
			
			// Make hole in walls
			if (other instanceof VoxelTerrainEntity) {
				VoxelTerrainEntity vte = (VoxelTerrainEntity)other;
				vte.removeSphereRange_Actual(this.getLocation(), 1f);
			}
		}
	}


	@Override
	public float getDamageCaused() {
		return damage;
	}


	@Override
	public void prePhysicsTick(PhysicsSpace space, float tpf) {
		if (!forceApplied) {
			forceApplied = true;
			rigidBodyControl.setLinearVelocity(shooter.getShootDir().mult(35));//40));
		}

	}


	@Override
	public void physicsTick(PhysicsSpace space, float tpf) {
		// Do nothing
	}


	@Override
	public int getSide() {
		return shooter.getSide();
	}



}
