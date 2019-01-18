package com.scs.samescreentowerdefence.abilities;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.abilities.AbstractAbility;
import com.scs.multiplayervoxelworld.blocks.StoneBlock;
import com.scs.multiplayervoxelworld.entities.AbstractPhysicalEntity;
import com.scs.multiplayervoxelworld.entities.AbstractPlayersAvatar;
import com.scs.multiplayervoxelworld.entities.VoxelTerrainEntity;
import com.scs.multiplayervoxelworld.modules.GameModule;

import mygame.blocks.BlockTerrainControl;
import mygame.blocks.IBlock;
import mygame.util.Vector3Int;

public class RemoveBlockAbility extends AbstractAbility {

	private static final float MAX_RANGE = 4;

	public RemoveBlockAbility(MultiplayerVoxelWorldMain _game, GameModule module, AbstractPlayersAvatar p) {
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
			if (result.getDistance() <= MAX_RANGE) {
				Geometry g = result.getGeometry();
				AbstractPhysicalEntity ape = (AbstractPhysicalEntity)GameModule.getEntityFromSpatial(g);
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
							player.resources++;
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
