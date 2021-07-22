package com.mafia.game.screens;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mafia.game.Main;
import com.mafia.game.sprites.Bullet;
import com.mafia.game.sprites.Enemy;
import com.mafia.game.sprites.Player;
import com.mafia.game.utils.*;

public class ShopLocation implements Screen
{

    // private World world;
   // private Player player;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private final PlayScreen playScreen;
    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Viewport gamePort;

    private GameContactListener gameContactListener;
    private TextureAtlas atlas;
    public Bullet bullet;
    private boolean isRight;

    private World world;
    private Player player;

    private RayHandler rayhandler;
    private ConeLight light1;
    private PointLight light2;

    Enemy enemy;


    public ShopLocation(final PlayScreen playScreen, TextureAtlas atlas)
    {
       this.playScreen = playScreen;

       this.atlas = atlas;
       camera = new OrthographicCamera();
       gamePort = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);

       mapLoader = new TmxMapLoader();
       map = mapLoader.load("sklep.tmx");
       renderer = new OrthogonalTiledMapRenderer(map);

       camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
       camera.zoom = 0.8f;

       world = new World(new Vector2(0, -10f), true);
       world.setContactListener(gameContactListener = new GameContactListener());
       bullet = new Bullet(world);

       //this.player = player;
       player = new Player(world, 451,60,15,22, "Player_2", playScreen);
       enemy = new Enemy(world, 470, 60, 15,22, "enemy", this);
       SensorCreate footSensor = new SensorCreate(0, -10, 7, 1, "foot", player.body);

       PlatformCreate platform = new PlatformCreate (world,(int)(18.8 * Constants.pixelPerMeters), (int) (1.5 * Constants.pixelPerMeters), 10, 10, "Platform" );
       SensorCreate doorSensorOut = new SensorCreate(0, 30, 7, 2, "door_1_out",  platform.getBody());

       box2DDebugRenderer = new Box2DDebugRenderer();

       MapShapeBuilder.buildShapes(map.getLayers().get("ObjectSklep").getObjects(), world);

        rayhandler = new RayHandler(world);
        rayhandler.setAmbientLight(.5f);
       //PlatformCreate platform = new PlatformCreate (world,0, 50, 100, 10, "Platform" );
      // SensorCreate doorSensorOut = new SensorCreate(-2, 16, 7, 10, "door_1_out", platform.getBody());
        setLights();

    }

    public TextureAtlas getAtlas() {return atlas;}

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
            isRight = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) && player.body.getLinearVelocity().x >= -2)
        {
            //speedPlayer -= 1.7;
            player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
            isRight = false;
        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            System.out.println(player.body.getPosition());
            bullet.shoot(player.body.getPosition().x, player.body.getPosition().y, isRight);
        }
        //player.body.setLinearVelocity(speedPlayer *5, player.body.getLinearVelocity().y);
    }

    public void doorUpdate()
    {
        if(gameContactListener.isPlayerTouchDoor_1_out() && Gdx.input.isKeyJustPressed((Input.Keys.E)))
        {
            playScreen.getMain().setScreen(playScreen);
        }
    }

    public void update(float delta)
    {
        handleInput(delta);
        rayhandler.update();
        doorUpdate();

        world.step(1/60f, 6,2);

        player.update(delta);
        enemy.update(delta, gameContactListener);


        camera.position.x = player.body.getPosition().x * Constants.pixelPerMeters;
        // camera.position.y = player.body.getPosition().y * Constants.pixelPerMeters;
        camera.update();

        renderer.setView(camera);
        rayhandler.setCombinedMatrix(camera.combined.cpy().scl(Constants.pixelPerMeters));
    }



    @Override
    public void render(float delta)
    {
      //  playScreen.render(delta);
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        playScreen.getMain().batch.setProjectionMatrix(camera.combined);
        playScreen.getMain().batch.begin();
        player.draw(playScreen.getMain().batch);
        enemy.draw(playScreen.getMain().batch);
        CheckForDelete();
        playScreen.getMain().batch.end();
        box2DDebugRenderer.render(world, camera.combined.scl(Constants.pixelPerMeters));
        rayhandler.render();
    }

    private void setLights()
    {
        light1 = new ConeLight (rayhandler, 100, Color.CORAL, 10,16.92f,5.7f,270, 30);
        light2 = new PointLight (rayhandler, 100, Color.CORAL, .9f, 16.92f, 5.7f);

        light1 = new ConeLight (rayhandler, 100, Color.CORAL, 10,21.51f,5.7f,270, 30);
        light2 = new PointLight (rayhandler, 100, Color.CORAL, .9f, 21.51f, 5.7f);

        light1 = new ConeLight (rayhandler, 100, Color.CORAL, 10,26.09f,5.7f,270, 30);
        light2 = new PointLight (rayhandler, 100, Color.CORAL, .9f, 26.09f, 5.7f);

        light1 = new ConeLight (rayhandler, 100, Color.CORAL, 10,30.69f,5.7f,270, 30);
        light2 = new PointLight (rayhandler, 100, Color.CORAL, .9f, 30.69f, 5.7f);

        light1 = new ConeLight (rayhandler, 100, Color.CORAL, 10,12.34f,5.7f,270, 30);
        light2 = new PointLight (rayhandler, 100, Color.CORAL, .9f, 12.34f, 5.7f);

        light1 = new ConeLight (rayhandler, 100, Color.CORAL, 10,7.76f,5.7f,270, 30);
        light2 = new PointLight (rayhandler, 100, Color.CORAL, .9f, 7.76f, 5.7f);
    }

    public void CheckForDelete()
    {
        Array<Fixture> tmp = new Array<>();
        world.getFixtures(tmp);
        for(int i = 0; i < world.getFixtureCount(); i++)
        {
            if ( tmp.get(i).getUserData() != null)
            {
                if (tmp.get(i).getUserData().equals("delete"))
                {
                    tmp.get(i).getBody().setActive(false);
                    world.destroyBody(tmp.get(i).getBody());
                }
            }
        }
    }
    @Override
    public void resize(int width, int height)
    {
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
    public void dispose() {
        renderer.dispose();
    }
}
