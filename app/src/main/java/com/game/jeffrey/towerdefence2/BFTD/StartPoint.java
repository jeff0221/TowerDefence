package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Hans on 14-12-2016.
 */

public class StartPoint extends ItemEntity
{
    public static float WIDTH = 50;
    public static float HEIGHT = 33;

    public ItemEntity.typeOfItem type = typeOfItem.StartPoint;

    public StartPoint(float x, float y)
    {
        super(x,y);

        super.type = typeOfItem.StartPoint;
    }
}
