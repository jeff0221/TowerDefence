package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

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
    Bitmap squareGFX;

    public GameScreen(GameEngine game)
    {
        super(game);
        squareGFX = game.loadBitmap("square.png");
        World.generateGrid();
    }

    @Override
    public void update(float deltaTime)
    {
        game.clearFramebuffer(Color.rgb(0, 0, 0));
        //Log.d("gs", "woop");
        Square square = null;
        int squareMax = world.grid.size();
        for (int i = 0; i < squareMax; i++)
        {
            square = world.grid.get(i);
            game.drawBitmap(squareGFX, square.getX() * world.gridSize, square.getY() * world.gridSize);
        }
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
