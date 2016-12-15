package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Zonde on 15-12-2016.
 */

public class SturdyCustomer extends GenericCustomer
{
    public SturdyCustomer(int HP, int speed, int lifeCost)
    {
        super(HP, speed, lifeCost);
        type = customerTypes.Sturdy;
    }
}
