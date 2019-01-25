package com.scs.splitscreenfpsengine.entities;

import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.scs.splitscreenfpsengine.MultiplayerVoxelWorldMain;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class DebuggingSphere extends AbstractPhysicalEntity {

	public DebuggingSphere(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, Vector3f pos) {
		super(_game, _module, "DebuggingSphere");

		Mesh sphere = new Sphere(8, 8, .2f, true, false);
		Geometry ball_geo = new Geometry("DebuggingSphere", sphere);
		ball_geo.setShadowMode(ShadowMode.Cast);
		TextureKey key = new TextureKey( "Textures/greensun.jpg");
		Texture tex = game.getAssetManager().loadTexture(key);
		Material mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");
		mat.setTexture("DiffuseMap", tex);
		ball_geo.setMaterial(mat);

		this.mainNode.attachChild(ball_geo);
		mainNode.setLocalTranslation(pos);
		
		module.addEntity(this);

		//super.rigidBodyControl = new RigidBodyControl(0);
		//mainNode.addControl(rigidBodyControl);
		//geometry.setUserData(Settings.ENTITY, this);

	}


}
