package com.mygdx.game.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.main.MyGdxGame;

/**
 * Created by jongzazaal on 21/10/2558.
 */
public class Crystal extends B2DSprite {
    public Crystal(Body body){
        super(body);

        Texture tex = MyGdxGame.res.getTexture("crystal");
        TextureRegion[] sprites = TextureRegion.split(tex, 16, 16)[0];

        setAnimation(sprites, 1 / 12f);
    }
}
