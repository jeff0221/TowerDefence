package com.game.jeffrey.towerdefence2;


import java.util.Random;

public class ClearScreen extends Screen
{
    Random rand = new Random();

    public ClearScreen(GameEngine game)
    {
        super(game);
    }


    @Override
    public void update(float deltaTime)
    {
        game.clearFramebuffer(rand.nextInt());
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}
