package com.mafia.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mafia.game.Main;

public class MenuScreen implements Screen
{
    private static final float PlayHeight = 22;
    private static final float PlayWidth = 118;
    private static final float ExitHeight = 22;
    private static final float ExitWidht = 118;
    private static final float PlayY = 540;
    private static final float ExitY = 440;

    private final Main main;
    public Texture PlayActive;
    public  Texture ExitActive;
    public  Texture PlayInActive;
    public  Texture ExitInActive;
    public  Texture Menu;

    public OrthographicCamera camera;
    public FitViewport gamePort;

    private PlayScreen playScreen;

    public boolean isActive;

    public MenuScreen(Main main, PlayScreen playScreen)
    {
        this.main = main;
        this.playScreen = playScreen;
        PlayActive = new Texture("menu/NewGameActive.png");
        ExitActive = new Texture("menu/ExitActive.png");
        PlayInActive = new Texture("menu/NewGameInActive.png");
        ExitInActive = new Texture("menu/ExitInActive.png");
        Menu = new Texture("menu/menu.png");

        isActive = false;
        camera = new OrthographicCamera();
        gamePort = new FitViewport(1980, 1080, camera);

        camera.position.set(gamePort.getWorldHeight() / 2, gamePort.getWorldWidth() / 2 , 0);
    }
    @Override
    public void show() {

    }

    public void update(float delta)
    {
        camera.update();
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        ScreenUtils.clear(0, 0, 0, 1);
        main.batch.begin();
        main.batch.draw(Menu, 0, 0, 1920, 1080 );
        if(Gdx.input.getX() > 1920 / 2 - PlayWidth / 2 && 1080 - Gdx.input.getY() > PlayY + 25 &&
                Gdx.input.getX() < 1920 / 2 + PlayWidth / 2 && 1080 - Gdx.input.getY() < PlayY + PlayHeight + 25 )
        {
            main.batch.draw(PlayActive, 1920 / 2 - PlayWidth / 2  , PlayY   );
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
            {
                main.setScreen(new Tutorial(playScreen, main));
            }

        }
        else
        {
            main.batch.draw(PlayInActive, 1920 / 2 - PlayWidth / 2  , PlayY);
        }

        if(Gdx.input.getX() > 1920 / 2 - ExitWidht / 2 && Gdx.input.getX() < 1920 / 2 + ExitWidht / 2 &&
                1080 - Gdx.input.getY() > ExitY + 25 && 1080 - Gdx.input.getY() < ExitY + ExitHeight + 25)
        {
            main.batch.draw(ExitActive, 1920 / 2 - ExitWidht / 2  , ExitY);
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
                Gdx.app.exit();
        }
        else
        {
            main.batch.draw(ExitInActive, 1920 / 2 - ExitWidht / 2  , ExitY);
        }
        main.batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
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