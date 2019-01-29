package com.scs.splitscreenfpsengine.entities;

import java.util.List;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Spatial;
import com.scs.splitscreenfpsengine.CameraSystem;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.MyBetterCharacterControl;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.abilities.IAbility;
import com.scs.splitscreenfpsengine.components.IAffectedByPhysics;
import com.scs.splitscreenfpsengine.components.ICanShoot;
import com.scs.splitscreenfpsengine.components.IEntity;
import com.scs.splitscreenfpsengine.components.IProcessable;
import com.scs.splitscreenfpsengine.components.IShowOnHUD;
import com.scs.splitscreenfpsengine.hud.IHud;
import com.scs.splitscreenfpsengine.input.IInputDevice;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public abstract class AbstractPlayersAvatar extends AbstractPhysicalEntity implements IProcessable, ICanShoot, IShowOnHUD, IAffectedByPhysics {

	// Player dimensions
	//public static final float PLAYER_HEIGHT = 1.5f;
	//public static final float PLAYER_RAD = 0.4f;
	private static final float WEIGHT = 1f;

	public final Vector3f walkDirection = new Vector3f();
	public float moveSpeed = Settings.PLAYER_MOVE_SPEED;
	protected IInputDevice input;

	//Temporary vectors used on each frame.
	public Camera cam;
	public final Vector3f camDir = new Vector3f();
	private final Vector3f camLeft = new Vector3f();

	public IHud hud;
	public MyBetterCharacterControl playerControl;
	public final int playerID; // 0-3
	public IAbility[] ability = new IAbility[2];
	protected Spatial playerGeometry; // todo - rename
	private CameraSystem camSys;
	private float radius;
	
	// Stats
	private int side;

	protected boolean restarting = false;
	protected float restartTime, invulnerableTime;
	private float timeSinceLastMove = 0;
	
	public AbstractPlayersAvatar(SplitScreenFpsEngine _game, AbstractGameModule _module, int _playerID, Camera _cam, IInputDevice _input, int _side) {
		super(_game, _module, "Player");

		playerID = _playerID;
		cam = _cam;
		input = _input;
		side = _side;

		int pid = playerID;
		playerGeometry = getPlayersModel(game, pid);
		this.getMainNode().attachChild(playerGeometry);

		BoundingBox bv = (BoundingBox)playerGeometry.getWorldBound();
		radius = bv.getXExtent();
		playerControl = new MyBetterCharacterControl(bv.getXExtent(), bv.getYExtent()*2, WEIGHT);
		playerControl.setJumpForce(new Vector3f(0, Settings.JUMP_FORCE, 0)); 
		this.getMainNode().addControl(playerControl);
		
		camSys = new CameraSystem(game, cam, this);
		camSys.setupCam(2f, 0.2f, true, 1f, 1f); // todo - get from model

		playerControl.getPhysicsRigidBody().setUserObject(this);

	}

	
	//public abstract float getCameraHeight();

	public void setHUD(IHud _hud) {
		hud = _hud;
	}


	protected abstract Spatial getPlayersModel(SplitScreenFpsEngine game, int pid);

		/*
	private Spatial getPlayersModel(MultiplayerVoxelWorldMain game, int pid) {
		if (Settings.USE_MODEL_FOR_PLAYERS) {
			return new RobotModel(game.getAssetManager(), pid);
		} else {
			// Add player's box
			Box box1 = new Box(PLAYER_RAD, PLAYER_HEIGHT/2, PLAYER_RAD);
			//Cylinder box1 = new Cylinder(1, 8, PLAYER_RAD, PLAYER_HEIGHT, true);
			Geometry playerGeometry = new Geometry("Player", box1);
			TextureKey key3 = new TextureKey("Textures/robot_green.png");
			key3.setGenerateMips(true);
			Texture tex3 = game.getAssetManager().loadTexture(key3);
			Material floor_mat = null;
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floor_mat.setTexture("DiffuseMap", tex3);
			playerGeometry.setMaterial(floor_mat);
			//playerGeometry.setLocalTranslation(new Vector3f(0, PLAYER_HEIGHT/2, 0)); // Need this to ensure the crate is on the floor
			playerGeometry.setLocalTranslation(new Vector3f(0, (PLAYER_HEIGHT/2)-.075f, 0)); // Need this to ensure the crate is on the floor
			return playerGeometry;
		}
	}
*/

	public void moveToStartPostion(boolean invuln) {
		Vector3f warpPos = module.getPlayerStartPos(playerID);//new Vector3f(p.x, module.mapData.getRespawnHeight(), p.y);
		this.playerControl.warp(warpPos);
		if (invuln) {
			invulnerableTime = SplitScreenFpsEngine.properties.GetInvulnerableTimeSecs();
		}
	}


	@Override
	public void process(float tpfSecs) {
		input.process(tpfSecs);
		
		if (this.restarting) {
			restartTime -= tpfSecs;
			if (this.restartTime <= 0) {
				this.moveToStartPostion(true);
				restarting = false;
				//killed = false;
				return;
			}
		}

		if (invulnerableTime >= 0) {
			invulnerableTime -= tpfSecs;
		}

		if (!this.restarting) {
			// Have we fallen off the edge
			/*if (this.playerControl.getPhysicsRigidBody().getPhysicsLocation().y < -1f) { // scs catching here after died!
				died("Too low");
				return;
			}*/

			timeSinceLastMove += tpfSecs;

			for (int num=0 ; num < 2 ; num++) {
				if (this.ability[num] != null) {
					ability[num].process(tpfSecs);
					if (input.isAbilityPressed(num)) { // Must be before we set the walkDirection & moveSpeed, as this method may affect it
						//Settings.p("Using " + this.ability.toString());
						this.ability[num].activate(tpfSecs);
						if (this.ability[num].onlyActivateOnClick()) {
							input.resetAbilitySwitch(num);
						}
						this.hud.refresh();
					}
					//this.hud.setAbilityGunText(this.ability[num].getHudText()); // todo - use diff hud fields
				}
			}

			camDir.set(cam.getDirection()).multLocal(moveSpeed, 0.0f, moveSpeed);
			camLeft.set(cam.getLeft()).multLocal(moveSpeed);
			if (input.getFwdValue() > 0) {	
				//Settings.p("fwd=" + input.getFwdValue());
				walkDirection.addLocal(camDir.mult(input.getFwdValue()));
				timeSinceLastMove = 0;
			} else if (input.getBackValue() > 0) {
				walkDirection.addLocal(camDir.negate().mult(input.getBackValue()));
				timeSinceLastMove = 0;
			}
			if (input.getStrafeLeftValue() > 0) {		
				walkDirection.addLocal(camLeft.mult(input.getStrafeLeftValue()));
				timeSinceLastMove = 0;
			} else if (input.getStrafeRightValue() > 0) {		
				walkDirection.addLocal(camLeft.negate().mult(input.getStrafeRightValue()));
				timeSinceLastMove = 0;
			}

			/*if (walkDirection.length() != 0) {
				Settings.p("walkDirection=" + walkDirection);
			}*/
			playerControl.setWalkDirection(walkDirection);

			if (input.isJumpPressed()){
				this.jump();
				timeSinceLastMove = 0;
			}
			/*
				if (input.isShootPressed()) {
					shoot();
				}

				// These must be after we might use them, so the hud is correct
				this.hud.setAbilityGunText(this.abilityGun.getHudText());
				if (abilityOther != null) {
					this.hud.setAbilityOtherText(this.abilityOther.getHudText());
				}
			 */
		}

		// Position camera at node
		//Vector3f vec = getMainNode().getWorldTranslation();
		//cam.setLocation(new Vector3f(vec.x, vec.y + (PLAYER_HEIGHT/2), vec.z)); // scs todo - position above wiz
		this.camSys.process(tpfSecs);

		// Rotate us to point in the direction of the camera
		Vector3f lookAtPoint = cam.getLocation().add(cam.getDirection().mult(10));
		//gun.lookAt(lookAtPoint.clone(), Vector3f.UNIT_Y);
		lookAtPoint.y = cam.getLocation().y; // Look horizontal
		this.playerGeometry.lookAt(lookAtPoint, Vector3f.UNIT_Y);
		//this.getMainNode().lookAt(lookAtPoint.clone(), Vector3f.UNIT_Y);  This won't rotate the model since it's locked to the physics controller

		// Move cam fwd so we don't see ourselves
		cam.setLocation(cam.getLocation().add(cam.getDirection().mult(radius)));
		cam.update();

		walkDirection.set(0, 0, 0);
	}


	public boolean isOnGround() {
		return playerControl.isOnGround();
	}


	public FrustumIntersect getInsideOutside(AbstractPhysicalEntity entity) {
		FrustumIntersect insideoutside = cam.contains(entity.getMainNode().getWorldBound());
		return insideoutside;
	}


	@Override
	public Vector3f getLocation() {
		return this.cam.getLocation();
		//return playerControl.getPhysicsRigidBody().getPhysicsLocation();  This is very low to the ground!
	}


	@Override
	public Vector3f getShootDir() {
		return this.cam.getDirection();
	}


	public void jump() {
		this.playerControl.jump();
	}



	@Override
	public void hasSuccessfullyHit(IEntity e) {
		/*this.incScore(20, "shot " + e.toString());
		//new AbstractHUDImage(game, module, this.hud, "Textures/text/hit.png", this.hud.hud_width, this.hud.hud_height, 2);
		this.hud.showCollectBox();
		//numShotsHit++;
		//calcAccuracy();*/
	}


	@Override
	public void applyForce(Vector3f force) {
		//playerControl.getPhysicsRigidBody().applyImpulse(force, Vector3f.ZERO);//.applyCentralForce(dir);
		//playerControl.getPhysicsRigidBody().applyCentralForce(force);
		//Settings.p("Applying force to player:" + force);
		//this.addWalkDirection.addLocal(force);
	}


	public Camera getCamera() {
		return this.cam;
	}


	@Override
	public void actuallyRemove() {
		super.actuallyRemove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.playerControl);

	}


	public Vector3f getPointOnFloor(float range) {
		Vector3f from = this.cam.getLocation();
		Vector3f to = this.cam.getDirection().normalize().multLocal(range).addLocal(from);
		List<PhysicsRayTestResult> results = module.bulletAppState.getPhysicsSpace().rayTest(from, to);
		float dist = -1;
		PhysicsRayTestResult closest = null;
		for (PhysicsRayTestResult r : results) {
			if (r.getCollisionObject().getUserObject() != null) {
				if (closest == null) {
					closest = r;
				} else if (r.getHitFraction() < dist) {
					closest = r;
				}
				dist = r.getHitFraction();
			}
		}
		if (closest != null) {
			AbstractEntity e = (AbstractEntity)closest.getCollisionObject().getUserObject();
			Vector3f hitpoint = to.subtract(from).multLocal(closest.getHitFraction()).addLocal(from);
			Settings.p("Hit " + e + " at " + hitpoint);
			//module.doExplosion(from, null);
			return hitpoint;
		}

		return null;
	}


	@Override
	public int getSide() {
		return side;
	}


	@Override
	public Vector3f getBulletStartPosition() {
		return this.getMainNode().getWorldBound().getCenter();
	}


	@Override
	public float getRadius() {
		return radius;
	}


}
