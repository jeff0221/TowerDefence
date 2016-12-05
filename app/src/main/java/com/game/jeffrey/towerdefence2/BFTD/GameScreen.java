package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.game.jeffrey.towerdefence2.GameEngine;
import com.game.jeffrey.towerdefence2.MultiTouchHandler;
import com.game.jeffrey.towerdefence2.R;
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
    Rect src;
    boolean touched = false;
    int startX = 0;
    int startY = 0;
    int diffX = 0;
    int diffY = 0;


    public GameScreen(GameEngine game)
    {
        super(game);
        squareGFX = game.loadBitmap("square.png");
        src = new Rect(0, 0, squareGFX.getWidth()-1, squareGFX.getHeight()-1);
        World.generateGrid();
        world = new World();
        renderer = new WorldRenderer(game, world);
    }

    @Override
    public void update(float deltaTime)
    {
        game.clearFramebuffer(Color.rgb(0, 0, 0));

        if(game.isDoubleTouchDown())
        {
            if (!touched) {
                touched = true;
                startX = game.getTouchX(0);
                startY = game.getTouchY(0);
                diffX = World.viewX;
                diffY = World.viewY;
            } else {
                diffX = game.getTouchX(0) - startX;
                diffY = game.getTouchY(0) - startY;
            }

            World.viewX = diffX;
            World.viewY = diffY;

            //Log.d("world", "x: " + diffX + ", y: " + diffY);
        } else {
            touched = false;
        }

        Square square = null;
        int squareMax = world.grid.size();
        int curX;
        int curY;
        for (int i = 0; i < squareMax; i++)
        {
            square = world.grid.get(i);
            square.setViewX(World.viewX);
            square.setViewY(World.viewY);
            square.setRect();
            curX = square.getDispX() + World.viewX;
            curY = square.getDispY() + World.viewY;
            if (curX + World.gridSize > World.MIN_X && curX < World.MAX_X &&
                curY + World.gridSize > World.MIN_Y && curY < World.MAX_Y) {
                game.drawBitmap(squareGFX, src, square.getRect());
            }
        }

        renderer.render();
        world.update(deltaTime, game.getTouchX(0), game.getTouchY(0), game.isTouchDown(0), game.isDoubleTouchDown());

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
