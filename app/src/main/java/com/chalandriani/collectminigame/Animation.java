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

    public static Animation makeAnimation(Bitmap res, int width, int height, int frames, int k){

        Bitmap[] images = new Bitmap[frames];

        for (int i = 0; i < images.length; i++)
        {
            images[i] =  Bitmap.createBitmap(res, i*width , k*height, width, height);
        }

        return new Animation(images,2);
    }

    public static Animation getWalkAnimation(Bitmap res, int width, int height, int direction){

        switch (direction){
            case JoystickView.UP:
                return makeAnimation(res,width,height,9,0);
            case JoystickView.DOWN:
                return makeAnimation(res,width,height,9,2);
            case JoystickView.RIGHT:
                return makeAnimation(res,width,height,9,3);
            case JoystickView.LEFT:
                return makeAnimation(res,width,height,9,1);
            case JoystickView.UP_LEFT:
                return makeAnimation(res,width,height,9,1);
            case JoystickView.UP_RIGHT:
                return makeAnimation(res,width,height,9,3);
            case JoystickView.DOWN_LEFT:
                return makeAnimation(res,width,height,9,1);
            case JoystickView.DOWN_RIGHT:
                return makeAnimation(res,width,height,9,3);
            default:
                return makeAnimation(res,width,height,2,2);
        }

    }

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