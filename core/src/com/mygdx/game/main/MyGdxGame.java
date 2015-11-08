package com.mygdx.game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.handlers.Content;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.MyInput;
import com.mygdx.game.handlers.MyInputProcessor;

//public class MyGdxGame extends ApplicationAdapter {
public class MyGdxGame implements ApplicationListener{
	SpriteBatch batch;
	Texture img;

	public static final String TITLE = "BBBBB";
	//public static final int V_WIDTH = 1024;
	//public static final int V_HEIGHT = 768;

	//public static final int V_WIDTH = 320;
	//public static final int V_HEIGHT = 240;

	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 600;

	public static final int SCALE = 1;

	public static final float STEP = 1/60f;
	private float accum;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;



	private GameStateManager gsm;

	public static Content res;
	@Override
	public void create () {
		Gdx.input.setInputProcessor(new MyInputProcessor());

		res = new Content();
		res.loadTexture("cha1.png", "bunny");
		res.loadTexture("crystal.png", "crystal");
		res.loadTexture("hud.png", "hud");
		

		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP){
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			MyInput.update();
		}
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
	}

	public void dispose(){}
	public SpriteBatch getSpriteBatch(){ return sb;}
	public OrthographicCamera getCamera(){ return cam;}
	public OrthographicCamera getHUDCamera(){ return hudCam;}

	public void resize(int w,int h){}

	public void pause(){}

	public void resume(){}
}
