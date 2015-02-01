package fr.alex.games;

public enum Level {
	LEVEL_1(1, "level1.json", "level1.atlas"),
	LEVEL_2(2, "level2.json", "level1.atlas");

	private int index;
	private String sceneFile, sceneAtlasFile;

	private Level(int index, String sceneFile, String sceneAtlasFile) {
		this.index = index;
		this.sceneFile = sceneFile;
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

	public String getSceneAtlasFile() {
		return sceneAtlasFile;
	}

	public void setSceneAtlasFile(String sceneAtlasFile) {
		this.sceneAtlasFile = sceneAtlasFile;
	}
}
