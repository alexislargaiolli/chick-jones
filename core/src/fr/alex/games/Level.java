package fr.alex.games;

public enum Level {
	LEVEL_1(1, "level-1.json", "scene1.atlas", "egypt.atlas"),
	LEVEL_2(2, "level-2.json", "scene1.atlas", "egypt.atlas"),
	LEVEL_3(3, "level-3.json", "scene1.atlas", "egypt.atlas"),
	LEVEL_4(4, "level-4.json", "scene1.atlas", "egypt.atlas"),
	LEVEL_5(5, "level-5.json", "scene1.atlas", "egypt.atlas");

	private int index;
	private String sceneFile, sceneAtlasFile, backgroundFile;	

	private Level(int index, String sceneFile, String sceneAtlasFile, String backgroundFile) {
		this.index = index;
		this.sceneFile = sceneFile;
		this.backgroundFile = backgroundFile;
		this.sceneAtlasFile = sceneAtlasFile;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getSceneFile() {
		return sceneFile;
	}

	public void setSceneFile(String sceneFile) {
		this.sceneFile = sceneFile;
	}

	public String getBackgroundFile() {
		return backgroundFile;
	}

	public void setBackgroundFile(String backgroundFile) {
		this.backgroundFile = backgroundFile;
	}

	public String getSceneAtlasFile() {
		return sceneAtlasFile;
	}

	public void setSceneAtlasFile(String sceneAtlasFile) {
		this.sceneAtlasFile = sceneAtlasFile;
	}
}
