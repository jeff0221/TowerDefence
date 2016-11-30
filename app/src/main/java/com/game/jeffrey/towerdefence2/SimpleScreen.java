package com.game.jeffrey.towerdefence2;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;

import com.game.jeffrey.towerdefence2.BFTD.Tower;

import java.util.List;


public class SimpleScreen extends Screen
{

    Sound sound;
    Bitmap bitmap;
    Bitmap background;
    Bitmap towerImage;
    int clearColor = Color.RED;
    Music music;
    boolean isPlaying = false;
    float x = 0;

    Tower tower = new Tower();

    public SimpleScreen(GameEngine game)
    {
        super(game);
        //sound = game.loadSound("bounce.wav");
        background = game.loadBitmap("blackfriday.png");
        //music = game.loadMusic("music.ogg");
        towerImage = game.loadBitmap("test_tower.png");
    }

    @Override
    public void update(float deltaTime)
    {
        Log.d("SimpleScreen","FPS: " + game.getFramesPerSecond() + " *************");

        game.clearFramebuffer(Color.BLUE);

        if(game.isTouchDown(0)) x = x + 250 * deltaTime;
        if((game.getFramebufferWidth()/2) < x) x = 0;

        if (game.isTouchDown(0))
        {
            tower.x = game.getTouchX(0) - tower.WIDTH/2;
            tower.y = game.getTouchY(0) - tower.HEIGHT/2;
        }

        game.drawBitmap(towerImage, (int)tower.x, (int)tower.y);


        //game.drawBitmap(bob, (int) x - 120,0);


        /*if(game.isTouchDown(0))
        {
            if(music.isPlaying())
            {
                music.pause();
                isPlaying = false;
            }
            else
            {
                music.play();
                isPlaying = true;
            }
        }*/
/*
        List<com.game.jeffrey.gameengine.KeyEvent> keyEvents = game.getKeyEvents();
        for(int i = 0; i< keyEvents.size() ;i++)
        {
            com.game.jeffrey.gameengine.KeyEvent event = keyEvents.get(i);
            Log.d("KeyEvent TEST","key: " + event.type + ", " + event.keyCode + ", " + event.character);
        }
*/

/*        float x = game.getAccelerometer()[0];
        float y = game.getAccelerometer()[1];
        //float z = game.getAccelerometer()[2];
        x = (x/10) * game.getFramebufferWidth()/2 + game.getFramebufferWidth()/2;
        y = (y/10) * game.getFramebufferHeight()/2 + game.getFramebufferHeight()/2;
        game.drawBitmap(bob,(int)(x-64),(int)(y-64));*/

        /*
        for(int i = 0; i< 20;i++)
        {
            if(game.isTouchDown(i))
            {
            game.drawBitmap(bob,game.getTouchX(i),game.getTouchY(i));
            }
        }
        */
    }
    /*
    if (game.isKeyPressed(KeyEvent.KEYCODE_VOLUME_DOWN))
    {
        clearColor = (int) (Math.random() + Integer.MAX_VALUE);
    }
    game.drawBitmap(bob, 10,10);
    game.drawBitmap(bob,150,150,0,0,64,64)
    */

    @Override
    public void pause()
    {
        music.pause();
    }

    @Override
    public void resume()
    {
        if(!isPlaying) music.play();
    }

    @Override
    public void dispose()
    {
        sound.dispose();
        music.dispose();
    }
}
