package com.chalandriani.collectminigame;

import android.content.res.Resources;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class AnimationManager {

    public static Resources resources;

    UniversalLPCSpritesheet sprites;
    ArrayList<Animation> walks;
    ArrayList<Animation> slashes;

    OnAnimationListener listener;

    public AnimationManager (Resources resources){

        this.resources = resources;
        sprites = new UniversalLPCSpritesheet(resources);

        walks = new ArrayList<>();
        walks.add(sprites.getWalkAnimation(JoystickView.UP));
        walks.add(sprites.getWalkAnimation(JoystickView.DOWN));
        walks.add(sprites.getWalkAnimation(JoystickView.LEFT));
        walks.add(sprites.getWalkAnimation(JoystickView.RIGHT));

        slashes = new ArrayList<>();
        slashes.add(sprites.getSlashAnimation(JoystickView.UP));
        slashes.add(sprites.getSlashAnimation(JoystickView.DOWN));
        slashes.add(sprites.getSlashAnimation(JoystickView.LEFT));
        slashes.add(sprites.getSlashAnimation(JoystickView.RIGHT));


    }

    public void setAnimationListener(OnAnimationListener listener) {
        this.listener = listener;
    }

    public interface OnAnimationListener {
        void onAnimationChange(Animation anim);
    }

    public void changeAnimation(Animation anim) {
        if (listener != null) {
            listener.onAnimationChange(anim);
        }
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

    public Animation getSlashAnimation(int direction){
        switch (direction){
            case JoystickView.UP:
                return slashes.get(0);
            case JoystickView.DOWN:
                return slashes.get(1);
            case JoystickView.RIGHT:
                return slashes.get(3);
            case JoystickView.LEFT:
                return slashes.get(2);
            case JoystickView.UP_LEFT:
                return slashes.get(2);
            case JoystickView.UP_RIGHT:
                return slashes.get(3);
            case JoystickView.DOWN_LEFT:
                return slashes.get(2);
            case JoystickView.DOWN_RIGHT:
                return slashes.get(3);
            default:
                return slashes.get(0);
        }
    }

    public static Resources getResources(){
            return resources;
    }

}
