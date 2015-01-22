package fr.alex.games.box2d.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

public class NormalMapComponent extends Component {
	public final static String name = "normal";
	protected Texture diffuse;
	protected Texture normal;	

	public NormalMapComponent(Entity entity, Texture diffuse, Texture normal) {
		super(entity);
		this.diffuse = diffuse;
		this.normal = normal;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		normal.bind(1);
		diffuse.bind(0);
	}

	@Override
	public void contact(Entity entity, Contact contact) {
		
	}

	@Override
	public String getName() {
		return name;
	}

}
