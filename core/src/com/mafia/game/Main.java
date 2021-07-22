package com.mafia.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mafia.game.screens.Hud;
import com.mafia.game.screens.MenuScreen;
import com.mafia.game.screens.PlayScreen;

public class Main extends Game {
	public SpriteBatch batch;
	public static final int V_WIDTH = 300;
	public static final int V_HEIGHT = 160;

	public Hud hud;

	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
		hud = new Hud(batch);
	}

	@Override
	public void render ()
	{
		super.render();
		hud.update();
		hud.stage.draw();
	}
	

}
