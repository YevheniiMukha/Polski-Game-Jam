package com.mafia.game.utils;

import com.badlogic.gdx.physics.box2d.*;

public class GameContactListener implements ContactListener
{

    private boolean playerOnGround = true;
    private boolean door_1 = false;
    private boolean door_1_out = false;



    public boolean isPlayerOnGround() {return playerOnGround;}
    public boolean isPlayerTouchDoor_1() { return  door_1;}
    public boolean isPlayerTouchDoor_1_out() { return  door_1_out;}



    @Override
    public void beginContact(Contact contact)
    {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if(fa.getUserData() != null && fa.getUserData().equals("foot"))
        {
            playerOnGround = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot"))
        {
            playerOnGround = true;
        }

        if(fa.getUserData() != null && fa.getUserData().equals("door_1"))
        {
            door_1 = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("door_1"))
        {
            door_1 = true;
        }

        if(fa.getUserData() != null && fa.getUserData().equals("door_1_out"))
        {
            door_1_out = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("door_1_out"))
        {
            door_1_out = true;
        }

    }

    @Override
    public void endContact(Contact contact)
    {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if(fa.getUserData() != null && fa.getUserData().equals("foot"))
        {
            playerOnGround = false;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot"))
        {
            playerOnGround = false;
        }

        if(fa.getUserData() != null && fa.getUserData().equals("door_1"))
        {
            door_1 = false;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("door_1"))
        {
            door_1 = false;
        }

        if(fa.getUserData() != null && fa.getUserData().equals("door_1_out"))
        {
            door_1_out = false;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("door_1_out"))
        {
            door_1_out = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold)
    {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse)
    {

    }
}