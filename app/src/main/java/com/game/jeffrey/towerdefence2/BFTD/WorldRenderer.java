package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.game.jeffrey.towerdefence2.GameEngine;

public class WorldRenderer
{
    GameEngine game;
    World world;
    Bitmap towerImage;
    Bitmap squareGFX;
    Bitmap bottomMenuImage;
    Bitmap wallImage;


    public WorldRenderer(GameEngine game, World world)
    {
        this.game = game;
        this.world = world;
        //load bitmaps
        this.towerImage = game.loadBitmap("test_tower.png");
        this.squareGFX = game.loadBitmap("square.png");
        this.bottomMenuImage = game.loadBitmap("Placeholdermenu.png");
        this.wallImage = game.loadBitmap("BFTDWall.png");
    }

    public void render()
    {
        //first
        game.clearFramebuffer(Color.rgb(0,0,0));

        //second
        renderWorld();

        //third
        renderTowers();

        renderBottomMenu();

        renderDraggedItem();


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

    public void renderWorld()
    {
        ItemEntity itemContext;
        for(int x = 0; x < world.worldMap.gridHeight;x++)
        {
            for(int y = 0; y < world.worldMap.gridWidth;y++)
            {
                int posX = 0;
                int posY = 0;

                itemContext = world.worldMap.grid[y][x];

                if(x == 0 || x == world.worldMap.gridHeight-1)
                {
                    itemContext.type = ItemEntity.typeOfItem.Wall;
                }

                if(y == 0 || y == world.worldMap.gridWidth-1)
                {
                    itemContext.type = ItemEntity.typeOfItem.Wall;
                }

                 if(itemContext.type == ItemEntity.typeOfItem.Tower) //if the square at said grid is something it should draw something specific
                {
                    posX = x*world.worldMap.gridSize;
                    posY = y*world.worldMap.gridSize;

                    game.drawBitmap(squareGFX,  (int)world.worldMap.viewX + posX,
                            (int)world.worldMap.viewY + posY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Wall)
                {
                    posX = x*world.worldMap.gridSize;
                    posY = y*world.worldMap.gridSize;

                    game.drawBitmap(wallImage,  (int)world.worldMap.viewX + posX,
                            (int)world.worldMap.viewY + posY);
                }else if(itemContext.type == ItemEntity.typeOfItem.Employee)
                {
                    posX = x*world.worldMap.gridSize;
                    posY = y*world.worldMap.gridSize;

                    game.drawBitmap(towerImage,  (int)world.worldMap.viewX + posX,
                            (int)world.worldMap.viewY + posY,0,0,30,30);
                }
                world.worldMap.grid[y][x].x = ((int)world.worldMap.viewX + posX);
                world.worldMap.grid[y][x].y = ((int)world.worldMap.viewY + posY);
            }
        }
    }

    public void renderBottomMenu()
    {
        game.drawBitmap(bottomMenuImage, (int)BottomMenu.MIN_X,(int)BottomMenu.MIN_Y);
        if(world.bottomMenu.selectedItem != null)
        {
            int itemHolderX = 280;
            int itemHolderY = 595;
            ItemEntity contextItem = world.bottomMenu.selectedItem;
            if(contextItem.type == ItemEntity.typeOfItem.Tower)
            {
                game.drawBitmap(towerImage,itemHolderX,itemHolderY);
            }
            else if(contextItem.type == ItemEntity.typeOfItem.Wall)
            {
                game.drawBitmap(wallImage,itemHolderX,itemHolderY);
            }
        }
    }

    public void renderDraggedItem()
    {
        if(world.bottomMenu.selectedItem != null)
        {
            ItemEntity contextItem = world.bottomMenu.selectedItem;

            if(contextItem.type == ItemEntity.typeOfItem.Tower)
            {
                game.drawBitmap(towerImage,(int)contextItem.x,(int)contextItem.y);
            }
            else if(contextItem.type == ItemEntity.typeOfItem.Wall)
            {
                game.drawBitmap(wallImage,(int)contextItem.x,(int)contextItem.y);
            }
        }
    }

}
