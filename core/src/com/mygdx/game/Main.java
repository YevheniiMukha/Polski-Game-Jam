package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.screens.Menu;

public class Main extends Game
{
	public SpriteBatch batch;
	public Texture img;
	
	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		setScreen(new Menu(this));
	}

	@Override
	public void render ()
	{
		super.render();
	}

}
