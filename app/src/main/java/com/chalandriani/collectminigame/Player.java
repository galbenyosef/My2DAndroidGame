package com.chalandriani.collectminigame;

/**
 * Created by Gal on 12-Sep-17.
 */

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Player extends GameObject{

    protected Animation animation;

    private String playerName;

    FirebaseDatabase database;
    DatabaseReference playerReference;

    public Player(){

    }

    public void setReference(String name){
        database = FirebaseDatabase.getInstance();
        setPlayerName(name);
        playerReference = database.getReference("players").child(name);
    }

    public void update()
    {

        if (!isMoving()){
            playerReference.setValue(this);
            return;
        }

        if (animation!=null)
            animation.update();

        setX(getX()+getDx());
        setY(getY()+getDy());
        playerReference.setValue(this);
    }


    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }

    public void setAnimation(Animation animation) {
        this.animation=animation;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}