package com.scs.multiplayervoxelworld.entities.nonphysical;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.blocks.GrassBlock;
import com.scs.multiplayervoxelworld.blocks.LavaBlock;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.entities.AbstractEntity;
import com.scs.multiplayervoxelworld.entities.VoxelTerrainEntity;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;

import mygame.blocks.IBlock;
import mygame.util.Vector3Int;

public class ChangeBlocksInSweep extends AbstractEntity implements IProcessable {

	private int currentX = 0;
	private int maxZ;
	private VoxelTerrainEntity vte;

	public ChangeBlocksInSweep(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, int _maxZ) {
		super(_game, _module, "ChangeBlocksInSweep");

		vte = module.getVoxelTerrainEntity();
		maxZ = _maxZ;
		
		module.addEntity(this);
	}

	@Override
	public void actuallyAdd() {
		// Do nothing

	}

	@Override
	public void actuallyRemove() {
		// Do nothing

	}

	@Override
	public void process(float tpfSecs) {
		for (int z=0 ; z<maxZ ; z++) {
			Vector3Int pos = new Vector3Int(currentX, 0, z);
			IBlock id = vte.blocks.getBlock(pos);
			if (id instanceof GrassBlock) {
				vte.blocks.setBlock(pos, LavaBlock.class);
			}
		}
		currentX++;

		if (currentX >= maxZ) {
			this.markForRemoval();
		}
	}

}
