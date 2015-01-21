package fr.alex.games.screens;


public enum Screens {

	INTRO {
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance() {
			return new IntroScreen();
		}
	},

	MAIN_MENU {
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance() {
			return new MainMenuScreen();
		}
	},

	LEVEL_MENU {
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance() {
			return new LevelsScreen();
		}
	},
	
	LOADING {
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance() {
			return new LoadingScreen();
		}
	},
	
	GAME {
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance() {
			return new GameScreen();
		}
	},
	
	SHOP {
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance() {
			return new ShopScreen();
		}
	},
	
	PLAYER {
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance() {
			return new PlayerScreen();
		}
	},
	
	CREDITS {
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance() {
			/*return new CreditsScreen();*/
			return null;
		}
	};

	protected abstract com.badlogic.gdx.Screen getScreenInstance();

}