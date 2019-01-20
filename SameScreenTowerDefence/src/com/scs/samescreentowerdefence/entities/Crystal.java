package com.scs.samescreentowerdefence.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.components.IAffectedByPhysics;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.components.IShowOnHUD;
import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;

public class Crystal extends AbstractPhysicalEntity implements IShowOnHUD, IProcessable {

	//private static final float SIZE = .1f;

	public Crystal(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, Vector3f pos) {
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
