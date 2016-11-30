package com.game.jeffrey.towerdefence2.BFTD;

import android.view.View;

import com.game.jeffrey.towerdefence2.GameEngine;
import com.game.jeffrey.towerdefence2.Screen;

public class BlackFriday extends GameEngine
{

    @Override
    public Screen createStartScreen()
    {
        //this.music = this.loadMusic("music.ogg");
        return new MainMenuScreen(this);
    }

    public void onPause()
    {
        super.onPause();
        //this.music.pause();
    }

    public void onResume()
    {
        super.onResume();
        //this.music.play();
    }

}
