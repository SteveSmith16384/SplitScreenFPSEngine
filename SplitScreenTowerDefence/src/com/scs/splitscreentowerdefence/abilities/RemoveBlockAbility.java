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
import com.scs.splitscreenfpsengine.entities.VoxelTerrainEntity;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;
import com.scs.splitscreentowerdefence.blocks.StoneBlock;

import mygame.blocks.BlockTerrainControl;
import mygame.blocks.IBlock;
import mygame.util.Vector3Int;

public class RemoveBlockAbility extends AbstractAbility {

	private static final float MAX_RANGE = 4;

	public RemoveBlockAbility(SplitScreenFpsEngine _game, AbstractGameModule module, AbstractPlayersAvatar p) {
		super(_game, module, p, "RemoveBlockAbility");
	}

	@Override
	public boolean process(float interpol) {
		return false;
	}


	@Override
	public boolean activate(float interpol) {
		Ray ray = new Ray(this.avatar.getCamera().getLocation(), this.avatar.getCamera().getDirection());

		CollisionResults results = new CollisionResults();
		module.getRootNode().collideWith(ray, results);

		CollisionResult result = results.getClosestCollision();
		if (result != null) {
			if (result.getDistance() <= MAX_RANGE) {
				Geometry g = result.getGeometry();
				AbstractPhysicalEntity ape = (AbstractPhysicalEntity)AbstractGameModule.getEntityFromSpatial(g);
				if (ape instanceof VoxelTerrainEntity) {
					VoxelTerrainEntity vte = (VoxelTerrainEntity)ape;
					BlockTerrainControl blocks = vte.blocks;
					Vector3f position = result.getContactPoint();
					Vector3Int blockPosition = blocks.getPointedBlockLocation(position);
					Settings.p("Clicked on " + blockPosition +  "(collision point: " + position + ")");
					if (blockPosition.getY() > 0) {
						IBlock block = blocks.getBlock(blockPosition);
						if (block instanceof StoneBlock) {
							blocks.removeBlock(blockPosition);
							//todo player.resources++;
						}
					} else {
						Settings.p("Cannot remove floor!");
					}
				} else {
					Settings.p(ape + " selected");
				}
			} else {
				Settings.p("Too far away");
			}
		}
		return true;
	}


	@Override
	public boolean onlyActivateOnClick() {
		return true;
	}


	@Override
	public String getHudText() {
		return "[Remove Blocks]";
	}

}
