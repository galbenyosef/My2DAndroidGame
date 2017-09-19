package com.chalandriani.collectminigame;

/**
 * Created by Gal on 12-Sep-17.
 */


import android.graphics.Bitmap;


public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;

    public Animation(Bitmap[] frames, long d){

        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        delay = d;
    }

    public void setFrame(int i){currentFrame= i;}


    public void update()
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;

        if(elapsed>delay)
        {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }
    public Bitmap getImage(){
        return frames[currentFrame];
    }
    public int getFrame(){return currentFrame;}
    public boolean playedOnce(){return playedOnce;}
}