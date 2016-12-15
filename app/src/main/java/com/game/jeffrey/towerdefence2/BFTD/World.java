package com.game.jeffrey.towerdefence2.BFTD;


import android.util.Log;

import java.util.ArrayList;

import java.util.List;

public class World
{
    public static float MIN_X = 0;
    public static float MIN_Y = 10;
    public static float MAX_X = 359;
    public static float MAX_Y = 639;

    public int lives = 20;
    public int stageLevel = 0;

    public int aimRotation = 0;
    public int testCounter = 0;
    public int gameCounter = 0;

    public WorldMap worldMap = new WorldMap();
    public Pather path = new Pather();

    public World() {
        path.calculatePath(worldMap);
    }

    BottomMenu bottomMenu = new BottomMenu();

    List<Tower> towers = new ArrayList<>();
    List<GenericCustomer> enemies = new ArrayList<>();

    GenericCustomer testCustomer = new HighCostCustomer(100,10,1);

    ItemEntity highLighted;

    public boolean finishedDrawingMaze = true;
    public boolean drawingMaze;
    List<ItemEntity> highLightedEntities = new ArrayList<>();

    int slowEnemyRelease = 0;

    // RESOLUTION: 640 x 360 (16:9)

    public void update(float deltaTime, float touchX, float touchY, boolean isTouch, boolean isDoubleTouch,boolean isTapped)
    {
        //TODO: Arrangement
        if(testCounter %10 == 0 && enemies.size() < 30)
        {
            generateEnemy();
        }
        //TODO: First we calculate some game logic

        aimRotation++;

        if(aimRotation >= 360)
        {
            aimRotation = 0;
        }

        if(testCounter %10 == 0)
        {
            calculateCustomerMoves();
        }

        testCounter++;

        //TODO: Second we calculate user inputs
        drawMaze(touchX,touchY,isTouch,isTapped,bottomMenu.selectedItem);

        if(!drawingMaze)
        {
            dragAndDropMenuItem(touchX,touchY,isTouch,isTapped);
        }
        if(touchY < BottomMenu.MIN_Y)
        {
            if(!bottomMenu.itemTouched && finishedDrawingMaze && !drawingMaze)
            {
                moveWorldView(touchX,touchY,isTouch);
                moveCustomerView(touchX,touchY,isTouch);
            }
        }
        if(touchY > BottomMenu.MIN_Y)
        {
            menu(touchX,touchY,isTapped);
        }

        gameCounter++;
        if(gameCounter >= enemies.size())
        {
            gameCounter = 0;
        }
    }

    public ItemEntity pickEntity(float touchX, float touchY, boolean isTouch, boolean tapped)
    {
        if(isTouch || tapped)
        {
        for(int x = 0; x < worldMap.gridWidth;x++)
        {
            for(int y = 0; y < worldMap.gridHeight;y++)
            {
                ItemEntity contextEntity = worldMap.grid[x][y];
                if(contextEntity.x == touchX || touchX > contextEntity.x && touchX < contextEntity.x + 30)
                {
                    if(contextEntity.y == touchY || touchY > contextEntity.y && touchY < contextEntity.y + 30)
                    {
                        return contextEntity;
                    }
                }
            }
        }
        }
        return null;
    }

    public Tower pickTower(float touchX, float touchY)
    {
        for(int i = 0; i < towers.size();i++)
        {
            Tower contextTower = towers.get(i);
            if(towerCollider(touchX,touchY,contextTower))
            {
                return contextTower;
            }
        }
    return null;
    }

    public boolean towerCollider(float touchX, float touchY, Tower tower)
    {
        boolean result = false;
        if(tower.x == touchX || touchX > tower.x && touchX < tower.x + Tower.WIDTH)
        {
            if(tower.y == touchY || touchY > tower.y && touchY < tower.y + Tower.HEIGHT)
            {
            result = true;

            }
        }
        return result;
    }

    public void moveWorldView(float touchX, float touchY, boolean isTouch)
    {
        //https://yal.cc/gamemaker-click-n-drag-to-pan-view/ guide used
        if(isTouch)
        {
            if(!worldMap.touched)
            {
                worldMap.touched = true;
                worldMap.startX = touchX;
                worldMap.startY = touchY;
            }

            if(worldMap.touched)
            {
                worldMap.viewX = worldMap.viewX - (worldMap.startX - touchX);
                worldMap.viewY = worldMap.viewY - (worldMap.startY - touchY);

                worldMap.viewX = Math.max((int)(-360 * 2), Math.min((int)worldMap.viewX, World.MAX_X / 2));
                worldMap.viewY = Math.max((int)(-640 / 2), Math.min((int)worldMap.viewY, World.MAX_Y / 2));

                worldMap.startX = touchX;
                worldMap.startY = touchY;
            }
        }
        else
        {
            worldMap.touched = false;
        }
    }

    public void menu(float touchX, float touchY, boolean tapped)
    {
        if(touchX > BottomMenu.MIN_X && touchY > BottomMenu.MIN_Y)
        {
            worldMap.touched = false;
            if(tapped)
            {
            if(touchX > 0 && touchX < 52)//button 1
            {
                bottomMenu.selectedItem = new Square(280,595);
                Log.d("Button 1 GROUND","Pressed");
            }
            else if(touchX > 53 && touchX < 106)//button 2
            {
                bottomMenu.selectedItem = new Wall(280,595);
                Log.d("Button 2 WALL","Pressed");
            }
            else if(touchX > 107 && touchX < 160)//button 3
            {
                bottomMenu.selectedItem = new Tower(280,595);
                Log.d("Button 3 TOWER","Pressed");
            }
            else if(touchX > 161 && touchX < 214)//button 4
            {
                if(drawingMaze)
                {
                    drawingMaze = false;
                    Log.d("Drawing Maze","False");
                }
                else
                {
                    if(bottomMenu.selectedItem != null)
                    {
                        drawingMaze = true;
                        Log.d("Drawing Maze","True");
                    }
                    else
                    {
                        drawingMaze = false;
                        Log.d("Drawing Maze","False");
                    }
                }
            }
            }

        }
    }

    public void dragAndDropMenuItem(float touchX, float touchY, boolean isTouch, boolean tapped)
    {
        //Calculates drag and drop
        if(bottomMenu.selectedItem != null)
        {
            if(isTouch)
            {
                if(!bottomMenu.itemTouched && touchX > 280 && touchY > 595)
                {
                    bottomMenu.selectedItem.x = touchX;
                    bottomMenu.selectedItem.y = touchY;
                    bottomMenu.itemTouched = true;
                }
                else if(bottomMenu.itemTouched)
                {
                    bottomMenu.selectedItem.x = touchX-ItemEntity.WIDTH/2;
                    bottomMenu.selectedItem.y = touchY-ItemEntity.HEIGHT/2;
                    highLighted = pickEntity(touchX,touchY,isTouch,tapped);
                }
            }
            else
            {
                if(bottomMenu.itemTouched &&
                        (touchY < 595 && touchX > 0 && touchY > 0) &&
                        (highLighted.arrayY < 100 && highLighted.arrayX < 100))
                {
                    ItemEntity contextEntity = highLighted;

                    bottomMenu.selectedItem.arrayX = contextEntity.arrayX;
                    bottomMenu.selectedItem.arrayY = contextEntity.arrayY;

                    worldMap.grid[contextEntity.arrayX][contextEntity.arrayY] = bottomMenu.selectedItem;

                    //TODO: Just for Testing atm START

                    path.calculatePath(worldMap);

                    //Virker ikke helt endnu.
                    //Den starter den nye path fra det sted i path.array den var nået til i sidste path progression.
                    //TODO: Just for Testing atm END

                    Log.d(contextEntity.type + " replaced by",bottomMenu.selectedItem.type+"");

                    bottomMenu.selectedItem = null;
                    bottomMenu.itemTouched = false;
                }
                else
                {
                    bottomMenu.itemTouched = false;
                    bottomMenu.selectedItem.x = 280;
                    bottomMenu.selectedItem.y = 595;
                }
            }
        }
        if(!bottomMenu.itemTouched)
        {
            highLighted = null;
        }
    }

    public void aimTowers()
    {

        for(int x = 0; x < worldMap.gridWidth; x++)
        {
         for(int y = 0; y < worldMap.gridHeight; y++)
         {
             if(worldMap.grid[x][y].type == ItemEntity.typeOfItem.Tower)
             {

             }

         }
        }
    }

    public void calculateCustomerMoves()
    {
        GenericCustomer contextCustomer;
        if(enemies.isEmpty())
        {
            enemies.add(testCustomer);
        }
            for(int i = 0; i < enemies.size();i++)
            {
            contextCustomer = enemies.get(i);
            if(!path.getPath().isEmpty()){
                if(!contextCustomer.spawned)
                {
                    contextCustomer.x = worldMap.pathStartX;
                    contextCustomer.y = worldMap.pathStartY;
                    contextCustomer.pathProgression = path.getPath().size();
                    contextCustomer.spawned = true;
                }

                if(contextCustomer.pathProgression >= path.getPath().size())
                {
                    contextCustomer.pathProgression = 0;
                }

                contextCustomer.x = path.getPath().get(contextCustomer.pathProgression).x;
                contextCustomer.y = path.getPath().get(contextCustomer.pathProgression).y;
                contextCustomer.currentSpace = path.getPath().get(contextCustomer.pathProgression);

                contextCustomer.pathProgression++;

            }
            contextCustomer.viewX = contextCustomer.x;
            contextCustomer.viewY = contextCustomer.y;
            }
    }

    public void moveCustomerView(float touchX, float touchY, boolean isTouch)
    {//Corrects visual for mob spawns
        GenericCustomer contextCustomer;
        if(enemies.isEmpty())
        {
            enemies.add(testCustomer);
        }

        for(int i = 0; i < enemies.size();i++)
        {
            contextCustomer = enemies.get(i);
            if (contextCustomer.spawned)
            {
                contextCustomer.viewX = contextCustomer.x;
                contextCustomer.viewY = contextCustomer.y;

                if (isTouch)
                {
                    if (!contextCustomer.touched)
                    {
                        contextCustomer.touched = true;
                        contextCustomer.startX = touchX;
                        contextCustomer.startY = touchY;
                    }
                    if (contextCustomer.touched)
                    {
                        contextCustomer.viewX = contextCustomer.viewX - (contextCustomer.startX - touchX);
                        contextCustomer.viewY = contextCustomer.viewY - (contextCustomer.startY - touchY);

                        contextCustomer.viewX = Math.max((int) (-360), Math.min((int) contextCustomer.viewX, World.MAX_X));
                        contextCustomer.viewY = Math.max((int) (-640), Math.min((int) contextCustomer.viewY, World.MAX_Y));

                        contextCustomer.startX = touchX;
                        contextCustomer.startY = touchY;
                    }
                } else
                {
                    contextCustomer.touched = false;
                }
            }
        }
    }

    public void drawMaze(float touchX, float touchY, boolean isTouch, boolean isTapped, ItemEntity itemEntity)
    {
        if(bottomMenu.selectedItem != null)
        {
            if(isTouch)
            {
                if(drawingMaze && finishedDrawingMaze)
                {
                    finishedDrawingMaze = false;
                    //Log.d("FinishedDrawing inside","False");
                    highLightedEntities = new ArrayList<>();
                }
                else
                {
                    if(touchY < 595 && touchX >= 0 && touchY >= 0)
                    {
                        ItemEntity pickedEntity = pickEntity(touchX,touchY,isTouch,isTapped);
                        if(pickedEntity != null&& pickedEntity.arrayX <= 20 && pickedEntity.arrayY <= 30)
                        {
                            if(!highLightedEntities.contains(pickedEntity))
                            {
                                highLightedEntities.add(pickedEntity);
                            }
                        }
                    }
                }
            }
            else
            {
                if(drawingMaze && !finishedDrawingMaze)
                {
                    ItemEntity contextItem = itemEntity;
                    ItemEntity typeItem;
                    for(int i = 0; i < highLightedEntities.size();i++)
                    {
                        if(contextItem.type == ItemEntity.typeOfItem.Wall)
                        {
                            typeItem = new Wall(contextItem.arrayX,contextItem.arrayY);
                        }
                        else if(contextItem.type == ItemEntity.typeOfItem.Tower)
                        {
                            typeItem = new Tower(contextItem.arrayX,contextItem.arrayY);
                        }
                        else
                        {
                            //TODO: Lave det så alle typerne some spillerne kan lave er her
                            typeItem = new Square(contextItem.arrayX,contextItem.arrayY);
                        }
                        typeItem.x = highLightedEntities.get(i).x;
                        typeItem.y = highLightedEntities.get(i).y;

                        worldMap.grid[highLightedEntities.get(i).arrayX][highLightedEntities.get(i).arrayY] = typeItem;
                    }
                    path.calculatePath(worldMap);

                    drawingMaze = false;
                    Log.d("Drawing inside","False");
                    bottomMenu.selectedItem = null;
                }
                //Log.d("FinishedDrawing inside","True");
                finishedDrawingMaze = true;
            }
        }
        if(!drawingMaze)
        {
            highLightedEntities = new ArrayList<>();
        }
    }

    public void generateEnemy()
    {

            //enemies.add(new HighCostCustomer(100,10,3));
            //enemies.add(new FastCustomer(100,30,1));
            enemies.add(new SturdyCustomer(200,10,2));

    }
}
