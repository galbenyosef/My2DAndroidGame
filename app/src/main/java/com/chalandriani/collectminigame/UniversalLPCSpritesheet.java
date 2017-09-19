package com.chalandriani.collectminigame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Gal on 17-Sep-17.
 */

public class UniversalLPCSpritesheet {

    public final static int WIDTH = 64;
    public final static int HEIGHT = 64;
    private Bitmap resource;


    public UniversalLPCSpritesheet(Resources resource){


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        this.resource =   BitmapFactory.decodeResource(resource, R.drawable.walk,options)   ;
    }

    public Animation makeAnimation(int row,int frames,int delay){

        Bitmap[] images = new Bitmap[frames];

        for (int i = 0; i < images.length; i++)
        {
            images[i] =  Bitmap.createBitmap(resource, i*WIDTH , row*HEIGHT, WIDTH, HEIGHT);
        }

        return new Animation(images,delay);
    }

    public Animation getWalkAnimation(int direction){

        switch (direction){
            case JoystickView.UP:
                return makeAnimation(0,9,0);
            case JoystickView.DOWN:
                return makeAnimation(2,9,0);
            case JoystickView.RIGHT:
                return makeAnimation(3,9,0);
            case JoystickView.LEFT:
                return makeAnimation(1,9,0);
            case JoystickView.UP_LEFT:
                return makeAnimation(1,9,0);
            case JoystickView.UP_RIGHT:
                return makeAnimation(3,9,0);
            case JoystickView.DOWN_LEFT:
                return makeAnimation(1,9,0);
            case JoystickView.DOWN_RIGHT:
                return makeAnimation(3,9,0);
            default:
                return makeAnimation(2,9,0);
        }

    }

}

