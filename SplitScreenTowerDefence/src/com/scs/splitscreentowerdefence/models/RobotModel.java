package com.scs.splitscreentowerdefence.models;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.scs.splitscreenfpsengine.components.IAvatarModel;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar.Anim;
import com.scs.splitscreenfpsengine.jme.JMEModelFunctions;

public class RobotModel extends Node implements IAvatarModel {
	
	public RobotModel(AssetManager assetManager, int playerid) {
		super("RobotModel");
		
		Spatial model = assetManager.loadModel("Models/AbstractRTSModels/Player.obj");
		model.scale(0.3f);
		
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		//Texture t = assetManager.loadTexture("Textures/sun.jpg");
		//Texture t = assetManager.loadTexture("Textures/cells3.png");
		Texture t = null;
		switch (playerid) {
		case 0:
			t = assetManager.loadTexture("Textures/robot_green.png");
			break;
		case 1:
			t = assetManager.loadTexture("Textures/robot_yellow.png");
			break;
		case 2:
			t = assetManager.loadTexture("Textures/robot_purple.png");
			break;
		case 3:
			t = assetManager.loadTexture("Textures/robot_red.png");
			break;
		}
		mat.setTexture("DiffuseMap", t);
		
	    //this.setMaterial(mat);
		JMEModelFunctions.setMaterialOnSpatial(model, mat);

	    this.attachChild(model);
	}

	@Override
	public Spatial getModel() {
		return this;
	}

	
	@Override
	public void setAvatarAnim(Anim anim) {
		// Do nothing, no anims
		
	}

}
