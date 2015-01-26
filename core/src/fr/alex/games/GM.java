package fr.alex.games;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gushikustudios.rube.RubeScene;

public class GM {
	public static AssetManager assetManager;
	public static World world;
	public static OrthographicCamera camera;
	public static Skin skin;
	public static int gold;
	public static int stars;
	public static TextureAtlas commonAtlas;
	public static TextureAtlas itemsAtlas;
	public static TextureAtlas bgAtlas;
	public static Level level;
	public static RubeScene scene;
	public static float ratio;
	public static int arrowFiredCount;
	public static int hitCount;
	public static int maxArrowCount;
	public static int arrowCount;
	/**
	 * Delta x for camera position relative to player position
	 */
	public static float cameraDeltaX = 0f;

	/**
	 * Delta y for camera position relative to player position
	 */
	public static float cameraDeltaY = 1.5f;
}
