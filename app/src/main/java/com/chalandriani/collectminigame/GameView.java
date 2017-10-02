package com.chalandriani.collectminigame;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
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
        Main.player.setSpeed(3);
        Main.player.setHeight(64);
        Main.player.setWidth(64);
        Main.player.setX(250);
        Main.player.setY(300);

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

    public boolean collision(){

        RectF playerOval = new RectF(Main.player.getX() + Main.player.getDx() + 25,Main.player.getY() + Main.player.getDy() + 55
                ,Main.player.getX() + Main.player.getDx() + Main.player.getWidth() - 25,Main.player.getY() + Main.player.getDy() + Main.player.getHeight());
     //   RectF player2Oval = new RectF(player2.getX()+25,player2.getY()+55,player2.getX()+player2.getWidth()-25,player2.getY()+player2.getHeight());
        if (playerOval.intersects(mapTopLimit.left,mapTopLimit.top,mapTopLimit.right,mapTopLimit.bottom)
                || playerOval.intersects(mapBottomLimit.left,mapBottomLimit.top,mapBottomLimit.right,mapBottomLimit.bottom)) {
            return true;
        }
//        if (RectF.intersects(playerOval,player2Oval)) {
//            return true;
//        }
//            for (GameObject obj: list) {
//                if (Rect.intersects(new Rect(obj.getX(),obj.getY(),obj.getX()+obj.getWidth(),obj.getY()+obj.getHeight()),new Rect(getX(),getY(),getX()+getWidth(),getY()+getHeight()))){
//                    return(true);
//                }
//            }
        return false;
    }

    public void update()
    {

        if (collision()) {
            Main.player.setDx(0);
            Main.player.setDy(0);
        }

        Main.player.update();
        FirebaseHandler.player_reference.setValue(Main.player);

        for(Player player : Main.players)
            player.update();

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

            //draw shadows by collision shapes +-
            Paint mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setAlpha(40);
            canvas.drawOval(new RectF(Main.player.getX() + Main.player.getDx() + 15,Main.player.getY() + Main.player.getDy() + 50
                    ,Main.player.getX() + Main.player.getDx() + Main.player.getWidth() - 15,Main.player.getY() + Main.player.getDy() + Main.player.getHeight()),mPaint);
            //canvas.drawOval(new RectF(player2.getX()+15,player2.getY()+50,player2.getX()+player2.getWidth()-15,player2.getY()+player2.getHeight()),mPaint);


            Main.player.draw(canvas);
            for (Player player : Main.players)
                player.draw(canvas);
          //  player2.draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }
}