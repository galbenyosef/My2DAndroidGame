package com.chalandriani.collectminigame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gal on 17-Sep-17.
 */

public class UniversalLPCSpritesheet {

    public static int testi = 0;
    public final static int WIDTH = 64;
    public final static int HEIGHT = 64;
    private Bitmap resource;

    ArrayList<Animation> walk;
    ArrayList<Animation> slash;

    public UniversalLPCSpritesheet(Bitmap character){

        this.resource = character;

        walk = new ArrayList<>();
        walk.add(getWalkAnimation(JoystickView.UP));
        walk.add(getWalkAnimation(JoystickView.DOWN));
        walk.add(getWalkAnimation(JoystickView.LEFT));
        walk.add(getWalkAnimation(JoystickView.RIGHT));

        slash = new ArrayList<>();
        slash.add(getSlashAnimation(JoystickView.UP));
        slash.add(getSlashAnimation(JoystickView.DOWN));
        slash.add(getSlashAnimation(JoystickView.LEFT));
        slash.add(getSlashAnimation(JoystickView.RIGHT));
    }

    public Animation makeAnimation(int row,int frames,int delay){

        Bitmap[] images = new Bitmap[frames];

        for (int i = 0; i < images.length; i++)
        {
            images[i] =  Bitmap.createBitmap(resource, i*WIDTH , row*HEIGHT, WIDTH, HEIGHT);
            ++testi;
            Log.d("H",""+testi+" "+images[i].getByteCount());
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

    public Bitmap getResource() {
        return resource;
    }
}

