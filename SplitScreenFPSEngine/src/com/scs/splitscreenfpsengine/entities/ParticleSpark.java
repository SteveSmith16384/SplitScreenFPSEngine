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

public class ParticleSpark extends AbstractPhysicalEntity implements IExpiringEffect {

	private float timeRemaining = 3;

	public ParticleSpark(SplitScreenFpsEngine _game, AbstractGameModule _module, Vector3f pos) {
		super(_game, _module, "ParticleSpark");

		ParticleEmitter spark = new ParticleEmitter("Spark", ParticleMesh.Type.Triangle, 30 * 1);
        spark.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1.0f / 1)));
        spark.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        spark.setStartSize(.5f);
        spark.setEndSize(1.5f);
        spark.setFacingVelocity(true);
        //scs new spark.setParticlesPerSec(5);
        spark.setGravity(0, 5, 0);
        spark.setLowLife(1.1f);
        spark.setHighLife(1.5f);
        spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 20, 0));
        spark.getParticleInfluencer().setVelocityVariation(1);
        spark.setImagesX(1);
        spark.setImagesY(1);
        Material mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", game.getAssetManager().loadTexture("Effects/Explosion/spark.png"));
        spark.setMaterial(mat);
        
        mainNode.attachChild(spark);
		
		this.mainNode.setLocalTranslation(pos);
		module.addEntity(this);
		
		Settings.p(this.name + " at " + pos);
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
