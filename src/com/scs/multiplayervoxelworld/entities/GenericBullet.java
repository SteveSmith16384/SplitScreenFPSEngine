package com.scs.multiplayervoxelworld.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class GenericBullet extends AbstractPhysicalEntity {

	public ICanShoot shooter;
	//private RigidBodyControl ball_phy;
	private float timeLeft;
	
	public GenericBullet(MultiplayerVoxelWorldMain _game, GameModule _module, ICanShoot _shooter, String tex, float mass, float speed, float dur, float rad) {
		super(_game, _module, "AbstractBullet");

		this.shooter = _shooter;
		this.timeLeft = dur;
		
		Sphere sphere = new Sphere(8, 8, rad, true, false);
		sphere.setTextureMode(TextureMode.Projected);
		/** Create a cannon ball geometry and attach to scene graph. */
		Geometry ball_geo = new Geometry("cannon ball", sphere);

		TextureKey key3 = new TextureKey(tex);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		Material floor_mat = null;
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floor_mat.setTexture("DiffuseMap", tex3);
		ball_geo.setMaterial(floor_mat);

		this.mainNode.attachChild(ball_geo);
		game.getRootNode().attachChild(this.mainNode);
		/** Position the cannon ball  */
		ball_geo.setLocalTranslation(shooter.getLocation().add(shooter.getShootDir().multLocal(PlayersAvatar.PLAYER_RAD*2)));
		/** Make the ball physical with a mass > 0.0f */
		rigidBodyControl = new RigidBodyControl(mass);
		/** Add physical ball to physics space. */
		ball_geo.addControl(rigidBodyControl);
		module.getBulletAppState().getPhysicsSpace().add(rigidBodyControl);
		/** Accelerate the physical ball to shoot it. */
		rigidBodyControl.setLinearVelocity(shooter.getShootDir().mult(speed));
		
		this.getMainNode().setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

	}

	
	@Override
	public void process(float tpf) {
		this.timeLeft -= tpf;
		if (this.timeLeft < 0) {
			//Settings.p("Bullet removed");
			this.markForRemoval();
		}
		
	}


}
