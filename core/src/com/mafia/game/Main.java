package com.mafia.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mafia.game.screens.PlayScreen;

public class Main extends Game {
	public SpriteBatch batch;

	
	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));

	}

	@Override
	public void render ()
	{
		super.render();
	}
	

}
