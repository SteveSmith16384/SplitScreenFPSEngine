package com.scs.multiplayervoxelworld.map;

import com.jme3.scene.Node;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.modules.GameModule;
import com.scs.multiplayervoxelworld.shapes.CreateShapes;

public class SimpleMapLoader implements IMapLoader {

	private MultiplayerVoxelWorldMain game;
	private GameModule module;
	private Node rootNode;
	private ISimpleMapData map;
	
	public SimpleMapLoader(MultiplayerVoxelWorldMain _game, GameModule _module, ISimpleMapData _data) {
		game = _game;
		module = _module;
		this.map = _data;
		this.rootNode = game.getRootNode();
		
	}


	public IPertinentMapData loadMap() {
		//ISimpleMapData map = new BoxMap(game, module); //EmptyMap(game);//

		// Floor first
		for (int z=0 ; z<map.getDepth() ; z+= Settings.FLOOR_SECTION_SIZE) {
			for (int x=0 ; x<map.getWidth() ; x+= Settings.FLOOR_SECTION_SIZE) {
				//p("Creating floor at " + x + "," + z);
				CreateShapes.CreateFloorTL(game.getAssetManager(), module.bulletAppState, this.rootNode, x, 0f, z, Settings.FLOOR_SECTION_SIZE, 0.1f, Settings.FLOOR_SECTION_SIZE, "Textures/sandstone.png");
			}			
		}

		// Now add scenery
		for (int z=0 ; z<map.getDepth() ; z++) {
			for (int x=0 ; x<map.getWidth() ; x++) {
				int code = map.getCodeForSquare(x, z);
				switch (code) {
				case Settings.MAP_NOTHING:
					break;


				default:
					//Settings.p("Ignoring map code " + code);
					//throw new RuntimeException("Unknown type:" + code);
				}
			}
		}
		
		map.addMisc();
		
		return (IPertinentMapData)map;

	}


}
