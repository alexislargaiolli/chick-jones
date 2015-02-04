package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.entity.Effect;
import fr.alex.games.entity.EffectManager;

/**
 * Add particle effect when an entity is destroyed
 * @author alex
 *
 */
public class DestoryedEffect extends Component{
	public final static String name = "effect";
	private Effect effect;
	private boolean effectPooled;
	private boolean trigger;

	public DestoryedEffect(Entity entity, Effect effect) {
		super(entity);
		entity.addListner(this);
		this.effect = effect;
	}

	@Override
	public void update(float delta) {
		if (trigger && !effectPooled) {
			EffectManager.get().effect(effect, this.entity.getPosition().x, this.entity.getPosition().y);
			effectPooled = true;
		}
	}
	
	@Override
	public void onEvent(ComponentEvent event) {
		trigger = true;
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		
	}
	
	@Override
	public String getName() {
		return name;
	}
	
}
