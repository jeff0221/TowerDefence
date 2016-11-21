package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Hans on 21-11-2016.
 */
public class Square
{
    private int x;
    private int y;
    private int size;

    public Square(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getSize()
    {
        return size;
    }
}
