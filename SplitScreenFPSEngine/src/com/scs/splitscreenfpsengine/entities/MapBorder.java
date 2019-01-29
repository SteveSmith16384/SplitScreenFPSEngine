package com.scs.splitscreenfpsengine.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class MapBorder extends AbstractPhysicalEntity {

	private static final boolean INVISIBLE = true;

	public Geometry geometry;

	public MapBorder(SplitScreenFpsEngine _game, AbstractGameModule _module, float x, float yBottom, float z, float w, float h, float d, String tex) {
		super(_game, _module, "MapBorder");

		Box box1 = new Box(w/2, h/2, d/2);

		box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{
				0, h, w, h, w, 0, 0, 0, // back
				0, h, d, h, d, 0, 0, 0, // right
				0, h, w, h, w, 0, 0, 0, // front
				0, h, d, h, d, 0, 0, 0, // left
				w, 0, w, d, 0, d, 0, 0, // top
				w, 0, w, d, 0, d, 0, 0  // bottom
		}));

		geometry = new Geometry("FloorGeom", box1);
		if (INVISIBLE) {
			geometry.setCullHint(CullHint.Always);
		} else {
			geometry.setShadowMode(ShadowMode.Receive);

			TextureKey key3 = new TextureKey(tex);
			key3.setGenerateMips(true);
			Texture tex3 = game.getAssetManager().loadTexture(key3);
			tex3.setWrap(WrapMode.Repeat);

			Material floorMat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floorMat.setTexture("DiffuseMap", tex3);
			geometry.setMaterial(floorMat);
		}

		geometry.setLocalTranslation(w/2, h/2, d/2); // Move it into position

		this.mainNode.attachChild(geometry);
		mainNode.setLocalTranslation(x, yBottom, z); // Move it into position

		super.rigidBodyControl = new RigidBodyControl(0);
		mainNode.addControl(rigidBodyControl);

		geometry.setUserData(Settings.ENTITY, this);

	}


}
