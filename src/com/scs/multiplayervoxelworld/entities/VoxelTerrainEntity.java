package com.scs.multiplayervoxelworld.entities;

import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.scs.multiplayervoxelworld.BlockCodes;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.modules.GameModule;

import mygame.BlockSettings;
import mygame.blocks.BlockTerrainControl;
import mygame.blocks.ChunkControl;
import mygame.blocks.IBlockTerrainListener;
import mygame.util.Vector3Int;

public class VoxelTerrainEntity extends AbstractPhysicalEntity implements IProcessable {

	public BlockTerrainControl blocks;
	private float blockSize;

	public VoxelTerrainEntity(MultiplayerVoxelWorldMain _game, GameModule _module, float x, float y, float z, Vector3Int worldSizeBlocks, int chunkSize, float _blockSize, float friction) {
		super(_game, _module, "VoxelTerrainEntity");

		blockSize = _blockSize;

		final BlockSettings blockSettings = new BlockSettings();
		blockSettings.setChunkSize(new Vector3Int(chunkSize, chunkSize, chunkSize));
		blockSettings.setBlockSize(blockSize);
		blockSettings.setMaterial(game.getAssetManager().loadMaterial("Materials/BlockTexture.j3m"));
		worldSizeBlocks.set(worldSizeBlocks.getX() / chunkSize + 1, worldSizeBlocks.getY() / chunkSize + 1, worldSizeBlocks.getZ() / chunkSize + 1);
		blockSettings.setWorldSizeInChunks(worldSizeBlocks);//new Vector3Int(s+1, s+1, s+1));
		blockSettings.texturesPerSheet = Settings.TEX_PER_SHEET;

		blocks = new BlockTerrainControl(blockSettings);

		this.getMainNode().addControl(blocks);
		this.getMainNode().setLocalTranslation(x, y, z);

		blocks.addListener(new IBlockTerrainListener() {

			@Override
			public void onChunkUpdated(ChunkControl c) {
				Geometry geom = c.getGeometry();
				RigidBodyControl control = geom.getControl(RigidBodyControl.class);
				if(control == null){
					control = new RigidBodyControl(0);
					geom.addControl(control);
					module.bulletAppState.getPhysicsSpace().add(control);
					control.setFriction(friction); // So players can jump up it easily
					//control.setRestitution(restitution); // So players can jump up it easily
				}
				control.setCollisionShape(new MeshCollisionShape(geom.getMesh()));
			}
		});
	}


	public void removeBlock(Vector3Int pos) {
		Vector3Int blockPosition = blocks.getPointedBlockLocation(pos);
		//Globals.p("Removing block at " + blockPosition);
		blocks.removeBlock(blockPosition);

	}


	public void addBlock_Actual(Vector3Int pos, int blockType) {
		Vector3Int blockPosition = blocks.getPointedBlockLocation(pos);
		//Globals.p("Adding block at " + blockPosition);
		blocks.setBlock(blockPosition, BlockCodes.getClassFromCode(blockType));

	}


	public void addBlock_Block(Vector3Int pos, int blockType) {
		//Vector3Int blockPosition = blocks.getPointedBlockLocation(pos);//, false);
		//Globals.p("Adding block at " + blockPosition);
		blocks.setBlock(pos, BlockCodes.getClassFromCode(blockType));

	}


	public void addRectRange_Actual(int blockType, Vector3Int pos, Vector3Int size) {
		int scale = (int)(1/blockSize);
		pos.multLocal(scale);
		size.multLocal(scale);
		this.addRectRange_Blocks(blockType, pos, size);
	}


	public void addRectRange_Blocks(int blockType, Vector3Int blockPos, Vector3Int size) {
		blocks.setBlockArea(blockPos, size, BlockCodes.getClassFromCode(blockType));

	}


	public void addArrayRange_Blocks(int blockType, Vector3Int blockPos, int[][] heights) {
		blocks.setBlockHeightsFromArray(blockPos, heights, BlockCodes.getClassFromCode(blockType));
	}


	public void addSphereRange_Blocks(int blockType, Vector3f worldPos, int size) {
		Vector3Int blockPos = new Vector3Int(worldPos.subtract(this.mainNode.getWorldTranslation()).multLocal(1/blockSize));
		blocks.setBlockAreaBySphere(blockPos, size, BlockCodes.getClassFromCode(blockType));
	}


	public void removeSphereRange_Blocks(Vector3f worldPos, int size) {
		Vector3Int blockPos = new Vector3Int(worldPos.subtract(this.mainNode.getWorldTranslation()).multLocal(1/blockSize));
		blocks.setBlockAreaBySphere(blockPos, size, null);
	}


	@Override
	public void process(float tpf) {
		// Do nothing for now
	}


}
