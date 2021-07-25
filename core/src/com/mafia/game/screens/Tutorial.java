package com.mafia.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mafia.game.Main;

public class Tutorial implements Screen {

    private final FitViewport gamePort;
    private final OrthographicCamera camera;
    private PlayScreen playScreen;
    private Main main;

    private Texture tutorial;

    public Tutorial(PlayScreen playScreen, Main main)
    {
        this.playScreen = playScreen;
        this.main = main;

        tutorial = new Texture("tutorial.png");

        camera = new OrthographicCamera();
        gamePort = new FitViewport(1980, 1080, camera);

        camera.position.set(gamePort.getWorldHeight() / 2, gamePort.getWorldWidth() / 2 , 0);

    }

    @Override
    public void show()
    {

    }

    public void update(float delta)
    {
        camera.update();
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
        {
            playScreen.isActive = true;
            main.setScreen(playScreen);
        }
        ScreenUtils.clear(0, 0, 0, 1);
        main.batch.begin();
        main.batch.draw(tutorial,0,0, 1920, 1080);
        main.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
