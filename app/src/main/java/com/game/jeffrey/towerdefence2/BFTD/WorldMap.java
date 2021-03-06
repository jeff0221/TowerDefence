package com.game.jeffrey.towerdefence2.BFTD;

public class WorldMap
{
    public int gridWidth = 20;
    public int gridHeight = 30;
    public int gridSize = 30;

    public ItemEntity[][] grid;
    float startX = 0;
    float startY = 0;
    float viewX = 0;
    float viewY = 0;
    boolean touched = false;



    // PREDEFINED MOVE LATER
    public int pathStartX = 1;
    public int pathStartY = 1;
    public int pathGoalX = 9;
    public int pathGoalY = 17;

    public WorldMap()
    {
        this.grid = new ItemEntity[gridWidth][gridHeight];
        generateGrid();
    }

    public void generateGrid() {


        ItemEntity itemContext;

        for (int y = 0; y < gridHeight; y++)
        {
            for (int x = 0; x < gridWidth; x++)
            {
                itemContext = new Square(x, y);

                //generate walls around map
                if(y == 0 || y == gridHeight-1)
                {
                    itemContext = new Wall(x, y);
                }
                if(x == 0 || x == gridWidth-1)
                {
                    itemContext = new Wall(x, y);
                }

                if (x == pathStartX && y == pathStartY) {
                    itemContext = new StartPoint(x, y);
                }
                if (x == pathGoalX && y == pathGoalY) {
                    itemContext = new EndPoint(x, y);
                }

                //final array insertion
                itemContext.arrayX = x;
                itemContext.arrayY = y;

                this.grid[x][y] = itemContext;
            }
        }


    }


}
