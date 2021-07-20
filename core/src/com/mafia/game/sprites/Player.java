package com.mafia.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mafia.game.screens.PlayScreen;
import com.mafia.game.utils.Constants;

public class Player extends Sprite
{
    public enum State {FALLING, JUMPING, STANDING, RUNNING, ATTACKS};
    public State currentState;
    public State previousState;
    public Body body;
    public World world;
    private int x, y, width, height;
    private String name;
    // private TextureRegion playerStand;

    private Animation playerRun;
    private Animation playerJump;
    private Animation playerFalling;
    private Animation playerStand;
    private Animation playerAttacks;

    private float stateTimer;
    private boolean runningRight;

    public Player(World world, int x, int y, int width, int height, String name, PlayScreen screen)
    {
        super(screen.getAtlas().findRegion("player_assets"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 0; i < 2; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * 21, 0, 20,24));

        }
        playerStand = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * 21, 31, 20,24));

        }
        playerRun = new Animation(0.1f, frames);
        frames.clear();

        /*for(int i = 0; i < 3; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * 15, 16, 14,12));
        }
        playerJump = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 2; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * 15, 29, 14,14));
        }
        playerFalling = new Animation(0.1f, frames);
        frames.clear();
        */
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        definePlayer();
        //playerStand = new TextureRegion(getTexture(), 3, 68, 13,13);
        setBounds(0, 0, 20  , 24);
        //setRegion((TextureRegion) playerStand.getKeyFrame(stateTimer, true));
    }

    public void update(float delta)
    {
        setPosition(body.getPosition().x * Constants.pixelPerMeters - (getRegionWidth() / 2f), body.getPosition().y * Constants.pixelPerMeters - (getRegionHeight() / 2f));
        setRegion(getFrame(delta));
    }


    public TextureRegion getFrame(float delta)
    {
        currentState = getState();
        TextureRegion region =  (TextureRegion) playerStand.getKeyFrame(stateTimer, true);
        switch(currentState)
        {
            case JUMPING:
                region = (TextureRegion) playerStand.getKeyFrame(stateTimer, true);
                break;
            case RUNNING:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = (TextureRegion) playerStand.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
                region = (TextureRegion) playerStand.getKeyFrame(stateTimer, true);
                break;
            case ATTACKS:
                break;

        }
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
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }

    public State getState()
    {
        if(body.getLinearVelocity().y > 0 )
            return State.JUMPING;
        else if (body.getLinearVelocity().y < 0 || previousState == State.JUMPING)
        {
            return State.FALLING;
        }
        else if (body.getLinearVelocity().x != 0)
            return  State.RUNNING;
        else
            return State.STANDING;
    }
    private void definePlayer()
    {
        BodyDef def = new BodyDef();
        PolygonShape shape = new PolygonShape();

        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / Constants.pixelPerMeters, y / Constants.pixelPerMeters);
        def.fixedRotation = true;
        body = world.createBody(def);
        shape.setAsBox(width / 2 / Constants.pixelPerMeters, height / 2 / Constants.pixelPerMeters);
        body.createFixture(shape, 1.0f).setUserData(name);
        // shape.dispose();
    }

}
