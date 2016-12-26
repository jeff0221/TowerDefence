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
    Bitmap sniperTowerImage;
    Bitmap squareGFX;
    Bitmap wallImage;
    Bitmap floorImage;

    Bitmap bottomMenuImage;
    Bitmap topBarImage;

    Bitmap towerAimImage;

    Bitmap standardShotImage;
    Bitmap explosionImage;

    Bitmap rangeImage;
    Bitmap rangeBigImage;

    Bitmap enemyImage;

    Bitmap sturdyEnemyImage;
    Bitmap fastEnemyImage;

    Bitmap highlightImage;
    Bitmap highCostEnemyImage;
    Bitmap sniperShotImage;

    Typeface font;
    
    public WorldRenderer(GameEngine game, World world)
    {
        this.game = game;
        this.world = world;
        //load bitmaps
        this.towerImage = game.loadBitmap("TowerPlatform.png");
        this.sniperTowerImage = game.loadBitmap("TowerPlatformSniper.png");
        this.squareGFX = game.loadBitmap("square.png");
        this.bottomMenuImage = game.loadBitmap("Placeholdermenu.png");
        this.wallImage = game.loadBitmap("BFTDWall.png");
        this.highlightImage = game.loadBitmap("HighlightPicture.png");
        this.floorImage = game.loadBitmap("floorTile.png");
        this.towerAimImage = game.loadBitmap("TowerGunPositions.png");
        this.enemyImage = game.loadBitmap("xyellowmonster.png");
        this.sturdyEnemyImage = game.loadBitmap("sturdyEnemy.png");
        this.fastEnemyImage = game.loadBitmap("fastEnemy.png");
        this.highCostEnemyImage= game.loadBitmap("highCostEnemy.png");
        this.standardShotImage = game.loadBitmap("gunfire.png");
        this.explosionImage = game.loadBitmap("explosion.png");
        this.font = game.loadFont("font.ttf");
        this.topBarImage = game.loadBitmap("Topbar.png");
        this.rangeImage = game.loadBitmap("ranges.png");
        this.rangeBigImage = game.loadBitmap("rangesBig.png");
        this.sniperShotImage = game.loadBitmap("sniperFire.png");

    }

    public void render()
    {
        //first
        game.clearFramebuffer(Color.rgb(0,0,0));
        //second
        renderWorld();
        //third

        renderDraggedItem();

        renderEnemy();

        renderShots();

        renderTopBar();

        renderBottomMenu();
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
                    renderTower(imagePosX,imagePosY,x,y);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Wall)
                {
                    drawWall(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Employee)
                {
                    drawGround(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.Ground)
                {
                    drawGround(imagePosX,imagePosY);
                }
                else if(itemContext.type == ItemEntity.typeOfItem.StartPoint ||
                        itemContext.type == ItemEntity.typeOfItem.GoalPoint) {
                    drawPoints(imagePosX, imagePosY);
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
        int buttonItems = world.bottomMenu.buttons.size();

        if(world.bottomMenu.selectedItem != null && !world.bottomMenu.itemTouched)
        {
            int itemHolderX = 280;
            int itemHolderY = 595;
            ItemEntity contextItem = world.bottomMenu.selectedItem;

            if(contextItem.type == ItemEntity.typeOfItem.Tower)
            {
                Tower contextTower = (Tower)contextItem;
                if(contextTower.worker.type == GenericWorker.workerType.Sniper)
                {
                    game.drawBitmap(sniperTowerImage,itemHolderX,itemHolderY);
                }
                else
                {
                    game.drawBitmap(towerImage,itemHolderX,itemHolderY);
                }
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
            drawPoints(175,(int)BottomMenu.MIN_Y);
            drawPoints(175,(int)BottomMenu.MIN_Y + 30);
            drawPoints(153,(int)BottomMenu.MIN_Y);
            drawPoints(153,(int)BottomMenu.MIN_Y + 30);
        }
        if(!world.enemySpawned)
        {
            game.drawBitmap(enemyImage,245,(int)BottomMenu.MIN_Y);
            game.drawBitmap(highCostEnemyImage,210,(int)BottomMenu.MIN_Y);
            game.drawBitmap(fastEnemyImage,245,(int)BottomMenu.MIN_Y + 30);
            game.drawBitmap(sturdyEnemyImage,210,(int)BottomMenu.MIN_Y + 30);
        }
    }
    public void renderDraggedItem()
    {
        if(world.bottomMenu.selectedItem != null)
        {
            ItemEntity contextItem = world.bottomMenu.selectedItem;

            if(contextItem.type == ItemEntity.typeOfItem.Tower)
            {
                Tower contextTower = (Tower)contextItem;
                if(contextTower.worker.type == GenericWorker.workerType.Sniper)
                {
                    game.drawBitmap(sniperTowerImage,(int)contextItem.x,(int)contextItem.y);
                }
                else
                {
                    game.drawBitmap(towerImage,(int)contextItem.x,(int)contextItem.y);
                }
            }
            else if(contextItem.type == ItemEntity.typeOfItem.Wall)
            {
                game.drawBitmap(wallImage,(int)contextItem.x,(int)contextItem.y);
            }

            if(contextItem.type == ItemEntity.typeOfItem.Tower && world.highLighted != null)
            {
                Tower contextTower = (Tower)contextItem;
                /*if(contextTower.worker.getRange() == 50)
                {
                }*/
                int range = contextTower.worker.getRange();
                int imagePosX = (int)(world.highLighted.x - range/2 + (Tower.WIDTH/2));
                int imagePosY = (int)(world.highLighted.y - range/2 + (Tower.HEIGHT/2));
                drawRange(imagePosX,imagePosY,range);
            }
        }
    }
    public void renderTower(int x, int y, int arrayX, int arrayY)
    {
        int pictureSpaceX = 0;
        int pictureSpaceY = 0;

        Tower contextTower =  (Tower)world.worldMap.grid[arrayX][arrayY];


        if(contextTower.worker.type == GenericWorker.workerType.Sniper)
        {
            game.drawBitmap(sniperTowerImage,x,y,0,0,30,30);
        }
        else
        {
            game.drawBitmap(towerImage,x,y,0,0,30,30);
        }

        if (contextTower.aimRotation >= 270 && contextTower.aimRotation < 360)
        {
            pictureSpaceY = 90;
            //System.out.println("UP " + pictureSpaceY);
        }
        else if(contextTower.aimRotation >= 180 && contextTower.aimRotation < 270)
        {
            pictureSpaceY = 60;
            //System.out.println("RIGHT " + pictureSpaceY);
        }
        else if(contextTower.aimRotation >= 90 && contextTower.aimRotation < 180)
        {
            pictureSpaceY = 0;
            //System.out.println("DOWN " + pictureSpaceY);
        }
        else if(contextTower.aimRotation >= 0 && contextTower.aimRotation < 90)
        {
            pictureSpaceY = 30;
            //System.out.println("LEFT " + pictureSpaceY);
        }

        game.drawBitmap(towerAimImage,x,y,pictureSpaceX,pictureSpaceY,30,30);

    }
    public void drawWall(int x, int y)
    {
        game.drawBitmap(wallImage,x,y,0,0,30,30);
    }
    public void drawGround(int x, int y)
    {
        game.drawBitmap(floorImage,x,y,0,0,30,30);
    }
    public void drawPoints(int x, int y) {
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
                contextCustomer.x >= -30 &&
                contextCustomer.y >= -30 &&
                contextCustomer.x <= World.MAX_X  &&
                contextCustomer.y <= World.MAX_Y - 70)
        {

            int contextPathProg = 0;
            if(world.path.getPath().size() > (int)contextCustomer.pathProgression + 1)
            {
                contextPathProg = (int)contextCustomer.pathProgression;
            }
            ItemEntity nextStep;

            if(contextPathProg != 0 && !(world.path.getPath().isEmpty()) &&
                    world.path.getPath().get(contextPathProg) != null)
            {
                nextStep = world.path.getPath().get(contextPathProg);

                if(nextStep.x > contextCustomer.x)
                {
                    world.enemies.get(i).viewX = world.enemies.get(i).viewX + (2*world.enemies.get(i).getSpeed());
                }
                else if(nextStep.x < contextCustomer.x)
                {
                    world.enemies.get(i).viewX = world.enemies.get(i).viewX - (2*world.enemies.get(i).getSpeed());
                }

                if(nextStep.y > contextCustomer.y)
                {
                    world.enemies.get(i).viewY = world.enemies.get(i).viewY + (2*world.enemies.get(i).getSpeed());
                }
                else if(nextStep.y < contextCustomer.y)
                {
                    world.enemies.get(i).viewY = world.enemies.get(i).viewY - (2*world.enemies.get(i).getSpeed());
                }
            }

            int imagePosX = (int)(contextCustomer.viewX);
            int imagePosY = (int)(contextCustomer.viewY);

            if(contextCustomer.type == GenericCustomer.customerTypes.Sturdy)
            {
                drawSturdyEnemy(imagePosX,imagePosY);
            }
            else if(contextCustomer.type == GenericCustomer.customerTypes.HighCost)
            {
                drawHighCostEnemy(imagePosX,imagePosY);
            }
            else if(contextCustomer.type == GenericCustomer.customerTypes.Fast)
            {
                drawFastEnemy(imagePosX,imagePosY);
            }
            else
            {
                drawGenericEnemy(imagePosX,imagePosY);
            }

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
        int shotsFired = world.shotsFired.size();
        for(int i = 0; i < shotsFired ;i++)
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

                drawTowerShot(imagePosX,imagePosY,contextShot.shotFrom.type);

                contextShot.x = contextShot.viewX - contextShot.target.arrayX/30;
                contextShot.y = contextShot.viewY - contextShot.target.arrayY/30;
            }
        }
        int explosionCount = world.explosions.size();
        Explosion contextExplosion;
        for(int y = 0; y < explosionCount;y++)
        {
            contextExplosion = world.explosions.get(y);
            if(contextExplosion.explosionAnimation>0)
            {
                drawExplosion((int)contextExplosion.x,(int)contextExplosion.y);
                contextExplosion.explosionAnimation--;
            }
            else
            {
                world.explosions.remove(contextExplosion);
                explosionCount--;
            }
        }
    }
    public void drawGenericEnemy(int x, int y)
    {
        game.drawBitmap(enemyImage,x,y);
    }
    public void drawTowerShot(int x, int y,GenericWorker.workerType type)
    {
        if(type == GenericWorker.workerType.Sniper)
        {
            game.drawBitmap(sniperShotImage,x-3,y-2);
        }
        else
        {
            game.drawBitmap(standardShotImage,x,y);

        }
    }
    public void drawExplosion(int x, int y)
    {
        game.drawBitmap(explosionImage,x,y);
    }
    public void drawRange(int x, int y , int range)
    {
        if(range > 150)
        {
            game.drawBitmap(rangeBigImage,x,y);
        }
        else
        {
            game.drawBitmap(rangeImage,x, y,0,0,range + 5,range + 5);
        }
    }
    public void drawSquare(int x, int y)
    {
        game.drawBitmap(squareGFX,x,y);
    }
    public void drawHighCostEnemy(int x, int y)
    {
        game.drawBitmap(highCostEnemyImage,x,y);
    }
    public void drawFastEnemy(int x, int y)
    {
        game.drawBitmap(fastEnemyImage,x,y);
    }
    public void drawSturdyEnemy(int x, int y)
    {
        game.drawBitmap(sturdyEnemyImage,x,y);
    }
}
