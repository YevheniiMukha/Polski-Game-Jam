package com.mafia.game.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlatformCreate
{
    public World world;
    private int x, y, width, height;
    private String name;
    private Body body;

    public PlatformCreate(World world, int x, int y, int width, int height, String name)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;

        createPlatform();
    }

    private void createPlatform()
    {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(x / Constants.pixelPerMeters, y / Constants.pixelPerMeters);
        def.fixedRotation = true;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / Constants.pixelPerMeters, height / 2 / Constants.pixelPerMeters);

        body.createFixture(shape, 1.0f).setUserData(name);
        shape.dispose();
       // return body;
    }

}
