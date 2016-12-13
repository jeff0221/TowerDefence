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
    Bitmap bottomMenuImage;
    Bitmap wallImage;
    Bitmap highlightImage;


    public WorldRenderer(GameEngine game, World world)
    {
        this.game = game;
        this.world = world;
        //load bitmaps
        this.towerImage = game.loadBitmap("test_tower.png");
        this.squareGFX = game.loadBitmap("square.png");
        this.bottomMenuImage = game.loadBitmap("Placeholdermenu.png");
        this.wallImage = game.loadBitmap("BFTDWall.png");
        this.highlightImage = game.loadBitmap("HighlightPicture.png");
    }

    public void render()
    {
        //first
        game.clearFramebuffer(Color.rgb(0,0,0));

        //second
        renderWorld();

        //third

        renderBottomMenu();

        renderDraggedItem();


    }

    public void deprecatedRenderTowers()
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
        for(int y = 0; y < world.worldMap.gridHeight;y++)
        {
            for(int x = 0; x < world.worldMap.gridWidth;x++)
            {
                int posX = 0;
                int posY = 0;
                itemContext = world.worldMap.grid[x][y];
                                        //[Width][Height]
                posX = y*world.worldMap.gridSize;
                posY = x*world.worldMap.gridSize;

                int imagePosX = (int)world.worldMap.viewX + posX;
                int imagePosY = (int)world.worldMap.viewY + posY;

                if(itemContext.type == ItemEntity.typeOfItem.Tower) //if the square at said grid is something it should draw something specific
                {
                    renderTower(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Wall)
                {
                    renderWall(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Employee)
                {
                    renderSquare(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Ground)
                {
                    renderSquare(imagePosX,imagePosY);
                }


                if(world.highLighted != null && itemContext.arrayY == world.highLighted.arrayY &&
                    itemContext.arrayX == world.highLighted.arrayX)
                {
                    game.drawBitmap(highlightImage,imagePosX,imagePosY);
                }

                world.worldMap.grid[x][y].x = ((int)world.worldMap.viewX + posX);
                world.worldMap.grid[x][y].y = ((int)world.worldMap.viewY + posY);

                world.worldMap.grid[x][y].arrayX = x;
                world.worldMap.grid[x][y].arrayY = y;
            }
        }
    }
    public void renderBottomMenu()
    {
        game.drawBitmap(bottomMenuImage, (int)BottomMenu.MIN_X,(int)BottomMenu.MIN_Y);
        if(world.bottomMenu.selectedItem != null && !world.bottomMenu.itemTouched)
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
    public void renderTower(int x, int y)
    {
        game.drawBitmap(towerImage,x,y,0,0,30,30);
    }
    public void renderWall(int x, int y)
    {
        game.drawBitmap(wallImage,x,y,0,0,30,30);
    }
    public void renderSquare(int x, int y)
    {
        game.drawBitmap(squareGFX,x,y,0,0,30,30);
    }
}
