package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;

import com.game.jeffrey.towerdefence2.GameEngine;
import com.game.jeffrey.towerdefence2.Screen;

public class MainMenuScreen extends Screen
{
    Bitmap mainmenu;
    float startTime = System.nanoTime();
    float passedTime = 0.0f;
    Typeface font;


    public MainMenuScreen(GameEngine game)
    {
        super(game);
        this.font = game.loadFont("font.ttf");
        this.mainmenu = game.loadBitmap("blackfriday.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if(game.isTouchDown(0))
        {
            Log.d("mms", "click");
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
