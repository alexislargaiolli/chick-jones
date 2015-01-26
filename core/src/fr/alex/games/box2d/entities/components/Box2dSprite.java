package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;

public class Box2dSprite extends Component {
	public final static String name = "sprite";
	protected Sprite sprite;
	protected Body body;
	protected boolean toDestroy;
	protected final Vector2 center = new Vector2();
	protected final Vector2 halfSize = new Vector2();
	protected float rotation;
	protected Vector2 tmp = new Vector2();

	public Box2dSprite(Entity entity, TextureRegion region, boolean flip, Body body, Color color, Vector2 size, Vector2 center, float rotationInDegrees) {
		super(entity);
		entity.addListner(this);
		this.sprite = new Sprite(region);
		this.body = body;
		this.sprite.flip(flip, false);
		this.sprite.setColor(color);
		this.rotation = rotationInDegrees;
		this.sprite.setSize(size.x, size.y);
		this.sprite.setOrigin(size.x / 2, size.y / 2);
		this.halfSize.set(size.x / 2, size.y / 2);
		this.center.set(center);

		if (body != null) {
			this.body.setUserData(entity);
			this.tmp.set(body.getPosition());
			this.sprite.setPosition(tmp.x - size.x / 2, tmp.y - size.y / 2);

			float angle = this.body.getAngle() * MathUtils.radiansToDegrees;
			this.tmp.set(this.center).rotate(angle).add(this.body.getPosition()).sub(halfSize);
			this.sprite.setRotation(this.rotation + angle);
		} else {
			this.tmp.set(center.x - size.x / 2, center.y - size.y / 2);
			this.sprite.setRotation(rotationInDegrees);
		}

		this.sprite.setPosition(this.tmp.x, this.tmp.y);
		entity.setPosition(this.tmp.x, this.tmp.y);
	}

	@Override
	public void update(float delta) {
		if (body != null) {
			float angle = body.getAngle() * MathUtils.radiansToDegrees;
			tmp.set(center).rotate(angle).add(body.getPosition()).sub(halfSize);
			sprite.setPosition(tmp.x, tmp.y);
			entity.setPosition(tmp.x, tmp.y);
			sprite.setRotation(rotation + angle);
			if (toDestroy && !entity.isToRemove()) {
				destroy();
				this.entity.setToRemove(true);
			}
		}
	}

	@Override
	public void onEvent(ComponentEvent event) {
		if (event.getType() == EventType.DESTROY) {
			toDestroy = true;
		}
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		sprite.draw(batch);
	}

	@Override
	public void contact(Entity entity, Contact contact) {

	}

	public void destroy() {
		GM.scene.getWorld().destroyBody(body);
	}

	public void flipX() {
		sprite.setFlip(!sprite.isFlipX(), sprite.isFlipY());
	}

	@Override
	public String getName() {
		return name;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
