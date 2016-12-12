package com.game.jeffrey.towerdefence2.BFTD;



public class WorldMap
{
    public ItemEntity[][] grid;
    public int gridWidth = 20;
    public int gridHeight = 30;
    public int gridSize = 30;
    float startX = 0;
    float startY = 0;
    float viewX = 0;
    float viewY = 0;
    boolean touched = false;

    public WorldMap()
    {
        grid = new ItemEntity[gridWidth][gridHeight];
        generateGrid();
    }

    public void generateGrid() {
        for (int y = 0; y < gridHeight; y++)
        {
            for (int x = 0; x < gridWidth; x++)
            {
                grid[x][y] = new Square(x, y);
            }
        }
    }
}
