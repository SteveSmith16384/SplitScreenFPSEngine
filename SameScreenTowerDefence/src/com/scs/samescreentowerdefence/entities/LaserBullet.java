package com.scs.samescreentowerdefence.entities;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.components.ICausesHarmOnContact;
import com.scs.multiplayervoxelworld.components.INotifiedOfCollision;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;
import com.scs.multiplayervoxelworld.entities.CubeExplosionShard;
import com.scs.multiplayervoxelworld.entities.AbstractPlayersAvatar;
import com.scs.multiplayervoxelworld.models.BeamLaserModel;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;

public class LaserBullet extends AbstractPhysicalEntity implements ICausesHarmOnContact, IProcessable, PhysicsTickListener, INotifiedOfCollision {

	public ICanShoot shooter;
	private float timeLeft = 3;
	private boolean forceApplied = false;

	public LaserBullet(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, ICanShoot _shooter) {
		super(_game, _module, "LaserBullet");

		this.shooter = _shooter;

		Vector3f origin = shooter.getBulletStartPosition().clone();

		Node laserBeam = BeamLaserModel.Factory(game.getAssetManager(), origin, origin.add(shooter.getShootDir().multLocal(1)), ColorRGBA.Pink);

		this.mainNode.attachChild(laserBeam);
		laserBeam.setLocalTranslation(origin.add(shooter.getShootDir().multLocal(AbstractPlayersAvatar.PLAYER_RAD*3)));
		laserBeam.getLocalTranslation().y -= 0.1f; // Drop bullets slightly
		
		rigidBodyControl = new RigidBodyControl(.1f);
		mainNode.addControl(rigidBodyControl);
		//rigidBodyControl.setGravity(Vector3f.ZERO);

		laserBeam.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);
		
		module.addEntity(this);

		AudioNode audio_gun = new AudioNode(game.getAssetManager(), "Sound/laser3.wav", false);
		audio_gun.setPositional(false);
		audio_gun.setLooping(false);
		audio_gun.setVolume(2);
		this.getMainNode().attachChild(audio_gun);
		audio_gun.play();

	}


	@Override
	public void process(float tpf) {
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
			//Settings.p("Laser collided with " + other);

			CubeExplosionShard.Factory(game, module, this.getLocation(), 3);

			module.audioSmallExplode.play();

			this.markForRemoval(); // Don't bounce
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
