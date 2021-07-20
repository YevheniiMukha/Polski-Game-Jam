package com.mafia.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mafia.game.Main;
import jdk.javadoc.internal.doclets.toolkit.taglets.UserTaglet;

public class Hud implements Disposable
{
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    public Integer score;
    private Image coinImage;
    Table table;

    Label scoreLabelInt;


    public Hud(SpriteBatch spriteBatch)
    {
        score = 0;

        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        coinImage= new Image(new Texture("10_zlotych.png"));
        coinImage.setScale(.3f);
        table = new Table();
        table.top();
        table.setFillParent(true);

        BitmapFont font = new BitmapFont();
        font.getData().setScale(.3f);

        scoreLabelInt = new Label(String.format("%06d", score), new Label.LabelStyle(font, Color.ORANGE));

        table.add(coinImage).expandX().padLeft(-90).padBottom(137f);
        table.add(scoreLabelInt).expandX().padLeft(-430).padBottom(120);

        stage.addActor(table);

    }

    public void update()
    {
        scoreLabelInt.setText(score);
    }


    @Override
    public void dispose()
    {
        stage.dispose();
    }
}
