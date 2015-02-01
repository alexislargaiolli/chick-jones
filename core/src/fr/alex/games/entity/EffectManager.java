package fr.alex.games.entity;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

import fr.alex.games.Utils;

public class EffectManager {
	public static final String PARTICLES_PATH = "particles/";

	private static EffectManager instance;	

	public static EffectManager get() {
		if (instance == null)
			instance = new EffectManager();
		return instance;
	}

	private SpriteBatch batch;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	private HashMap<Effect, ParticleEffectPool> effectPools;

	private EffectManager() {
		
		effectPools = new HashMap<Effect, ParticleEffectPool>();
		for(Effect e : Effect.values()){
			ParticleEffect fEffect = new ParticleEffect();
			fEffect.load(Gdx.files.internal(PARTICLES_PATH + e.getFile()), Gdx.files.internal(""));
			ParticleEffectPool effectPool = new ParticleEffectPool(fEffect, 1, 2);
			effectPools.put(e, effectPool);
		}		
		
		batch = new SpriteBatch();
	}

	public void draw(Matrix4 m, float delta) {
		batch.setProjectionMatrix(m);
		batch.begin();
		for (int i = effects.size - 1; i >= 0; i--) {
			PooledEffect effect = effects.get(i);
			effect.draw(batch, delta);
			if (effect.isComplete()) {
				effect.free();
				effects.removeIndex(i);
			}
		}
		batch.end();
	}

	public PooledEffect effect(Effect e, float x, float y) {
		PooledEffect effect = effectPools.get(e).obtain();		
		
		effect.setPosition(Utils.toWorld(x), Utils.toWorld(y));
		effects.add(effect);
		return effect;
	}	
	
}
