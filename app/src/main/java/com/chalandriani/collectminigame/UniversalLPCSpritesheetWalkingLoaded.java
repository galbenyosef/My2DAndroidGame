package com.chalandriani.collectminigame;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gal on 17-Sep-17.
 */

public class UniversalLPCSpritesheetWalkingLoaded {

    public static final int WALK_UP = 8;
    public static final int WALK_DOWN = 10;
    public static final int WALK_LEFT = 9;
    public static final int WALK_RIGHT = 11;

    public static final int SLASH_UP = 12;
    public static final int SLASH_DOWN = 14;
    public static final int SLASH_LEFT = 13;
    public static final int SLASH_RIGHT = 15;

    public static final int RISING = 20;
    public static final int FALLING = 20;

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    public static ArrayList<Animation> walks;

    private Bitmap resource;

    public UniversalLPCSpritesheetWalkingLoaded(Bitmap character) {
        this.resource = character;
        walks= new ArrayList<>();
        walks.add(makeAnimation(WALK_LEFT,9,0,false));
        walks.add(makeAnimation(WALK_UP,9,0,false));
        walks.add(makeAnimation(WALK_RIGHT,9,0,false));
        walks.add(makeAnimation(WALK_DOWN,9,0,false));
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
                return walks.get(1);
            case JoystickView.DOWN:
                return walks.get(3);
            case JoystickView.RIGHT:
                return walks.get(2);
            case JoystickView.LEFT:
                return walks.get(0);
            case JoystickView.UP_LEFT:
                return walks.get(0);
            case JoystickView.UP_RIGHT:
                return walks.get(2);
            case JoystickView.DOWN_LEFT:
                return walks.get(0);
            case JoystickView.DOWN_RIGHT:
                return walks.get(2);
            case JoystickView.CENTER:
                return walks.get(3);
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

