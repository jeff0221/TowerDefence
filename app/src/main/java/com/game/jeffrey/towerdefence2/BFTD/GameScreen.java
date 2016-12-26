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

    public GameScreen(GameEngine game)
    {
        super(game);
        world = new World();
        renderer = new WorldRenderer(game, world);
    }

    @Override
    public void update(float deltaTime)
    {
        renderer.render();
        world.update(deltaTime, game.getTouchX(0), game.getTouchY(0), game.isTouchDown(0), game.isDoubleTouchDown(),game.isTapped());
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
