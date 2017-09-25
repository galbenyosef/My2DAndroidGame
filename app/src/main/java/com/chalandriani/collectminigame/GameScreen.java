package com.chalandriani.collectminigame;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;


public class GameScreen extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 2400  ;
    public static final int HEIGHT = 480;
    private MainThread thread;
    private Background bg;
    private AnimationManager animator;
    private Player player;
    private int playerCount = 0;
    private OnlinePlayer player2;
    private Rect mapTopLimit,mapBottomLimit;

    FirebaseDatabase database ;

    public GameScreen(Context context)
    {
        super(context);
    }

    public GameScreen(Context context, AttributeSet attrs) {
        super (context, attrs);

        animator = new AnimationManager(getResources());
        database  = FirebaseDatabase.getInstance();
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);

    }

    public GameScreen(Context context, AttributeSet attrs,
                        int defStyle) {
        super (context, attrs, defStyle);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter < 1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;

            }catch(InterruptedException e){e.printStackTrace();}
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.map));
        showJoystick();
        mapTopLimit = new Rect(0,0,2400,8*24);
        mapBottomLimit = new Rect(0,18*24,2400,20*24);

        player = new Player();
        player.setReference("gal");
        player2 = new OnlinePlayer();
        player2.setReference("ziviva");

        DatabaseReference r = database.getReference("players");
        r.addListenerForSingleValueEvent(playerReader);
        r.addValueEventListener(playerUpdater);

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        }
        return super.onTouchEvent(event);
    }

    void setJoystickKeys(JoystickView joy){
        joy.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                player.setMoving(true);
                player.setDirection(direction);
            }

            @Override
            public void onReleased() {

                player.setMoving(false);
            }
        });
    }

    void showJoystick(){

        JoystickView joyview = new JoystickView(getContext());
        joyview.setLayoutParams(new FrameLayout.LayoutParams(getHeight()/5,getWidth()/5, Gravity.BOTTOM));
        setJoystickKeys(joyview);
        View rootView = ((Activity)getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
        ((FrameLayout)rootView).addView(joyview);
    }

    ValueEventListener playerUpdater = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String,Player>> player_db = new GenericTypeIndicator<Map<String,Player>>(){};
            if (dataSnapshot.getValue(player_db) != null) {
                ArrayList<Player> players_updates = new ArrayList<>(dataSnapshot.getValue(player_db).values());
                playerCount = players_updates.size();
                if (playerCount == 0) return;
                for (int i = 0; i < players_updates.size(); i++) {
                    Player currentPlayer = players_updates.get(i);
                    if (!currentPlayer.getPlayerName().equals(player.getPlayerName())) {
                        player2.setX(currentPlayer.getX());
                        player2.setY(currentPlayer.getY());
                        player2.setDirection(currentPlayer.getDirection());
                        player2.setOld_direction(currentPlayer.getOld_direction());
                        player2.setWidth(currentPlayer.getWidth());
                        player2.setHeight(currentPlayer.getHeight());
                        player2.setMoving(currentPlayer.isMoving());
                        player2.setSpeed(currentPlayer.getSpeed());

                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
        }
    };

    ValueEventListener playerReader = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String,Player>> player_db = new GenericTypeIndicator<Map<String,Player>>(){};
            if (dataSnapshot.getValue(player_db) != null) {
                ArrayList<Player> players_updates = new ArrayList<>(dataSnapshot.getValue(player_db).values());
                playerCount = players_updates.size();
                if (playerCount == 0) return;
                for (int i = 0; i < players_updates.size(); i++) {
                    Player currentPlayer = players_updates.get(i);
                    if (!currentPlayer.getPlayerName().equals(player.getPlayerName())) {
                        player2.setX(currentPlayer.getX());
                        player2.setY(currentPlayer.getY());
                        player2.setDirection(currentPlayer.getDirection());
                        player2.setOld_direction(currentPlayer.getOld_direction());
                        player2.setWidth(currentPlayer.getWidth());
                        player2.setHeight(currentPlayer.getHeight());
                        player2.setMoving(currentPlayer.isMoving());
                        player2.setSpeed(currentPlayer.getSpeed());
                        player2.setAnimation(animator.getWalkAnimation(player2.getDirection()));

                    } else {
                        player.setX(currentPlayer.getX());
                        player.setY(currentPlayer.getY());
                        player.setDirection(currentPlayer.getDirection());
                        player.setOld_direction(currentPlayer.getOld_direction());
                        player.setWidth(currentPlayer.getWidth());
                        player.setHeight(currentPlayer.getHeight());
                        player.setMoving(currentPlayer.isMoving());
                        player.setSpeed(currentPlayer.getSpeed());
                        player.setAnimation(animator.getWalkAnimation(player.getDirection()));

                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public boolean collision(){

        RectF playerOval = new RectF(player.getX() + player.getDx() + 25,player.getY() + player.getDy() + 55
                ,player.getX() + player.getDx() + player.getWidth() - 25,player.getY() + player.getDy() + player.getHeight());
        RectF player2Oval = new RectF(player2.getX()+25,player2.getY()+55,player2.getX()+player2.getWidth()-25,player2.getY()+player2.getHeight());
        if (playerOval.intersects(mapTopLimit.left,mapTopLimit.top,mapTopLimit.right,mapTopLimit.bottom)
                || playerOval.intersects(mapBottomLimit.left,mapBottomLimit.top,mapBottomLimit.right,mapBottomLimit.bottom)) {
            return true;
        }
        if (RectF.intersects(playerOval,player2Oval)) {
            return true;
        }
//            for (GameObject obj: list) {
//                if (Rect.intersects(new Rect(obj.getX(),obj.getY(),obj.getX()+obj.getWidth(),obj.getY()+obj.getHeight()),new Rect(getX(),getY(),getX()+getWidth(),getY()+getHeight()))){
//                    return(true);
//                }
//            }
        return false;
    }

    public void update()
    {
        if (playerCount > 0) {

            ArrayList<GameObject> others = new ArrayList<>();
            others.add(player2);
            if (collision()) {
                player.setDx(0);
                player.setDy(0);
            }
            if (player.getDirection() != player.getOld_direction()) {

                player.setAnimation(animator.getWalkAnimation(player.getDirection()));
                player.setOld_direction(player.getDirection());
            }
            player.update();

            if (player2.getOld_direction() != player2.getDirection()) {

                player2.setAnimation(animator.getWalkAnimation(player2.getDirection()));
                player2.setOld_direction(player2.getDirection());
            }
            player2.update();

            bg.update(player.getDirection(), player.getSpeed() / 2);
            if (player.getX() <= 40) {
                player.setX(player.getX() + 200);
                bg.setX(bg.getX() + 200);
            }
            if (player.getX() >= WIDTH - 30) {
                player.setX(player.getX() - 200);
                bg.setX(bg.getX() - 200);
            }
        }
    }
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        final float scaleFactorX = (float)getWidth()/(WIDTH/3*1.f);
        final float scaleFactorY = (float)getHeight()/(HEIGHT*1.f);
        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);

            //draw shadows by collision shapes +-
            Paint mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setAlpha(40);
            canvas.drawOval(new RectF(player.getX() + player.getDx() + 15,player.getY() + player.getDy() + 50
                    ,player.getX() + player.getDx() + player.getWidth() - 15,player.getY() + player.getDy() + player.getHeight()),mPaint);
            canvas.drawOval(new RectF(player2.getX()+15,player2.getY()+50,player2.getX()+player2.getWidth()-15,player2.getY()+player2.getHeight()),mPaint);

            player.draw(canvas);
            player2.draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }
}