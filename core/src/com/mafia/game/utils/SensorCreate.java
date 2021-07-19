package com.mafia.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class SensorCreate
{
    private int x, y, width, height;
    private String name;
    Body body;

    public SensorCreate(int x, int y, int width, int height, String name, Body body)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.body = body;

        createSensor();
    }

    private void createSensor()
    {
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        shape.setAsBox( width/ Constants.pixelPerMeters, height/ Constants.pixelPerMeters, new Vector2(x, y / Constants.pixelPerMeters), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(name);
    }


}
