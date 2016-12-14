package com.game.jeffrey.towerdefence2.BFTD;

import android.graphics.Rect;

/**
 * Created by Hans on 21-11-2016.
 */
public class Square extends ItemEntity
{

    private int dispX;
    private int dispY;
    private int size;
    private Rect rect;

    private int viewX = 0;
    private int viewY = 0;

    public ItemEntity.typeOfItem type = typeOfItem.Ground;

    public Square(float x, float y)
    {
        super(x,y);
        super.type = typeOfItem.Ground;
        this.size = 30;
        this.dispX = (int)x * size;
        this.dispY = (int)y * size;
        this.rect = new Rect(dispX, dispY, dispX + size, dispY + size);
    }


    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getSize()
    {
        return size;
    }

    public Rect getRect()
    {
        return rect;
    }

    public void setRect() {
        this.rect.left = dispX + viewX;
        this.rect.top = dispY + viewY;
        this.rect.right = dispX + viewX + this.size;
        this.rect.bottom = dispY + viewY + this.size;
    }

    public int getDispX()
    {
        return dispX;
    }

    public int getDispY()
    {
        return dispY;
    }
}
