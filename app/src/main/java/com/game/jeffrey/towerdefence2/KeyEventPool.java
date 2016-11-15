package com.game.jeffrey.towerdefence2;

public class KeyEventPool extends Pool<KeyEvent>
{

    @Override
    protected KeyEvent newItem()
    {
        return new KeyEvent();
    }
}
