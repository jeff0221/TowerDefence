package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Zonde on 16-12-2016.
 */

public class FastWorker extends GenericWorker
{
    public FastWorker(int damage, int stamina, int attackSpeed, int level, int range, ItemEntity platform)
    {
        super(damage, stamina, attackSpeed, level, range, platform);
        super.type = workerType.Fast;
    }
    public FastWorker(ItemEntity platform)
    {
        super(10,10,20,1,100, platform);
        super.type = workerType.Fast;
    }
}
