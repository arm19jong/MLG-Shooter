package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.main.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MyGdxGame.V_WIDTH * MyGdxGame.SCALE;
		config.height = MyGdxGame.V_HEIGHT * MyGdxGame.SCALE;
		config.title = MyGdxGame.TITLE;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
