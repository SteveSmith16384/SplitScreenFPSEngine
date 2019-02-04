package com.scs.splitscreenfpsengine.entities;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.components.IExpiringEffect;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class ParticleShockwave extends AbstractPhysicalEntity implements IExpiringEffect {

	private float timeRemaining = 1.8f; // Just enough time to show one	

	public ParticleShockwave(SplitScreenFpsEngine _game, AbstractGameModule _module, Vector3f pos) {
		super(_game, _module, "ParticleShockwave");
		
		Settings.p(this + " at " + pos);

		ParticleEmitter shockwave = new ParticleEmitter("Shockwave", ParticleMesh.Type.Triangle, 1);
		//       shockwave.setRandomAngle(true);
		shockwave.setFaceNormal(Vector3f.UNIT_Y);
		shockwave.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, (float)0.8f));
		shockwave.setEndColor(new ColorRGBA(.48f, 0.17f, 0.01f, 0f));

		shockwave.setStartSize(0f);
		shockwave.setEndSize(7f);

		shockwave.setParticlesPerSec(1);
		shockwave.setGravity(0, 0, 0);
		shockwave.setLowLife(0.5f);
		shockwave.setHighLife(0.5f);
		shockwave.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
		shockwave.getParticleInfluencer().setVelocityVariation(0f);
		shockwave.setImagesX(1);
		shockwave.setImagesY(1);
		Material mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", game.getAssetManager().loadTexture("Effects/Explosion/shockwave.png"));
		shockwave.setMaterial(mat);

		mainNode.attachChild(shockwave);
		shockwave.emitAllParticles();

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
		this.markForRemoval();

	}

}
