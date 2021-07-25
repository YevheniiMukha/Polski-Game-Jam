package com.mafia.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mafia.game.screens.Hud;
import com.mafia.game.screens.MenuScreen;
import com.mafia.game.screens.PlayScreen;

public class Main extends Game
{
	public SpriteBatch batch;
	public static final int V_WIDTH = 300;
	public static final int V_HEIGHT = 160;

	public Hud hud;
	private MenuScreen menuScreen;
	private PlayScreen playScreen;
	private Music music;

	@Override
	public void create ()
	{
		music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/music.wav"));
		batch = new SpriteBatch();
		playScreen = new PlayScreen(this);
		menuScreen = new MenuScreen(this,playScreen);
		setScreen(menuScreen);
		hud = new Hud(batch);

		music.setVolume(0.5f);
		music.setLooping(true);
		music.play();
	}

	@Override
	public void render ()
	{
		super.render();
		if(playScreen.isActive)
		{
			hud.update();
			hud.stage.draw();
		}
	}
	

}
