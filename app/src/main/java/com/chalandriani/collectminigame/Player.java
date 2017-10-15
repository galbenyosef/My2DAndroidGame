package com.chalandriani.collectminigame;

/**
 * Created by Gal on 12-Sep-17.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;


public class Player extends GameObject implements IMoveable,IDestroyable {
    //Character attributes
    //Animation
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
    protected boolean jumping;

    //Used for Image loading optimization heuristics
    protected boolean changedDirection;

    //Character states times
    private long slashingStartTime;
    private long risingStartTime;
    private int jumpingLevel;
    protected Rectangle rectangle; //defines Collision

    //Empty params c'tor is needed for firebase database
    Player(){

    }

    //Player basic information
    void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
    public int getCharacterId() {
        return characterId;
    }
    //Animation handling
    public void setAnimation(Animation animation) {
        this.animation=animation;
    }
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
    public Rectangle getRectangle() {
        return rectangle;
    }
    public void setWalking(boolean walking){

        if (!isSlashing() && !isJumping() && isChangedDirection() && walking) {
            this.setAnimation(Main.animator.getCharacter().getWalkAnimation(getDirection()));
        }
        else if (this.walking && !walking){
            this.setAnimation(Main.animator.getCharacter().getWalkAnimation(getDirection()));
        }

        setChangedDirection(false);
        this.walking = walking;
    }
    public boolean isWalking() {
        return walking;
    }
    public void setSlashing(boolean slashing){

        if (!this.slashing && slashing) {

            slashingStartTime = System.nanoTime();
            this.setAnimation(Main.animator.getCharacter().getSlashAnimation(getDirection()));

        }
        else if(this.slashing && !slashing) {

            this.setAnimation(Main.animator.getCharacter().getWalkAnimation(getDirection()));

        }

        this.slashing=slashing;
    }
    public boolean isSlashing() { return slashing; }
    public void setJumping(boolean jumping) {

        if (!isJumping() && !isSlashing() && jumping){

            jumpingLevel=0;

        }
        else if (isJumping() && !jumping){
            setChangedDirection(true);
            this.jumping=false;
            setWalking(false);

        }
        else if  (isJumping() && jumping) {
            if (jumpingLevel >= Main.gameLoop.FPS){
                setJumping(false);
                jumpingLevel=0;
                return;
            }
            int direction = getDirection();
            setDirection(getDirection());
            if (jumpingLevel < (Main.gameLoop.FPS / 2)) {
                setY(getY() - getSpeed());
            } else {
                setY(getY() + getSpeed());
            }
        }
        this.jumping = jumping;
    }
    public boolean isJumping() {
        return jumping;
    }
    public void setChangedDirection(boolean changedDirection) {
        this.changedDirection = changedDirection;
    }
    public boolean isChangedDirection() {
        return changedDirection;
    }
    //IMoveable
    public void setDx(int dx) {
        this.dx=dx;
    }
    public int getDx() {
        return dx;
    }
    public void setDy(int dy) {
        this.dy=dy;
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

        if (this.direction == JoystickView.UP && newDirection == JoystickView.UP )
            setChangedDirection(false);
        else if (this.direction == JoystickView.DOWN && newDirection == JoystickView.DOWN )
            setChangedDirection(false);
        else if ((this.direction == JoystickView.RIGHT || this.direction == JoystickView.UP_RIGHT || this.direction == JoystickView.DOWN_RIGHT)
                && (newDirection == JoystickView.RIGHT || newDirection == JoystickView.UP_RIGHT || newDirection == JoystickView.DOWN_RIGHT) )
            setChangedDirection(false);
        else if ((this.direction == JoystickView.LEFT || this.direction == JoystickView.UP_LEFT || this.direction == JoystickView.DOWN_LEFT)
                && (newDirection == JoystickView.LEFT || newDirection == JoystickView.UP_LEFT || newDirection == JoystickView.DOWN_LEFT) )
            setChangedDirection(false);
        else if (this.direction ==  0)
            setChangedDirection(false);
        else
            setChangedDirection(true);

        this.direction = newDirection;

    }
    public int getDirection() {
        return direction;
    }
    public void setSpeed(int speed) {
        this.speed=speed;
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
        if (hp < 0)
            hp=0;
    }
    public void increaseHp(int amount){
        hp+=amount;
        if (hp > hpMax)
            hp = hpMax;
    }
    //Core
    public void update() {

        if (isWalking() || isSlashing() || isJumping()) {
            if (animation!=null)
                animation.update();

            if (isWalking() && !isSlashing()) {
                setX(getX() + getDx());
                setY(getY() + getDy());
            }

            setRectangle(new Rectangle(Main.player.getX()+Main.player.getWidth()/3,
                    Main.player.getY()+Main.player.getHeight()-Main.player.getHeight()/6
                    ,Main.player.getX()+Main.player.getWidth()-Main.player.getWidth()/3
                    ,Main.player.getY()+Main.player.getHeight()));

            if (isJumping()) {
                setJumping(isJumping());
                jumpingLevel++;
            }

            if (isSlashing()) {
                if ((System.nanoTime()-slashingStartTime)/1000000 > 1000) {
                    slashingStartTime=0;
                    //candidated to be replaced by slashing its own finish time boolean value
                    setSlashing(false);
                }
            }
        }
    }
    public void drawShadow(Canvas canvas){
        Paint mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAlpha(40);
        int shadowFactor;
        //shadow drawing
        //regular
        if (!isJumping()) {
            RectF shadowRect = new RectF(getRectangle().getLeft()+getDx(),
                    getRectangle().getTop()+getDy(),
                    getRectangle().getRight()+getDx(),
                    getRectangle().getBottom()+getDy());
            canvas.drawOval(shadowRect, mPaint);
        }
        //jumping is visualised by shadow distance from character (Y)
        else {
            if (jumpingLevel <= Main.gameLoop.FPS/2) {
                shadowFactor = (jumpingLevel) * getSpeed();
            }
            else if (jumpingLevel <= Main.gameLoop.FPS){
                shadowFactor = (Main.gameLoop.FPS - jumpingLevel +1) * getSpeed();
            }
            else {
                shadowFactor = 0;
            }
            RectF shadowRect = new RectF(getRectangle().getLeft()+getDx(),
                    getRectangle().getTop()+getDy() + shadowFactor,
                    getRectangle().getRight()+getDx(),
                    getRectangle().getBottom()+getDy() + shadowFactor);
            canvas.drawOval(shadowRect, mPaint);
        }
    }
    public void draw(Canvas canvas) {
        if (animation!=null) {

            canvas.drawBitmap(animation.getImage(), x, y, null);
            drawShadow(canvas);

        }
    }

}