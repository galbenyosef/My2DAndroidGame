package com.chalandriani.collectminigame;

/**
 * Created by Gal on 12-Sep-17.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Player extends GameObject{

    private long startTime;
    private Animation walkup,walkleft,walkdown,walkright,current;

    public Player(){}

    public Player(Bitmap res, int w, int h, int x, int y, int v) {

        //init screen coordinates
        setX(x);
        setY(y);
        setSpeed(v);
        setHeight(h);
        setWidth(w);
        setDirection(JoystickView.CENTER);
        setMoving(false);
        setRectangle(new Rect(x,y,x+width,y+height));

        walkup = Animation.getWalkAnimation(res,w,h,JoystickView.UP);
        walkdown = Animation.getWalkAnimation(res,w,h,JoystickView.DOWN);
        walkleft = Animation.getWalkAnimation(res,w,h,JoystickView.LEFT);
        walkright = Animation.getWalkAnimation(res,w,h,JoystickView.RIGHT);
        current = walkright;


        startTime = System.nanoTime();

    }

    public void update()
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference r = database.getReference("players").child("utsi");

        long elapsed = (System.nanoTime()-startTime)/1000000;

        if(elapsed>100)
        {
            startTime = System.nanoTime();
        }

        if (!isMoving()){
            r.setValue(this);
            return;
        }
        switch (direction) {
            case (JoystickView.UP) :
                current = walkup ;
                y -= speed;
                break;
            case (JoystickView.DOWN) :
                current = walkdown;
                y += speed;
                break;

            case (JoystickView.LEFT) :
                current = walkleft;
                x -= speed;
                break;
            case (JoystickView.RIGHT) :
                current = walkright;
                x += speed;
                break;
            case (JoystickView.UP_LEFT) :
                current = walkleft;
                x -= speed;
                y -= speed;
                break;
            case (JoystickView.UP_RIGHT) :
                current = walkright;
                x += speed;
                y -= speed;
                break;
            case (JoystickView.DOWN_LEFT) :
                current = walkleft;
                x -= speed;
                y += speed;
                break;
            case (JoystickView.DOWN_RIGHT) :
                current = walkright;
                x += speed;
                y += speed;
                break;
        }

        current.update();

        r.setValue(this);


    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(current.getImage(),x,y,null);
    }

    public void setWalkup(Animation walkup) {
        this.walkup = walkup;
    }

    public void setWalkdown(Animation walkdown) {
        this.walkdown = walkdown;
    }

    public void setWalkleft(Animation walkleft) {
        this.walkleft = walkleft;
    }

    public void setWalkright(Animation walkright) {
        this.walkright = walkright;
    }

    public void setCurrent(Animation current) {
        this.current = current;
    }

//    public Animation getWalkdown() {
//        return walkdown;
//    }
//
//    public Animation getWalkleft() {
//        return walkleft;
//    }
//
//    public Animation getWalkright() {
//        return walkright;
//    }
//
//    public Animation getWalkup() {
//        return walkup;
//    }
}