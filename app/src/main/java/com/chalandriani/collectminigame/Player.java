package com.chalandriani.collectminigame;

/**
 * Created by Gal on 12-Sep-17.
 */

import android.graphics.Canvas;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;


public class Player extends GameObject implements IMoveable,IDestroyable {
    //Character attributes
    //Animation
    private AnimationManager animator = Main.animator;
    private Animation animation;
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
    protected boolean changedDirection;

    //Character states times
    private long slashingStartTime;
    private long risingStartTime;

    //Empty params c'tor is needed for firebase database
    Player(){
    }

    void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerName() {
        return playerName;
    }



    public void setCharacterId(int characterId) {
        this.characterId = characterId;
        setAnimation(animator.getCharacter(characterId).getRisingAnimation());

    }
    public int getCharacterId() {
        return characterId;
    }
    private void setAnimation(Animation animation) {
        this.animation=animation;
    }
    public boolean isWalking() {
        return walking;
    }
    public boolean isSlashing() { return slashing; }
    public void setWalking(boolean walking){
        if (!isSlashing() && isChangedDirection()) {

            setAnimation(animator.getCharacter(characterId).getWalkAnimation(getDirection()));
            Log.d("H",""+animation.getImage().getByteCount());

        }
        setChangedDirection(false);
        this.walking = walking;

    }
    public void setSlashing(boolean slashing){
        if (!this.slashing && slashing) {
            slashingStartTime = System.nanoTime();
            setAnimation(animator.getCharacter(characterId).getSlashAnimation(getDirection()));
        }
        else if(!slashing) {
            setAnimation(animator.getCharacter(characterId).getWalkAnimation(getDirection()));
        }
        this.slashing=slashing;
    }
    public boolean isChangedDirection() {
        return changedDirection;
    }
    public void setChangedDirection(boolean changedDirection) {
        this.changedDirection = changedDirection;
    }
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
    public void setDirection(int newDirection) {
        dx=dy=0;

        switch (newDirection) {
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

        setChangedDirection(true);

        if (this.direction == JoystickView.UP && newDirection == JoystickView.UP )
            setChangedDirection(false);
        if (this.direction == JoystickView.DOWN && newDirection == JoystickView.DOWN )
            setChangedDirection(false);
        if ((this.direction == JoystickView.RIGHT || this.direction == JoystickView.UP_RIGHT || this.direction == JoystickView.DOWN_RIGHT)
                && (newDirection == JoystickView.RIGHT || newDirection == JoystickView.UP_RIGHT || newDirection == JoystickView.DOWN_RIGHT) )
            setChangedDirection(false);
        if ((this.direction == JoystickView.LEFT || this.direction == JoystickView.UP_LEFT || this.direction == JoystickView.DOWN_LEFT)
                && (newDirection == JoystickView.LEFT || newDirection == JoystickView.UP_LEFT || newDirection == JoystickView.DOWN_LEFT) )
            setChangedDirection(false);

        if (isChangedDirection())

        this.direction = newDirection;

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
            if (animation!=null)
                animation.update();

            if (isWalking() && !isSlashing()) {
                setX(getX() + getDx());
                setY(getY() + getDy());
            }
            if (isSlashing()) {
                if ((System.nanoTime()-slashingStartTime)/1000000 > 1000) {
                    slashingStartTime=0;
                    //candidated to be replaced by slashing its own finish time boolean value
                    setSlashing(false);
                    Log.d("H","FLASE SLASHING");
                }
            }
        }
    }
    public void draw(Canvas canvas)
    {
        if (animation!=null)
            canvas.drawBitmap(animation.getImage(),x,y,null);
    }

}