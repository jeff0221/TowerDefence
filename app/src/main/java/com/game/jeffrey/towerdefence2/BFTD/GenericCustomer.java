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

    private int HP;
    private int speed;
    private int lifeCost;
    public float x = 0;
    public float y = 0;
    public boolean spawned = false;
    public int pathProgression = 0;

    public GenericCustomer(int HP, int speed, int lifeCost)
    {
        this.HP = HP;
        this.speed = speed;
        this.lifeCost = lifeCost;
    }

    public int getHP()
    {
        return HP;
    }

    public void setHP(int HP)
    {
        this.HP = HP;
    }

    public int getSpeed()
    {
        return speed;
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
