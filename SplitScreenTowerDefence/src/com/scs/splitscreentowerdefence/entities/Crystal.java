package com.scs.splitscreentowerdefence.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.components.IProcessable;
import com.scs.splitscreenfpsengine.components.IShowOnHUD;
import com.scs.splitscreenfpsengine.entities.AbstractPhysicalEntity;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class Crystal extends AbstractPhysicalEntity implements IShowOnHUD, IProcessable {

	//private static final float SIZE = .1f;

	public Crystal(SplitScreenFpsEngine _game, AbstractGameModule _module, Vector3f pos) {
		super(_game, _module, "Crystal");

		Spatial model = game.getAssetManager().loadModel("Models/mese-crystal/mese.blend");
		//model.setLocalScale(3);
		model.setShadowMode(ShadowMode.CastAndReceive);
		
		/*Material floor_mat = null;
		floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		floor_mat.setTexture("DiffuseMap", tex3);
		geometry.setMaterial(floor_mat);
*/
		this.mainNode.attachChild(model);
		mainNode.setLocalTranslation(pos);

		rigidBodyControl = new RigidBodyControl(0f);
		mainNode.addControl(rigidBodyControl);

		model.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

	}

	@Override
	public void process(float tpfSecs) {
		// TODO - rotate
		
	}


}
