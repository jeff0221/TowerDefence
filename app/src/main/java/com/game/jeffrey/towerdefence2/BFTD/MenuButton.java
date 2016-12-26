package com.game.jeffrey.towerdefence2.BFTD;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MenuButton
{
    List<ItemEntity> items;
    boolean pressed = false;
    boolean optionsShown = false;
    public BottomMenu holder;
    public boolean clicked = false;

    public MenuButton(ItemEntity item)
    {
        items = new ArrayList<>();
        items.add(item);
        try
        {
            Tower typeItem = (Tower)item;
            if(typeItem.type == com.game.jeffrey.towerdefence2.BFTD.ItemEntity.typeOfItem.Tower)
            {
                SniperWorker typeWorker = new SniperWorker(typeItem);
                typeItem.worker = typeWorker;
                items.add(typeItem);
            }
        }
        catch (Exception b)
        {
            Log.d("Type","Probably not a tower");
        }
    }

    public ItemEntity getButtonItem()
    {
        if(items.get(0) instanceof Tower)
        {
            if(clicked)
            {
                clicked = false;
                GenericWorker workerEntity = new SniperWorker(null);
                Tower towerEntity = new Tower(280,595,workerEntity);
                towerEntity.worker.platform = towerEntity;
                return towerEntity;
            }
            else
            {
                clicked = true;
                GenericWorker workerEntity = new FastWorker(null);
                Tower towerEntity = new Tower(280,595,workerEntity);
                towerEntity.worker.platform = towerEntity;
                return towerEntity;
            }
        }
        else
        {
            return items.get(0);
        }
    }
}
