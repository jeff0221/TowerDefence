package com.game.jeffrey.towerdefence2.BFTD;


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
    public static int gridWidth = 12;
    public static int gridHeight = 21;
    public static int gridSize = 30;

    // RESOLUTION: 640 x 360 (16:9)

    public static void generateGrid() {
        for (int y = 0; y < gridHeight; y++)
        {
            for (int x = 0; x < gridWidth; x++)
            {
                grid.add(new Square(x, y, gridSize));
                //Log.d("world", "x: " + (x * gridSize) + ", y: " + (y * gridSize));
            }
        }
    }
}
