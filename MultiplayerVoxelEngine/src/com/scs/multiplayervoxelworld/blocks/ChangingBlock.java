package com.scs.multiplayervoxelworld.blocks;

import mygame.blocktypes.AbstractBlock;
import mygame.util.Vector3Int;

public class ChangingBlock extends AbstractBlock {

	public Class<? extends AbstractBlock> newClass;
	public Vector3Int pos;
	
	public ChangingBlock() {
		this(null, null);
	}
	
	
	public ChangingBlock(Class<? extends AbstractBlock> _newClass, Vector3Int _pos) {
		super(1, 0); // todo
		
		newClass = _newClass;
		pos = _pos;
	}

}
