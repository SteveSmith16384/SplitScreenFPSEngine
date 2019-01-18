package com.scs.multiplayervoxelworld;

import com.atr.jme.font.asset.TrueTypeLoader;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.Joystick;
import com.jme3.system.AppSettings;
import com.scs.multiplayervoxelworld.modules.GameModule;
import com.scs.multiplayervoxelworld.modules.IModule;

public abstract class MultiplayerVoxelWorldMain extends SimpleApplication {

	private static final String PROPS_FILE = Settings.NAME.replaceAll(" ", "") + "_settings.txt";
	public static float MAX_TURN_SPEED = -1;
	public static float BASE_SCORE_INC = 0.005f;

	private IModule currentModule, pendingModule;
	//public static BitmapFont guiFont_small; // = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
	public static AppSettings settings;
	public static MultiplayerVoxelWorldProperties properties;
	/*
	public static void main(String[] args) {
		try {
			properties = new MultiplayerVoxelWorldProperties(PROPS_FILE);
			settings = new AppSettings(true);
			try {
				settings.load(Settings.NAME);
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			settings.setUseJoysticks(true);
			settings.setTitle(Settings.NAME + " (v" + Settings.VERSION + ")");
			if (Settings.RELEASE_MODE) {
				//todo settings.setSettingsDialogImage("Textures/text/multiplayerarena.png");
			} else {
				settings.setSettingsDialogImage(null);
			}

			MAX_TURN_SPEED = MultiplayerVoxelWorldMain.properties.GetMaxTurnSpeed();
			BASE_SCORE_INC = MultiplayerVoxelWorldMain.properties.GetBaseScoreInc();
			
			MultiplayerVoxelWorldMain app = new MultiplayerVoxelWorldMain();
			app.setSettings(settings);
			//app.setPauseOnLostFocus(true);

			/*File video, audio;
			if (Settings.RECORD_VID) {
				//app.setTimer(new IsoTimer(60));
				video = File.createTempFile("JME-water-video", ".avi");
				audio = File.createTempFile("JME-water-audio", ".wav");
				Capture.captureVideo(app, video);
				Capture.captureAudio(app, audio);
			}*/

			/*if (Settings.RECORD_VID) {
				System.out.println("Video saved at " + video.getCanonicalPath());
				System.out.println("Audio saved at " + audio.getCanonicalPath());
			}*/
/*
			try {
				settings.save(Settings.NAME);
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}

			app.start();

		} catch (Exception e) {
			Settings.p("Error: " + e);
			e.printStackTrace();
		}

	}

*/

	public abstract GameModule getGameModule();


	public int getNumPlayers() {
		Joystick[] joysticks = getInputManager().getJoysticks();
		int numPlayers = (Settings.PLAYER1_IS_MOUSE ? 1 : 0) +joysticks.length;
		return numPlayers;
	}
	
	
	@Override
	public void simpleInitApp() {
		getAssetManager().registerLoader(TrueTypeLoader.class, "ttf");

		// Clear existing mappings
		getInputManager().clearMappings();
		getInputManager().clearRawInputListeners();
		
		//guiFont_small = getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		
		cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);
/*
		if (Settings.RELEASE_MODE) {
			currentModule = new StartModule(this, GameMode.Skirmish);
		} else {
			TowerDefence td = new TowerDefence();
			currentModule = new GameModule(this, new TowerDefence());
			td.setGameModule((GameModule)currentModule);
		}*/
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
		
		currentModule.update(tpf_secs);
	}


	public void setNextModule(IModule newModule) {
		pendingModule = newModule;
	}
	
	
}
