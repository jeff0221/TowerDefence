package com.game.jeffrey.towerdefence2.BFTD;

public class Tower extends ItemEntity
{
    public static float WIDTH = 30;
    public static float HEIGHT = 30;
    public int aimRotation = 0;

    public ItemEntity.typeOfItem type = typeOfItem.Tower;

    public GenericWorker worker;

    public Tower(float x, float y, GenericWorker worker)
    {
        super(x,y);

        super.type = typeOfItem.Tower;

            //GenericWorker contextWorker = new FastWorker(10,10,20,1,50);
            GenericWorker contextWorker = worker;
            contextWorker.x = x;
            contextWorker.y = y;
            contextWorker.arrayX = arrayX;
            contextWorker.arrayY = arrayY;
            this.worker = worker;

    }



}
