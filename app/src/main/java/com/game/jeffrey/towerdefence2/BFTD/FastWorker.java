package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Zonde on 16-12-2016.
 */

public class FastWorker extends GenericWorker
{
    public FastWorker(int damage, int stamina, int attackSpeed, int level, int range,ItemEntity platform)
    {
        super(damage, stamina, attackSpeed, level, range, platform);
        super.type = workerType.Fast;
    }
}
