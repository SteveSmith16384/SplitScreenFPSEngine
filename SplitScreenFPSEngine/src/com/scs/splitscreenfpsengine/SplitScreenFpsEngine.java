package com.scs.splitscreenfpsengine;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.system.AppSettings;
import com.scs.splitscreenfpsengine.input.IControllerListener;
import com.scs.splitscreenfpsengine.input.JamepadControllerManager;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;
import com.scs.splitscreenfpsengine.modules.AbstractStartModule;
import com.scs.splitscreenfpsengine.modules.IModule;

public abstract class SplitScreenFpsEngine extends SimpleApplication implements IControllerListener {

	public static float MAX_TURN_SPEED = -1;

	private IModule currentModule, pendingModule;
	public static AppSettings settings;
	public static MultiplayerVoxelWorldProperties properties;
	public JamepadControllerManager controllerManager;;



	public abstract AbstractGameModule getGameModule();

	public abstract AbstractStartModule getStartModule();

	public int getNumPlayers() {
		return 1 + controllerManager.getNumControllers();
	}

	@Override
	public void simpleInitApp() {
		// Clear existing mappings
		getInputManager().clearMappings();
		getInputManager().clearRawInputListeners();

		stateManager.detach( stateManager.getState(FlyCamAppState.class) );
		stateManager.detach( stateManager.getState(StatsAppState.class) );

		controllerManager = new JamepadControllerManager(this);

		cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);

		if (Settings.RELEASE_MODE) {
			currentModule = this.getStartModule();// new StartModule(this, GameMode.Skirmish);
		} else {
			currentModule = this.getGameModule();
		}
		currentModule.init();

		if (Settings.RECORD_VID) {
			Settings.p("Recording video");
			VideoRecorderAppState video_recorder = new VideoRecorderAppState();
			stateManager.attach(video_recorder);
		}

	}


	@Override
	public void simpleUpdate(float tpf_secs) {
		if (tpf_secs > 1f) {
			tpf_secs = 1f;
		}

		if (this.pendingModule != null) {
			this.currentModule.destroy();
			this.rootNode.detachAllChildren();
			this.guiNode.detachAllChildren();

			// Remove existing lights
			getRootNode().getWorldLightList().clear();
			getRootNode().getLocalLightList().clear();

			this.currentModule = pendingModule;
			this.currentModule.init();
			pendingModule = null;
		}

		controllerManager.checkControllers();

		currentModule.update(tpf_secs);
	}


	public void setNextModule(IModule newModule) {
		pendingModule = newModule;
	}


	@Override
	public void newController(int idx) {
		if (currentModule != null) {
			currentModule.newController(idx);
		}
	}


	@Override
	public void controllerDisconnected(int idx) {
		if (currentModule != null) {
			currentModule.controllerDisconnected(idx);
		}		
	}

}
