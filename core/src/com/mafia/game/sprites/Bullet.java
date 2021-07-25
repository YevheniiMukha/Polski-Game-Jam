package com.mafia.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mafia.game.utils.Constants;

import java.util.Random;

public class Bullet extends Sprite
{
    private float Speed = 7, x, y;
    private World world;
    private static final Texture bulletImg = new Texture("bullet.png");;
    private Body bullet;
    boolean isRight;
    Sprite sprite;



    public Bullet(World world, float x, float y, boolean isRight)
    {
        super(bulletImg);
        this.x = x;
        this.y = y;
        this.isRight = isRight;
        this.world = world;
        sprite = new Sprite(bulletImg);

        if(!isRight)
        {
            sprite.flip(true, false);
        }
        shoot();
    }

    public Body getBullet() {
        return bullet;
    }

    public void update(float delta)
    {

        setScale(.1f);
        setPosition(bullet.getPosition().x * Constants.pixelPerMeters - (getRegionWidth() / 2f), bullet.getPosition().y * Constants.pixelPerMeters - (getRegionHeight() / 2f));
        setRegion(sprite);

    }

    public void shoot()
    {
        double d = Math.random() * (0.1 - (-0.1)) + (-0.1);
        BodyDef def = new BodyDef();
        PolygonShape shape = new PolygonShape();

        def.type = BodyDef.BodyType.DynamicBody;
        if (isRight)
            def.position.set( x + .5f, y + (float)d  );
        else  def.position.set( x - .5f, y + (float)d );
        def.fixedRotation = true;
        def.bullet = true;
        def.gravityScale = 0;
        bullet =  world.createBody(def);
        shape.setAsBox( 0.5f / Constants.pixelPerMeters, 0.5f / Constants.pixelPerMeters);
        bullet.createFixture(shape, 1.0f).setUserData("bullet");

        if (isRight)
            bullet.setLinearVelocity(Speed, 0);
        else  bullet.setLinearVelocity(-Speed, 0);
    }
}
