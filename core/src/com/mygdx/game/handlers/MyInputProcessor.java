package com.mygdx.game.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by jongzazaal on 14/10/2558.
 */
public class MyInputProcessor  extends InputAdapter{
    public  boolean keyDown(int k) {
        if (k == Keys.UP) {
            MyInput.setKeys(MyInput.BUTTON1, true);
        }
        if (k == Keys.X) {
            MyInput.setKeys(MyInput.BUTTON2, true);
        }
        if (k == Keys.LEFT) {
            MyInput.setKeys(MyInput.BUTTON_L, true);
        }
        if (k == Keys.RIGHT) {
            MyInput.setKeys(MyInput.BUTTON_R, true);
        }
        return true;
    }

    public boolean keyUp(int k){
        if (k == Keys.UP) {
            MyInput.setKeys(MyInput.BUTTON1, false);
        }
        if (k == Keys.X) {
            MyInput.setKeys(MyInput.BUTTON2, false);
        }
        if (k == Keys.LEFT) {
            MyInput.setKeys(MyInput.BUTTON_L, false);
        }
        if (k == Keys.RIGHT) {
            MyInput.setKeys(MyInput.BUTTON_R, false);
        }
        return true;
    }
}
