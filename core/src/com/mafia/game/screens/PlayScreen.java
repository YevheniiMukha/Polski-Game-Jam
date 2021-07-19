package com.mafia.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mafia.game.Main;

public class PlayScreen implements Screen
{

    private final Main main;
    public Texture img;

    public PlayScreen(final Main main)
    {
        img = new Texture("badlogic.jpg");
        this.main = main;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);
        main.batch.begin();
        main.batch.draw(img, 0, 0);
        main.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
