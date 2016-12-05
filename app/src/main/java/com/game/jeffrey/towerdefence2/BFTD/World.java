package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;
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

    public static List<Square> grid = new LinkedList<>();
    public static int gridWidth = 10;
    public static int gridHeight = 10;
    public static int gridSize = 60;

    public static int viewX = 0;
    public static int viewY = 0;


    Tower tower = new Tower();

    List<Tower> towers = new ArrayList<>();

    // RESOLUTION: 640 x 360 (16:9)

    public static void generateGrid() {
        for (int y = 0; y < gridHeight; y++)
        {
            for (int x = 0; x < gridWidth; x++)
            {
                grid.add(new Square(x, y, gridSize));
            }
        }
    }
    public void generateRandomTowers()
    {
        for(int i = 0; i < 5 ;i++){
            towers.add(new Tower(i+5,(i*10)+50));
        }
    }

    public void update(float deltaTime, float touchX, float touchY, boolean isTouch, boolean isDoubleTouch)
    {
        if(towers.isEmpty()){
            generateRandomTowers();
        }

        if(isTouch && !isDoubleTouch)
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
}
