package com.mafia.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mafia.game.Main;
import com.mafia.game.sprites.Player;
import com.mafia.game.utils.Constants;
import com.mafia.game.utils.GameContactListener;
import com.mafia.game.utils.PlatformCreate;
import com.mafia.game.utils.SensorCreate;

public class Location_1 implements Screen
{

    // private World world;
   // private Player player;
    private final PlayScreen playScreen;
    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Viewport gamePort;

    private GameContactListener gameContactListener;

    private World world;
    private Player player;

    public Location_1(final PlayScreen playScreen)
    {
       this.playScreen = playScreen;


       camera = new OrthographicCamera();
       gamePort = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);
       camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(gameContactListener = new GameContactListener());
        //this.player = player;

        player = new Player(world, 0,120,15,22, "Player_2", playScreen);
        box2DDebugRenderer = new Box2DDebugRenderer();

        PlatformCreate platform = new PlatformCreate (world,0, 50, 100, 10, "Platform" );
        SensorCreate doorSensorOut = new SensorCreate(-2, 16, 7, 10, "door_1_out", platform.getBody());
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

    public void doorUpdate()
    {
        if(gameContactListener.isPlayerTouchDoor_1_out() & Gdx.input.isKeyJustPressed((Input.Keys.E)))
        {
            System.out.println("chuj zopa ");
            playScreen.getMain().setScreen(playScreen);
        }
    }

    public void update(float delta)
    {
        handleInput(delta);
        doorUpdate();

        world.step(1/60f, 6,2);

        player.update(delta);

        camera.position.x = player.body.getPosition().x * Constants.pixelPerMeters;
        // camera.position.y = player.body.getPosition().y * Constants.pixelPerMeters;
        camera.update();
    }


    @Override
    public void render(float delta)
    {
      //  playScreen.render(delta);
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playScreen.getMain().batch.setProjectionMatrix(camera.combined);
        playScreen.getMain().batch.begin();
        player.draw(playScreen.getMain().batch);
        playScreen.getMain().batch.end();
        box2DDebugRenderer.render(world, camera.combined.scl(Constants.pixelPerMeters));
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

    }
}
