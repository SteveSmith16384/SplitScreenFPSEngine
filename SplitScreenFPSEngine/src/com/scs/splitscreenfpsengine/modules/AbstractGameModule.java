package com.scs.splitscreenfpsengine.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.scs.splitscreenfpsengine.CollisionLogic;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.components.IAffectedByPhysics;
import com.scs.splitscreenfpsengine.components.IEntity;
import com.scs.splitscreenfpsengine.components.IExpiringEffect;
import com.scs.splitscreenfpsengine.components.IProcessable;
import com.scs.splitscreenfpsengine.entities.AbstractPhysicalEntity;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfpsengine.entities.AbstractTerrainEntity;
import com.scs.splitscreenfpsengine.entities.CubeExplosionShard;
import com.scs.splitscreenfpsengine.entities.FloorOrCeiling;
import com.scs.splitscreenfpsengine.entities.ParticleExplosion;
import com.scs.splitscreenfpsengine.entities.VoxelTerrainEntity;
import com.scs.splitscreenfpsengine.hud.IHud;
import com.scs.splitscreenfpsengine.input.IInputDevice;
import com.scs.splitscreenfpsengine.input.JamepadCamera;
import com.scs.splitscreenfpsengine.input.MouseAndKeyboardCamera;
import com.scs.splitscreenfpsengine.systems.ExpiringEffectSystem;

public abstract class AbstractGameModule implements IModule, PhysicsCollisionListener, ActionListener {

	private static final String QUIT = "Quit";
	private static final String TEST = "Test";

	public static String HELP_TEXT = "";

	protected SplitScreenFpsEngine game;

	public List<IEntity> entities = new ArrayList<IEntity>();
	private List<IProcessable> entitiesForProcessing = new ArrayList<IProcessable>();
	private List<IEntity> entitiesToAdd = new LinkedList<>();
	private List<IEntity> entitiesToRemove = new LinkedList<>();
	public BulletAppState bulletAppState;

	public AudioNode audioExplode, audioSmallExplode;
	private AudioNode audioMusic;
	public DirectionalLight sun;
	private boolean gameOver = false;
	
	private AbstractPlayersAvatar playerDebug;

	protected ExpiringEffectSystem expiringEffectSystem;

	public AbstractGameModule(SplitScreenFpsEngine _game) {
		super();

		game = _game;
	}


	@Override
	public void init() {
		game.getInputManager().clearMappings();

		game.getInputManager().addMapping(QUIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		game.getInputManager().addListener(this, QUIT);            

		game.getInputManager().addMapping(TEST, new KeyTrigger(KeyInput.KEY_T));
		game.getInputManager().addListener(this, TEST);            

		// Set up Physics
		bulletAppState = new BulletAppState();
		game.getStateManager().attach(bulletAppState);
		bulletAppState.getPhysicsSpace().addCollisionListener(this);
		//bulletAppState.setDebugEnabled(true);

		game.getRenderManager().removeMainView(game.getViewPort()); // Since we create new ones for each player

		setUpLight();
		setupLevel();

		int numPlayers = game.getNumPlayers();

		// Auto-Create player 0
		{
			Camera newCam = this.createCamera(0, numPlayers);
			IInputDevice input = null;
			//if (Settings.PLAYER1_IS_MOUSE) {
			input = new MouseAndKeyboardCamera(newCam, game.getInputManager());
			/*} else {
				if (joysticks.length > 0) {
					input = new JMEJoystickCamera(newCam, joysticks[0], game.getInputManager());
				} else {
					throw new RuntimeException("No gamepads found");
				}
			}*/
			AbstractPlayersAvatar player = this.addPlayersAvatar(0, newCam, input, 0);
			IHud hud = this.createHUD(newCam, player);
			player.setHUD(hud);
			
			playerDebug = player;
		}

		// Create players for each joystick
		int joyid = 0;//Settings.PLAYER1_IS_MOUSE ? 0 : 1;
		int playerid = 1;//Settings.PLAYER1_IS_MOUSE ? 1 : 0;
		while (joyid < numPlayers-1) {//joysticks.length) {
			Camera newCam = this.createCamera(playerid, numPlayers);
			JamepadCamera jameCam = new JamepadCamera(newCam, game.controllerManager.getController(joyid), game.controllerManager.getInitialStates(joyid));
			AbstractPlayersAvatar player = this.addPlayersAvatar(playerid, newCam, jameCam, playerid);
			IHud hud = this.createHUD(newCam, player);
			player.setHUD(hud);

			joyid++;
			playerid++;
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

		/*audioExplode = new AudioNode(game.getAssetManager(), "Sound/explode.wav", false);
		audioExplode.setPositional(false);
		audioExplode.setLooping(false);
		//audio_gun.setVolume(2);
		game.getRootNode().attachChild(audioExplode);

		audioSmallExplode = new AudioNode(game.getAssetManager(), "Sound/explodemini.wav", false);
		audioSmallExplode.setPositional(false);
		audioSmallExplode.setLooping(false);
		//audio_gun.setVolume(2);
		game.getRootNode().attachChild(audioSmallExplode);
		 */

		this.expiringEffectSystem = new ExpiringEffectSystem(this);
	}


	protected abstract void setupLevel();

	public abstract Vector3f getPlayerStartPos(int id);


	private Camera createCamera(int playerID, int numPlayers) {
		Camera newCam = null;
		if (playerID == 0) {
			newCam = game.getCamera();
		} else {
			newCam = game.getCamera().clone();
		}

		if (Settings.ALWAYS_SHOW_4_CAMS || numPlayers > 2) {
			newCam.setFrustumPerspective(Settings.FRUSTUM_ANGLE, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			switch (playerID) { // left/right/bottom/top, from bottom-left!
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
				throw new RuntimeException("Unknown player id: " + playerID);
			}
		} else if (numPlayers == 2) {
			newCam.setFrustumPerspective(Settings.FRUSTUM_ANGLE, (float) (newCam.getWidth()*2) / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			switch (playerID) { // left/right/bottom/top, from bottom-left!
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
				throw new RuntimeException("Unknown player id: " + playerID);
			}
		} else if (numPlayers == 1) {
			newCam.setFrustumPerspective(Settings.FRUSTUM_ANGLE, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
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

		{
			// Add shadows to this viewport
			final int SHADOWMAP_SIZE = 2048;
			DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(game.getAssetManager(), SHADOWMAP_SIZE, 2);
			dlsr.setLight(sun);
			view2.addProcessor(dlsr);
		}

		onViewportCreated(view2);

		return newCam;
	}


	protected void onViewportCreated(ViewPort viewport) {
		// Override if required
	}

	private IHud createHUD(Camera c, AbstractPlayersAvatar player) {
		// HUD coords are full screen co-ords!
		// cam.getWidth() = 640x480, cam.getViewPortLeft() = 0.5f
		float xBL = c.getWidth() * c.getViewPortLeft();
		//float y = (c.getHeight() * c.getViewPortTop())-(c.getHeight()/2);
		float yBL = c.getHeight() * c.getViewPortBottom();

		Settings.p("Created HUD for " + player + ": " + xBL + "," +yBL);

		float w = c.getWidth() * (c.getViewPortRight()-c.getViewPortLeft());
		float h = c.getHeight() * (c.getViewPortTop()-c.getViewPortBottom());
		IHud hud = generateHUD(game, this, player, xBL, yBL, w, h, c);
		game.getGuiNode().attachChild(hud.getSpatial());
		return hud;

	}


	protected abstract IHud generateHUD(SplitScreenFpsEngine _game, AbstractGameModule _module, AbstractPlayersAvatar _player, float xBL, float yBL, float w, float h, Camera _cam);


	private AbstractPlayersAvatar addPlayersAvatar(int id, Camera cam, IInputDevice input, int side) {
		AbstractPlayersAvatar player = getPlayersAvatar(game, this, id, cam, input, side);
		this.addEntity(player);
		player.moveToStartPostion(true);
		return player;
	}


	protected abstract AbstractPlayersAvatar getPlayersAvatar(SplitScreenFpsEngine _game, AbstractGameModule _module, int _playerID, Camera _cam, IInputDevice _input, int _side);


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
		sun.setDirection(new Vector3f(-.7f, -.1f, -.7f).normalizeLocal());
		game.getRootNode().addLight(sun);

	}


	@Override
	public void update(float tpfSecs) {
		addAndRemoveEntities();

		expiringEffectSystem.process(tpfSecs);

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
				Settings.pe(oa + " has no entity data!");
			}
			if (b == null) {
				Settings.pe(ob + " has no entity data!");
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
			Vector3f pos = playerDebug.getLocation();
			pos.y += 2f;
			new ParticleExplosion(game, this, pos);
		
		} else if (name.equals(QUIT)) {
			game.setNextModule(game.getStartModule());//new StartModule(game, GameMode.Skirmish));
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
		if (audioMusic != null) {
			audioMusic.stop();
		}

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
		if (e instanceof IExpiringEffect) {
			this.expiringEffectSystem.add((IExpiringEffect)e);
		}
	}


	/**
	 * Should only be called by AbstractEntity
	 * @param e
	 */
	public void markEntityForRemoval(IEntity e) {
		this.entitiesToRemove.add(e);
	}


	public void actuallyRemoveEntity(IEntity e) {
		e.actuallyRemove();
		this.entities.remove(e);
		if (e instanceof IProcessable) {
			this.entitiesForProcessing.remove((IProcessable)e);
		}
		this.entitiesToAdd.remove(e); // Just in case it's not been formally added yet
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
		this.gameOver = true;
	}


	public boolean isGameOver() {
		return this.gameOver;
	}


	@Override
	public void newController(int idx) {
		// Do nothing
	}


	@Override
	public void controllerDisconnected(int id) {
		// todo - kill avatar

	}


	public AbstractPhysicalEntity getWithRay(AbstractPlayersAvatar wiz, Class<? extends AbstractPhysicalEntity> clazz, float range) {
		Ray ray = new Ray(wiz.getCamera().getLocation(), wiz.getCamera().getDirection());

		CollisionResults results = new CollisionResults();
		game.getRootNode().collideWith(ray, results);

		Iterator<CollisionResult> it = results.iterator();
		while (it.hasNext()) {
			CollisionResult col = it.next();
			if (range > 0 && wiz.distance(col.getContactPoint()) > range) {
				wiz.hud.appendToLog("Too far away");
				return null;
			}
			Geometry g = col.getGeometry();
			AbstractPhysicalEntity ape = (AbstractPhysicalEntity)AbstractGameModule.getEntityFromSpatial(g);
			if (ape != null) {
				if (clazz.isAssignableFrom(ape.getClass())) { // todo - isAssignableFrom??
					//if (ape.getClass().isAssignableFrom(clazz)) { // todo - isAssignableFrom??
					//if (ape.getClass() == clazz || ape.getClass().getSuperclass() == clazz) { // todo - isAssignableFrom??
					return ape;
				}
			}
		}
		int dfg;
		return null;
	}


	public Vector3f getFloorPointWithRay(AbstractPlayersAvatar wiz, float range) {
		if (Settings.USE_TERRAIN) {
			return getPointWithRay(wiz, AbstractTerrainEntity.class, range);
		} else {
			return getPointWithRay(wiz, FloorOrCeiling.class, range);
		}
	}


	public Vector3f getPointWithRay(AbstractPlayersAvatar wiz, Class<? extends AbstractPhysicalEntity> clazz, float range) {
		Ray ray = new Ray(wiz.getCamera().getLocation(), wiz.getCamera().getDirection());

		CollisionResults results = new CollisionResults();
		game.getRootNode().collideWith(ray, results);

		Iterator<CollisionResult> it = results.iterator();
		while (it.hasNext()) {
			CollisionResult col = it.next();
			if (range > 0 && wiz.distance(col.getContactPoint()) > range) {
				wiz.hud.appendToLog("Too far away");
				return null;
			}
			Geometry g = col.getGeometry();
			AbstractPhysicalEntity ape = (AbstractPhysicalEntity)AbstractGameModule.getEntityFromSpatial(g);
			if (ape != null) {
				if (ape.collides()) {
					//if (ape.getClass() == clazz || ape.getClass().getSuperclass() == clazz) { // todo - isAssignableFrom??
					if (ape.getClass() == clazz || ape.getClass().getSuperclass() == clazz) { // todo - isAssignableFrom??
						return col.getContactPoint();
					}
				}
			}
		}
		return null;
	}


	public boolean isAreaClear(BoundingVolume bv) {
		for (IEntity e : this.entities) {
			if (e instanceof AbstractTerrainEntity || e instanceof FloorOrCeiling || e instanceof VoxelTerrainEntity) {
				continue;
			}
			try {
				if (e instanceof AbstractPhysicalEntity) {
					AbstractPhysicalEntity ape = (AbstractPhysicalEntity)e;
					if (ape.collides()) {
						if (ape.getMainNode().getWorldBound().intersects(bv)) { // error
							return false;
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return true;
	}
}
