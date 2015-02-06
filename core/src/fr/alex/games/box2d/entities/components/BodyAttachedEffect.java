package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.Utils;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;
import fr.alex.games.entity.Effect;
import fr.alex.games.entity.EffectManager;

/**
 * Add particle effect when an entity is destroyed
 * @author alex
 *
 */
public class BodyAttachedEffect extends Component{
	public final static String name = "attacheffect";
	private PooledEffect pooledEffect;
	private Body body;
	float x, y;

	public BodyAttachedEffect(Entity entity, Body body, Effect effect) {
		super(entity);
		entity.addListner(this);
		this.body = body;
		pooledEffect = EffectManager.get().effect(effect, this.entity.getPosition().x, this.entity.getPosition().y);
	}

	@Override
	public void update(float delta) {
		x = body.getPosition().x + MathUtils.cos(body.getAngle()) * .5f;
		y = body.getPosition().y + MathUtils.sin(body.getAngle()) * .5f;
		pooledEffect.setPosition(Utils.toWorld(x), Utils.toWorld(y));
	}
	
	@Override
	public void onEvent(ComponentEvent event) {
		if (event.getType() == EventType.DESTROY) {
			EffectManager.get().removed(pooledEffect);
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		
	}
	
	@Override
	public String getName() {
		return name;
	}
	
}
