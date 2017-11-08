package com.chalandriani.collectminigame;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 2400;
    public static final int HEIGHT = 480;
    public static float scaleFactorX;
    public static float scaleFactorY;
    Background bg;
    Rect mapTopLimit,mapBottomLimit;


    public GameView(Context context, AttributeSet attrs) {
        super (context, attrs);

        getHolder().addCallback(this);
        Main.gameLoop = new GameLoop(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter < 100)
        {
            counter++;
            try{
                Main.gameLoop.setRunning(false);
                Main.gameLoop.join();
                retry = false;
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.map));
        mapTopLimit = new Rect(0,0,2400,8*24);
        mapBottomLimit = new Rect(0,18*24,2400,20*24);

        scaleFactorX = (float)getWidth()/(WIDTH/3*1.f);
        scaleFactorY = (float)getHeight()/(HEIGHT*1.f);

        //we can safely start the game loop
        Main.gameLoop.setRunning(true);
        Main.gameLoop.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        }
        return super.onTouchEvent(event);
    }

    public boolean mapCollision(){

        Rectangle collisionRect = new Rectangle(Main.player.getRectangle().getLeft()+Main.player.getDx(),
                Main.player.getRectangle().getTop()+Main.player.getDy(),
                Main.player.getRectangle().getRight()+Main.player.getDx(),
                Main.player.getRectangle().getBottom()+Main.player.getDy());
        if (collisionRect.intersects(mapTopLimit.left,mapTopLimit.top,mapTopLimit.right,mapTopLimit.bottom)
                || collisionRect.intersects(mapBottomLimit.left,mapBottomLimit.top,mapBottomLimit.right,mapBottomLimit.bottom)) {
            return true;
        }

        return false;
    }

    public GameObject objectCollision(){

        Rectangle collisionRect = new Rectangle(Main.player.getRectangle().getLeft()+Main.player.getDx(),
                Main.player.getRectangle().getTop()+Main.player.getDy(),
                Main.player.getRectangle().getRight()+Main.player.getDx(),
                Main.player.getRectangle().getBottom()+Main.player.getDy());

        if (!Main.players.isEmpty()) {
            for (Player player : Main.players.values()){
                if (collisionRect.intersects(player.getRectangle()))
                    return player;
            }
        }

        return null;
    }

    public void drawLifeBar(Canvas canvas){

        Paint mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(40);
        canvas.drawRect(0,0,WIDTH/3/5,HEIGHT/10,mPaint);
        if (Main.player.getHp() > 30) {
            mPaint.setColor(Color.GREEN);
            canvas.drawRect(5, 5, Main.player.getHp() / 100 * WIDTH / 3 / 5, HEIGHT / 10, mPaint);
        }
        else {
            mPaint.setColor(Color.RED);
            canvas.drawRect(5,5,Main.player.getHp()/100 * WIDTH/3/5,HEIGHT/10,mPaint);
        }
    }

    public void update()
    {

        GameObject objCollision = objectCollision();
        if (objCollision != null){
            Log.d("H","collision");

            if (objCollision instanceof Player){
                   if (((Player) objCollision).isSlashing()){
                       Main.player.decreaseHp(5);
                       Log.d("H","Slashed by: " + ((Player) objCollision).getPlayerName());
                       if (Main.player.getHp() == 0){
                           Log.d("H",Main.player.getPlayerName() + " is dead by " + ((Player) objCollision).getPlayerName());
                       }
                }
            }
            Main.player.setDx(0);
            Main.player.setDy(0);
        }

        if (mapCollision()) {
            Main.player.setDx(0);
            Main.player.setDy(0);
        }

        Main.player.update();
        if (GameLoop.frameCount%2 == 0) {
            FirebaseHandler.player_reference.setValue(Main.player);
        }

        for(Player player : Main.players.values()) {
            player.update();
        }

        bg.update(Main.player.getDirection(), Main.player.getSpeed() / 2);
        if (Main.player.getX() <= 40) {
            Main.player.setX(Main.player.getX() + 200);
            bg.setX(bg.getX() + 200);
        }
        if (Main.player.getX() >= WIDTH - 30) {
            Main.player.setX(Main.player.getX() - 200);
            bg.setX(bg.getX() - 200);
        }
    }
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);

            ArrayList<Player> playersSorted = ySort();

            for (Player player : playersSorted)
            {
                player.draw(canvas);
            }

            drawLifeBar(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    private ArrayList<Player> ySort(){
        ArrayList<Player> all = new ArrayList<>();
        all.add(Main.player);
        if (!Main.players.isEmpty())
            all.addAll(Main.players.values());

        Collections.sort(all, new Comparator<Player>() {
            @Override
            public int compare(Player player, Player t1) {
                if (player.getY() == t1.getY())
                    return 0;
                else if  (player.getY() < t1.getY())
                    return -1;
                else
                    return 1;
            }
        });
        return all;
    }
}