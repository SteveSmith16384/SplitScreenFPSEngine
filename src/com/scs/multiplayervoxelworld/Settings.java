package com.scs.multiplayervoxelworld;

public class Settings {

	public static final boolean RELEASE_MODE = false;
	public static final boolean RECORD_VID = false;

	// DEBUG
	public static final boolean DEBUG_NO_MAP = true;
	public static final boolean USE_MODEL_FOR_PLAYERS = false;

	// Unused debug
	public static final boolean DEBUG_SIMPLE_MAP = false;
	public static final boolean DEBUG_GAMEPAD_DIV_TPF = false;
	public static final boolean DEBUG_GAMEPAD_MULT_VALUE = false;	
	public static final boolean GAMEPAD_USE_AVG = false;
	public static final boolean DEBUG_GAMEPAD_TURNING = false;
	public static final boolean DEBUG_PYRAMID = false;
	public static final boolean DEBUG_SPELLS = false;
	public static final boolean DEBUG_WATCH_AI = false;
	public static final boolean DEBUG_DEATH = false;
	public static final boolean DEBUG_HUD = false;
	public static final boolean DEBUG_TARGETTER = false;

	public static final String VERSION = "0.01";
	public static final boolean ALWAYS_SHOW_4_CAMS = false;
	public static final boolean SHOW_FLASH_EXPLOSIONS = false;
	public static final boolean PLAYER1_IS_MOUSE = true;
	public static final int TEX_PER_SHEET = 2;

	public enum GameMode {Skirmish, KingOfTheHill, Dodgeball, Bladerunner, CloneWars, Evolve, Sorcerers } // Evolve & Sorcerers not finished (hardly started...)
	
	// Game settings
	public static GameMode GAME_MODE;
	public static boolean PVP = true;
	public static int NUM_AI = 0;
	public static int NUM_COLLECTABLES = 0;
	public static int NUM_SECTORS = 3;
	
	// Our movement speed
	public static final float PLAYER_MOVE_SPEED = 3f;
	public static final float JUMP_FORCE = 8f;

	public static final float CAM_DIST = 100f;
	//public static final int FLOOR_SECTION_SIZE = 12;
	public static final String NAME = "Multiplayer Voxel World";
	//public static final int CLONE_ID = 2; // Which model to use for clones
	
	// User Data
	public static final String ENTITY = "Entity";
	
	// Map codes
	public static final int MAP_NOTHING = 0;

	
	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ": " + s);
	}

	
}
