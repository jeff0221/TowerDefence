package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

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
    Bitmap standardShotImage;
    Bitmap topBarImage;
    Typeface font;


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
        this.standardShotImage = game.loadBitmap("gunfire.png");
        this.font = game.loadFont("font.ttf");
        this.topBarImage = game.loadBitmap("Topbar.png");
    }

    public void render()
    {
        //first
        game.clearFramebuffer(Color.rgb(0,0,0));

        //second
        renderWorld();
        //third
        renderTopBar();

        renderBottomMenu();

        renderDraggedItem();

        renderEnemy();

        renderShots();

        game.drawText(font, "FPS: " + game.getFramesPerSecond(), 24, 13, Color.RED, 10);
        game.drawText(font, "Enemies: " + world.enemies.size(), 124, 13, Color.RED, 10);
        game.drawText(font, "Shots: " + world.shotsFired.size(), 224, 13, Color.RED, 10);

    }

    public void deprecatedRenderTowers()
    {
        /*Tower tower;
        for(int i = 0; i < world.towers.size();i++)
        {
            tower = world.towers.get(i);
            game.drawBitmap(towerImage, (int)tower.x, (int)tower.y);
        }*/
    }

    public void renderTopBar()
    {
        game.drawBitmap(topBarImage,0,0);
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

                if(itemContext.type == ItemEntity.typeOfItem.Tower)
                {
                    Tower towerItem = (Tower)itemContext;
                    towerItem.x = ((int)world.worldMap.viewX + posX);
                    towerItem.y = ((int)world.worldMap.viewY + posY);
                    towerItem.arrayX = x;
                    towerItem.arrayY = y;
                }
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
        GenericCustomer contextCustomer;
        for(int i = 0; i < world.enemies.size();i++)
        {
        contextCustomer = world.enemies.get(i);
        //if the thing is spawned AND it is on screen
        if(contextCustomer.spawned &&
                contextCustomer.x >= 0 &&
                contextCustomer.y >= 0 &&
                contextCustomer.x <= World.MAX_X  &&
                contextCustomer.y <= World.MAX_Y - 70)
        {
            int imagePosX = (int)(contextCustomer.x);
            int imagePosY = (int)(contextCustomer.y);

            drawGenericEnemy(imagePosX,imagePosY);

            contextCustomer.x = contextCustomer.viewX - contextCustomer.currentSpace.arrayX / 30;
            contextCustomer.y = contextCustomer.viewY - contextCustomer.currentSpace.arrayY / 30;
        }
        else
        {
            if(contextCustomer.currentSpace != null)
            {
                contextCustomer.x = contextCustomer.currentSpace.x;
                contextCustomer.y = contextCustomer.currentSpace.y;
            }
        }
        }
    }
    public void renderShots()
    {
        TowerShot contextShot;
        for(int i = 0; i < world.shotsFired.size();i++)
        {
            contextShot = world.shotsFired.get(i);
            //if the thing is spawned AND it is on screen
            if(contextShot.x >= 0 &&
                    contextShot.y >= 0 &&
                    contextShot.x <= World.MAX_X  &&
                    contextShot.y <= World.MAX_Y - 70)
            {
                int imagePosX = (int)(contextShot.x);
                int imagePosY = (int)(contextShot.y);

                drawTowerShot(imagePosX,imagePosY);

                contextShot.x = contextShot.viewX - contextShot.target.arrayX/30;
                contextShot.y = contextShot.viewY - contextShot.target.arrayY/30;
            }
        }
    }
    public void drawGenericEnemy(int x, int y)
    {
        game.drawBitmap(enemyImage,x,y);
    }
    public void drawTowerShot(int x, int y)
    {
        game.drawBitmap(standardShotImage,x,y);
    }
}
