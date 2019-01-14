package com.scs.multiplayervoxelworld.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.components.IAffectedByPhysics;
import com.scs.multiplayervoxelworld.components.IShowOnHUD;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class Collectable extends AbstractPhysicalEntity implements IShowOnHUD, IAffectedByPhysics {

	private static final float SIZE = .1f;

	public boolean collected = false;

	public Collectable(MultiplayerVoxelWorldMain _game, GameModule _module, float x, float y, float z) {
		super(_game, _module, "Collectable");

		Box box1 = new Box(SIZE, SIZE, SIZE);
		Geometry geometry = new Geometry("Collectable", box1);
		TextureKey key3 = new TextureKey("Textures/greensun.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		geometry.setShadowMode(ShadowMode.CastAndReceive);
		
		Material floor_mat = null;
		floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		floor_mat.setTexture("DiffuseMap", tex3);
		geometry.setMaterial(floor_mat);

		this.mainNode.attachChild(geometry);
		mainNode.setLocalTranslation(x, y, z); // Drop from sky

		rigidBodyControl = new RigidBodyControl(0.1f);
		mainNode.addControl(rigidBodyControl);

		//module.bulletAppState.getPhysicsSpace().add(rigidBodyControl);

		geometry.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

		rigidBodyControl.setRestitution(.5f);

		module.addEntity(this); // need this to target it

		Settings.p("Created collectable");

	}


}
