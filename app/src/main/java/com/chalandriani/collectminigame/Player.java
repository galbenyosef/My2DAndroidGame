package com.chalandriani.collectminigame;

/**
 * Created by Gal on 12-Sep-17.
 */

import android.graphics.Canvas;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Player extends GameObject{

    protected Animation animation;

    private String playerName;

    FirebaseDatabase database;
    DatabaseReference playerReference;

    public Player(){

        database = FirebaseDatabase.getInstance();
        playerReference = database.getReference("players").child(getPlayerName());

    }

    public Player(int x, int y, int v) {

        //init screen coordinates
        setX(x);
        setY(y);
        setSpeed(v);
        setHeight(UniversalLPCSpritesheet.HEIGHT);
        setWidth(UniversalLPCSpritesheet.WIDTH);
        setMoving(false);
        setRectangle(x,x+width,y,y+height);
        setPlayerName("gal");
    }

    public void update()
    {

        if (!isMoving()){
            playerReference.setValue(this);
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