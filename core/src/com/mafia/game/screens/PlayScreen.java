package com.mafia.game.screens;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mafia.game.Main;
import com.mafia.game.sprites.Player;
import com.mafia.game.utils.*;

public class PlayScreen implements Screen
{

    private final Main main;

    private Viewport gamePort;
    private OrthographicCamera camera;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Player player;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private SensorCreate footSensor, doorSensor;
    private GameContactListener gameContactListener;
    private TextureAtlas atlas;

    private RayHandler rayhandler;
    private ConeLight light1, light2;

    public PlayScreen(final Main main)
    {

        atlas = new TextureAtlas("Player_Animations.pack");

        this.main = main;

        camera = new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);


        mapLoader = new TmxMapLoader();
        map = mapLoader.load("TestGround.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(gameContactListener = new GameContactListener());
        box2DDebugRenderer = new Box2DDebugRenderer();


        player = new Player(world, 50,140,15,22, "Player", this);
        footSensor = new SensorCreate(0, -10, 6, 1, "foot", player.body);

        //PlatformCreate platform = new PlatformCreate (world,0, 20, 100, 10, "Platform" );
        //doorSensor = new SensorCreate(2, 16, 7, 10, "door_1",  getBody());

        MapShapeBuilder.buildShapes(map.getLayers().get("Object").getObjects(), world);

        rayhandler = new RayHandler(world);
        rayhandler.setAmbientLight(.1f);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,6.9f,4.6f,270, 20);
        light2 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,1.4f,4.6f,270, 20);

    }

    public TextureAtlas getAtlas() {return atlas;}

    public void doorUpdate()
    {
        if(gameContactListener.isPlayerTouchDoor_1() & Gdx.input.isKeyJustPressed((Input.Keys.E)))
        {
            main.setScreen(new Location_1(this));
        }
    }

    public Main getMain() {
        return main;
    }

    @Override
    public void show()
    {

    }

    public void handleInput(float delta)
    {
        //float speedPlayer = 0;
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && gameContactListener.isPlayerOnGround())
        {
            //player.body.applyForceToCenter(player.body.getPosition().x, 300, true);
            player.body.applyLinearImpulse(new Vector2(0, 2.5f), player.body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) && player.body.getLinearVelocity().x <= 2)
        {
            //speedPlayer += 1.7;
            player.body.applyLinearImpulse(new Vector2(0.1f, 0), player.body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) && player.body.getLinearVelocity().x >= -2)
        {
            //speedPlayer -= 1.7;
            player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {

        }
        //player.body.setLinearVelocity(speedPlayer *5, player.body.getLinearVelocity().y);
    }

    public void update(float delta)
    {
        //camera.position.x = 0;


        handleInput(delta);
        rayhandler.update();
        doorUpdate();

        world.step(1/60f, 6,2);

        player.update(delta);

        camera.position.x = player.body.getPosition().x * Constants.pixelPerMeters;
       // camera.position.y = player.body.getPosition().y * Constants.pixelPerMeters;
        camera.update();
        renderer.setView(camera);
        rayhandler.setCombinedMatrix(camera.combined.cpy().scl(Constants.pixelPerMeters));
    }


    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();


        main.batch.setProjectionMatrix(camera.combined);

        main.batch.begin();
        player.draw(main.batch);
        main.batch.end();

        rayhandler.render();
        //box2DDebugRenderer.render(world, camera.combined.scl(Constants.pixelPerMeters));

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
        map.dispose();
        renderer.dispose();
        world.dispose();
    }
}
