package com.scs.splitscreenfpsengine.entities;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class TerrainEntity extends AbstractPhysicalEntity {

	//private TerrainQuad terrain;
	Material mat_terrain;

	public TerrainEntity(SplitScreenFpsEngine _game, AbstractGameModule _module, int mapSize) {
		super(_game, _module, "Terrain");

		AssetManager assetManager = game.getAssetManager();

		/** 1. Create terrain material and load four textures into it. */
		mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");

		/** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
		mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));

		/** 1.2) Add GRASS texture into the red layer (Tex1). */
		Texture grass = assetManager.loadTexture("Textures/blocks/lavarock.jpg");//Terrain/splat/grass.jpg"); // todo - pass the textures as a param 
		grass.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex1", grass);
		mat_terrain.setFloat("Tex1Scale", 64f);

		/** 1.3) Add DIRT texture into the green layer (Tex2) */
		Texture dirt = assetManager.loadTexture("Textures/blocks/lavarock.jpg");//Terrain/splat/dirt.jpg");
		dirt.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex2", dirt);
		mat_terrain.setFloat("Tex2Scale", 32f);

		/** 1.4) Add ROAD texture into the blue layer (Tex3) */
		/*Texture rock = assetManager.loadTexture("Textures/blocks/lavarock.jpg");//Terrain/splat/road.jpg");
		rock.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex3", rock);
		mat_terrain.setFloat("Tex3Scale", 128f);
*/
		/** 2. Create the height map */
		AbstractHeightMap heightmap = null;
		Texture heightMapImage = assetManager.loadTexture("Textures/blocks/lavarock.jpg");//Terrain/splat/mountains512.png");
		heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
		heightmap.load();

		/** 3. We have prepared material and heightmap.
		 * Now we create the actual terrain:
		 * 3.1) Create a TerrainQuad and name it "my terrain".
		 * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
		 * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
		 * 3.4) As LOD step scale we supply Vector3f(1,1,1).
		 * 3.5) We supply the prepared heightmap itself.
		 */
		int patchSize = (mapSize)/2;//16;
		mainNode = new TerrainQuad("my terrain", patchSize, mapSize+1, heightmap.getHeightMap());
		/** 4. We give the terrain its material, position & scale it, and attach it. */
		mainNode.setMaterial(mat_terrain);
		mainNode.setLocalTranslation(mapSize/2, 0, mapSize/2);
		mainNode.setLocalScale(1f, .01f, 1f);
		mainNode.setShadowMode(ShadowMode.CastAndReceive);
		//mainNode.getWorldBound()

		CollisionShape terrainShape = CollisionShapeFactory.createMeshShape((Node) mainNode);
		rigidBodyControl = new RigidBodyControl(terrainShape, 0);
		mainNode.addControl(rigidBodyControl);

		/** 5. The LOD (level of detail) depends on were the camera is: */
		//TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
		//terrain.addControl(control);
		
		mainNode.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

	}

}
