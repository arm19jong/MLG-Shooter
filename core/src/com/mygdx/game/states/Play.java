package com.mygdx.game.states;

import static com.mygdx.game.handlers.B2DVars.BIT_GREEN;
import static com.mygdx.game.handlers.B2DVars.BIT_PLAYER;
import static com.mygdx.game.handlers.B2DVars.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.B2DSprite;
import com.mygdx.game.entities.Crystal;
import com.mygdx.game.entities.HUD;
import com.mygdx.game.entities.Player;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.MyInput;
import com.mygdx.game.handlers.MycontactactListener;
import com.mygdx.game.main.MyGdxGame;
import com.badlogic.gdx.utils.Array;

//import java.lang.reflect.Array;
import java.util.Vector;
import java.util.logging.Filter;

import javafx.scene.shape.Circle;

/**
 * Created by jongzazaal on 14/10/2558.
 */
public class Play extends GameState {


    private boolean debug = false;

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;

    private MycontactactListener cl;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;

    private Player player;
    private com.badlogic.gdx.utils.Array<Crystal> crystals;

    private HUD hud;

    //R=0 , L=1
    private int check = 0;


    public Play(GameStateManager gsm){
        super(gsm);

        //set up box2d stuff
        world = new World(new Vector2(0, -9.81f), true);
        cl = new MycontactactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        //create player
        createPlayer();

        //create tiles
        createTiles();

        //create crystals
        createCrystals();

        //setup box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, MyGdxGame.V_WIDTH/ PPM, MyGdxGame.V_HEIGHT/ PPM);

        //set up hud
        hud = new HUD(player);

    }
    public void handleInput(){
        //player jump

        if (MyInput.isPressed(MyInput.BUTTON1)|| MyInput.isDown(MyInput.BUTTON1)){

            if(cl.isPlayerOnGround()){

                //player.getBody().applyForceToCenter(0, 1250, true);
                //player.getBody().setLinearVelocity(0, 10);
               Jump();
                //player.getBody().applyForce(0, 5000, player.getPosition().x, player.getPosition().y, true);

                //player.getBody().applyForceToCenter(0, 2000, true);
                //player.getBody().setLinearVelocity(0, 2);
            }
        }
        //player left
        else if(MyInput.isPressed(MyInput.BUTTON_L)|| MyInput.isDown(MyInput.BUTTON_L)){

            //Texture tex = MyGdxGame.res.getTexture("bunny");
            //TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[1];
            //setAnimation(sprites, 3 / 12f);
            check += 1;

            MoveLeft();
            if (check >=2){
                check = 1;
                return;
            }
            ChangeAnimationLeft();
        }

        //player right
        else if (MyInput.isPressed(MyInput.BUTTON_R) || MyInput.isDown(MyInput.BUTTON_R)) {
            check -= 1;
            MoveRight();
            if (check <=-1){
                check = 0;
                return;
            }
            ChangeAnimationRight();
        }

        //player stop
        else {
            player.getBody().setLinearVelocity(0, -3);
            if (check == 1){
                ChangeAnimationLeft();
            }
            else {
                ChangeAnimationRight();
            }

        }


    }
    public void update(float dt){
        //check input
        handleInput();


        //update box2d
        world.step(dt, 6, 2);

        //remove crystals
        Array<Body> bodies = cl.getBodiesToRemove();
        for (int i = 0; i<bodies.size; i++){
            Body b = bodies.get(i);
            crystals.removeValue((Crystal) b.getUserData(), true);
            world.destroyBody(b);
            player.collectCrystal();
        }
       bodies.clear();
        player.update(dt);
        for (int i = 0; i < crystals.size; i++){
            crystals.get(i).update(dt);
        }

    }
    public void render(){
        //clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set camera to folllow player
        cam.position.set(player.getPosition().x * PPM + MyGdxGame.V_WIDTH / 4, player.getPosition().y * PPM + MyGdxGame.V_HEIGHT / 10, 0);
        cam.update();

        //draw tilemap
        tmr.setView(cam);
        tmr.render();

        //draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        //draw crystals
        for (int i = 0; i < crystals.size; i++){
            crystals.get(i).render(sb);
        }

        //draw hud
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);

        //draw box2d world
        if(debug) {
            b2dr.render(world, b2dCam.combined);
        }
    }
    public void dispose(){}
    private void createPlayer(){
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        //create player
        bdef.position.set(50 / PPM, 700 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearVelocity.set(0, 0);
        Body body = world.createBody(bdef);

        shape.setAsBox(13 / PPM, 13 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_RED | B2DVars.BIT_CRYSTAL;
        // fdef.restitution = 0.2f;
        body.createFixture(fdef).setUserData("player");

        //create foot sensor
        shape.setAsBox(13/PPM, 2/PPM, new Vector2(0, -13/PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_RED;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        //create player
        player = new Player(body);

        body.setUserData(player);

    }




    private void createTiles(){
        //load tile map
        //tiledMap = new TmxMapLoader().load("maps/test.tmx");
        tiledMap = new TmxMapLoader().load("lvl1.tmx");
        //tiledMap = new TmxMapLoader().load("map/test2.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);

        tileSize = (Integer) tiledMap.getProperties().get("tilewidth");

        TiledMapTileLayer layer;
        /*
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("red");
        createLayer(layer, B2DVars.BIT_RED);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("green");
        createLayer(layer, B2DVars.BIT_GREEN);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("blue");
        createLayer(layer, B2DVars.BIT_BLUE);
        */

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("Base");
        createLayer(layer, B2DVars.BIT_RED);
    }

    private void createLayer(TiledMapTileLayer layer, short bits){

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        // go through all the cell in the layer
        for(int row = 0; row< layer.getHeight(); row++ ){
            for (int col = 0; col < layer.getWidth(); col++){

                //get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col,row);

                //check if cell exists
                if (cell == null) continue;
                if (cell.getTile()==null)continue;

                //create a body +Fixture from cell
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col+0.5f)*tileSize/PPM, (row + 0.5f)*tileSize/PPM);

                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[0] = new Vector2(-tileSize/2/PPM, -tileSize/2/PPM);
                v[1] = new Vector2(-tileSize/2/PPM,  tileSize/2/PPM);
                v[2] = new Vector2( tileSize/2/PPM,  tileSize/2/PPM);
                v[3] = new Vector2( tileSize/2/PPM, -tileSize/2/PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);

            }
        }

    }

    private void createCrystals(){
        crystals = new com.badlogic.gdx.utils.Array<Crystal>();
        MapLayer layer = tiledMap.getLayers().get("crystals");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for(MapObject mo : layer.getObjects()){

            bdef.type = BodyDef.BodyType.StaticBody;

            float x = (Float) mo.getProperties().get("x")/PPM;
            float y = (Float) mo.getProperties().get("y")/PPM;

            bdef.position.set(x, y);
            CircleShape cshape = new CircleShape();
            cshape.setRadius(8/PPM);

            fdef.shape = cshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = B2DVars.BIT_CRYSTAL;
            fdef.filter.maskBits = B2DVars.BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("crystal");

            Crystal c = new Crystal(body);
            crystals.add(c);

            body.setUserData(c);

        }
    }

    /*
    private void switchBlocks(){
        com.badlogic.gdx.physics.box2d.Filter filter = player.getBody().getFixtureList().first().getFilterData();
        short bits = filter.maskBits;
        //System.out.println("xx2");
        //switch to next color
        //red->green->blue->red
        if((bits & B2DVars.BIT_RED) != 0){
            bits &= ~B2DVars.BIT_RED;
            bits |= B2DVars.BIT_GREEN;
            System.out.println("1");
        }
        else  if((bits & B2DVars.BIT_GREEN) != 0){
            bits &= ~B2DVars.BIT_GREEN;
            bits |= B2DVars.BIT_BLUE;
            System.out.println("2");
        }
        else  if((bits & B2DVars.BIT_BLUE) != 0){
            bits &= ~B2DVars.BIT_BLUE;
            bits |= B2DVars.BIT_RED;
            System.out.println("3");
        }

        //set new  mask bits
        filter.maskBits = bits;
        player.getBody().getFixtureList().first().setUserData(filter);

        //set new mask bits for foot
        filter = player.getBody().getFixtureList().get(1).getFilterData();
        bits &= ~B2DVars.BIT_CRYSTAL;
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(1).setUserData(filter);




    }
    */
    private void MoveLeft(){
        player.getBody().setLinearVelocity(-2, -4);
    }
    private void MoveRight() {
        player.getBody().setLinearVelocity(2, -4);
    }
    private void Jump(){
        player.getBody().applyForceToCenter(0, 150, true);
    }
    private void ChangeAnimationLeft(){
        Texture tex = MyGdxGame.res.getTexture("bunny");

        TextureRegion[] sprites = new TextureRegion[2];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, 32*i, 64, 32, 32);
            sprites[i].flip(true, false);
        }
        player.setAnimation(sprites, 1 / 9f);
    }
    private void ChangeAnimationRight(){
        Texture tex = MyGdxGame.res.getTexture("bunny");

        TextureRegion[] sprites = new TextureRegion[2];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, 32*i, 32, 32, 32);
            sprites[i].flip(true, false);
        }
        player.setAnimation(sprites, 1 / 9f);
    }
}
