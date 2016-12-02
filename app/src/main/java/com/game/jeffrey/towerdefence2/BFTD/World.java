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

    public void update(float deltaTime, float touchX, float touchY, boolean isTouch)
    {
        if (isTouch)
        {
            tower.x = touchX - Tower.WIDTH/2;
            tower.y = touchY - Tower.HEIGHT/2;
        }
    }
}
