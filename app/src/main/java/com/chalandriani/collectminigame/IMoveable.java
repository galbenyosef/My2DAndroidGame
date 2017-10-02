package com.chalandriani.collectminigame;

/**
 * Created by Gal on 26/09/2017.
 */

interface IMoveable  {

    void setDx(int dx);
    void setDy(int dy);
    int getDx();
    int getDy();
    void setDirection(int newdirection);
    void setSpeed(int speed);
    int getDirection();
    int getSpeed();

}
