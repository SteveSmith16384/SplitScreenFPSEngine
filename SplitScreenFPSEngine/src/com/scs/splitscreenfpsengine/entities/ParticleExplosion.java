package com.scs.splitscreenfpsengine.entities;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.components.IExpiringEffect;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class ParticleExplosion extends AbstractPhysicalEntity implements IExpiringEffect {

	private float timeRemaining = 2;	

	public ParticleExplosion(SplitScreenFpsEngine _game, AbstractGameModule _module, Vector3f pos) {
		super(_game, _module, "ParticleExplosion");

		ParticleEmitter debris = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
		Material debris_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
		debris_mat.setTexture("Texture", game.getAssetManager().loadTexture("Textures/Debris.png"));
		debris.setMaterial(debris_mat);
		debris.setImagesX(3);
		debris.setImagesY(3); // 3x3 texture animation
		debris.setRotateSpeed(4);
		debris.setSelectRandomImage(true);
		debris.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 4, 0));
		debris.setStartColor(ColorRGBA.White);
		debris.setGravity(0, 6, 0);
		debris.getParticleInfluencer().setVelocityVariation(.60f);
		mainNode.attachChild(debris);
		debris.emitAllParticles();
		
		this.mainNode.setLocalTranslation(pos);
		module.addEntity(this);
	}

	
	@Override
	public float getTimeRemaining() {
		return timeRemaining;
	}

	
	@Override
	public void setTimeRemaining(float t) {
		timeRemaining = t;
	}

	
	@Override
	public void effectFinished() {
		//module.markEntityForRemoval(this);
		this.markForRemoval();
	
	}

}
