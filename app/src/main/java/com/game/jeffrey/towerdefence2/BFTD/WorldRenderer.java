package com.game.jeffrey.towerdefence2.BFTD;


import android.graphics.Bitmap;
import android.util.Log;

import com.game.jeffrey.towerdefence2.GameEngine;

public class WorldRenderer
{
    GameEngine game;
    World world;
    Bitmap towerImage;

    public WorldRenderer(GameEngine game, World world)
    {
        this.game = game;
        this.world = world;
        //load bitmaps
        this.towerImage = game.loadBitmap("test_tower.png");
    }

    public void render()
    {
//        Log.d("mms", "Load");
//        Log.d("XXX" + world.tower.x,"XXX");
//        Log.d("YYY" + world.tower.y,"YYY");

        //game.drawBitmap(towerImage, (int)world.tower.x, (int)world.tower.y);

        for(int i = 0; i < world.towers.size();i++)
        {
            game.drawBitmap(towerImage, (int)world.towers.get(i).x, (int)world.towers.get(i).y);
        }
    }
}
