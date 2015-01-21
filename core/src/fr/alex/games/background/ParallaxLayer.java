package fr.alex.games.background;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import fr.alex.games.GM;

public class ParallaxLayer implements com.badlogic.gdx.utils.Json.Serializable {
	public TextureRegion region;
	public String regionName;
	public Vector2 parallaxRatio;
	public Vector2 startPosition;
	public Vector2 padding;

	public ParallaxLayer(String regionName, Vector2 parallaxRatio, Vector2 padding) {
		this(regionName, parallaxRatio, new Vector2(0, 0), padding);
	}

	/**
	 * @param region
	 *            the TextureRegion to draw , this can be any width/height
	 * @param parallaxRatio
	 *            the relative speed of x,y
	 *            {@link ParallaxBackground#ParallaxBackground(ParallaxLayer[], float, float, Vector2)}
	 * @param startPosition
	 *            the init position of x,y
	 * @param padding
	 *            the padding of the region at x,y
	 */
	public ParallaxLayer(String regionName, Vector2 parallaxRatio, Vector2 startPosition, Vector2 padding) {
		this.regionName = regionName;
		this.region = GM.bgAtlas.findRegion(regionName);
		this.parallaxRatio = parallaxRatio;
		this.startPosition = startPosition;
		this.padding = padding;
	}
	
	public ParallaxLayer() {
		super();
	}

	@Override
	public void write(Json json) {
		json.writeValue("name", regionName);
		json.writeValue("paralaxX", parallaxRatio.x);
		json.writeValue("paralaxY", parallaxRatio.y);
		json.writeValue("startX", startPosition.x);
		json.writeValue("startY", startPosition.y);
		json.writeValue("padX",padding.x);
		json.writeValue("padY",padding.y);
	}
	
	@Override
	public void read(Json json, JsonValue jsonData) {
		this.regionName = jsonData.getString("name");
		this.region = GM.bgAtlas.findRegion(regionName);
		this.parallaxRatio = new Vector2();
		this.parallaxRatio.x = jsonData.getFloat("paralaxX");
		this.parallaxRatio.y = jsonData.getFloat("paralaxY");
		this.startPosition = new Vector2();
		this.startPosition.x = jsonData.getFloat("startX");
		this.startPosition.y = jsonData.getFloat("startY");
		this.padding = new Vector2();
		this.padding.x = jsonData.getFloat("padX");
		this.padding.y = jsonData.getFloat("padY");		
	}

}
