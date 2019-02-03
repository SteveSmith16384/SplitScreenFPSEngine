package com.scs.splitscreentowerdefence.abilities;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.abilities.AbstractAbility;
import com.scs.splitscreenfpsengine.entities.AbstractPhysicalEntity;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfpsengine.entities.FloorOrCeiling;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;
import com.scs.splitscreentowerdefence.entities.Turret;

public class PlaceTurretAbility extends AbstractAbility {

	public PlaceTurretAbility(SplitScreenFpsEngine _game, AbstractGameModule module, AbstractPlayersAvatar p) {
		super(_game, module, p, "PlaceTurretAbility");
	}


	@Override
	public boolean process(float interpol) {
		return false;
	}


	@Override
	public boolean activate(float interpol) {
		/*todo if (this.player.resources < TDSettings.TURRET_COST) {
			Settings.p("Not enough resources");
			//return false;
		}*/
		Ray ray = new Ray(this.avatar.getCamera().getLocation(), this.avatar.getCamera().getDirection());

		CollisionResults results = new CollisionResults();
		module.getRootNode().collideWith(ray, results);

		CollisionResult result = results.getClosestCollision();
		if (result != null) {
			if (result.getDistance() > 1f) { // So we don't build a block on top of ourselves
				Geometry g = result.getGeometry();
				AbstractPhysicalEntity ape = (AbstractPhysicalEntity)AbstractGameModule.getEntityFromSpatial(g);
				if (ape instanceof FloorOrCeiling) {
					//VoxelTerrainEntity vte = (VoxelTerrainEntity)ape;
					Vector3f position = result.getContactPoint();
					if (position.y == 1f) { // Must be on floor
						//Vector3Int blockPosition = blocks.getPointedBlockLocation(position, true);
						new Turret(game, module, position, this.avatar.getSide());
						//todo player.resources -= TDSettings.TURRET_COST;
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
