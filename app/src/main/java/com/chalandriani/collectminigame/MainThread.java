package com.chalandriani.collectminigame;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread
{
    private int FPS = 10;
    private SurfaceHolder surfaceHolder;
    private GameScreen gameScreen;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GameScreen gameScreen)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameScreen = gameScreen;
    }
    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        int frameCount =0;
        long targetTime = 1000/FPS;

        while(running) {
            try{
                //this.sleep(10000);
            }catch(Exception e){}
            startTime = System.nanoTime();
            canvas = null;

            //try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameScreen.update();
                    this.gameScreen.draw(canvas);
                }
            } catch (Exception e) {
                Log.d("EX",e.getMessage());
            }
            finally{
                if(canvas!=null)
                {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;

            try{
                this.sleep(waitTime);
            }catch(Exception e){}

            frameCount++;
            if(frameCount == FPS)
            {
                frameCount=0;
            }
        }
    }
    public void setRunning(boolean b)
    {
        running=b;
    }
}