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
    public Bullet bullet;
    private boolean isRight;

    private SensorCreate footSensor, doorSensor;
    private GameContactListener gameContactListener;
    private TextureAtlas atlas;

    private RayHandler rayhandler;
    private ConeLight light1;
    private PointLight light2;
    private ShopLocation location_1, location_2, location_3;

    PlatformCreate platform;

    public PlayScreen(final Main main)
    {

        atlas = new TextureAtlas("Player_Animations.pack");

        this.main = main;

        camera = new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);


        mapLoader = new TmxMapLoader();
        map = mapLoader.load("GameMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        camera.zoom = 0.8f;

        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(gameContactListener = new GameContactListener());
        box2DDebugRenderer = new Box2DDebugRenderer();
        bullet = new Bullet(world);


        player = new Player(world, 356,60,15,22, "Player", this);
        footSensor = new SensorCreate(0, -10, 6, 1, "foot", player.body);

        platform = new PlatformCreate (world,(int)(33.5 * Constants.pixelPerMeters), (int) (1.5 * Constants.pixelPerMeters), 10, 10, "Platform" );
        doorSensor = new SensorCreate(0, 30, 7, 2, "door_1",  platform.getBody());

        platform = new PlatformCreate (world,(int)(89.4 * Constants.pixelPerMeters), (int) (1.5 * Constants.pixelPerMeters), 10, 10, "Platform_2" );
        doorSensor = new SensorCreate(0, 30, 7, 2, "door_2",  platform.getBody());

        platform = new PlatformCreate (world,(int)(119.7 * Constants.pixelPerMeters), (int) (1.5 * Constants.pixelPerMeters), 10, 10, "Platform_3" );
        doorSensor = new SensorCreate(0, 30, 7, 2, "door_3",  platform.getBody());

        MapShapeBuilder.buildShapes(map.getLayers().get("Object").getObjects(), world);

        rayhandler = new RayHandler(world);
        rayhandler.setAmbientLight(.1f);

        lightsCreate();

        location_1 = new ShopLocation(this, new TextureAtlas("Enemy/NPC_1.pack"));
        location_2 = new ShopLocation(this, new TextureAtlas("Enemy/NPC_1.pack"));
        location_3 = new ShopLocation(this, new TextureAtlas("Enemy/NPC_1.pack"));

    }

    public TextureAtlas getAtlas() {return atlas;}


    public void doorUpdate()
    {
        if(gameContactListener.isPlayerTouchDoor_1() && Gdx.input.isKeyJustPressed((Input.Keys.E)))
        {
            main.setScreen(location_1);
        }
        else if(gameContactListener.isPlayerTouchDoor_2() && Gdx.input.isKeyJustPressed((Input.Keys.E)))
        {
            main.setScreen(location_2);
        }
        else if (gameContactListener.isPlayerTouchDoor_3() && Gdx.input.isKeyJustPressed((Input.Keys.E)))
        {
            main.setScreen(location_3);
        }
    }

    public Main getMain()
    {
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
            isRight = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) && player.body.getLinearVelocity().x >= -2)
        {
            //speedPlayer -= 1.7;
            player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
            isRight = false;
        }
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            System.out.println(player.body.getPosition());
            bullet.shoot(player.body.getPosition().x, player.body.getPosition().y, isRight);
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
        CheckForDelete();
        //drawBullet();
        main.batch.end();

        rayhandler.render();
        box2DDebugRenderer.render(world, camera.combined.scl(Constants.pixelPerMeters));

    }

    private void lightsCreate()
    {
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 11.42f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 16.92f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 22.45f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 27.95f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 32.52f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 37.10f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 41.69f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 46.26f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 52.69f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 54.53f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 62.77f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 72.87f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 78.37f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 83.87f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 88.44f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 93.01f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 97.58f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 102.16f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 108.59f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 110.43f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 118.72f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 123.29f, 4.55f);
        light2 = new PointLight (rayhandler, 100, Color.ORANGE, 1f, 127.86f, 4.55f);

        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,16.92f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,11.42f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,22.45f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,27.95f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,32.52f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,37.10f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,41.69f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,46.26f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,52.69f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,54.53f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,62.77f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,72.87f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,78.37f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,83.87f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,88.44f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,93.01f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,97.58f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,102.16f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,108.59f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,110.43f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,118.72f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,123.29f,4.7f,270, 20);
        light1 = new ConeLight  (rayhandler, 100, Color.ORANGE, 10,127.86f,4.7f,270, 20);
    }


    @Override
    public void resize(int width, int height)
    {
        gamePort.update(width,height);
    }

    public void drawBullet()
    {
        Array<Fixture> tmp = new Array<>();
        world.getFixtures(tmp);
        for(int i = 0; i < world.getFixtureCount(); i++)
        {
            if ( tmp.get(i).getUserData() != null)
            {
                if(tmp.get(i).getUserData().equals("bullet"))
                    main.batch.draw(bullet.bulletImg, tmp.get(i).getBody().getPosition().x + 400, tmp.get(i).getBody().getPosition().y +50, 50, 50);
            }
        }
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
