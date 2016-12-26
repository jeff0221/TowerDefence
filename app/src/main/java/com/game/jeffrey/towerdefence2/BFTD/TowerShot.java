package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Zonde on 16-12-2016.
 */

public class TowerShot
{
    GenericWorker shotFrom;
    GenericCustomer target;
    public float x;
    public float y;

    int lifeTime = 0;

    float startX = 0;
    float startY = 0;
    float viewX = 0;
    float viewY = 0;
    boolean touched = false;

    public TowerShot(GenericWorker shotFrom, GenericCustomer target)
    {
        this.shotFrom = shotFrom;
        this.target = target;
        this.x = shotFrom.x + (GenericWorker.WIDTH/2);
        this.y = shotFrom.y + (GenericWorker.HEIGHT/2);
    }
}
