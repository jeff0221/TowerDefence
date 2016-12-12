package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class World
{
    public static float MIN_X = 0;
    public static float MIN_Y = 10;
    public static float MAX_X = 359;
    public static float MAX_Y = 639;

    public WorldMap worldMap = new WorldMap();

    public float viewX = 0;
    public float viewY = 0;

    Bitmap squareGFX;
    Rect src;
    boolean touched = false;



    Tower tower = new Tower();

    List<Tower> towers = new ArrayList<>();

    // RESOLUTION: 640 x 360 (16:9)


    public void generateRandomTowers()
    {
        for(int i = 0; i < 5 ;i++)
        {
            towers.add(new Tower(i+5,(i*10)+50));
        }
    }

    public void update(float deltaTime, float touchX, float touchY, boolean isTouch, boolean isDoubleTouch)
    {
        if(towers.isEmpty())
        {
            generateRandomTowers();
        }
        if(isTouch)
        {
            tower.x = touchX - Tower.WIDTH/2;
            tower.y = touchY - Tower.HEIGHT/2;
            //towers array
            Tower contextTower = pickTower(touchX,touchY);
                if(contextTower != null)
                {
                    contextTower.x = touchX - Tower.WIDTH/2;
                    contextTower.y = touchY - Tower.HEIGHT/2;
                }
        }

        if(isTouch)
        {
            Square contextSquare = pickSquare(touchX,touchY);
            if(contextSquare != null)
            {
                Log.d("Square picked at x: " + contextSquare.x," and y: " + contextSquare.y);
                Log.d("Square type","" + contextSquare.type);
            }
        }

        moveWorldView(touchX,touchY,isTouch);




    }

    public Square pickSquare(float touchX, float touchY)
    {
        for(int x = 0; x < worldMap.gridWidth-1;x++)
        {
            for(int y = 0; y < worldMap.gridHeight-1;y++)
            {
                Square contextSquare = worldMap.grid[x][y];

                if(contextSquare.x == touchX || touchX > contextSquare.x && touchX < contextSquare.x + 30)
                {
                    if(contextSquare.y == touchY || touchY > contextSquare.y && touchY < contextSquare.y + 30)
                    {
                        return contextSquare;
                    }
                }
            }
        }
        return null;
    }

    public Tower pickTower(float touchX, float touchY)
    {
        for(int i = 0; i < towers.size();i++)
        {
            Tower contextTower = towers.get(i);
            if(towerCollider(touchX,touchY,contextTower))
            {
                return contextTower;
            }
        }
    return null;
    }

    public boolean towerCollider(float touchX, float touchY, Tower tower)
    {
        boolean result = false;
        if(tower.x == touchX || touchX > tower.x && touchX < tower.x + Tower.WIDTH)
        {
            if(tower.y == touchY || touchY > tower.y && touchY < tower.y + Tower.HEIGHT)
            {
            result = true;

            }
        }
        return result;
    }

    public void moveWorldView(float touchX, float touchY, boolean isTouch)
    {
        //https://yal.cc/gamemaker-click-n-drag-to-pan-view/ guide used
        if(isTouch && pickTower(touchX,touchY) == null)
        {
            if(!touched)
            {
            touched = true;
            worldMap.startX = touchX;
            worldMap.startY = touchY;
            }

            if(touched)
            {
                worldMap.viewX = worldMap.viewX - (worldMap.startX - touchX);
                worldMap.viewY = worldMap.viewY - (worldMap.startY - touchY);

                worldMap.viewX = Math.max((int)(-360 * 2), Math.min((int)worldMap.viewX, World.MAX_X / 2));
                worldMap.viewY = Math.max((int)(-640 / 2), Math.min((int)worldMap.viewY, World.MAX_Y / 2));

                worldMap.startX = touchX;
                worldMap.startY = touchY;

                Log.d("diffpos X","" + worldMap.viewX);
                Log.d("diffpos Y","" + worldMap.viewY);
            }
        }
        else
        {
            touched = false;
        }
    }
}
