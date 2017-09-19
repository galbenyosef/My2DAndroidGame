package com.chalandriani.collectminigame;

/**
 * Created by Gal on 18-Sep-17.
 */

public class OnlinePlayer extends Player {

    @Override
    public void update() {
        if (!isMoving()){
            return;
        }

        if (animation!=null)
            animation.update();

        switch (direction) {
            case (JoystickView.UP) :
                y -= speed;
                break;
            case (JoystickView.DOWN) :
                y += speed;
                break;
            case (JoystickView.LEFT) :
                x -= speed;
                break;
            case (JoystickView.RIGHT) :
                x += speed;
                break;
            case (JoystickView.UP_LEFT) :
                x -= speed;
                y -= speed;
                break;
            case (JoystickView.UP_RIGHT) :
                x += speed;
                y -= speed;
                break;
            case (JoystickView.DOWN_LEFT) :
                x -= speed;
                y += speed;
                break;
            case (JoystickView.DOWN_RIGHT) :
                x += speed;
                y += speed;
                break;
        }
    }
}
