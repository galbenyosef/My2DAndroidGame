package com.chalandriani.collectminigame;

/**
 * Created by Gal on 12-Sep-17.
 */

import android.graphics.Rect;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dy;
    protected int dx;
    protected int width;
    protected int height;
    protected Rect rectangle;
    protected boolean moving;
    protected int direction;
    protected int speed;

    public void setX(int x)
    {
        this.x = x;
    }
    public void setY(int y)
    {
        this.y = y;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getHeight()
    {
        return height;
    }
    public int getWidth()
    {
        return width;
    }
    public void setWidth(int width) {this.width = width;}
    public void setHeight(int height){this.height=height;}

    public boolean isMoving() {
        return moving;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Rect getRectangle()
    {
        return rectangle;
    }

    public void setRectangle(Rect rectangle){this.rectangle = rectangle;}

}