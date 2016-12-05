package com.game.jeffrey.towerdefence2.BFTD;

public class Tower
{
    public static float WIDTH = 50;
    public static float HEIGHT = 33;
    public float x = 180 - WIDTH*2;
    public float y = World.MAX_Y - 2*HEIGHT;

    public Tower(){}
    public Tower(float x, float y)
    {
        this.x = x;
        this.y = y;
    }



}
