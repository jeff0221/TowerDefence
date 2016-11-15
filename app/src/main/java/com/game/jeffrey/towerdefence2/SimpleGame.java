package com.game.jeffrey.towerdefence2;


import android.view.KeyEvent;
import android.view.View;

public class SimpleGame extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        return new SimpleScreen(this);
    }


}
