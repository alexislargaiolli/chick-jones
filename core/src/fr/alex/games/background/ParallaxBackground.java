package fr.alex.games.background;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import fr.alex.games.GM;
import fr.alex.games.Utils;

public class ParallaxBackground implements com.badlogic.gdx.utils.Json.Serializable{

	private ParallaxLayer[] layers;
	private Camera camera;
	private SpriteBatch batch;
	private Vector2 speed = new Vector2();
	float width, height;

	/**
	 * @param layers
	 *            The background layers
	 * @param width
	 *            The screenWith
	 * @param height
	 *            The screenHeight
	 * @param speed
	 *            A Vector2 attribute to point out the x and y speed
	 */
	public ParallaxBackground(ParallaxLayer[] layers, float width, float height, Vector2 speed) {
		this.width = width;
		this.height = height;
		this.layers = layers;
		this.speed.set(speed);
		camera = new OrthographicCamera(width * .5f * GM.ratio, height * .5f* GM.ratio);
		this.batch = new SpriteBatch();
	}

	public ParallaxBackground() {
		super();
	}

	public void render(float delta) {
		this.camera.position.add(speed.x * Utils.BOX_WORLD_TO, speed.y* Utils.BOX_WORLD_TO, 0);
		if(MathUtils.isZero(speed.x)){
			speed.x = 0;
		}
		if(MathUtils.isZero(speed.y)){
			speed.y = 0;
		}
		batch.setProjectionMatrix(camera.projection);
		batch.begin();
		for (ParallaxLayer layer : layers) {
			
			float currentX = -camera.position.x * layer.parallaxRatio.x % (width + layer.padding.x);

			if (speed.x < 0){
				currentX += -(width + layer.padding.x);
			}
			do {
				float currentY = -camera.position.y * layer.parallaxRatio.y % (height + layer.padding.y);
				if (speed.y < 0){
					//currentY += -(height + layer.padding.y);
				}
				//do {
					batch.draw(layer.region, -this.camera.viewportWidth / 2 + currentX + layer.startPosition.x, -this.camera.viewportHeight / 2 + currentY + layer.startPosition.y);
					//currentY += (height + layer.padding.y);
				//} while (currentY <= camera.viewportHeight);
				currentX += (width + layer.padding.x);
			} while (currentX < camera.viewportWidth);			
		}
		batch.end();
		batch.flush();
		
	}

	public Vector2 getSpeed() {
		return speed;
	}

	public void setSpeed(float x, float y) {
		this.speed.set(x, y);
	}

	@Override
	public void write(Json json) {
		json.writeValue("width", width);
		json.writeValue("height", height);
		json.writeValue("speedX", speed.x);
		json.writeValue("speedY", speed.y);
		json.writeValue("layers", layers);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		this.width = jsonData.getFloat("width");
		this.height = jsonData.getFloat("height");
		this.speed = new Vector2();
		this.speed.x = jsonData.getFloat("speedX");
		this.speed.y = jsonData.getFloat("speedY");
		this.layers = json.readValue("layers", ParallaxLayer[].class, jsonData);
		
		camera = new OrthographicCamera(width, height);
		this.batch = new SpriteBatch();
	}	

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}