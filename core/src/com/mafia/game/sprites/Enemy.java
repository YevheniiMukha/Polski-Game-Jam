package com.mafia.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mafia.game.screens.ShopLocation;
import com.mafia.game.utils.Constants;
import com.mafia.game.utils.GameContactListener;

import javax.swing.*;

public class Enemy extends Sprite
{
    public enum State {RUNNING, DEAD};
    public State currentState;
    public State previousState;
    public Body body;
    public World world;
    private int x, y, width, height;
    private String name;

    public boolean isActiveBody;

    private Animation enemyRunning;
    private Animation enemyDead;

    private Vector2 velocity;

    private float stateTimer;
    private boolean runningRight;

    public Enemy( World world, int x, int y, int width, int height, String name, ShopLocation screen)
    {

        super(screen.getAtlas().findRegion("npc"));
        this.world = world;
        isActiveBody = true;
        currentState = State.RUNNING;
        previousState = State.RUNNING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * 15, 29, 12,21));

        }
        enemyRunning = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * 24, 0, 22,24));

        }
        enemyDead = new Animation(0.1f, frames);
        frames.clear();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        defineEnemy();
        velocity  = new Vector2(2, 0);

        setBounds(0, 0, 12  , 21);

    }

    public void update(float delta, GameContactListener gameContactListener)
    {
        if(isActiveBody)
        {
            setPosition(body.getPosition().x * Constants.pixelPerMeters - (getRegionWidth() / 2f), body.getPosition().y * Constants.pixelPerMeters - (getRegionHeight() / 2f));

            if(gameContactListener.isEnemyMoveRight())
                body.setLinearVelocity(velocity);
            else
                body.setLinearVelocity(velocity.x * -1, velocity.y);
        }

        setRegion(getFrame(delta));

    }

    private TextureRegion getFrame(float delta)
    {

        TextureRegion region;
        currentState = getState();
        if(isActiveBody)
        {
            region = (TextureRegion) enemyRunning.getKeyFrame(stateTimer, true);
        }
        else
            region =  (TextureRegion) enemyDead.getKeyFrame(stateTimer, true);

        switch(currentState)
        {
            case RUNNING:
                region = (TextureRegion) enemyRunning.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = (TextureRegion) enemyDead.getKeyFrame(stateTimer);
                break;

        }
        if(isActiveBody)
        {
            if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX())
            {
                region.flip(true, false);
                runningRight = false;
            }
            else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX())
            {
                region.flip(true, false);
                runningRight = true;
            }

        }

            stateTimer = currentState == previousState ? stateTimer + delta : 0;
            previousState = currentState;

        return region;
    }

    public State getState()
    {

        if (isActiveBody)
            return  State.RUNNING;
        else
            return State.DEAD;
    }

    private void defineEnemy()
    {
        BodyDef def = new BodyDef();
        PolygonShape shape = new PolygonShape();

        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / Constants.pixelPerMeters, y / Constants.pixelPerMeters);
        def.fixedRotation = true;
        body = world.createBody(def);
        shape.setAsBox(width / 2 / Constants.pixelPerMeters, height / 2 / Constants.pixelPerMeters);
        body.createFixture(shape, 1.0f).setUserData(name);
        shape.dispose();
    }

}
