package com.game.jeffrey.towerdefence2.BFTD;

public abstract class ItemEntity
{
    enum typeOfItem
    {
        Tower,
        Wall,
        Employee
    }

    public typeOfItem type;
    public float x;
    public float y;
    public int arrayX;
    public int arrayY;

    public ItemEntity(float x, float y)
    {
        this.x = x;
        this.y = y;

    }
}
