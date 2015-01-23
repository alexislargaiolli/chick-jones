package fr.alex.games.box2d.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.entity.Effect;
import fr.alex.games.entity.EffectManager;

public class Destroyable extends Component {
	public final static String name = "destroyable";
	private Effect effect;
	private boolean effectPooled;

	public Destroyable(Entity entity, Effect effect) {
		super(entity);
		this.effect = effect;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		
	}

	@Override
	public void contact(Entity entity, Contact contact) {
		if (!effectPooled && entity.contains(Destroyer.name)) {
			EffectManager.get().effect(effect, this.entity.getPosition().x, this.entity.getPosition().y);
			effectPooled = true;
		}
	}

	@Override
	public String getName() {
		return name;
	}

}
