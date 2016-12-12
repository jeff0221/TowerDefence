package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;
import android.graphics.Color;

import com.game.jeffrey.towerdefence2.GameEngine;

public class WorldRenderer
{
    GameEngine game;
    World world;
    Bitmap towerImage;
    Bitmap squareGFX;


    public WorldRenderer(GameEngine game, World world)
    {
        this.game = game;
        this.world = world;
        //load bitmaps
        this.towerImage = game.loadBitmap("test_tower.png");
        this.squareGFX = game.loadBitmap("square.png");
    }

    public void render()
    {
        //first
        game.clearFramebuffer(Color.rgb(0,0,0));

        //second
        Square square;
        for(int x = 0; x < world.worldMap.gridHeight;x++)
        {
            for(int y = 0; y < world.worldMap.gridWidth;y++)
            {
                int posX = 0;
                int posY = 0;

                square = world.worldMap.grid[y][x];



                if(x == 0 || x == world.worldMap.gridHeight-1)
                {
                    square.type = ItemEntity.typeOfItem.Wall;
                }

                if(y == 0 || y == world.worldMap.gridWidth-1)
                {
                    square.type = ItemEntity.typeOfItem.Wall;
                }



                if(square.type == ItemEntity.typeOfItem.Tower) //if the square at said grid is something it should draw something specific
                {
                    posX = x*world.worldMap.gridSize;
                    posY = y*world.worldMap.gridSize;

                    game.drawBitmap(squareGFX,  (int)world.worldMap.viewX + posX,
                                                (int)world.worldMap.viewY + posY);
                }
                else if(square.type == ItemEntity.typeOfItem.Wall)
                {
                    posX = x*world.worldMap.gridSize;
                    posY = y*world.worldMap.gridSize;

                    game.drawBitmap(towerImage,  (int)world.worldMap.viewX + posX,
                            (int)world.worldMap.viewY + posY,0,0,30,30);
                }

                world.worldMap.grid[y][x].setX((int)world.worldMap.viewX + posX);
                world.worldMap.grid[y][x].setY((int)world.worldMap.viewY + posY);

            }
        }

        //third
        renderTowers();
    }

    public void renderTowers()
    {
        Tower tower;
        for(int i = 0; i < world.towers.size();i++)
        {
            tower = world.towers.get(i);
            game.drawBitmap(towerImage, (int)tower.x, (int)tower.y);
        }
    }
}
