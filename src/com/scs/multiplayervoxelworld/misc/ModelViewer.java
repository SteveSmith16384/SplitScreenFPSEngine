package com.scs.multiplayervoxelworld.misc;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.scs.multiplayervoxelworld.jme.JMEModelFunctions;

public class ModelViewer extends SimpleApplication { // todo - copy SteveTech version

	public static void main(String[] args){
		ModelViewer app = new ModelViewer();
		app.showSettings = false;

		app.start();
	}


	@Override
	public void simpleInitApp() {
		super.getViewPort().setBackgroundColor(ColorRGBA.Black);

		cam.setFrustumPerspective(60, settings.getWidth() / settings.getHeight(), .1f, 100);
		
		setupLight();

		Node model = (Node)assetManager.loadModel("Models/Turret_0/Base1.blend");
		((Node)model.getChild(0)).getChild(0).removeFromParent();
		JMEModelFunctions.setTextureOnSpatial(assetManager, model, "Models/Turret_0/Turret1_Albedo.png");
		//model.setQueueBucket(Bucket.Transparent);
		
		//model.setModelBound(new BoundingBox());
		//model.updateModelBound();

		rootNode.attachChild(model);

		this.rootNode.attachChild(JMEModelFunctions.getGrid(assetManager, 10));

		this.flyCam.setMoveSpeed(12f);

		//rootNode.updateGeometricState();

	}


	private void setupLight() {
		// Remove existing lights
		this.rootNode.getWorldLightList().clear();
		LightList list = this.rootNode.getWorldLightList();
		for (Light it : list) {
			this.rootNode.removeLight(it);
		}

		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		rootNode.addLight(al);

		DirectionalLight dirlight = new DirectionalLight(); // FSR need this for textures to show
		dirlight.setColor(ColorRGBA.White);
		rootNode.addLight(dirlight);

	}


	@Override
	public void simpleUpdate(float tpf) {
		//System.out.println("Pos: " + this.cam.getLocation());
		//this.rootNode.rotate(0,  tpf,  tpf);
	}


}