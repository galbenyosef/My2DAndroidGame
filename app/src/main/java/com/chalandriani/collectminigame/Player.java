package com.chalandriani.collectminigame;

/**
 * Created by Gal on 12-Sep-17.
 */

import android.graphics.Canvas;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Player extends GameObject implements IMoveable,IDestroyable {

    //Character attributes
    //Animation
    protected AnimationManager animator = Main.animator;
    protected Animation animation;
    protected int characterId;
    protected String playerName;
    //IMoveable variables
    protected int dx;
    protected int dy;
    protected int direction;
    protected int speed;
    //IDestroyable variables
    protected int hp;
    protected int hpMax;
    //Character states
    protected boolean walking;
    protected boolean slashing;

    //Character states times
    protected long slashingStartTime;

    //Empty c'tor is needed for firebase database
    Player(){
    }

    void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerName() {
        return playerName;
    }
    void setWalking(boolean walking){
        if (!isSlashing()) {
            setAnimation(animator.getCharacter(characterId).getWalkAnimation(getDirection()));
        }
        this.walking = walking;

    }
    void setSlashing(boolean slashing){
        if (!this.slashing) {
            slashingStartTime = System.nanoTime();
            setAnimation(animator.getCharacter(characterId).getSlashAnimation(getDirection()));
        }
        this.slashing=slashing;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
    private void setAnimation(Animation animation) {
        this.animation=animation;
    }
    public boolean isWalking() {
        return walking;
    }
    public boolean isSlashing() { return slashing; }
    //IMoveable
    public void setDx(int dx) {
        this.dx=dx;
    }
    public void setDy(int dy) {
        this.dy=dy;
    }
    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
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
    public void setSpeed(int speed) {
        this.speed=speed;
    }
    public int getDirection() {
        return direction;
    }
    public int getSpeed() {
        return speed;
    }
    //IDestroyable
    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getHp() {
        return hp;
    }
    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }
    public void decreaseHp(int amount){
        hp-=amount;
        if (hp > 0)
            hp=0;
    }
    public void increaseHp(int amount){
        hp+=amount;
        if (hp > hpMax)
            hp = hpMax;
    }
    //Core
    public void update() {
        if (isWalking() || isSlashing()) {
            animation.update();

            if (isWalking()) {
                setX(getX() + getDx());
                setY(getY() + getDy());
            }
            if (isSlashing()) {
                if ((System.nanoTime()-slashingStartTime)/1000000 > 1000) {
                    setSlashing(false);
                }
            }
        }
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }

}