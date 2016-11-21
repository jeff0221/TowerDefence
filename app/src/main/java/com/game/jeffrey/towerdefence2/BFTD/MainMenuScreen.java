package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;

import com.game.jeffrey.towerdefence2.GameEngine;
import com.game.jeffrey.towerdefence2.Screen;

public class MainMenuScreen extends Screen
{
    Bitmap mainmenu;
    float startTime = System.nanoTime();
    float passedTime = 0.0f;

    public MainMenuScreen(GameEngine game)
    {
        super(game);
        //mainmenu = game.loadBitmap("mainmenu.png");

    }

    @Override
    public void update(float deltaTime)
    {
        if(game.isTouchDown(0))
        {
            game.setScreen(new GameScreen(game));
            return;
        }

        passedTime = passedTime + deltaTime;
        game.drawBitmap(mainmenu,0,0);


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
