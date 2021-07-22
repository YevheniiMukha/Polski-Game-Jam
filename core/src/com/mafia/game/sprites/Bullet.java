package com.mafia.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mafia.game.utils.Constants;

public class Bullet extends Sprite
{
    private float Speed = 20;
    private World world;
    public Texture bulletImg;


    public Bullet(World world)
    {
        this.world = world;
        bulletImg = new Texture("bullet.jpg");
    }

    public void shoot( float x, float y, boolean isRight)
    {
        BodyDef def = new BodyDef();
        PolygonShape shape = new PolygonShape();

        def.type = BodyDef.BodyType.DynamicBody;
        if (isRight)
            def.position.set( x + .5f, y  );
        else  def.position.set( x - .5f, y  );
        def.fixedRotation = true;
        def.bullet = true;
        def.gravityScale = 0;
        Body bullet =  world.createBody(def);
        shape.setAsBox( 1 / Constants.pixelPerMeters, 1 / Constants.pixelPerMeters);
        bullet.createFixture(shape, 1.0f).setUserData("bullet");

        if (isRight)
            bullet.setLinearVelocity(Speed, 0);
        else  bullet.setLinearVelocity(-Speed, 0);
    }
}
