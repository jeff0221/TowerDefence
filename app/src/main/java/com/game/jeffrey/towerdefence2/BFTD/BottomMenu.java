package com.game.jeffrey.towerdefence2.BFTD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zonde on 12-12-2016.
 */
public class BottomMenu
{
    public static float MIN_X = 0;
    public static float MAX_X = 359;

    public static float MAX_Y = 639;
    public static float MIN_Y = MAX_Y - 50;

    public boolean itemTouched = false;

    public boolean touched = false;

    public ItemEntity selectedItem;

    List<MenuButton> buttons;

    public BottomMenu()
    {
        buttons = new ArrayList<>();
        //Laver knapperne
        MenuButton button1 = new MenuButton(new Square(280,595));
        buttons.add(button1);

        MenuButton button2 = new MenuButton(new Wall(280,595));
        buttons.add(button2);

        GenericWorker workerEntity = new FastWorker(null);
        Tower towerEntity = new Tower(280,595,workerEntity);
        towerEntity.worker.platform = towerEntity;

        MenuButton button3 = new MenuButton(towerEntity);
        buttons.add(button3);

        /*MenuButton<ItemEntity> button4 = new MenuButton(this,new Square(280,595));
        buttons.add(button4);*/
    }
}
