package com.mafia.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mafia.game.Main;
import com.mafia.game.sprites.Player;
import com.mafia.game.utils.Constants;
import com.mafia.game.utils.GameContactListener;
import com.mafia.game.utils.PlatformCreate;
import com.mafia.game.utils.SensorCreate;

public class PlayScreen implements Screen
{

    private final Main main;

    private Viewport gamePort;
    private OrthographicCamera camera;

    private Player player;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private SensorCreate sensorCreate;
    private GameContactListener gameContactListener;

    public PlayScreen(final Main main)
    {

        this.main = main;

        camera = new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);

        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(gameContactListener = new GameContactListener());
        box2DDebugRenderer = new Box2DDebugRenderer();


        player = new Player(world, 0,120,64,64, "Player", this);
        sensorCreate = new SensorCreate(0, -29, 31, 5, "foot", player.body);
        PlatformCreate platform = new PlatformCreate (world,0, 70, 500, 20, "Platform" );


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
            player.body.applyLinearImpulse(new Vector2(0, 4f), player.body.getWorldCenter(), true);
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

        world.step(1/60f, 6,2);

        player.update(delta);

        camera.position.x = player.body.getPosition().x * Constants.pixelPerMeters;
       // camera.position.y = player.body.getPosition().y * Constants.pixelPerMeters;
        camera.update();
    }


    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.batch.setProjectionMatrix(camera.combined);
        box2DDebugRenderer.render(world, camera.combined.scl(Constants.pixelPerMeters));

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
    public void dispose() {

    }
}
