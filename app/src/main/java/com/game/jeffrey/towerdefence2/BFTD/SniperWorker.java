package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Zonde on 26-12-2016.
 */

public class SniperWorker extends GenericWorker
{
    public SniperWorker(int damage, int stamina, int attackSpeed, int level, int range,ItemEntity platform)
    {
        super(damage, stamina, attackSpeed, level, range, platform);
        super.type = workerType.Sniper;
    }
    public SniperWorker(ItemEntity platform)
    {
        super(250, 10, 50, 1, 200, platform);
        super.type = workerType.Sniper;
    }

}
