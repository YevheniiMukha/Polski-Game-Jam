package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;

public class Menu implements Screen
{
    final Main main;

    public Menu(final Main main)
    {
        this.main = main;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(1, 0, 0, 1);
        main.batch.begin();
        main.batch.draw(main.img, 0, 0);
        main.batch.end();
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        main.dispose();
    }
}
