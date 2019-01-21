package com.scs.multiplayervoxelworld.modules;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.scs.multiplayervoxelworld.CollisionLogic;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.Settings.GameMode;
import com.scs.multiplayervoxelworld.components.IAffectedByPhysics;
import com.scs.multiplayervoxelworld.components.IEntity;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;
import com.scs.multiplayervoxelworld.entities.AbstractPlayersAvatar;
import com.scs.multiplayervoxelworld.entities.CubeExplosionShard;
import com.scs.multiplayervoxelworld.entities.VoxelTerrainEntity;
import com.scs.multiplayervoxelworld.entities.nonphysical.ChangeBlocksInSweep;
import com.scs.multiplayervoxelworld.hud.HUD;
import com.scs.multiplayervoxelworld.input.IInputDevice;
import com.scs.multiplayervoxelworld.input.JoystickCamera;
import com.scs.multiplayervoxelworld.input.MouseAndKeyboardCamera;

public abstract class AbstractGameModule implements IModule, PhysicsCollisionListener, ActionListener {

	private static final String QUIT = "Quit";
	private static final String TEST = "Test";

	public static String HELP_TEXT = "";

	protected MultiplayerVoxelWorldMain game;

	public List<IEntity> entities = new ArrayList<IEntity>();
	private List<IProcessable> entitiesForProcessing = new ArrayList<IProcessable>();
	private List<IEntity> entitiesToAdd = new LinkedList<>();
	private List<IEntity> entitiesToRemove = new LinkedList<>();
	public BulletAppState bulletAppState;

	public AudioNode audioExplode, audioSmallExplode;
	private AudioNode audioMusic;
	public DirectionalLight sun;
	//public AbstractGame level;
	private boolean gameOver = false;

	public AbstractGameModule(MultiplayerVoxelWorldMain _game) {
		super();

		game = _game;
	}


	@Override
	public void init() {
		game.getInputManager().addMapping(QUIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		game.getInputManager().addListener(this, QUIT);            

		game.getInputManager().addMapping(TEST, new KeyTrigger(KeyInput.KEY_T));
		game.getInputManager().addListener(this, TEST);            

		// Set up Physics
		bulletAppState = new BulletAppState();
		game.getStateManager().attach(bulletAppState);
		bulletAppState.getPhysicsSpace().addCollisionListener(this);
		//bulletAppState.getPhysicsSpace().enableDebug(game.getAssetManager());

		game.getRenderManager().removeMainView(game.getViewPort()); // Since we create new ones for each player

		setUpLight();
		setupLevel();

		Joystick[] joysticks = game.getInputManager().getJoysticks();
		int numPlayers = game.getNumPlayers();

		// Auto-Create player 0
		{
			Camera newCam = this.createCamera(0, numPlayers);
			IInputDevice input = null;
			if (Settings.PLAYER1_IS_MOUSE) {
				input = new MouseAndKeyboardCamera(newCam, game.getInputManager());
			} else {
				if (joysticks.length > 0) {
					input = new JoystickCamera(newCam, joysticks[0], game.getInputManager());
				} else {
					throw new RuntimeException("No gamepads found");
				}
			}
			AbstractPlayersAvatar player = this.addPlayersAvatar(0, newCam, input, 0);
			HUD hud = this.createHUD(newCam, player);
			player.setHUD(hud);
		}

		// Create players for each joystick
		int joyid = Settings.PLAYER1_IS_MOUSE ? 0 : 1;
		int playerid = Settings.PLAYER1_IS_MOUSE ? 1 : 0;
		if (joysticks != null && joysticks.length > 0) {
			while (joyid < joysticks.length) {
				Camera newCam = this.createCamera(playerid, numPlayers);
				JoystickCamera joyCam = new JoystickCamera(newCam, joysticks[joyid], game.getInputManager());
				AbstractPlayersAvatar player = this.addPlayersAvatar(playerid, newCam, joyCam, 0);
				HUD hud = this.createHUD(newCam, player);
				player.setHUD(hud);

				joyid++;
				playerid++;
			}
		}
		playerid++;

		/*
		if (Settings.ALWAYS_SHOW_4_CAMS) {
			// Create extra cameras
			for (int id=playerid ; id<=3 ; id++) {
				Camera cam = this.createCamera(id, numPlayers);
				this.createHUD(cam, id);
				switch (id) {
				case 1:
					cam.setLocation(new Vector3f(2f, PlayersAvatar.PLAYER_HEIGHT, 2f));
					break;
				case 2:
					cam.setLocation(new Vector3f(mapData.getWidth()-3, PlayersAvatar.PLAYER_HEIGHT, 2f));
					break;
				case 3:
					cam.setLocation(new Vector3f(2f, PlayersAvatar.PLAYER_HEIGHT, mapData.getDepth()-3));
					break;
				}
				cam.lookAt(new Vector3f(mapData.getWidth()/2, PlayersAvatar.PLAYER_HEIGHT, mapData.getDepth()/2), Vector3f.UNIT_Y);
			}
		}
		 */

		audioExplode = new AudioNode(game.getAssetManager(), "Sound/explode.wav", false);
		audioExplode.setPositional(false);
		audioExplode.setLooping(false);
		//audio_gun.setVolume(2);
		game.getRootNode().attachChild(audioExplode);

		audioSmallExplode = new AudioNode(game.getAssetManager(), "Sound/explodemini.wav", false);
		audioSmallExplode.setPositional(false);
		audioSmallExplode.setLooping(false);
		//audio_gun.setVolume(2);
		game.getRootNode().attachChild(audioSmallExplode);

		// Audio
		audioMusic = new AudioNode(game.getAssetManager(), "Sound/n-Dimensions (Main Theme - Retro Ver.ogg", true, false);
		audioMusic.setLooping(true);  // activate continuous playing
		audioMusic.setPositional(false);
		audioMusic.setVolume(3);
		game.getRootNode().attachChild(audioMusic);
		//audioMusic.play(); // play continuously! - todo - re-add?

		//this.getRootNode().attachChild(SkyFactory.createSky(game.getAssetManager(), "Textures/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));

	}


	protected abstract void setupLevel();

	public abstract Vector3f getPlayerStartPos(int id);


	private Camera createCamera(int id, int numPlayers) {
		Camera newCam = null;
		if (id == 0) {
			newCam = game.getCamera();
		} else {
			newCam = game.getCamera().clone();
		}

		if (Settings.ALWAYS_SHOW_4_CAMS || numPlayers > 2) {
			newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			switch (id) { // left/right/bottom/top, from bottom-left!
			case 0: // TL
				newCam.setViewPort(0f, 0.5f, 0.5f, 1f);
				newCam.setName("Cam_TL");
				break;
			case 1: // TR
				newCam.setViewPort(0.5f, 1f, 0.5f, 1f);
				newCam.setName("Cam_TR");
				break;
			case 2: // BL
				newCam.setViewPort(0f, 0.5f, 0f, .5f);
				newCam.setName("Cam_BL");
				break;
			case 3: // BR
				newCam.setViewPort(0.5f, 1f, 0f, .5f);
				newCam.setName("Cam_BR");
				break;
			default:
				throw new RuntimeException("Unknown player id: " + id);
			}
		} else if (numPlayers == 2) {
			newCam.setFrustumPerspective(45f, (float) (newCam.getWidth()*2) / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			switch (id) { // left/right/bottom/top, from bottom-left!
			case 0: // TL
				//Settings.p("Creating camera top");
				newCam.setViewPort(0f, 1f, 0.5f, 1f);
				newCam.setName("Cam_Top");
				break;
			case 1: // TR
				//Settings.p("Creating camera bottom");
				newCam.setViewPort(0.0f, 1f, 0f, .5f);
				newCam.setName("Cam_bottom");
				break;
			default:
				throw new RuntimeException("Unknown player id: " + id);
			}
		} else if (numPlayers == 1) {
			newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			//Settings.p("Creating full-screen camera");
			newCam.setViewPort(0f, 1f, 0f, 1f);
			newCam.setName("Cam_FullScreen");
		} else {
			throw new RuntimeException("Unknown number of players");

		}

		final ViewPort view2 = game.getRenderManager().createMainView("viewport_" + newCam.toString(), newCam);
		view2.setBackgroundColor(ColorRGBA.Black);
		view2.setClearFlags(true, true, true);
		view2.attachScene(game.getRootNode());

		// Add shadows to this viewport
		final int SHADOWMAP_SIZE = 2048;
		DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(game.getAssetManager(), SHADOWMAP_SIZE, 2);
		dlsr.setLight(sun);
		view2.addProcessor(dlsr);

		// DepthOfFieldFilter
		FilterPostProcessor fpp = new FilterPostProcessor(game.getAssetManager());
		DepthOfFieldFilter dff = new DepthOfFieldFilter(); 
		fpp.addFilter(dff);
		view2.addProcessor(fpp);

		return newCam;
	}


	private HUD createHUD(Camera c, AbstractPlayersAvatar player) {
		// HUD coords are full screen co-ords!
		// cam.getWidth() = 640x480, cam.getViewPortLeft() = 0.5f
		float xBL = c.getWidth() * c.getViewPortLeft();
		//float y = (c.getHeight() * c.getViewPortTop())-(c.getHeight()/2);
		float yBL = c.getHeight() * c.getViewPortBottom();

		Settings.p("Created HUD for " + player + ": " + xBL + "," +yBL);

		float w = c.getWidth() * (c.getViewPortRight()-c.getViewPortLeft());
		float h = c.getHeight() * (c.getViewPortTop()-c.getViewPortBottom());
		HUD hud = new HUD(game, this, player, xBL, yBL, w, h, c);
		//game.getGuiNode().attachChild(hud);
		return hud;

	}


	private AbstractPlayersAvatar addPlayersAvatar(int id, Camera cam, IInputDevice input, int side) {
		AbstractPlayersAvatar player = getPlayersAvatar(game, this, id, cam, input, side);
		this.addEntity(player);
		player.moveToStartPostion(true);
		return player;
	}


	protected abstract AbstractPlayersAvatar getPlayersAvatar(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, int _playerID, Camera _cam, IInputDevice _input, int _side);

	private void setUpLight() {
		// Remove existing lights
		this.game.getRootNode().getWorldLightList().clear();
		LightList list = this.game.getRootNode().getWorldLightList();
		for (Light it : list) {
			this.game.getRootNode().removeLight(it);
		}

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		game.getRootNode().addLight(al);

		sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White);
		sun.setDirection(new Vector3f(-.5f, -.1f, -.5f).normalizeLocal());
		game.getRootNode().addLight(sun);

	}


	@Override
	public void update(float tpfSecs) {
		addAndRemoveEntities();

		for(IProcessable ip : this.entitiesForProcessing) { // this.entities
			ip.process(tpfSecs);
		}

	}


	@Override
	public void collision(PhysicsCollisionEvent event) {
		AbstractPhysicalEntity a=null, b=null;
		Object oa = event.getObjectA().getUserObject();
		if (oa instanceof Spatial) {
			a = getEntityFromSpatial((Spatial)oa);
		} else if (oa instanceof AbstractPhysicalEntity) {
			a = (AbstractPhysicalEntity)oa;
		}

		Object ob = event.getObjectB().getUserObject(); 
		if (ob instanceof Spatial) {
			b = getEntityFromSpatial((Spatial)ob);
		} else if (ob instanceof AbstractPhysicalEntity) {
			b = (AbstractPhysicalEntity)ob;
		}

		if (a != null && b != null) {
			CollisionLogic.collision(this, a, b);
		} else {
			if (a == null) {
				Settings.p(oa + " has no entity data!");
			}
			if (b == null) {
				Settings.p(ob + " has no entity data!");
			}
		}
	}


	public BulletAppState getBulletAppState() {
		return bulletAppState;
	}


	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (!value) {
			return;
		}

		if (name.equals(TEST)) {
			new ChangeBlocksInSweep(game, this, 100);
		} else if (name.equals(QUIT)) {
			game.setNextModule(new StartModule(game, GameMode.Skirmish));
		}

	}


	public void doExplosion(Vector3f pos, IEntity ignore) {
		//Settings.p("Showing explosion");
		float range = 5;
		float power = 20f;

		for(IEntity e : entities) {
			if (e != ignore) { // Stop infinite loop
				if (e instanceof IAffectedByPhysics) {
					IAffectedByPhysics pe = (IAffectedByPhysics)e;
					float dist = pe.getLocation().subtract(pos).length();
					if (dist <= range) {
						//Settings.p("Applying explosion force to " + e);
						Vector3f force = pe.getLocation().subtract(pos).normalizeLocal().multLocal(power);
						pe.applyForce(force);
						/*if (e instanceof IDamagable) {
						IDamagable id = (IDamagable)e;
						id.damaged(1 * (range-dist));
					}*/
					}
				}
			}
		}

		CubeExplosionShard.Factory(game, this, pos, 10);
	}


	@Override
	public void destroy() {
		audioMusic.stop();

		game.getInputManager().removeListener(this);
		game.getInputManager().clearMappings();
		game.getInputManager().clearRawInputListeners();
	}


	private void addAndRemoveEntities() {
		while (this.entitiesToRemove.size() > 0) {
			IEntity e = this.entitiesToRemove.remove(0);
			this.actuallyRemoveEntity(e);
		}

		while (this.entitiesToAdd.size() > 0) {
			IEntity e = this.entitiesToAdd.remove(0);
			actuallyAddEntity(e);
		}
	}


	public void addEntity(IEntity e) {
		this.entitiesToAdd.add(e);
	}


	private void actuallyAddEntity(IEntity e) {
		this.entities.add(e);
		e.actuallyAdd();
		if (e instanceof IProcessable) {
			this.entitiesForProcessing.add((IProcessable)e);
		}
	}


	public void markEntityForRemoval(IEntity e) {
		this.entitiesToRemove.add(e);
	}


	public void actuallyRemoveEntity(IEntity e) {
		e.actuallyRemove();
		this.entities.remove(e);
		if (e instanceof IProcessable) {
			this.entitiesForProcessing.remove((IProcessable)e);
		}
	}


	public Node getRootNode() {
		return game.getRootNode();
	}


	public static AbstractPhysicalEntity getEntityFromSpatial(Spatial ga) {
		final Spatial ref = ga;
		while (true) {
			AbstractPhysicalEntity a = ga.getUserData(Settings.ENTITY);
			if (a == null) {
				ga = ga.getParent();
				if (ga == null) {
					//throw new RuntimeException("Unable to get entity for " + ref);
					Settings.pe("No entity for " + ref);
					return null;
				}
			} else {
				return a;
			}
		}
	}


	public VoxelTerrainEntity getVoxelTerrainEntity() {
		for(IEntity e : this.entities) {
			if (e instanceof VoxelTerrainEntity) {
				return (VoxelTerrainEntity)e;
			}
		}
		return null;
	}


	public void gameOver() {
		Settings.p("Game over!");
		//todo this.gameOver = true;
	}


	public boolean isGameOver() {
		return this.gameOver;
	}
}
