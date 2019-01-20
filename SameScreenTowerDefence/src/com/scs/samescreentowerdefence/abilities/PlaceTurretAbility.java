package com.scs.samescreentowerdefence.abilities;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.abilities.AbstractAbility;
import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;
import com.scs.multiplayervoxelworld.entities.AbstractPlayersAvatar;
import com.scs.multiplayervoxelworld.entities.VoxelTerrainEntity;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;
import com.scs.samescreentowerdefence.entities.Turret;

public class PlaceTurretAbility extends AbstractAbility {

	public PlaceTurretAbility(MultiplayerVoxelWorldMain _game, AbstractGameModule module, AbstractPlayersAvatar p) {
		super(_game, module, p);
	}


	@Override
	public boolean process(float interpol) {
		return false;
	}


	@Override
	public boolean activate(float interpol) {
		if (this.player.resources < Settings.TURRET_COST) {
			Settings.p("Not enough resources");
			//return false;
		}
		Ray ray = new Ray(this.player.getCamera().getLocation(), this.player.getCamera().getDirection());

		CollisionResults results = new CollisionResults();
		module.getRootNode().collideWith(ray, results);

		CollisionResult result = results.getClosestCollision();
		if (result != null) {
			if (result.getDistance() > 1f) { // So we don't build a block on top of ourselves
				Geometry g = result.getGeometry();
				AbstractPhysicalEntity ape = (AbstractPhysicalEntity)AbstractGameModule.getEntityFromSpatial(g);
				if (ape instanceof VoxelTerrainEntity) {
					VoxelTerrainEntity vte = (VoxelTerrainEntity)ape;
					Vector3f position = result.getContactPoint();
					if (position.y == 1f) { // Must be on floor
						//Vector3Int blockPosition = blocks.getPointedBlockLocation(position, true);
						Turret turret = new Turret(game, module, position, this.player.getSide());
						player.resources -= Settings.TURRET_COST;
						return true;
					}
				} else {
					Settings.p(ape + " selected");
				}
			}
		}
		return false;
	}


	@Override
	public boolean onlyActivateOnClick() {
		return true;
	}


	@Override
	public String getHudText() {
		return "[LayTrapAbility]";
	}

}
