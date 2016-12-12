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

    public WorldMap worldMap = new WorldMap();

    BottomMenu bottomMenu = new BottomMenu();

    List<Tower> towers = new ArrayList<>();

    public boolean draggingItem = false;

    // RESOLUTION: 640 x 360 (16:9)


    public void generateRandomTowers()
    {
        for(int i = 0; i < 5 ;i++)
        {
            //towers.add(new Tower(i+5,(i*10)+50));
        }
    }

    public void update(float deltaTime, float touchX, float touchY, boolean isTouch, boolean isDoubleTouch,boolean isTapped)
    {
        if(towers.isEmpty())
        {
            //generateRandomTowers();
        }

        if(touchY < BottomMenu.MIN_Y)
        {

            if(isTouch) //if the player touches the game elements and not the menu
            {
                //finds tower on screen
                Tower contextTower = pickTower(touchX,touchY);
                if(contextTower != null)
                {
                    contextTower.x = touchX - Tower.WIDTH/2;
                    contextTower.y = touchY - Tower.HEIGHT/2;
                }
                //calculates world view changes
            }
            moveWorldView(touchX,touchY,isTouch);
            dragAndDropItem(touchX,touchY,isTouch,isTapped);
        }
        else if(touchY > BottomMenu.MIN_Y)
        {
            menu(touchX,touchY,isTapped);
        }
    }

    public ItemEntity pickEntity(float touchX, float touchY)
    {
        for(int x = 0; x < worldMap.gridWidth-1;x++)
        {
            for(int y = 0; y < worldMap.gridHeight-1;y++)
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
        if(isTouch && pickTower(touchX,touchY) == null)
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
                bottomMenu.selectedItem = new Tower(280,595);
                Log.d("Button 1","Pressed");
            }
            else if(touchX > 53 && touchX < 106)//button 2
            {
                bottomMenu.selectedItem = new Wall(280,595);
                Log.d("Button 2","Pressed");
            }
            else if(touchX > 107 && touchX < 160)//button 3
            {
                Log.d("Button 3","Pressed");
            }
            }
        }
    }

    public void dragAndDropItem(float touchX, float touchY, boolean isTouch, boolean tapped)
    {
        if(bottomMenu.selectedItem != null && isTouch)
        {
            if(!draggingItem && touchX > 275 && touchX < 360)
            {
                bottomMenu.selectedItem.x = touchX;
                bottomMenu.selectedItem.y = touchY;
                draggingItem = true;
            }
            bottomMenu.selectedItem.x = touchX;
            bottomMenu.selectedItem.y = touchY;
        }
        else
        {
            draggingItem = false;

            ItemEntity item = pickEntity(touchX,touchY);
            if(item != null && tapped)
            {
                worldMap.grid[item.arrayX][item.arrayX] = bottomMenu.selectedItem;
            }

        }
    }
}
