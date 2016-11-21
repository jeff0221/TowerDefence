package com.game.jeffrey.towerdefence2.BFTD;


import com.game.jeffrey.towerdefence2.GameEngine;
import com.game.jeffrey.towerdefence2.Screen;

public class GameScreen extends Screen
{
    enum State
    {
        Paused,
        Running,
        GameOver
    }

    State state = State.Running;

    //TODO: bitmaps and sounds missing

    World world;
    WorldRenderer renderer;

    public GameScreen(GameEngine game)
    {
        super(game);

    }

    @Override
    public void update(float deltaTime)
    {

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
