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
import mygame.util.Vector3Int;

public class AddBlockAbility extends AbstractAbility {

	public AddBlockAbility(SplitScreenFpsEngine _game, AbstractGameModule module, AbstractPlayersAvatar p) {
		super(_game, module, p);
	}


	@Override
	public boolean process(float interpol) {
		return false;
	}


	@Override
	public boolean activate(float interpol) {
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
					BlockTerrainControl blocks = vte.blocks;
					Vector3f position = result.getContactPoint();
					Vector3Int blockPosition = blocks.getPointedBlockLocation(position, true);
					Settings.p("Adding to " + blockPosition);
					if (blocks.getBlock(blockPosition) == null) {
						vte.addBlock_Block(blockPosition, StoneBlock.class);
					}
				} else {
					Settings.p(ape + " selected");
				}
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
		return "[Add Blocks]";
	}

}
