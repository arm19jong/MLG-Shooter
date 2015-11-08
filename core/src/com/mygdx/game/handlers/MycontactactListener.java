package com.mygdx.game.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import java.awt.event.ContainerListener;

/**
 * Created by jongzazaal on 14/10/2558.
 */
public class MycontactactListener implements ContactListener {

    private int numFootContact;
    private Array<Body> bodiesToRemove;

    public MycontactactListener(){
        super();
        bodiesToRemove = new Array<Body>();
    }
    //called when two fixtures start to collide
    public void beginContact(Contact c){
        //System.out.println("Begin Contact");
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        //System.out.println("End Contact");
        if(fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")){
            //System.out.print("fa is foot");
            numFootContact++;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")){
            //System.out.print("fb is foot");
            numFootContact++;
        }

        if (fa.getUserData() != null && fa.getUserData().equals("crystal")){
            //remove crystal
           // System.out.println("remove");
            bodiesToRemove.add(fa.getBody());

        }
        if (fb.getUserData() != null && fb.getUserData().equals("crystal")) {
            //remove crystal
            bodiesToRemove.add(fb.getBody());
        }
    }

    //called when two fixtures no longer collide
    public void endContact(Contact c){
        //System.out.println("Begin Contact");
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        //System.out.println("End Contact");

        if(fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")){
            //System.out.print("fa is foot");
            numFootContact--;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")){
            //System.out.print("fb is foot");
            numFootContact--;
        }

    }
    public boolean isPlayerOnGround(){return numFootContact > 0;}
    public Array<Body> getBodiesToRemove(){
        return bodiesToRemove;
    }
    //collision detection
    //presolve
    //collision handling
    //postsolve

    public void preSolve(Contact c, Manifold m){}
    public void postSolve(Contact c, ContactImpulse ci){}
}
