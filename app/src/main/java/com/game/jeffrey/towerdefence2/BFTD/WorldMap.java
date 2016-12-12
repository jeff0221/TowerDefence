package com.game.jeffrey.towerdefence2.BFTD;


import java.util.LinkedList;
import java.util.List;

public class WorldMap
{
    public Square[][] grid;
    public int gridWidth = 20;
    public int gridHeight = 30;
    public int gridSize = 30;
    float startX = 0;
    float startY = 0;
    float diffX = 0;
    float diffY = 0;
    float viewX = 0;
    float viewY = 0;

    public WorldMap()
    {
        grid = new Square[gridWidth][gridHeight];
        generateGrid();
    }

    public void generateGrid() {
        for (int y = 0; y < gridHeight; y++)
        {
            for (int x = 0; x < gridWidth; x++)
            {
                grid[x][y] = new Square(x, y, gridSize);
            }
        }
    }
}
