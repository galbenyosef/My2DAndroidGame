package com.chalandriani.collectminigame;

/**
 * Created by Gal on 12-Sep-17.
 */

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;
    protected boolean moving;
    protected int direction;
    protected int old_direction;
    protected int speed;

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setDx(int dx) {
        this.dx = dx;
    }
    public void setDy(int dy) {
        this.dy = dy;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setMoving(boolean moving) {
        this.moving = moving;
        if (!moving){
           // dx=dy=0;
        }
    }
    public void setDirection(int newdirection) {
        this.direction = newdirection;
        dx=dy=0;

        switch (direction) {
            case (JoystickView.UP) :
                dy -= speed;
                break;
            case (JoystickView.DOWN) :
                dy += speed;
                break;
            case (JoystickView.LEFT) :
                dx -= speed;
                break;
            case (JoystickView.RIGHT) :
                dx += speed;
                break;
            case (JoystickView.UP_LEFT) :
                dx -= speed;
                dy -= speed;
                break;
            case (JoystickView.UP_RIGHT) :
                dx += speed;
                dy -= speed;
                break;
            case (JoystickView.DOWN_LEFT) :
                dx -= speed;
                dy += speed;
                break;
            case (JoystickView.DOWN_RIGHT) :
                dx += speed;
                dy += speed;
                break;
        }

    }
    public void setOld_direction(int direction) {
        this.old_direction = direction;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public boolean isMoving() {
        return moving;
    }
    public int getDirection() {
        return direction;
    }
    public int getOld_direction() {
        return old_direction;
    }
    public int getSpeed() {
        return speed;
    }

}