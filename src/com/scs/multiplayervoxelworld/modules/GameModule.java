package com.scs.multiplayervoxelworld.modules;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jme3.app.StatsAppState;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
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
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.util.SkyFactory;
import com.scs.multiplayervoxelworld.CollisionLogic;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.Settings.GameMode;
import com.scs.multiplayervoxelworld.components.IAffectedByPhysics;
import com.scs.multiplayervoxelworld.components.IEntity;
import com.scs.multiplayervoxelworld.components.IMustRemainInArena;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;
import com.scs.multiplayervoxelworld.entities.Collectable;
import com.scs.multiplayervoxelworld.entities.CubeExplosionShard;
import com.scs.multiplayervoxelworld.entities.PlayersAvatar;
import com.scs.multiplayervoxelworld.hud.HUD;
import com.scs.multiplayervoxelworld.input.IInputDevice;
import com.scs.multiplayervoxelworld.input.JoystickCamera2;
import com.scs.multiplayervoxelworld.input.MouseAndKeyboardCamera;
import com.scs.multiplayervoxelworld.map.DefaultMap;
import com.scs.multiplayervoxelworld.map.IPertinentMapData;

import ssmith.util.RealtimeInterval;

public class GameModule implements IModule, PhysicsCollisionListener, ActionListener {

	private static final String QUIT = "Quit";
	private static final String TEST = "Test";

	public static String HELP_TEXT = "";

	protected MultiplayerVoxelWorldMain game;

	public List<IEntity> entities = new ArrayList<IEntity>();
	private List<IProcessable> entitiesForProcessing = new ArrayList<IProcessable>();
	private List<IEntity> entitiesToAdd = new LinkedList<>();
	private List<IEntity> entitiesToRemove = new LinkedList<>();
	public BulletAppState bulletAppState;

	public IPertinentMapData mapData;
	private RealtimeInterval checkOutOfArena = new RealtimeInterval(1000);

	public AudioNode audioExplode, audioSmallExplode;
	private AudioNode audioMusic;
	public DirectionalLight sun;
	//private CollisionLogic collisionLogic = new CollisionLogic();

	public GameModule(MultiplayerVoxelWorldMain _game) {
		super();

		game = _game;
	}


	@Override
	public void init() {
		game.getCamera().setLocation(new Vector3f(0f, 0f, 10f));
		game.getCamera().lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);

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

		mapData = new DefaultMap(game, this);
		mapData.setup();

		Collectable collectable = new Collectable(game, this, 5, 5, 5);
		this.addEntity(collectable);

		Joystick[] joysticks = game.getInputManager().getJoysticks();
		int numPlayers = game.getNumPlayers();

		// Auto-Create player 0
		{
			Camera newCam = this.createCamera(0, numPlayers);
			HUD hud = this.createHUD(newCam, 0);
			IInputDevice input = null;
			if (Settings.PLAYER1_IS_MOUSE) {
				input = new MouseAndKeyboardCamera(newCam, game.getInputManager());
			} else {
				if (joysticks.length > 0) {
					input = new JoystickCamera2(newCam, joysticks[0], game.getInputManager());
				} else {
					throw new RuntimeException("No gamepads found");
				}
			}
			this.addPlayersAvatar(0, newCam, input, hud, 0);
		}

		// Create players for each joystick
		int joyid = Settings.PLAYER1_IS_MOUSE ? 0 : 1;
		int playerid = Settings.PLAYER1_IS_MOUSE ? 1 : 0;
		if (joysticks != null && joysticks.length > 0) {
			//for (int id=nextid ; id<joysticks.length ; id++) {
			while (joyid < joysticks.length) {
				//for (Joystick j : joysticks) {
				Camera newCam = this.createCamera(playerid, numPlayers);
				HUD hud = this.createHUD(newCam, playerid);
				//JoystickCamera_ORIG joyCam = new JoystickCamera_ORIG(newCam, j, game.getInputManager());
				JoystickCamera2 joyCam = new JoystickCamera2(newCam, joysticks[joyid], game.getInputManager());
				this.addPlayersAvatar(playerid, newCam, joyCam, hud, 0);
				//}
				joyid++;
				playerid++;
			}
		}
		playerid++;

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
		audioMusic.play(); // play continuously!

		//if (Settings.SHOW_FPS) {
		BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		game.getStateManager().attach(new StatsAppState(game.getGuiNode(), guiFont_small));
		//}
		
		this.getRootNode().attachChild(SkyFactory.createSky(game.getAssetManager(), "Textures/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));

	}


	private Camera createCamera(int id, int numPlayers) {
		Camera newCam = null;
		if (id == 0) {
			newCam = game.getCamera();
		} else {
			newCam = game.getCamera().clone();
		}

		if (Settings.ALWAYS_SHOW_4_CAMS || numPlayers > 2) {
			//newCam.resize(Overwatch.settings.getWidth()/2, Overwatch.settings.getHeight()/2, true);
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
			//newCam.resize(Overwatch.settings.getWidth(), Overwatch.settings.getHeight()/2, true);
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
			//newCam.resize(Overwatch.settings.getWidth(), Overwatch.settings.getHeight(), true);
			newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			//Settings.p("Creating full-screen camera");
			newCam.setViewPort(0f, 1f, 0f, 1f);
			newCam.setName("Cam_FullScreen");
		} else {
			throw new RuntimeException("Unknown number of players");

		}

		final ViewPort view2 = game.getRenderManager().createMainView("viewport_"+newCam.toString(), newCam);
		view2.setBackgroundColor(ColorRGBA.Cyan);
		view2.setClearFlags(true, true, true);
		view2.attachScene(game.getRootNode());

		final int SHADOWMAP_SIZE = 2048;
		DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(game.getAssetManager(), SHADOWMAP_SIZE, 2);
		dlsr.setLight(sun);
		view2.addProcessor(dlsr);

		BitmapFont guiFont = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		BitmapText hudText = new BitmapText(guiFont, false);
		hudText.setSize(guiFont.getCharSet().getRenderedSize()+2);
		hudText.setColor(ColorRGBA.White);
		hudText.setText("+");
		hudText.setLocalTranslation(newCam.getWidth() / 2 - hudText.getLineWidth()/2, newCam.getHeight() / 2 + hudText.getLineHeight(), 0); // position
		game.getGuiNode().attachChild(hudText);

		/*FilterPostProcessor fpp = new FilterPostProcessor(game.getAssetManager());
		if (Settings.NEON) {
			BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Scene);
			bloom.setEnabled(true);
			bloom.setBloomIntensity(40f);//50f);
			bloom.setBlurScale(6f);//3f);//10f);
			fpp.addFilter(bloom);

			// test filter
			//RadialBlurFilter blur = new RadialBlurFilter();
			//fpp.addFilter(blur);
		}
		view2.addProcessor(fpp);*/

		return newCam;
	}


	private HUD createHUD(Camera c, int id) {
		BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		// HUD coords are full screen co-ords!
		// cam.getWidth() = 640x480, cam.getViewPortLeft() = 0.5f
		float xBL = c.getWidth() * c.getViewPortLeft();
		//float y = (c.getHeight() * c.getViewPortTop())-(c.getHeight()/2);
		float yBL = c.getHeight() * c.getViewPortBottom();

		//Settings.p("Created HUD for " + id + ": " + xBL + "," +yBL);

		float w = c.getWidth() * (c.getViewPortRight()-c.getViewPortLeft());
		float h = c.getHeight() * (c.getViewPortTop()-c.getViewPortBottom());
		HUD hud = new HUD(game, this, xBL, yBL, w, h, guiFont_small, id, c);
		game.getGuiNode().attachChild(hud);
		return hud;

	}


	private void addPlayersAvatar(int id, Camera cam, IInputDevice input, HUD hud, int side) {
		PlayersAvatar player = new PlayersAvatar(game, this, id, cam, input, hud, side);
		this.addEntity(player);
		player.moveToStartPostion(true);
		// Look towards centre
		player.getMainNode().lookAt(new Vector3f(mapData.getWidth()/2, PlayersAvatar.PLAYER_HEIGHT, mapData.getDepth()/2), Vector3f.UNIT_Y);
	}


	private void setUpLight() {
		// Remove existing lights
		this.game.getRootNode().getWorldLightList().clear(); //this.rootNode.getWorldLightList().size();
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

		boolean check = checkOutOfArena.hitInterval();

		for(IProcessable ip : this.entitiesForProcessing) {
			ip.process(tpfSecs);
			if (check) {
				if (ip instanceof IMustRemainInArena) { 
					IMustRemainInArena mr = (IMustRemainInArena)ip;
					Vector3f pos = mr.getLocation();
					if (pos.x < 0 || pos.z < 0 || pos.x > mapData.getWidth() || pos.z > mapData.getDepth()) {
						Settings.p("Respawning " + mr);
						mr.markForRemoval();
						mr.respawn();
					}
				}
			}
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
		} else if (oa instanceof AbstractPhysicalEntity) {
			b = (AbstractPhysicalEntity)ob;
		}

		if (a != null && b != null) {
			CollisionLogic.collision(this, a, b);
			/*if (a instanceof INotifiedOfCollision && b instanceof INotifiedOfCollision) {
				//Settings.p(a + " has collided with " + b);
				INotifiedOfCollision ica = (INotifiedOfCollision)a;
				INotifiedOfCollision icb = (INotifiedOfCollision)b;
				ica.collidedWith(icb);
				icb.collidedWith(ica);
			}*/
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
			for(IEntity e : entities) {
				if (e instanceof PlayersAvatar) {
					PlayersAvatar ip = (PlayersAvatar)e;

					ip.damaged(999, "Test");

					/*Vector3f pos = ip.getLocation().clone();
					pos.x-=2;
					pos.y = 0;
					pos.z-=2;
					doExplosion(pos);//, 5, 10);*/
					//break;
				}
			}

			/*Vector3f tmp = new Vector3f();
			this.getBulletAppState().getPhysicsSpace().getGravity(tmp);
			this.getBulletAppState().getPhysicsSpace().setGravity(tmp.mult(-1));*/
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


	public float getPlayersHealth(int id) {
		if (Settings.GAME_MODE == GameMode.KingOfTheHill) {
			return 100f;
		} else {
			return 1f; // Die immed
		}
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
		while (true) {
			AbstractPhysicalEntity a = ga.getUserData(Settings.ENTITY);
			if (a == null) {
				ga = ga.getParent();
			} else {
				return a;
			}
		}
	}
}
