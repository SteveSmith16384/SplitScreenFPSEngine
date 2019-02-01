package com.scs.splitscreenfpsengine.entities;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.components.ICanShoot;
import com.scs.splitscreenfpsengine.components.IExpiringEffect;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class ParticleCastEffect extends AbstractPhysicalEntity implements IExpiringEffect {

	private float timeRemaining = 1;

	public ParticleCastEffect(SplitScreenFpsEngine _game, AbstractGameModule _module, ICanShoot wiz) {
		super(_game, _module, "ParticleCastEffect");

		ParticleEmitter magic = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
		Material mat_red = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
		//mat_red.setTexture("Texture", game.getAssetManager().loadTexture("Textures/flame.png"));
		mat_red.setTexture("Texture", game.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
		magic.setMaterial(mat_red);
		magic.setImagesX(2);
		magic.setImagesY(2); // 2x2 texture animation
		magic.setEndColor(  new ColorRGBA(0f, 1f, 0f, 1f));
		magic.setStartColor(new ColorRGBA(1f, 0f, 1f, 0.5f));
		//fire.getParticleInfluencer().setInitialVelocity(new Vector3f(2, 0, 0));
		magic.getParticleInfluencer().setInitialVelocity(wiz.getShootDir().mult(1));
		magic.setStartSize(.5f);
		magic.setEndSize(0.1f);
		magic.setGravity(0, 0, 0);
		magic.setLowLife(1f);
		magic.setHighLife(3f);
		magic.getParticleInfluencer().setVelocityVariation(0.1f);
		
		mainNode.attachChild(magic);
		mainNode.setLocalTranslation(wiz.getBulletStartPosition());
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
