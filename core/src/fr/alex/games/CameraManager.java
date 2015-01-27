package fr.alex.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraManager {

	/**
	 * Camera
	 */
	private OrthographicCamera camera;

	private float ratio;

	private Vector2 targetPosition;

	private Vector2 center;

	private Vector2 tmp2;

	private Vector3 tmp3;

	public CameraManager(float viewportHeight, Vector2 targetPosition, Vector2 center) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		this.targetPosition = targetPosition;
		ratio = (float) Gdx.graphics.getHeight() / viewportHeight;
		tmp3 = new Vector3();
		tmp2 = new Vector2();
		camera = new OrthographicCamera(viewportHeight * w / h, viewportHeight);
		camera.position.set(targetPosition, 0);
		this.center = new Vector2(camera.viewportWidth * .5f - (center.x * camera.viewportWidth), camera.viewportHeight * .5f - (center.y * camera.viewportHeight));
	}

	public void update(float delta) {
		float deltaX = (targetPosition.x + center.x) - camera.position.x;
		float deltaY = (targetPosition.y + center.y) - camera.position.y;
		camera.translate(deltaX * .1f, deltaY * .1f);
		camera.update();
	}

	public Vector2 toWorld(Vector2 point) {
		tmp3.set(point, 0);
		tmp3 = camera.unproject(tmp3);
		return tmp2.set(tmp3.x, tmp3.y);
	}

	public Vector2 toWorld(float x, float y) {
		tmp3.set(x, y, 0);
		tmp3 = camera.unproject(tmp3);
		return tmp2.set(tmp3.x, tmp3.y);
	}

	public Vector2 toScreen(Vector2 point) {
		tmp3.set(point, 0);
		tmp3 = camera.project(tmp3);
		return tmp2.set(tmp3.x, tmp3.y);
	}

	public Vector2 toScreen(float x, float y) {
		tmp3.set(x, y, 0);
		tmp3 = camera.project(tmp3);
		return tmp2.set(tmp3.x, tmp3.y);
	}

	public Matrix4 combined() {
		return camera.combined;
	}

	public Matrix4 combined(float scaleX, float scaleY) {
		return camera.combined.cpy().scale(scaleX, scaleY, 0);
	}

	public boolean isInScreen(float x, float y) {
		return x > (camera.position.x - camera.viewportWidth * 1.5f) && x < (camera.position.x + camera.viewportWidth * .75f);
	}

	public boolean isInScreen(Vector2 position) {
		return isInScreen(position.x, position.y);
	}

	public void setTargetX(float x) {
		targetPosition.x = x;
	}

	public void setTargetY(float y) {
		targetPosition.y = y;
	}
	
	public float getScreenCenterX(){
		return (camera.viewportWidth * .5f - center.x) * ratio;
	}
	
	public float getScreenCenterY(){
		return (camera.viewportHeight * .5f - center.y) * ratio;
	}

	public float getX() {
		return camera.position.x;
	}

	public float getY() {
		return camera.position.y;
	}

	public float getCenterX() {
		return center.x;
	}

	public float getCenterY() {
		return center.y;
	}

	public float getRatio() {
		return ratio;
	}

}
