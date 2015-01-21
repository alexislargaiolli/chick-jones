package fr.alex.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.IntMap;

public final class ScreenManager {
	 
    private static ScreenManager instance;
 
    private Game game;
 
    private IntMap<com.badlogic.gdx.Screen> screens;
 
    private ScreenManager() {
        screens = new IntMap<com.badlogic.gdx.Screen>();
    }
 
    public static ScreenManager getInstance() {
        if (null == instance) {
            instance = new ScreenManager();
        }
        return instance;
    }
 
    public void initialize(Game game) {
        this.game = game;
    }
 
    public void show(Screens screen) {
        if (null == game) return;
        if (!screens.containsKey(screen.ordinal())) {
            screens.put(screen.ordinal(), screen.getScreenInstance());
        }
        game.setScreen(screens.get(screen.ordinal()));
    }
    
    public Screen getScreen(Screens screen){
    	if (!screens.containsKey(screen.ordinal())) {
            screens.put(screen.ordinal(), screen.getScreenInstance());
        }
    	return screens.get(screen.ordinal());
    }
 
    public void dispose(Screens screen) {
        if (!screens.containsKey(screen.ordinal())) return;
        screens.remove(screen.ordinal()).dispose();
    }
 
    public void dispose() {
        for (com.badlogic.gdx.Screen screen : screens.values()) {
            screen.dispose();
        }
        screens.clear();
        instance = null;
    } 
}
