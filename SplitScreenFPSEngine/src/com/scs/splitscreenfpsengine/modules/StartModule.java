package com.scs.splitscreenfpsengine.modules;

import java.util.List;

import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.input.JoystickButton;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;
import com.scs.splitscreenfpsengine.MultiplayerVoxelWorldMain;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.Settings.GameMode;


public class StartModule implements IModule, ActionListener, RawInputListener {

	private static final String START = "Start";
	private static final String QUIT = "Quit";

	protected MultiplayerVoxelWorldMain game;
	private Spatial robot;
	private AudioNode audioMusic;
	private GameMode gameMode;

	public StartModule(MultiplayerVoxelWorldMain _game, GameMode _gameMode) {
		super();

		game = _game;
		gameMode = _gameMode;
	}


	@Override
	public void init() {
		game.getCamera().setLocation(new Vector3f(0f, 0f, 10f));
		game.getCamera().lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);

		List<ViewPort> views = game.getRenderManager().getMainViews();
		while (!views.isEmpty()) {
			game.getRenderManager().removeMainView(views.get(0));
			views = game.getRenderManager().getMainViews();
		}

		// Create viewport
		Camera newCam = game.getCamera();
		newCam.resize(MultiplayerVoxelWorldMain.settings.getWidth(), MultiplayerVoxelWorldMain.settings.getHeight(), true);
		newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
		newCam.setViewPort(0f, 1f, 0f, 1f);

		final ViewPort view2 = game.getRenderManager().createMainView("viewport_" + newCam.toString(), newCam);
		view2.setBackgroundColor(new ColorRGBA(0, 0, 0, 0f));
		view2.setClearFlags(true, true, true);
		view2.attachScene(game.getRootNode());

		game.getInputManager().addMapping(QUIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		game.getInputManager().addListener(this, QUIT);
		game.getInputManager().addMapping(START, new KeyTrigger(KeyInput.KEY_0));
		game.getInputManager().addListener(this, START);
		for (int i=1 ; i<=6 ; i++) {
			game.getInputManager().addMapping(""+i, new KeyTrigger(KeyInput.KEY_1+i-1));
			game.getInputManager().addListener(this, ""+i);
		}

		// Lights
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(3));
		game.getRootNode().addLight(al);

		game.getInputManager().addRawInputListener(this);

		if (Settings.RELEASE_MODE) {
			Picture pic = new Picture("HUD Picture");
			pic.setImage(game.getAssetManager(), "Textures/text/multiplayerarena.png", true);
			pic.setWidth(game.getCamera().getWidth());
			pic.setHeight(game.getCamera().getWidth()/7);
			game.getGuiNode().attachChild(pic);
		}

		BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");

/*
		TrueTypeContainer screenText = ttfSmall.getFormattedText(new StringContainer(ttfSmall, "KILL THE N00BS!"), ColorRGBA.Green);
		//screenText.setColor(ColorRGBA.Green);
		screenText.setText(Settings.NAME +  " (version " + Settings.VERSION + ")\n\n" + gameMode.toString() + " Selected.\n\nSelect a different game mode, or press 0 to start:\n" +
				"1 - Skirmish\n" +
				"2 - King of the Hill\n" +
				"3 - Dodgeball\n" +
				"4 - Bladerunner\n" +
				"5 - Clone Wars");
		screenText.updateGeometry();
		screenText.setLocalTranslation(20, game.getCamera().getHeight()-20, 0);
		game.getGuiNode().attachChild(screenText);
*/
		/*
		TrueTypeContainer gameModeSpecificText = ttfSmall.getFormattedText(new StringContainer(ttfSmall, "KILL THE N00BS!"), ColorRGBA.Yellow);
		//gameModeSpecificText.setColor(ColorRGBA.Green);
		gameModeSpecificText.setLocalTranslation(20, game.getCamera().getHeight()-160, 0);
		game.getGuiNode().attachChild(gameModeSpecificText);
*/
		game.getRootNode().attachChild(robot);
		/*gameModeSpecificText.setText(gameMode.toString() + ": " + gameModeSpecificText.getText() + "\n\nThe winner is the first player to 100 points.");
		gameModeSpecificText.updateGeometry();
		
		TrueTypeContainer numPlayerText = ttfSmall.getFormattedText(new StringContainer(ttfSmall, "KILL THE N00BS!"), ColorRGBA.Magenta);
		numPlayerText.setText(game.getNumPlayers() + " player(s) found.");
		numPlayerText.updateGeometry();
		numPlayerText.setLocalTranslation(20, game.getCamera().getHeight()-280, 0);
		game.getGuiNode().attachChild(numPlayerText);
*/
		// Audio
		audioMusic = new AudioNode(game.getAssetManager(), "Sound/n-Dimensions (Main Theme - Retro Ver.ogg", true, false);
		//audioMusic.setLooping(true);  // activate continuous playing.  BROKEN!
		audioMusic.setPositional(false);
		audioMusic.setVolume(3);
		game.getRootNode().attachChild(audioMusic);
		audioMusic.play(); // play continuously!

	}


	@Override
	public void update(float tpf) {
		robot.getWorldTranslation();
		robot.rotate(0, tpf, 0);
	}


	@Override
	public void destroy() {
		audioMusic.stop();

		game.getInputManager().clearMappings();
		game.getInputManager().clearRawInputListeners();
		game.getInputManager().removeListener(this);

	}


	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (!value) {
			return;
		}

		if (name.equals(START)) {
			Settings.GAME_MODE = gameMode;
			switch (gameMode) {
			case Skirmish:
				/*Settings.NUM_SECTORS = 3;
				Settings.PVP = true;
				Settings.NUM_AI = 0;
				Settings.NUM_COLLECTABLES = 1;
				GameModule.HELP_TEXT = "Skirmish: Hunt the other players";*/
				startGame();
				break;
				
			case KingOfTheHill:
				// King of the Hill
				/*Settings.NUM_SECTORS = 3;
				Settings.PVP = true;
				Settings.NUM_AI = 0;
				Settings.NUM_COLLECTABLES = 0;*/
				AbstractGameModule.HELP_TEXT = "King of the Hill: Dominate the base";
				startGame();
				break;
				
			default:
				throw new RuntimeException("Unknown Game Mode: " + gameMode);
			}
		} else if (name.equals("1")) {
			game.setNextModule(new StartModule(game, GameMode.Skirmish));
		} else if (name.equals("2")) {
			game.setNextModule(new StartModule(game, GameMode.KingOfTheHill));
		} else if (name.equals("3")) {
			//game.setNextModule(new StartModule(game, GameMode.Dodgeball));
		} else if (name.equals("4")) {
			//game.setNextModule(new StartModule(game, GameMode.Bladerunner));
		} else if (name.equals("5")) {
			//game.setNextModule(new StartModule(game, GameMode.CloneWars));
		} else if (name.equals(QUIT)) {
			MultiplayerVoxelWorldMain.properties.saveProperties();
			game.stop();
		}
	}


	private void startGame() {
		game.setNextModule(game.getGameModule());// new GameModule(game, new TowerDefence()));

	}
	
	
	// Raw Input Listener ------------------------

	@Override
	public void onJoyAxisEvent(JoyAxisEvent evt) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme3.input.RawInputListener#onJoyButtonEvent(com.jme3.input.event.JoyButtonEvent)
	 * 1 = X
	 * 2 = O
	 * 5 = R1
	 * 7 = R2
	 */
	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {
		JoystickButton button = evt.getButton();
		//Settings.p("button.getButtonId()=" + button.getButtonId());
		if (button.getButtonId() > 0) {
			//startGame();
		}
	}

	public void beginInput() {}
	public void endInput() {}
	public void onMouseMotionEvent(MouseMotionEvent evt) {}
	public void onMouseButtonEvent(MouseButtonEvent evt) {}
	public void onKeyEvent(KeyInputEvent evt) {}
	public void onTouchEvent(TouchEvent evt) {}

	// End of Raw Input Listener


	@Override
	public void newController() {
		Settings.p("New controller!");
		
	}


	@Override
	public void controllerDisconnected() {
		Settings.p("Controller Disconnected!");
		
	}


}
