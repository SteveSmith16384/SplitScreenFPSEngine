package com.scs.multiplayervoxelworld.entities;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.components.IEntity;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;

public abstract class AbstractEntity implements IEntity, Savable {
	
	private static int nextId = 0;
	
	public final int id;
	protected MultiplayerVoxelWorldMain game;
	protected AbstractGameModule module;
	public String name;
	private boolean markedForRemoval = false;

	public AbstractEntity(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, String _name) {
		id = nextId++;
		game = _game;
		module = _module;
		name = _name;
		
		
	}


	@Override
	public void actuallyRemove() {
		if (!this.isMarkedForRemoval()) {
			throw new RuntimeException("You must mark an item for removal!");
		}
	}


	public void markForRemoval() {
		if (!markedForRemoval) {
			markedForRemoval = true;
			module.markEntityForRemoval(this);
		}
	}


	public boolean isMarkedForRemoval() {
		return markedForRemoval;
	}

	
	
	@Override
	public String toString() {
		return "E_" + name + "_" + id;
	}


	@Override
	public void write(JmeExporter ex) throws IOException {
		
	}


	@Override
	public void read(JmeImporter im) throws IOException {
		
	}

}
