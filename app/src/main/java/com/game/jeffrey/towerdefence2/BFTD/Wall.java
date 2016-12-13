package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Zonde on 12-12-2016.
 */
public class Wall extends ItemEntity
{
    public static float WIDTH = 50;
    public static float HEIGHT = 33;

    public ItemEntity.typeOfItem type = typeOfItem.Wall;

    public Wall(float x, float y)
    {
        super(x,y);

        super.type = typeOfItem.Wall;
    }


}
