package com.chalandriani.collectminigame;

import android.content.res.Resources;

import java.util.ArrayList;

public class AnimationManager {


    UniversalLPCSpritesheet sprites;
    ArrayList<Animation> walks;


    public AnimationManager (Resources resources){

        sprites = new UniversalLPCSpritesheet(resources);

        walks = new ArrayList<>();
        walks.add(sprites.getWalkAnimation(JoystickView.UP));
        walks.add(sprites.getWalkAnimation(JoystickView.DOWN));
        walks.add(sprites.getWalkAnimation(JoystickView.LEFT));
        walks.add(sprites.getWalkAnimation(JoystickView.RIGHT));

    }


    public Animation getWalkAnimation(int direction){
        switch (direction){
            case JoystickView.UP:
                return walks.get(0);
            case JoystickView.DOWN:
                return walks.get(1);
            case JoystickView.RIGHT:
                return walks.get(3);
            case JoystickView.LEFT:
                return walks.get(2);
            case JoystickView.UP_LEFT:
                return walks.get(2);
            case JoystickView.UP_RIGHT:
                return walks.get(3);
            case JoystickView.DOWN_LEFT:
                return walks.get(2);
            case JoystickView.DOWN_RIGHT:
                return walks.get(3);
            default:
                return walks.get(0);
        }
    }


}
