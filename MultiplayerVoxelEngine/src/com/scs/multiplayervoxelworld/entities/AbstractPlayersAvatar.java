package com.scs.multiplayervoxelworld.entities;

import java.util.List;

import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Spatial;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.MyBetterCharacterControl;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.abilities.IAbility;
import com.scs.multiplayervoxelworld.components.IAffectedByPhysics;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.components.ICausesHarmOnContact;
import com.scs.multiplayervoxelworld.components.IDamagable;
import com.scs.multiplayervoxelworld.components.IEntity;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.components.IShowOnHUD;
import com.scs.multiplayervoxelworld.components.ITargetByAI;
import com.scs.multiplayervoxelworld.hud.AbstractHUDImage;
import com.scs.multiplayervoxelworld.hud.IHud;
import com.scs.multiplayervoxelworld.input.IInputDevice;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;

public abstract class AbstractPlayersAvatar extends AbstractPhysicalEntity implements IProcessable, ICanShoot, IShowOnHUD, ITargetByAI, IAffectedByPhysics, IDamagable {

	// Player dimensions
	public static final float PLAYER_HEIGHT = 1.5f;
	public static final float PLAYER_RAD = 0.4f;
	private static final float WEIGHT = 1f;

	public final Vector3f walkDirection = new Vector3f();
	public float moveSpeed = Settings.PLAYER_MOVE_SPEED;
	private IInputDevice input;

	//Temporary vectors used on each frame.
	public Camera cam;
	public final Vector3f camDir = new Vector3f();
	private final Vector3f camLeft = new Vector3f();

	public IHud hud;
	public MyBetterCharacterControl playerControl;
	public final int playerID;
	public IAbility[] ability = new IAbility[2];
	private Spatial playerGeometry;

	// Stats
	private float health;
	private int side;

	private boolean restarting = false;
	private float restartTime, invulnerableTime;
	private float timeSinceLastMove = 0;
	
	public AbstractHUDImage gamepadTest;

	public AbstractPlayersAvatar(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, int _playerID, Camera _cam, IInputDevice _input, int _side) {
		super(_game, _module, "Player");

		playerID = _playerID;
		cam = _cam;
		input = _input;
		//hud = _hud;
		health = 100;//module.getPlayersHealth(playerID);
		side = _side;

		int pid = playerID;
		playerGeometry = getPlayersModel(game, pid);
		this.getMainNode().attachChild(playerGeometry);

		playerControl = new MyBetterCharacterControl(PLAYER_RAD, PLAYER_HEIGHT, WEIGHT);
		playerControl.setJumpForce(new Vector3f(0, Settings.JUMP_FORCE, 0)); 
		this.getMainNode().addControl(playerControl);

		playerControl.getPhysicsRigidBody().setUserObject(this);

	}


	public void setHUD(IHud _hud) {
		hud = _hud;
	}


	protected abstract Spatial getPlayersModel(MultiplayerVoxelWorldMain game, int pid);

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
		//Point p = module.level.getPlayerStartPos(playerID);
		Vector3f warpPos = module.getPlayerStartPos(playerID);//new Vector3f(p.x, module.mapData.getRespawnHeight(), p.y);
		//Settings.p("Scheduling player to start position: " + warpPos);
		this.playerControl.warp(warpPos);
		if (invuln) {
			invulnerableTime = MultiplayerVoxelWorldMain.properties.GetInvulnerableTimeSecs();
		}
	}


	@Override
	public void process(float tpf) {
		if (this.restarting) {
			restartTime -= tpf;
			if (this.restartTime <= 0) {
				this.moveToStartPostion(true);
				restarting = false;
				return;
			}
		}

		if (invulnerableTime >= 0) {
			invulnerableTime -= tpf;
		}

		if (!this.restarting) {
			// Have we fallen off the edge
			if (this.playerControl.getPhysicsRigidBody().getPhysicsLocation().y < -1f) { // scs catching here after died!
				died("Too low");
				return;
			}

			timeSinceLastMove += tpf;

			for (int num=0 ; num < 2 ; num++) {
				if (this.ability[num] != null) {
					ability[num].process(tpf);
					if (input.isAbilityPressed(num)) { // Must be before we set the walkDirection & moveSpeed, as this method may affect it
						//Settings.p("Using " + this.ability.toString());
						this.ability[num].activate(tpf);
						if (this.ability[num].onlyActivateOnClick()) {
							input.resetAbilitySwitch(num);
						}
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
		Vector3f vec = getMainNode().getWorldTranslation();
		cam.setLocation(new Vector3f(vec.x, vec.y + (PLAYER_HEIGHT/2), vec.z));

		// Rotate us to point in the direction of the camera
		Vector3f lookAtPoint = cam.getLocation().add(cam.getDirection().mult(10));
		//gun.lookAt(lookAtPoint.clone(), Vector3f.UNIT_Y);
		lookAtPoint.y = cam.getLocation().y; // Look horizontal
		this.playerGeometry.lookAt(lookAtPoint, Vector3f.UNIT_Y);
		//this.getMainNode().lookAt(lookAtPoint.clone(), Vector3f.UNIT_Y);  This won't rotate the model since it's locked to the physics controller

		// Move cam fwd so we don't see ourselves
		cam.setLocation(cam.getLocation().add(cam.getDirection().mult(PLAYER_RAD)));
		cam.update();

		//this.input.resetFlags();

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


	public void hitByBullet(ICausesHarmOnContact bullet) {
		if (invulnerableTime <= 0) {
			float dam = bullet.getDamageCaused();
			if (dam > 0) {
				Settings.p("Player hit by bullet");
				module.doExplosion(this.mainNode.getWorldTranslation(), this);
				module.audioExplode.play();
				//this.health -= dam;
				//this.hud.setHealth(this.health);
				this.hud.showDamageBox();

				died("hit by " + bullet.toString());
			}
		} else {
			Settings.p("Player hit but is currently invulnerable");
		}
	}


	private void died(String reason) {
		Settings.p("Player died: " + reason);
		this.restarting = true;
		this.restartTime = MultiplayerVoxelWorldMain.properties.GetRestartTimeSecs();
		//invulnerableTime = RESTART_DUR*3;

		// Move us below the map
		Vector3f pos = this.getMainNode().getWorldTranslation().clone();//.floor_phy.getPhysicsLocation().clone();
		playerControl.warp(pos);
	}


	@Override
	public void hasSuccessfullyHit(IEntity e) {
		/*this.incScore(20, "shot " + e.toString());
		//new AbstractHUDImage(game, module, this.hud, "Textures/text/hit.png", this.hud.hud_width, this.hud.hud_height, 2);
		this.hud.showCollectBox();
		//numShotsHit++;
		//calcAccuracy();*/
	}

/*
	public void incScore(float amt, String reason) {
		Settings.p("Inc score: +" + amt + ", " + reason);
		this.score += amt;
		//this.hud.setScore(this.score);

		if (this.score >= 100) {
			new AbstractHUDImage(game, module, this.hud, "Textures/text/winner.png", this.hud.hud_width, this.hud.hud_height, 10);
		}
	}
*/
	
	/*
	@Override
	public void collidedWith(INotifiedOfCollision other) {
		if (other instanceof ICausesHarmOnContact) {
			ICausesHarmOnContact bullet = (ICausesHarmOnContact)other;
			if (bullet.getShooter() != null) {
				if (bullet.getShooter() != this) {
					if (Settings.PVP || !(bullet.getShooter() instanceof PlayersAvatar)) {
						this.hitByBullet(bullet);
						bullet.getShooter().hasSuccessfullyHit(this);
					}
				}
			}
		} else if (other instanceof Collectable) {
			Collectable col = (Collectable)other;
			col.markForRemoval();
			if (!col.collected) {
				this.incScore(10, "Collectable");
				this.hud.showCollectBox();
				//module.createCollectable();
			}

		}
	}
	 */

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
	public void damaged(float amt, String reason) {
		died(reason);
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

/*
	public int getScore() {
		return score;
	}
*/

	@Override
	public Vector3f getBulletStartPosition() {
		return this.getMainNode().getWorldBound().getCenter();
	}

}
