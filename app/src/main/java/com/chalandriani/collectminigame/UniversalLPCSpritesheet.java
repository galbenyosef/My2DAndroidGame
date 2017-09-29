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
    private Bitmap punch;


    public UniversalLPCSpritesheet(Resources resource){

        this.resource =   BitmapFactory.decodeResource(resource, R.drawable.character_5)   ;
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
                return makeAnimation(8,9,0);
            case JoystickView.DOWN:
                return makeAnimation(10,9,0);
            case JoystickView.RIGHT:
                return makeAnimation(11,9,0);
            case JoystickView.LEFT:
                return makeAnimation(9,9,0);
            case JoystickView.UP_LEFT:
                return makeAnimation(9,9,0);
            case JoystickView.UP_RIGHT:
                return makeAnimation(11,9,0);
            case JoystickView.DOWN_LEFT:
                return makeAnimation(9,9,0);
            case JoystickView.DOWN_RIGHT:
                return makeAnimation(11,9,0);
            default:
                return makeAnimation(10,9,0);
        }

    }

    public Animation getSlashAnimation(int direction){

        switch (direction){
            case JoystickView.UP:
                return makeAnimation(12,6,0);
            case JoystickView.DOWN:
                return makeAnimation(14,6,0);
            case JoystickView.RIGHT:
                return makeAnimation(15,6,0);
            case JoystickView.LEFT:
                return makeAnimation(13,6,0);
            case JoystickView.UP_LEFT:
                return makeAnimation(13,6,0);
            case JoystickView.UP_RIGHT:
                return makeAnimation(15,6,0);
            case JoystickView.DOWN_LEFT:
                return makeAnimation(13,6,0);
            case JoystickView.DOWN_RIGHT:
                return makeAnimation(15,6,0);
            default:
                return makeAnimation(14,6,0);
        }

    }

}

