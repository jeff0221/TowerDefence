package com.game.jeffrey.towerdefence2.BFTD;

import android.graphics.Rect;

/**
 * Created by Hans on 21-11-2016.
 */
public class Square implements ItemEntity
{
    public int x;
    public int y;
    private int dispX;
    private int dispY;
    private int size;
    private Rect rect;

    private int viewX = 0;
    private int viewY = 0;

    public ItemEntity.typeOfItem type = typeOfItem.Tower;


    public Square(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.dispX = x * size;
        this.dispY = y * size;
        this.size = size;
        this.rect = new Rect(dispX, dispY, dispX + size, dispY + size);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
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

    public void setViewX(int viewX)
    {
        this.viewX = viewX;
    }

    public void setViewY(int viewY)
    {
        this.viewY = viewY;
    }

    public int getViewX()
    {
        return viewX;
    }

    public int getViewY()
    {
        return viewY;
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
