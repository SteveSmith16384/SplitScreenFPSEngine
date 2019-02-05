package com.scs.splitscreenfpsengine.entities;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.components.IEntity;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public abstract class AbstractEntity implements IEntity, Savable {
	
	private static int nextId = 0;
	
	public final int id;
	protected SplitScreenFpsEngine game;
	protected AbstractGameModule module;
	public String name;
	private boolean markedForRemoval = false;

	protected AbstractEntity(SplitScreenFpsEngine _game, AbstractGameModule _module, String _name) {
		id = nextId++;
		game = _game;
		module = _module;
		name = _name;
		
		
	}


	@Override
	public String getName() {
		return name;
	}


	@Override
	public void actuallyAdd() {
		// Do nothing
		
	}


	@Override
	public void actuallyRemove() {
		if (!this.isMarkedForRemoval()) {
			throw new RuntimeException("You must mark " + this + " for removal!");
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

	
	/*
	@Override
	public String toString() {
		return "E_" + name + "_" + id;
	}
*/

	@Override
	public void write(JmeExporter ex) throws IOException {
		// Do nothing
	}


	@Override
	public void read(JmeImporter im) throws IOException {
		// Do nothing
	}

}
