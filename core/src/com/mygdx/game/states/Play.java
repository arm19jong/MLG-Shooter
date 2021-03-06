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
import com.mygdx.game.entities.Player2;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.MyInput;
import com.mygdx.game.handlers.MycontactactListener;
import com.mygdx.game.handlers.MycontactactListener2;
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
    private World world2;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;

    private MycontactactListener cl;
    private MycontactactListener2 cl2;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;

    private Player player;
    private Player player_2;
    private com.badlogic.gdx.utils.Array<Crystal> crystals;

    private HUD hud;

    //R=0 , L=1
    private int check = 0;
    private int check2 = 0;


    public Play(GameStateManager gsm){
        super(gsm);

        //set up box2d stuff
        world = new World(new Vector2(0, -9.81f), true);
        cl = new MycontactactListener();
        world.setContactListener(cl);

        world2 = new World(new Vector2(0, -9.81f), true);
        cl2 = new MycontactactListener2();
        world2.setContactListener(cl2);

        b2dr = new Box2DDebugRenderer();

        //create player
        createPlayer();
        createPlayer2();

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
    public void handleInput() {
        //player jump

        if (MyInput.isPressed(MyInput.BUTTON1) || MyInput.isDown(MyInput.BUTTON1)) {

            if (cl.isPlayerOnGround()) {

                //player.getBody().applyForceToCenter(0, 1250, true);
                //player.getBody().setLinearVelocity(0, 10);
                Jump();
                //player.getBody().applyForce(0, 5000, player.getPosition().x, player.getPosition().y, true);

                //player.getBody().applyForceToCenter(0, 2000, true);
                //player.getBody().setLinearVelocity(0, 2);
            }
        }
        //player left
        else if (MyInput.isPressed(MyInput.BUTTON_L) || MyInput.isDown(MyInput.BUTTON_L)) {

            //Texture tex = MyGdxGame.res.getTexture("bunny");
            //TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[1];
            //setAnimation(sprites, 3 / 12f);
            check += 1;

            MoveLeft();
            if (check >= 2) {
                check = 1;
                return;
            }
            ChangeAnimationLeft();
        }

        //player right
        else if (MyInput.isPressed(MyInput.BUTTON_R) || MyInput.isDown(MyInput.BUTTON_R)) {
            check -= 1;
            MoveRight();
            if (check <= -1) {
                check = 0;
                return;
            }
            ChangeAnimationRight();
        }

        //player stop
        else {
            player.getBody().setLinearVelocity(0, -3);
            if (check == 1) {
                ChangeAnimationLeft();
            } else {
                ChangeAnimationRight();
            }

        }
    }
        //player2222222222222222222222222

        //player jump
    private  void handleInput2(){
        if (MyInput.isPressed(MyInput.BUTTON_UP_p2)|| MyInput.isDown(MyInput.BUTTON_UP_p2)){

            if(cl2.isPlayerOnGround()){
                Jump2();
            }
        }
        //player left
        else if(MyInput.isPressed(MyInput.BUTTON_L_p2)|| MyInput.isDown(MyInput.BUTTON_L_p2)){

            //Texture tex = MyGdxGame.res.getTexture("bunny");
            //TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[1];
            //setAnimation(sprites, 3 / 12f);
            check2 += 1;

            MoveLeft2();
            if (check2 >=2){
                check2 = 1;
                return;
            }
            ChangeAnimationLeft2();
        }

        //player right
        else if (MyInput.isPressed(MyInput.BUTTON_R_p2) || MyInput.isDown(MyInput.BUTTON_R_p2)) {
            check2 -= 1;
            MoveRight2();
            if (check2 <=-1){
                check2 = 0;
                return;
            }
            ChangeAnimationRight2();
        }

        //player stop
        else {
            player_2.getBody().setLinearVelocity(0, -3);
            if (check2 == 1){
                ChangeAnimationLeft2();
            }
            else {
                ChangeAnimationRight2();
            }

        }


    }
    public void update(float dt){
        //check input
        handleInput();
        handleInput2();

        //update box2d
        world.step(dt, 6, 2);
        world2.step(dt, 6, 2);

        //remove crystals
        Array<Body> bodies = cl.getBodiesToRemove();
        for (int i = 0; i<bodies.size; i++){
            Body b = bodies.get(i);
            crystals.removeValue((Crystal) b.getUserData(), true);
            world.destroyBody(b);
            player.collectCrystal();
        }
       bodies.clear();

        for (int i = 0; i<bodies.size; i++){
            Body b = bodies.get(i);
            crystals.removeValue((Crystal) b.getUserData(), true);
            world.destroyBody(b);
            player_2.collectCrystal();
        }
        bodies.clear();

        player.update(dt);
        player_2.update(dt);

        for (int i = 0; i < crystals.size; i++){
            crystals.get(i).update(dt);
        }

    }
    public void render(){


        //clear screen
        //System.out.println(player.getBody().getPosition().y);
        if (player.getBody().getPosition().y < 0){
            Gdx.app.exit();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set camera to follow player
        cam.position.set(player.getPosition().x * PPM + MyGdxGame.V_WIDTH / 4, player.getPosition().y * PPM + MyGdxGame.V_HEIGHT / 10, 0);
        cam.update();


        //b2dr.render(world, b2dCam.combined);
        

        //b2dr.render(world, b2dCam.combined.setTranslation(2,2,2));

        //draw tilemap
        tmr.setView(cam);
        tmr.render();

        //draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        sb.setProjectionMatrix(cam.combined);
        player_2.render(sb);

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
            b2dr.render(world2, b2dCam.combined);
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
        shape.setAsBox(13 / PPM, 2 / PPM, new Vector2(0, -13 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_RED;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        //create player
        player = new Player(body);
        body.setUserData(player);
    }
    private void createPlayer2(){
        BodyDef bdef2 = new BodyDef();
        FixtureDef fdef2 = new FixtureDef();
        PolygonShape shape2 = new PolygonShape();

        //create player
        bdef2.position.set(50 / PPM, 700 / PPM);
        bdef2.type = BodyDef.BodyType.DynamicBody;
        bdef2.linearVelocity.set(0, 0);
        Body body = world2.createBody(bdef2);

        shape2.setAsBox(13 / PPM, 13 / PPM);
        fdef2.shape = shape2;
        fdef2.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef2.filter.maskBits = B2DVars.BIT_RED | B2DVars.BIT_CRYSTAL;
        // fdef.restitution = 0.2f;
        body.createFixture(fdef2).setUserData("player_2");

        //create foot sensor
        shape2.setAsBox(13/PPM, 2/PPM, new Vector2(0, -13/PPM), 0);
        fdef2.shape = shape2;
        fdef2.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef2.filter.maskBits = B2DVars.BIT_RED;
        fdef2.isSensor = true;
        body.createFixture(fdef2).setUserData("foot");

        //create player
        player_2 = new Player(body);

        body.setUserData(player_2);


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
                Vector2[] v = new Vector2[5];
                v[0] = new Vector2(-tileSize/2/PPM, -tileSize/2/PPM);//left
                v[1] = new Vector2(-tileSize/2/PPM,  tileSize/2/PPM);
                v[2] = new Vector2( tileSize/2/PPM,  tileSize/2/PPM);
                v[3] = new Vector2( tileSize/2/PPM, -tileSize/2/PPM);//right
                v[4] = new Vector2(-tileSize/2/PPM, -tileSize/2/PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);
                world2.createBody(bdef).createFixture(fdef);

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
    //control 2222222222
    private void MoveLeft2(){
        player_2.getBody().setLinearVelocity(-2, -4);
    }
    private void MoveRight2() {

        player_2.getBody().setLinearVelocity(2, -4);
    }
    private void Jump2(){

        player_2.getBody().applyForceToCenter(0, 150, true);
    }
    private void ChangeAnimationLeft2(){
        Texture tex = MyGdxGame.res.getTexture("bunny");

        TextureRegion[] sprites = new TextureRegion[2];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, 32*i, 64, 32, 32);
            sprites[i].flip(true, false);
        }
        player_2.setAnimation(sprites, 1 / 9f);
    }
    private void ChangeAnimationRight2(){
        Texture tex = MyGdxGame.res.getTexture("bunny");

        TextureRegion[] sprites = new TextureRegion[2];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, 32*i, 32, 32, 32);
            sprites[i].flip(true, false);
        }
        player_2.setAnimation(sprites, 1 / 9f);
    }
}
