package com.scs.multiplayervoxelworld.blocks;

import java.io.IOException;

import com.scs.multiplayervoxelworld.Settings;

import mygame.texturesheet.TextureSheetGenerator;

public class GenerateMVWTextureSheet {
	
	private static final int TILE_SIZE_PIXELS = 64;

	public static void main(String[] args) {
		try {
			new GenerateMVWTextureSheet();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public GenerateMVWTextureSheet() throws IOException {
		String[][] tiles = {
				{"grass.jpg", "lavarock.jpg", "stone.png"}, 
				{"road2.png", "sand.jpg"}
		}; // Keep these in the same order!

		TextureSheetGenerator gen = new TextureSheetGenerator();
		gen.generateTextureSheet("assets/Textures/blocks", tiles, Settings.TEX_PER_SHEET, TILE_SIZE_PIXELS, "mvw_tiles", 4, true);

	}

}
