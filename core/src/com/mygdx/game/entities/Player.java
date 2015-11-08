package com.mygdx.game.entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.main.MyGdxGame;

/**
 * Created by jongzazaal on 20/10/2558.
 */
public class Player extends B2DSprite {
    private int numCrystals;
    private int totalCrystals;

    public Player(Body body){
        super(body);

        Texture tex = MyGdxGame.res.getTexture("bunny");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[2];
        setAnimation(sprites, 3 / 12f);

    }

    public void collectCrystal(){numCrystals++;}
    public int getNumCrystals(){return numCrystals;}
    public void setTotalCrystals(int i) {totalCrystals = i;}
    public int getTotalCrystals(){return totalCrystals;}
}
