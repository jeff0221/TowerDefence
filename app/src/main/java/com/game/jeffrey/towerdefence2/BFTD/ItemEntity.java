package com.game.jeffrey.towerdefence2.BFTD;

public abstract class ItemEntity
{
    public static float WIDTH = 50;
    public static float HEIGHT = 33;

    public ItemEntity cameFrom;

    enum typeOfItem
    {
        Tower,
        Wall,
        Employee,
        Ground,
        StartPoint,
        GoalPoint
    }

    public typeOfItem type = typeOfItem.Ground;
    public float x;
    public float y;
    public int arrayX = 100;
    public int arrayY = 100;

    public ItemEntity(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /*
    public String toString() {
        return "x: " + arrayX + ", y: " + arrayY;
    }
    */
}
