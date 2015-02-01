package fr.alex.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gushikustudios.rube.loader.RubeSceneLoader;

public class AM {
	public static final String SCENES_PATH = "scenes/";
	public static final String SCENES_ATLAS_PATH = "scenes/atlas/";	
	public static final String SPINES_ATLAS = "spines/spines.atlas";
	
	
	public static String diffuse(String altasName){
		return altasName.replace(".png", "_d.png");
	}
	
	public static String normal(String altasName){
		return altasName.replace(".png", "_n.png");
	}
	
	
	
	
	
	public static TextureAtlas getSpineAtlas(){
		return GM.assetManager.get(getSpineAtlasFile(), TextureAtlas.class);
	}
	
	public static String getSpineAtlasFile(){
		return SPINES_ATLAS;
	}
	
	public static Texture getSpineDiffuse(){
		return GM.assetManager.get(getSpineDiffuseFile(), Texture.class);
	}
	
	public static String getSpineDiffuseFile(){
		return diffuse(SPINES_ATLAS.replace(".atlas", ".png"));
	}
	
	public static Texture getSpineNormal(){
		return GM.assetManager.get(getSpineNormalFile(), Texture.class);
	}
	
	public static String getSpineNormalFile(){
		return normal(SPINES_ATLAS.replace(".atlas", ".png"));
	}
	
	
	
	
	
	
	public static TextureAtlas getSceneAtlas(){
		return GM.assetManager.get(getSceneAtlasFile(), TextureAtlas.class);
	}
	
	public static String getSceneAtlasFile(){
		return SCENES_ATLAS_PATH + GM.level.getSceneAtlasFile();
	}
	
	public static Texture getSceneDiffuse(){
		return GM.assetManager.get(getSceneDiffuseFile(), Texture.class);
	}
	
	public static String getSceneDiffuseFile(){
		return diffuse(getSceneAtlasFile().replace(".atlas", ".png"));
	}
	
	public static Texture getSceneNormal(){
		return GM.assetManager.get(getSceneNormalFile(), Texture.class);
	}
	
	public static String getSceneNormalFile(){
		return normal(getSceneAtlasFile().replace(".atlas", ".png"));
	}
	
	public static void loadScene(){
		RubeSceneLoader loader = new RubeSceneLoader();
		GM.scene = loader.loadScene(Gdx.files.internal(SCENES_PATH + GM.level.getSceneFile()));
		
		GM.assetManager.load(getSceneAtlasFile(), TextureAtlas.class);
		GM.assetManager.load(getSceneDiffuseFile(), Texture.class);
		GM.assetManager.load(getSceneNormalFile(), Texture.class);
		
		GM.assetManager.load(getSpineAtlasFile(), TextureAtlas.class);
		GM.assetManager.load(getSpineDiffuseFile(), Texture.class);
		GM.assetManager.load(getSpineNormalFile(), Texture.class);
	}
}
