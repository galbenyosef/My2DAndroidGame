package com.chalandriani.collectminigame;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gal on 17-Sep-17.
 */

public class UniversalLPCSpritesheet {

    public final int WALK_UP = 8;
    public final int WALK_DOWN = 10;
    public final int WALK_LEFT = 9;
    public final int WALK_RIGHT = 11;

    public final int SLASH_UP = 12;
    public final int SLASH_DOWN = 14;
    public final int SLASH_LEFT = 13;
    public final int SLASH_RIGHT = 15;

    public final int RISING = 20;
    public final int FALLING = 20;

    public final int WIDTH = 64;
    public final int HEIGHT = 64;

    private Bitmap resource;

    public UniversalLPCSpritesheet(Bitmap character) {

        this.resource = character;
    }

    public Animation makeAnimation(int row,int frames,int delay,boolean reversed){

        Bitmap[] images = new Bitmap[frames];

        if (!reversed) {
            for (int i = 0; i < images.length; i++) {
                images[i] = Bitmap.createBitmap(resource, i * WIDTH, row * HEIGHT, WIDTH, HEIGHT);
            }
        }
        else {
            for (int i = images.length-1; i > -1; i--) {
                images[i] = Bitmap.createBitmap(resource, i * WIDTH, row * HEIGHT, WIDTH, HEIGHT);
            }
        }
        return new Animation(images,delay);
    }

    public Animation getWalkAnimation(int direction){

        switch (direction){
            case JoystickView.UP:
                return makeAnimation(WALK_UP,9,0,false);
            case JoystickView.DOWN:
                return makeAnimation(WALK_DOWN,9,0,false);
            case JoystickView.RIGHT:
                return makeAnimation(WALK_RIGHT,9,0,false);
            case JoystickView.LEFT:
                return makeAnimation(WALK_LEFT,9,0,false);
            case JoystickView.UP_LEFT:
                return makeAnimation(WALK_LEFT,9,0,false);
            case JoystickView.UP_RIGHT:
                return makeAnimation(WALK_RIGHT,9,0,false);
            case JoystickView.DOWN_LEFT:
                return makeAnimation(WALK_LEFT,9,0,false);
            case JoystickView.DOWN_RIGHT:
                return makeAnimation(WALK_RIGHT,9,0,false);
            default:
                return null;
        }

    }

    public Animation getSlashAnimation(int direction) {

        switch (direction) {
            case JoystickView.UP:
                return makeAnimation(SLASH_UP, 6, 0, false);
            case JoystickView.DOWN:
                return makeAnimation(SLASH_DOWN, 6, 0, false);
            case JoystickView.RIGHT:
                return makeAnimation(SLASH_RIGHT, 6, 0, false);
            case JoystickView.LEFT:
                return makeAnimation(SLASH_LEFT, 6, 0, false);
            case JoystickView.UP_LEFT:
                return makeAnimation(SLASH_LEFT, 6, 0, false);
            case JoystickView.UP_RIGHT:
                return makeAnimation(SLASH_RIGHT, 6, 0, false);
            case JoystickView.DOWN_LEFT:
                return makeAnimation(SLASH_LEFT, 6, 0, false);
            case JoystickView.DOWN_RIGHT:
                return makeAnimation(SLASH_RIGHT, 6, 0, false);
            default:
                return null;
        }
    }

    public Animation getRisingAnimation() {

        return makeAnimation(RISING, 5, 0, true);

    }

        public Bitmap getResource() {
        return resource;
    }
}

