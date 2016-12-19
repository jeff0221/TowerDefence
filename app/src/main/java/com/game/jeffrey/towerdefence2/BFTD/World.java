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

    List<GenericWorker> towers = new ArrayList<>();
    List<GenericCustomer> enemies = new ArrayList<>();
    List<TowerShot> shotsFired = new ArrayList<>();

    ItemEntity highLighted;

    public boolean finishedDrawingMaze = true;
    public boolean drawingMaze;
    List<ItemEntity> highLightedEntities = new ArrayList<>();

    int slowEnemyRelease = 0;

    private boolean enemySpawned = false;

    // RESOLUTION: 640 x 360 (16:9)

    public void update(float deltaTime, float touchX, float touchY, boolean isTouch, boolean isDoubleTouch,boolean isTapped)
    {
        //TODO: Arrangement
        if(testCounter %10 == 0 && enemies.size() < 16 && !enemySpawned)
        {
            generateEnemy();
        }
        else if(enemies.size() >= 15)
        {
            enemySpawned = true;
        }
        //TODO: First we calculate some game logic

        aimRotation++;

        if(aimRotation >= 360)
        {
            aimRotation = 0;
        }

        if(!shotsFired.isEmpty())
        {
            calculateShotsFired();
        }

        if(testCounter %10 == 0)
        {
            calculateCustomerMoves();
            if(!towers.isEmpty())
            {
                towerScanForTarget();
                calculateTowerAction();
            }
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
                //Correct graphics for the world view
                moveWorldView(touchX,touchY,isTouch);
                moveCustomerView(touchX,touchY,isTouch);
                moveShotView(touchX,touchY,isTouch);
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

    public Tower deprecatePickTower(float touchX, float touchY)
    {
        /*for(int i = 0; i < towers.size();i++)
        {
            Tower contextTower = towers.get(i);
            if(towerCollider(touchX,touchY,contextTower))
            {
                return contextTower;
            }
        }*/
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
                Tower towerEntity = new Tower(280,595, new FastWorker(10,10,20,1,200,null));
                towerEntity.worker.platform = towerEntity;
                bottomMenu.selectedItem = towerEntity;
                Log.d("Button 3 TOWER","Pressed");
            }
            else if(touchX > 214 && touchX < 267)
            {
                enemySpawned = false;
                Log.d("Secret Button","Pressed");
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
/*        for(int i = 0; i<towers.size();i++)
        {
            System.out.println("Tower " + i + "***********************************");
            System.out.println("Tower x: " + towers.get(i).x + " ArrayX: " + towers.get(i).arrayX);
            System.out.println("Tower Y: " + towers.get(i).y + " ArrayY: " + towers.get(i).arrayY);
            System.out.println("Tower " + i + "***********************************");
        }*/
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

                    if(bottomMenu.selectedItem.type == ItemEntity.typeOfItem.Tower)
                    {
                        Tower contextTower = (Tower)bottomMenu.selectedItem;
                        GenericWorker worker = new FastWorker(10,10,20,1,200,contextTower);
                        worker.calibratePosition();
                        towers.add(worker);
                        //TODO: will make it so that it adds to global "tower" list better later
                    }
                    if(worldMap.grid[contextEntity.arrayY][contextEntity.arrayY].type == ItemEntity.typeOfItem.Tower)
                    {
                        Tower contextItem = (Tower)worldMap.grid[contextEntity.arrayY][contextEntity.arrayY];
                        try{
                            towers.remove(contextItem.worker);
                        }catch(Exception e)
                        {
                            Log.d("Removed of tower","in drag and drop menu failed");
                        }
                    }
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
            int enemyCount = enemies.size();
            for(int i = 0; i < enemyCount;i++)
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

                contextCustomer.currentSpace = path.getPath().get(contextCustomer.pathProgression);

                contextCustomer.x = contextCustomer.currentSpace.x;
                contextCustomer.y = contextCustomer.currentSpace.y;
                contextCustomer.arrayX = contextCustomer.currentSpace.arrayX;
                contextCustomer.arrayY = contextCustomer.currentSpace.arrayY;

                contextCustomer.pathProgression++;

            }
            contextCustomer.viewX = contextCustomer.x;
            contextCustomer.viewY = contextCustomer.y;
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
                                Log.d("Picked X: " + pickedEntity.x,"Picked Y: "+pickedEntity.y);
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
                    int highlightCount = highLightedEntities.size();
                    for(int i = 0; i < highlightCount;i++)
                    {
                        contextItem = highLightedEntities.get(i);
                        if(itemEntity.type == ItemEntity.typeOfItem.Wall)
                        {
                            typeItem = new Wall(contextItem.x,contextItem.y);
                        }
                        else if(itemEntity.type == ItemEntity.typeOfItem.Tower)
                        {
                            GenericWorker worker = new FastWorker(10,10,20,1,200,null);
                            typeItem = new Tower(contextItem.x,contextItem.y,worker);
                            worker.platform = typeItem;
                            worker.calibratePosition();
                            towers.add(worker);
                            //TODO: will make it so that it adds to global "tower" list better later
                        }
                        else
                        {
                            //TODO: Lave det så alle typerne some spillerne kan lave er her
                            typeItem = new Square(contextItem.x,contextItem.y);
                        }
                        typeItem.x = highLightedEntities.get(i).x;
                        typeItem.y = highLightedEntities.get(i).y;
                        typeItem.arrayX = highLightedEntities.get(i).arrayX;
                        typeItem.arrayY = highLightedEntities.get(i).arrayY;

                        GenericWorker cleanUpworker;
                        int towerCount = towers.size();
                        for(int towerIndex = 0; towerIndex < towerCount;towerIndex++)
                        {
                            cleanUpworker = towers.get(towerIndex);
                            if(highLightedEntities.get(i).arrayX == cleanUpworker.arrayX &&
                                    highLightedEntities.get(i).arrayY == cleanUpworker.arrayY)
                            {
                                towers.remove(towerIndex);
                                towerCount--;
                            }
                        }

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
        GenericCustomer contextCustomer;
            //enemies.add(new HighCostCustomer(100,10,3));
            //enemies.add(new FastCustomer(100,30,1));
            contextCustomer = new SturdyCustomer(100,10,2);
            enemies.add(contextCustomer);
            refreshEnemiesArray();
    }

    public void refreshEnemiesArray()
    {
        if(!enemies.isEmpty())
        {
            int enemyCount = enemies.size();
            for(int i = 0; i<enemyCount;i++)
            {
                enemies.get(i).setArrayIndex(i);
                //System.out.println("Enemy: " + enemies.get(i).getArrayIndex() + " contextIndex: " + i);
            }
        }
    }

    public void refreshTowersArray()
    {//Har aldrig været så træt i mit liv
        for(int x = 0; x < worldMap.gridWidth;x++)
        {
        for(int y = 0; y < worldMap.gridHeight;y++)
        {
        for(int i = 0; i<towers.size();i++)
        {
            GenericWorker contextWorker = towers.get(i);
            if(worldMap.grid[x][y].x == contextWorker.x &&
                    worldMap.grid[x][y].y == contextWorker.y)
            {
                contextWorker.arrayX = worldMap.grid[x][y].arrayX;
                contextWorker.arrayY = worldMap.grid[x][y].arrayY;
            }
        }
        }
        }
    }

    public void towerScanForTarget()
    {
        GenericWorker contextWorker;
        int towerCount = towers.size();
        for(int i = 0; i < towerCount;i++)
    {
        contextWorker = towers.get(i);

        int centerX = (int)contextWorker.arrayX;
        int centerY = (int)contextWorker.arrayY;
        int radius = contextWorker.getRange()/worldMap.gridSize;

        if(towers.get(i).target == null)
        {
            int enemyCount = enemies.size();
        for(int x = centerX - radius ; x < centerX + radius ; x++)
        {
            for(int y = centerY - radius ; y < centerY + radius ; y++)
            {
                for(int enemyIndex = 0; enemyIndex < enemyCount;enemyIndex++)
                {
                    if(enemies.get(enemyIndex).arrayX == x && enemies.get(enemyIndex).arrayY == y)
                    {
                        contextWorker.target = enemies.get(enemyIndex);
                    }
                }
            }
        }
        }
        else
        {
            towers.get(i).target = null;
        }
    }
        //System.out.println("Enemies = " + enemies.size() + " Towers = " + towers.size());
    }

    public void calculateTowerAction()//fine
    {
        GenericWorker contextTower;
        int towerCount = towers.size();
        for(int i = 0; i<towerCount;i++)
        {
            contextTower = towers.get(i);
            if(towers.get(i).target != null)
            {
                shotsFired.add(new TowerShot(contextTower,contextTower.target));
            }
            Tower contextType;
            if(contextTower.arrayX < 100 && contextTower.arrayY < 100)
            {
                if(worldMap.grid[contextTower.arrayX][contextTower.arrayY].type == ItemEntity.typeOfItem.Tower)
                {
                    contextType = (Tower)worldMap.grid[contextTower.arrayX][contextTower.arrayY];
                    contextTower.x = contextType.x;
                    contextTower.y = contextType.y;
                }
            }
            else
            {
                refreshTowersArray();
            }
        }
    }

    public void calculateShotsFired()
    {
        TowerShot contextShot;
        int shotCount = shotsFired.size();
        for(int i = 0;i<shotCount;i++)
        {
            contextShot = shotsFired.get(i);
            contextShot.lifeTime++;
            int speedFactor = contextShot.shotFrom.getAttackSpeed()/5;

            if(contextShot.target != null && contextShot.lifeTime < contextShot.shotFrom.getRange()/4 &&
                    contextShot.x > 0 && contextShot.y > 0)
            {
                if(contextShot.x < contextShot.target.x + GenericCustomer.WIDTH/2)
                {
                    contextShot.x = contextShot.x + speedFactor;
                }
                else if(contextShot.x > contextShot.target.x)
                {
                    contextShot.x = contextShot.x - speedFactor;
                }

                if(contextShot.y < contextShot.target.y + GenericCustomer.HEIGHT/2)
                {
                    contextShot.y = contextShot.y + speedFactor;
                }
                else if(contextShot.y > contextShot.target.y)
                {
                    contextShot.y = contextShot.y - speedFactor;
                }

                if(contextShot.x >= contextShot.target.x &&
                        contextShot.y >= contextShot.target.y &&
                        contextShot.x <= contextShot.target.x + GenericCustomer.WIDTH &&
                        contextShot.y <= contextShot.target.y + GenericCustomer.HEIGHT)
                {
                    if(contextShot.target.getHP()- contextShot.shotFrom.getDamage() > 0)
                    {
                        contextShot.target.setHP(contextShot.target.getHP()- contextShot.shotFrom.getDamage());
                        shotsFired.remove(i);
                        shotCount--;
                        Log.d("Target","Hit!!!");
                    }
                    else
                    {
                        if(!enemies.isEmpty() && contextShot.target.getArrayIndex() < enemies.size()){
                        Log.d("Target","Killed!!!");
                        enemies.remove(contextShot.target.getArrayIndex());
                        refreshEnemiesArray();
                        shotsFired.remove(i);
                            shotCount--;
                        Log.d("Enemies",""+ enemies.size());
                        }
                    }
                }
            }
            else
            {
                shotsFired.remove(i);
                shotCount--;
                Log.d("Shot removed",""+ shotsFired.size());
                if(enemies.isEmpty())
                {
                    shotsFired.clear();
                    break;
                }
            }
        }
    }

    public void moveShotView(float touchX, float touchY, boolean isTouch)
    {//Corrects visual for mob spawns
        TowerShot contextShots;
        int shotCount = shotsFired.size();
        for(int i = 0; i < shotCount;i++)
        {
            contextShots = shotsFired.get(i);
            if (contextShots != null)
            {
                contextShots.viewX = contextShots.x;
                contextShots.viewY = contextShots.y;

                if (isTouch)
                {
                    if (!contextShots.touched)
                    {
                        contextShots.touched = true;
                        contextShots.startX = touchX;
                        contextShots.startY = touchY;
                    }
                    if (contextShots.touched)
                    {
                        contextShots.viewX = contextShots.viewX - (contextShots.startX - touchX);
                        contextShots.viewY = contextShots.viewY - (contextShots.startY - touchY);

                        contextShots.viewX = Math.max((int) (-360), Math.min((int) contextShots.viewX, World.MAX_X));
                        contextShots.viewY = Math.max((int) (-640), Math.min((int) contextShots.viewY, World.MAX_Y));

                        contextShots.startX = touchX;
                        contextShots.startY = touchY;
                    }
                } else
                {
                    contextShots.touched = false;
                }
            }
        }
    }

    public void moveCustomerView(float touchX, float touchY, boolean isTouch)
    {//Corrects visual for mob spawns
        GenericCustomer contextCustomer;
        int enemyCount = enemies.size();
        for(int i = 0; i < enemyCount;i++)
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


}
