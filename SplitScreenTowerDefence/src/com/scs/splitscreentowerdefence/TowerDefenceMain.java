package com.scs.splitscreentowerdefence;

import java.util.prefs.BackingStoreException;

import com.jme3.system.AppSettings;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.MultiplayerVoxelWorldProperties;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;
import com.scs.splitscreenfpsengine.modules.AbstractStartModule;

public class TowerDefenceMain extends SplitScreenFpsEngine {

	private static final String PROPS_FILE = "chaos_settings.txt";
	
	public static void main(String[] args) {
		try {
			properties = new MultiplayerVoxelWorldProperties(PROPS_FILE);
			settings = new AppSettings(true);
			try {
				settings.load(Settings.NAME);
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			//settings.setUseJoysticks(true);
			settings.setTitle(Settings.NAME + " (v" + Settings.VERSION + ")");
			if (Settings.RELEASE_MODE) {
				//todo settings.setSettingsDialogImage("Textures/text/multiplayerarena.png");
			} else {
				settings.setSettingsDialogImage(null);
			}

			MAX_TURN_SPEED = SplitScreenFpsEngine.properties.GetMaxTurnSpeed();
			//BASE_SCORE_INC = MultiplayerVoxelWorldMain.properties.GetBaseScoreInc();
			
			TowerDefenceMain app = new TowerDefenceMain();
			app.setSettings(settings);

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


	public TowerDefenceMain() {
		super();
	}


	@Override
	public AbstractGameModule getGameModule() {
		return new TowerDefenceGameModule(this);
	}


	@Override
	public AbstractStartModule getStartModule() {
		return new TowerDefenceStartModule(this);
	}

}
