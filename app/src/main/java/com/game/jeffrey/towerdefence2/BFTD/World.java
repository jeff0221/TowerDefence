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

    float startTime = System.nanoTime();
    float passedTime = 0.0f;

    public int testCounter = 0;

    public int lives = 20;
    public int stageLevel = 0;

    public WorldMap worldMap = new WorldMap();
    public Pather path = new Pather();

    public World() {
        path.calculatePath(worldMap);
    }

    BottomMenu bottomMenu = new BottomMenu();

    List<GenericWorker> towers = new ArrayList<>();
    List<GenericCustomer> enemies = new ArrayList<>();
    List<TowerShot> shotsFired = new ArrayList<>();
    List<Explosion> explosions = new ArrayList<>();

    ItemEntity highLighted;

    public boolean finishedDrawingMaze = true;
    public boolean drawingMaze;
    List<ItemEntity> highLightedEntities = new ArrayList<>();

    boolean secondPassed = false;

    boolean enemySpawned = true;

    // RESOLUTION: 640 x 360 (16:9)

    public void update(float deltaTime, float touchX, float touchY, boolean isTouch, boolean isDoubleTouch,boolean isTapped)
    {
        if(System.nanoTime() - startTime > 999999999)
        {
            secondPassed = true;
            startTime = System.nanoTime();
        }
        else
        {
            if(secondPassed)
            {
                secondPassed = false;
            }
        }

        //TODO: Arrangement

        if(secondPassed && enemies.size() < 16 && !enemySpawned)
        {
            generateEnemy();
        }
        else if(enemies.size() >= 15)
        {
            enemySpawned = true;
        }

        //TODO: First we calculate some game logic

        calculateAimRotation();

        if(!shotsFired.isEmpty())
        {
            calculateShotsFired();
        }

        if(secondPassed)
        {
            calculateCustomerMoves();
            if(!towers.isEmpty())
            {
                towerScanForTarget();
                calculateTowerAction();
            }
        }

        if(!towers.isEmpty() && System.nanoTime() - startTime > 999999999)
        {
            calculateTowerAction();
        }

        //TODO: Second we calculate user inputs
        drawMaze(touchX,touchY,isTouch,bottomMenu.selectedItem);

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
        else
        {
            menu(touchX,touchY,isTapped);
        }

        passedTime = startTime;

    }

    public void calculateAimRotation()
    {
        int towerCount = towers.size();
        Tower contextTower;
        for(int i = 0; i<towerCount;i++)
        {
            contextTower = (Tower) towers.get(i).platform;

            contextTower.aimRotation++;
            contextTower.aimRotation++;

            if(contextTower.aimRotation >= 360)
            {
                contextTower.aimRotation = 0;
            }
        }
    }

    public ItemEntity pickEntity(float touchX, float touchY, boolean isTouch)
    {
        ItemEntity contextEntity = null;
        if(isTouch)
        {
        for(int x = 0; x < worldMap.gridWidth;x++)
        {
            for(int y = 0; y < worldMap.gridHeight;y++)
            {
                contextEntity = worldMap.grid[x][y];
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
        else
        {
            return contextEntity;
        }
        return contextEntity;
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

    public void menu(float touchX, float touchY, boolean isTapped)
    {
        if(touchY > BottomMenu.MIN_Y)
        {
            if(isTapped)
            {
                if(touchX > 0 && touchX < 52)//button 1
                {
                    bottomMenu.selectedItem = (ItemEntity) bottomMenu.buttons.get(0).getButtonItem();

                    Log.d("Button 1 GROUND","Pressed");
                }
                else if(touchX > 53 && touchX < 106)//button 2
                {
                    bottomMenu.selectedItem = (ItemEntity) bottomMenu.buttons.get(1).getButtonItem();

                    Log.d("Button 2 WALL","Pressed");
                }
                else if(touchX > 107 && touchX < 160)//button 3
                {
                    bottomMenu.selectedItem = (ItemEntity) bottomMenu.buttons.get(2).getButtonItem();

                    Log.d("Button 3 TOWER","Pressed ");
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
                    highLighted = pickEntity(touchX,touchY,isTouch);
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
                        GenericWorker worker;
                        if(contextTower.worker.type == GenericWorker.workerType.Sniper)
                        {
                            worker = new SniperWorker(contextTower);
                        }
                        else
                        {
                            worker = new FastWorker(contextTower);
                        }
                        worker.calibratePosition();
                        towers.add(worker);
                        //TODO: will make it so that it adds to global "tower" list better later
                    }

                    replaceEntity(contextEntity.arrayX,contextEntity.arrayY,bottomMenu.selectedItem);

                    //TODO: Just for Testing atm START

                    path.calculatePath(worldMap);

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

                contextCustomer.currentSpace = path.getPath().get((int)contextCustomer.pathProgression);

                contextCustomer.x = contextCustomer.currentSpace.x;
                contextCustomer.y = contextCustomer.currentSpace.y;
                contextCustomer.arrayX = contextCustomer.currentSpace.arrayX;
                contextCustomer.arrayY = contextCustomer.currentSpace.arrayY;

                contextCustomer.pathProgression = contextCustomer.pathProgression + 1 + contextCustomer.getSpeed();

            }
            contextCustomer.viewX = contextCustomer.x;
            contextCustomer.viewY = contextCustomer.y;
            }
    }

    public void drawMaze(float touchX, float touchY, boolean isTouch, ItemEntity itemEntity)
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
                        ItemEntity pickedEntity = pickEntity(touchX,touchY,isTouch);
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
                    ItemEntity contextItem;
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
                            GenericWorker worker = new FastWorker(null);
                            typeItem = new Tower(contextItem.x,contextItem.y,worker);
                            worker.platform = typeItem;
                            worker.calibratePosition();
                            Tower contextType = (Tower) typeItem;
                            contextType.worker = worker;
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

                    replaceEntity(highLightedEntities.get(i).arrayX,highLightedEntities.get(i).arrayY,typeItem);
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
            enemies.add(new HighCostCustomer(300,30,3));
            enemies.add(new FastCustomer(100,50,1));
            enemies.add(new SturdyCustomer(500,10,2));
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
        boolean targetPicked = false;

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
                        targetPicked = true;
                    }
                }
            }
            }
            if(!targetPicked)
            {
                contextWorker.target = null;
            }
        }
        else
        {
            contextWorker.target = null;
        }
    }
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
                    if(contextShot.target.getHP() - contextShot.shotFrom.getDamage() > 0)
                    {
                        explosions.add(new Explosion(contextShot.x,contextShot.y));
                        contextShot.shotFrom.target = null;
                        contextShot.target.setHP(contextShot.target.getHP()- contextShot.shotFrom.getDamage());
                        shotsFired.remove(i);
                        shotCount--;
                    }
                    else
                    {
                        explosions.add(new Explosion(contextShot.x,contextShot.y));

                        if(!enemies.isEmpty() && contextShot.target.getArrayIndex() < enemies.size()){
                        enemies.remove(contextShot.target.getArrayIndex());
                        refreshEnemiesArray();
                        shotsFired.remove(i);
                            shotCount--;
                        }
                    }
                }
            }
            else
            {
                shotsFired.remove(i);
                shotCount--;
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
                }
                else
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

    public void replaceEntity(int arrayX, int arrayY, ItemEntity item)
    {
        if(worldMap.grid[arrayX][arrayY].type == ItemEntity.typeOfItem.Tower)
        {
            GenericWorker cleanUpworker;
            int towerCount = towers.size();
            for(int towerIndex = 0; towerIndex < towerCount;towerIndex++)
            {
                cleanUpworker = towers.get(towerIndex);
                if(arrayX == cleanUpworker.arrayX &&
                        arrayY == cleanUpworker.arrayY)
                {
                    towers.remove(towerIndex);
                    towerCount--;
                }
            }
        }

        Log.d(worldMap.grid[arrayX][arrayY].type + " replaced by",item.type+"");

        worldMap.grid[arrayX][arrayY] = item;
    }
}
