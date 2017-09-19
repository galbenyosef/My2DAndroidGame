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
    protected boolean moving;
    protected int direction;
    protected int old_direction;
    protected int speed;
    protected int rect_x_begin,rect_y_begin,rect_x_end,rect_y_end;

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

    public void setDirection(int newdirection) {
        this.direction = newdirection;
    }

    public void setOld_direction(int direction) {
        this.old_direction = direction;
    }

    public int getOld_direction() {
        return old_direction;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRect_x_begin() {
        return rect_x_begin;
    }

    public int getRect_x_end() {
        return rect_x_end;
    }

    public int getRect_y_begin() {
        return rect_y_begin;
    }

    public int getRect_y_end() {
        return rect_y_end;
    }

    public void setRectangle(int x, int y, int x_offset, int y_offset){

        this.rect_x_begin = x;
        this.rect_x_end = x_offset;
        this.rect_y_begin=y;
        this.rect_y_end=y_offset;

    }


}