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
    Bitmap floorImage;
    Bitmap towerAimImage;
    Bitmap enemyImage;


    public WorldRenderer(GameEngine game, World world)
    {
        this.game = game;
        this.world = world;
        //load bitmaps
        this.towerImage = game.loadBitmap("TowerPlatform.png");
        this.squareGFX = game.loadBitmap("square.png");
        this.bottomMenuImage = game.loadBitmap("Placeholdermenu.png");
        this.wallImage = game.loadBitmap("BFTDWall.png");
        this.highlightImage = game.loadBitmap("HighlightPicture.png");
        this.floorImage = game.loadBitmap("floorTile.png");
        this.towerAimImage = game.loadBitmap("TowerGunPositions.png");
        this.enemyImage = game.loadBitmap("xyellowmonster.png");
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

        renderEnemy();
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

                if(itemContext.type == ItemEntity.typeOfItem.Tower)
                {
                    renderTower(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Wall)
                {
                    renderWall(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Employee)
                {
                    renderGround(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Ground)
                {
                    renderGround(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.StartPoint ||
                        itemContext.type == ItemEntity.typeOfItem.GoalPoint) {
                    renderPoints(imagePosX, imagePosY);
                }


                if(world.highLighted != null && itemContext.arrayY == world.highLighted.arrayY &&
                    itemContext.arrayX == world.highLighted.arrayX)
                {
                    game.drawBitmap(highlightImage,imagePosX,imagePosY);
                }

                for(int i = 0; i < world.highLightedEntities.size();i++)
                {
                    if(world.highLightedEntities != null &&
                    itemContext.arrayY == world.highLightedEntities.get(i).arrayY &&
                    itemContext.arrayX == world.highLightedEntities.get(i).arrayX)
                    {
                        game.drawBitmap(highlightImage,imagePosX,imagePosY);
                    }
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
            else if(contextItem.type == ItemEntity.typeOfItem.Ground)
            {
                game.drawBitmap(floorImage,itemHolderX,itemHolderY);
            }
        }
        if(world.drawingMaze)
        {
            game.drawBitmap(highlightImage,175,(int)BottomMenu.MIN_Y);
            game.drawBitmap(highlightImage,153,(int)BottomMenu.MIN_Y);
            game.drawBitmap(highlightImage,175,(int)BottomMenu.MIN_Y + 30);
            game.drawBitmap(highlightImage,153,(int)BottomMenu.MIN_Y + 30);
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

        int pictureSpaceX = 0;
        int pictureSpaceY = 0;

        if (world.aimRotation >= 270 && world.aimRotation < 360)
        {
            pictureSpaceY = 90;
            //System.out.println("UP " + pictureSpaceY);
        }
        else if(world.aimRotation >= 180 && world.aimRotation < 270)
        {
            pictureSpaceY = 60;
            //System.out.println("RIGHT " + pictureSpaceY);
        }
        else if(world.aimRotation >= 90 && world.aimRotation < 180)
        {
            pictureSpaceY = 0;
            //System.out.println("DOWN " + pictureSpaceY);
        }
        else if(world.aimRotation >= 0 && world.aimRotation < 90)
        {
            pictureSpaceY = 30;
            //System.out.println("LEFT " + pictureSpaceY);
        }

        game.drawBitmap(towerAimImage,x,y,pictureSpaceX,pictureSpaceY,30,30);

    }
    public void renderWall(int x, int y)
    {
        game.drawBitmap(wallImage,x,y,0,0,30,30);
    }
    public void renderGround(int x, int y)
    {
        game.drawBitmap(floorImage,x,y,0,0,30,30);
    }
    public void renderPoints(int x, int y) {
        game.drawBitmap(highlightImage, x, y, 0, 0, 30, 30);
    }

    public void renderEnemy()
    {
        //if the thing is spawned AND it is on screen
        if(world.testCustomer.spawned &&
                world.testCustomer.x >= 0 &&
                world.testCustomer.y >= 0 &&
                world.testCustomer.x <= World.MAX_X  &&
                world.testCustomer.y <= World.MAX_Y - 70)
        {
            int imagePosX = (int)(world.testCustomer.x);
            int imagePosY = (int)(world.testCustomer.y);

            drawGenericEnemy(imagePosX,imagePosY);

world.testCustomer.x = world.testCustomer.viewX - world.testCustomer.currentSpace.arrayX / 30;
world.testCustomer.y = world.testCustomer.viewY - world.testCustomer.currentSpace.arrayY / 30;
        }
        else
        {
            if(world.testCustomer.currentSpace != null)
            {
            world.testCustomer.x = world.testCustomer.currentSpace.x;
            world.testCustomer.y = world.testCustomer.currentSpace.y;
            }
        }
    }

    public void drawGenericEnemy(int x, int y)
    {
        game.drawBitmap(enemyImage,x,y);
    }
}
