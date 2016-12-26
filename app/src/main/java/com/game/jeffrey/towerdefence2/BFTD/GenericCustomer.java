package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Zonde on 14-12-2016.
 */

public abstract class GenericCustomer
{
    enum customerTypes
    {
        Fast,
        Sturdy,
        HighCost
    }

    public static float WIDTH = 30;
    public static float HEIGHT = 30;

    private int HP;
    private int speed;
    private int lifeCost;
    public float x = 0;
    public float y = 0;
    public int arrayX = 0;
    public int arrayY = 0;
    public boolean spawned = false;
    public float pathProgression = 0;
    public customerTypes type;

    //visual test
    public float smoothX = 0;
    public float smoothY = 0;

    ItemEntity currentSpace;

    //Just to have the visuals be better i guess
    float startX = 0;
    float startY = 0;
    float viewX = 0;
    float viewY = 0;
    boolean touched = false;

    private int arrayIndex = 0;

    public GenericCustomer(int HP, int speed, int lifeCost)
    {
        this.HP = HP;
        this.speed = speed;
        this.lifeCost = lifeCost;
    }

    public int getArrayIndex()
    {
        return arrayIndex;
    }

    public void setArrayIndex(int arrayIndex)
    {
        this.arrayIndex = arrayIndex;
    }

    public int getHP()
    {
        return HP;
    }

    public void setHP(int HP)
    {
        this.HP = HP;
    }

    public float getSpeed()
    {
        return speed/30;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public int getLifeCost()
    {
        return lifeCost;
    }

    public void setLifeCost(int lifeCost)
    {
        this.lifeCost = lifeCost;
    }
}
