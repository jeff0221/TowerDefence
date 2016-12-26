package com.game.jeffrey.towerdefence2;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class GameEngine extends Activity implements Runnable, View.OnKeyListener, SensorEventListener
{
    private Thread mainLoopThread;
    protected State state = State.Paused;
    private List<State> stateChanges = new LinkedList<>();

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    public Music music;

    private Screen screen;
    private Canvas canvas;
    private Bitmap offscreenSurface;
    private Rect src = new Rect();
    private Rect dst = new Rect();

    private boolean pressedKeys [] = new boolean[256];

    private KeyEventPool keyEventPool = new KeyEventPool();
    private List<com.game.jeffrey.towerdefence2.KeyEvent> keyEvents = new ArrayList<>();
    private List<com.game.jeffrey.towerdefence2.KeyEvent> keyEventBuffer = new ArrayList<>();

    private TouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEvents = new ArrayList<>();
    private List<TouchEvent> touchEventBuffer = new ArrayList<>();

    private float[] accelerometer = new float[3];

    private SoundPool soundPool;

    private int framesPerSecond = 0;

    Paint paint = new Paint();

    //********* Variables above and methods below *********

    public abstract Screen createStartScreen();

    public void onCreate(Bundle instanceBundle)
    {
        super.onCreate(instanceBundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();
        this.soundPool = new SoundPool(20,AudioManager.STREAM_MUSIC, 0);
        screen = createStartScreen();


        int testScreenOrientation = this.getResources().getConfiguration().orientation;
        System.out.println(testScreenOrientation + "*****************************************************************");

        if(testScreenOrientation == 2)
        {
            setOffscreenSurface(640,360);
        }
        else
        {
            setOffscreenSurface(360,640);
        }
        surfaceView.setFocusable(true);
        surfaceView.requestFocus();
        surfaceView.setOnKeyListener(this);

        touchHandler = new MultiTouchHandler(surfaceView, touchEventBuffer,touchEventPool);
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
        {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }//end of onCreate

    public void setOffscreenSurface(int width, int height)
    {
        if(offscreenSurface != null)
        {
            offscreenSurface.recycle();
        }
        offscreenSurface = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
        canvas = new Canvas(offscreenSurface);
    }

    public void setScreen(Screen screen)
    {
        if(this.screen != null)
        {
            this.screen.dispose();
        }
        this.screen = screen;
    }

    public Bitmap loadBitmap(String fileName)
    {
        InputStream inFromFile = null;
        Bitmap bitmap = null;
        try
        {
            inFromFile = getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(inFromFile);
            if(bitmap == null)
            {
                throw new RuntimeException("loaded bitmap from asset " + fileName + " failed please check it!");
            }
        }catch(IOException e)
        {
            throw new RuntimeException("Could not load bitmap from asset " + fileName + "!");
        }
        finally
        {
            if(inFromFile != null)
            {
                try
                {
                    inFromFile.close();
                }
                catch(IOException e)
                {

                }
            }
        }
        return bitmap;
    }

    public Sound loadSound(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor,0);
            return new Sound(soundPool,soundId);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Could not load sound: " + fileName + "!!! stupid !!!");

        }

    }
    public Music loadMusic(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            return new Music(assetFileDescriptor);
        }
        catch (IOException e)
        {
            throw new RuntimeException("GameEngine: Oh hello, we couldn't create a Music object!");
        }
    }

    public Typeface loadFont(String fileName)
    {
        Typeface font = Typeface.createFromAsset(getAssets(),fileName);
        if(font == null)
        {
            throw new RuntimeException("Couldn't load font from file: " + fileName);
        }
        return font;
    }

    public void drawText(Typeface font, String text, int x, int y, int color, int size)
    {
        paint.setTypeface(font);
        paint.setTextSize(size);
        paint.setColor(color);
        canvas.drawText(text, x, y, paint);
    }

    public void clearFramebuffer(int color)
    {
        if(this.canvas != null)
        {
            canvas.drawColor(color);
        }
    }

    public int getFramebufferWidth()
    {
        return surfaceView.getWidth();
    }

    public int getFramebufferHeight()
    {
        return surfaceView.getHeight();
    }

    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if(canvas != null)
        {
            canvas.drawBitmap(bitmap,x,y,null);
        }
    }
    public void drawBitmap(Bitmap bitmap, Rect src, Rect dest) {
        if (canvas == null) {
            return;
        }

        canvas.drawBitmap(bitmap, src, dest, null);
    }

    public void drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
    {
        if(canvas == null)
        {
            return;
        }
        Rect src = new Rect();
        Rect dst = new Rect();
        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;
        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;
        canvas.drawBitmap(bitmap, src,dst,null);
    }

    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            pressedKeys[keyCode] = true;
            synchronized (keyEventBuffer)
            {
                com.game.jeffrey.towerdefence2.KeyEvent keyEvent = keyEventPool.obtain();
                keyEvent.type = com.game.jeffrey.towerdefence2.KeyEvent.KeyEventType.Down;
                keyEvent.keyCode = keyCode;
                keyEvent.character = (char) event.getUnicodeChar();
                keyEventBuffer.add(keyEvent);
            }
        }
        if(event.getAction() == KeyEvent.ACTION_UP)
        {
            pressedKeys[keyCode] = false;
            synchronized (keyEventBuffer)
            {
                com.game.jeffrey.towerdefence2.KeyEvent keyEvent = keyEventPool.obtain();
                keyEvent.type = com.game.jeffrey.towerdefence2.KeyEvent.KeyEventType.Up;
                keyEvent.keyCode = keyCode;
                keyEvent.character = (char) event.getUnicodeChar();
                keyEventBuffer.add(keyEvent);

            }
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode)
    {
        return pressedKeys[keyCode];
    }

    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }

    public boolean isDoubleTouchDown()
    {
        //Log.d("game", "touches: " + touchEvents);
        boolean result = false;

        int amountOfTouch = 0;
        for(int i = 0; i < touchEvents.size(); i++)
        {
            if(touchEvents.get(i)!=null)
            {
            amountOfTouch++;
            }
        }

        if(amountOfTouch == 2)
        {
            result = true;
        }
        else
        {
            result = false;
        }

        return result;
    }

    public boolean isTapped()
    {
        boolean result = false;
        for(int i = 0; i < getTouchEvents().size();i++)
        {
            if(getTouchEvents().get(i) != null)
            {
                if(getTouchEvents().get(i).type == TouchEvent.TouchEventType.Up)
                {
                    return true;
                }
            }
        }

        return result;
    }

    public int getTouchX(int pointer)
    {
        return (int)(touchHandler.getTouchX(pointer) / (float) surfaceView.getWidth() * offscreenSurface.getWidth());
    }

    public int getTouchY(int pointer)
    {
        return (int)(touchHandler.getTouchY(pointer) / (float) surfaceView.getHeight() * offscreenSurface.getHeight());
    }
    public List<com.game.jeffrey.towerdefence2.KeyEvent> getKeyEvents()
    {
        return keyEvents;
    }

    public List<TouchEvent> getTouchEvents()
    {
        return touchEvents;
    }

    private void fillEvents()
    {
        synchronized (keyEventBuffer)
        {
            int stop = keyEventBuffer.size();
            for(int i = 0; i < stop ;i++)
            {
                keyEvents.add(keyEventBuffer.get(i));
            }
            keyEventBuffer.clear();
        }
        synchronized (touchEventBuffer)
        {
            int stop = touchEventBuffer.size();
            for(int i = 0; i<stop;i++)
            {
                touchEvents.add(touchEventBuffer.get(i));
            }
            touchEventBuffer.clear();
        }
    }

    private void freeEvents()
    {
        synchronized (keyEvents)
        {
            int stop = keyEvents.size();
            for(int i = 0; i<stop;i++)
            {
                keyEventPool.free(keyEvents.get(i));
            }
            keyEvents.clear();
        }
        synchronized (touchEvents)
        {
            int stop = touchEvents.size();
            for(int i = 0; i<stop;i++)
            {
                touchEventPool.free(touchEvents.get(i));
            }
            touchEvents.clear();
        }
    }

    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    public void clearAccelerometer()
    {
        accelerometer[0] = 0;
        accelerometer[1] = 0;
        accelerometer[2] = 0;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
    public void onSensorChanged(SensorEvent event)
    {
        System.arraycopy(event.values,0,accelerometer,0,3);
    }

    public int getFramesPerSecond()
    {
        return framesPerSecond;
    }


    public void run()
    {
        int frames = 0;
        long startTime = System.nanoTime();
        long lastTime = System.nanoTime();
        long currentTime = 0;
        while(true)
        {
            synchronized (stateChanges)
            {
                for(int i = 0; i<stateChanges.size();i++)
                {
                    state = stateChanges.get(i);
                    if(state == State.Disposed)
                    {
                        Log.d("GameEngine","State = disposed");
                        return;
                    }
                    if(state == State.Paused)
                    {
                        Log.d("GameEngine","State = paused");
                        return;
                    }
                    if(state == State.Resumed)
                    {
                        state = State.Running;
                        Log.d("GameEngine","Game is resumed and State = running");
                    }//end of if statement
                }//end of for loop
                stateChanges.clear();
            }//end of synchronize
            if(state == State.Running)
            {
                if(!surfaceHolder.getSurface().isValid())
                {
                    continue;
                }
                Canvas canvas = surfaceHolder.lockCanvas();
                // all drawing happens here!!!!!!!!!!!!
                canvas.drawColor(Color.RED);
                fillEvents();
                currentTime = System.nanoTime();
                if(screen != null)
                {
                    screen.update((currentTime - lastTime)/1000000000.0f);
                }
                lastTime = currentTime;
                freeEvents();
                src.left = 0;
                src.top = 0;
                src.right = offscreenSurface.getWidth()-1;
                src.bottom = offscreenSurface.getHeight()-1;
                dst.left = 0;
                dst.top = 0;
                dst.right = surfaceView.getWidth();
                dst.bottom = surfaceView.getHeight();
                canvas.drawBitmap(offscreenSurface,src,dst,null);
                surfaceHolder.unlockCanvasAndPost(canvas);
                canvas = null;
            }
            frames = frames + 1;
            if(System.nanoTime() - startTime > 999999999)
            {
                framesPerSecond = frames;
                frames = 0;
                startTime = System.nanoTime();
            }
            //Log.d("Framerate: ","" + framesPerSecond);
        }//end of while loop
    }//end of run() method

    public void onPause()
    {
        super.onPause();
        synchronized (stateChanges)
        {
            if(isFinishing())
            {
                stateChanges.add(stateChanges.size(),state.Disposed);
            }
            else
            {
                stateChanges.add(stateChanges.size(),state.Paused);
            }
        }
        try
        {
            mainLoopThread.join();
        }
        catch(InterruptedException e)
        {
            //do nothing
        }
        if(isFinishing())
        {
            ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            soundPool.release();
        }
    }

    public void onResume()
    {
        super.onResume();
        mainLoopThread = new Thread(this);
        mainLoopThread.start();
        synchronized (stateChanges)
        {
            stateChanges.clear();
            stateChanges.add(stateChanges.size(),state.Resumed);
        }
    }


}
