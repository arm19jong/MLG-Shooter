package com.mygdx.game.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.B2DSprite;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.main.MyGdxGame;

/**
 * Created by jongzazaal on 14/10/2558.
 */
public abstract class GameState extends B2DSprite{
    protected GameStateManager gsm;
    protected MyGdxGame game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    public GameState(GameStateManager gsm){
        super();
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpriteBatch();
        cam = game.getCamera();
        hudCam = game.getHUDCamera();
    }
    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
