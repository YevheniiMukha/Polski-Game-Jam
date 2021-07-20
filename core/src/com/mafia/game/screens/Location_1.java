package com.mafia.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mafia.game.sprites.Player;
import com.mafia.game.utils.Constants;

public class Location_1 implements Screen
{

    // private World world;
   // private Player player;
    private final PlayScreen playScreen;
    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;

    public Location_1(final PlayScreen playScreen)
    {
       this.playScreen = playScreen;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
      //  playScreen.render(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playScreen.getMain().batch.setProjectionMatrix(playScreen.getCamera().combined);
        playScreen.getMain().batch.begin();
        playScreen.getPlayer().draw(playScreen.getMain().batch);
        playScreen.getMain().batch.end();
        playScreen.getBox2DDebugRenderer().render(playScreen.getWorld(), playScreen.getCamera().combined.scl(Constants.pixelPerMeters));
    }

    @Override
    public void resize(int width, int height)
    {
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
