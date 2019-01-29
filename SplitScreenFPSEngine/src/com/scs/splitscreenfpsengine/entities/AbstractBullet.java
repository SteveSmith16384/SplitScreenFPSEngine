package com.scs.splitscreenfpsengine.entities;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
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

	public AbstractBullet(SplitScreenFpsEngine _game, AbstractGameModule _module, String name, ICanShoot _shooter) {
		super(_game, _module, name);

		this.shooter = _shooter;

		Vector3f origin = shooter.getBulletStartPosition().clone();

		Spatial bullet = createBulletModel();// BeamLaserModel.Factory(game.getAssetManager(), origin, origin.add(shooter.getShootDir().multLocal(1)), ColorRGBA.Pink);

		this.mainNode.attachChild(bullet);
		mainNode.setLocalTranslation(origin.add(shooter.getShootDir().multLocal(shooter.getRadius())));
		mainNode.getLocalTranslation().y -= 0.1f; // Drop bullets slightly

		rigidBodyControl = new RigidBodyControl(.1f);
		mainNode.addControl(rigidBodyControl);
		//rigidBodyControl.setGravity(Vector3f.ZERO);

		bullet.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

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
		if (Settings.DEBUG_FIREBALL_POS) {
			//new DebuggingSphere(game, module, this.getLocation());
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
			Settings.p(this + " collided with " + other);
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
		return 1;
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

	}


	@Override
	public int getSide() {
		return shooter.getSide();
	}



}
