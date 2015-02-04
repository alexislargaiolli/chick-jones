package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.Entity;

public class NormalMap extends Component {
	public final static String name = "normal";
	protected Texture diffuse;
	protected Texture normal;	

	public NormalMap(Entity entity, Texture diffuse, Texture normal) {
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
	public String getName() {
		return name;
	}

}
