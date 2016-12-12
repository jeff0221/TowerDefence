package com.game.jeffrey.towerdefence2.BFTD;

public class Tower extends ItemEntity
{
    public static float WIDTH = 50;
    public static float HEIGHT = 33;

    public ItemEntity.typeOfItem type = typeOfItem.Tower;

    public Tower(float x, float y)
    {
        super(x,y);

        super.type = typeOfItem.Tower;
    }



}
