package fr.alex.games.box2d.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.StickyInfo;
import fr.alex.games.screens.GameScreen;

public class Stick extends Component{
	public final static String name = "stick";
	public boolean sticked;
	
	public Stick(Entity entity) {
		super(entity);
	}	

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer,
			float delta) {
		
	}

	@Override
	public void contact(Entity entity, Contact contact) {
		if(!sticked && entity.contains(Stickable.name)){
			Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
			contact.setEnabled(false);
			Body stickBody = contact.getFixtureA().getBody();
			Body stickableBody = contact.getFixtureB().getBody();
			GameScreen.arrowToStick.add(new StickyInfo(stickBody, stickableBody, stickBody.getWorldCenter()));
			sticked = true;
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
}
